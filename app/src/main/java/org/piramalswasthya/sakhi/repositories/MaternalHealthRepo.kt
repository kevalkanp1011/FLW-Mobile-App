package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.database.room.dao.UserDao
import org.piramalswasthya.sakhi.helpers.hasPendingAncVisit
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.AmritApiService
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

class MaternalHealthRepo @Inject constructor(
    private val amritApiService: AmritApiService,
    private val maternalHealthDao: MaternalHealthDao,
    private val userRepo: UserRepo,
    private val benDao: BenDao,
    private val userDao: UserDao
) {

    suspend fun getSavedRegistrationRecord(benId: Long): PregnantWomanRegistrationCache? {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getSavedRecord(benId)
        }
    }

    suspend fun getSavedAncRecord(benId: Long, visitNumber: Int): PregnantWomanAncCache? {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getSavedRecord(benId, visitNumber)
        }
    }

    suspend fun getBenFromId(benId: Long): BenRegCache? {
        return withContext(Dispatchers.IO) {
            benDao.getBen(benId)
        }
    }

    suspend fun persistRegisterRecord(pregnancyRegistrationForm: PregnantWomanRegistrationCache) {
        withContext(Dispatchers.IO) {
            maternalHealthDao.saveRecord(pregnancyRegistrationForm)
        }
    }


    suspend fun getAllAncRecords(benId: Long): List<AncStatus> {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getAllAncRecordsFor(benId)
        }
    }

    suspend fun persistAncRecord(ancCache: PregnantWomanAncCache) {
        withContext(Dispatchers.IO) {
            maternalHealthDao.saveRecord(ancCache)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val ancDueCount = maternalHealthDao.getAllPregnancyRecords().transformLatest {
        Timber.d("From DB : ${it.count()}")
        var count = 0
        it.map {
            Timber.d(
                "Values emitted : ${
                    it.value.map {
                        AncStatus(
                            it.benId,
                            it.visitNumber,
                            AncFormState.ALREADY_FILLED
                        )
                    }
                }"
            )
            val regis = it.key
            val visitPending = hasPendingAncVisit(
                it.value.map {
                    AncStatus(
                        it.benId,
                        it.visitNumber,
                        AncFormState.ALREADY_FILLED
                    )
                },
                regis.lmpDate,
                regis.benId,
                Calendar.getInstance().apply {
                    set(Calendar.SECOND, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.HOUR_OF_DAY, 0)
                }.timeInMillis
            )
            if (visitPending)
                count++
        }
        emit(count)
    }

    suspend fun processNewAncVisit(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val ancList = maternalHealthDao.getAllUnprocessedAncVisits()

            val ancPostList = mutableSetOf<PregnantWomanAncCache>()

            ancList.forEach {
                ancPostList.clear()
                val ben = benDao.getBen(it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                ancPostList.add(it)
                it.syncState = SyncState.SYNCING
                maternalHealthDao.updateANC(it)
                val uploadDone = postDataToAmritServer(ancPostList)
                if (uploadDone) {
                    it.processed = "P"
                    it.syncState = SyncState.SYNCED
                } else {
                    it.syncState = SyncState.UNSYNCED
                }
                maternalHealthDao.updateANC(it)
            }

            return@withContext true
        }
    }

    private suspend fun postDataToAmritServer(ancPostList: MutableSet<PregnantWomanAncCache>): Boolean {
        if (ancPostList.isEmpty()) return false

        try {

            val response = amritApiService.postAncForm(ancPostList.toList())
            val statusCode = response.code()

            if (statusCode == 200) {
                try {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errormessage = jsonObj.getString("message")
                        if (jsonObj.isNull("status")) throw IllegalStateException("Amrit server not responding properly, Contact Service Administrator!!")
                        val responsestatuscode = jsonObj.getInt("status")

                        when (responsestatuscode) {
                            200 -> {
                                Timber.d("Saved Successfully to server")
                                return true
                            }
                            5002 -> {
                                val user = userRepo.getLoggedInUser()
                                    ?: throw IllegalStateException("User seems to be logged out!!")
                                if (userRepo.refreshTokenD2d(
                                        user.userName,
                                        user.password
                                    )
                                ) throw SocketTimeoutException()
                            }
                            else -> {
                                throw IOException("Throwing away IO eXcEpTiOn")
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                //server_resp5();
            }
            Timber.w("Bad Response from server, need to check $ancPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postDataToAmritServer(ancPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }
}