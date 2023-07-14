package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.AmritApiService
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class EcrRepo @Inject constructor(
    private val amritApiService: AmritApiService,
    private val userRepo: UserRepo,
    private val database: InAppDb
)  {

    suspend fun persistRecord(ecrForm: EligibleCoupleRegCache) {
        withContext(Dispatchers.IO){
            database.ecrDao.upsert(ecrForm)
        }
    }

    suspend fun getBenFromId(benId: Long): BenRegCache? {
        return withContext(Dispatchers.IO){
            database.benDao.getBen(benId)
        }
    }

    suspend fun getSavedRecord(benId: Long): EligibleCoupleRegCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getSavedECR(benId)
        }
    }

    suspend fun getEct(benId: Long): EligibleCoupleTrackingCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getEct(benId)
        }
    }

    suspend fun saveEct(eligibleCoupleTrackingCache: EligibleCoupleTrackingCache) {
        withContext(Dispatchers.IO){
            database.ecrDao.saveRecord(eligibleCoupleTrackingCache)
        }
    }

    suspend fun processNewEcr(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = database.userDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val ecrList = database.ecrDao.getAllUnprocessedECR()

            val ecrPostList = mutableSetOf<EcrPost>()

            ecrList.forEach {
                ecrPostList.clear()
                val ben = database.benDao.getBen(it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                ecrPostList.add(it.asPostModel())
                it.syncState = SyncState.SYNCING
                database.ecrDao.update(it)
                val uploadDone = postDataToAmritServer(ecrPostList)
                if (uploadDone) {
                    it.processed = "P"
                    it.syncState = SyncState.SYNCED
                } else {
                    it.syncState = SyncState.UNSYNCED
                }
                database.ecrDao.update(it)
            }

            return@withContext true
        }
    }

    private suspend fun postDataToAmritServer(ecrPostList: MutableSet<EcrPost>): Boolean {
        if (ecrPostList.isEmpty()) return false

        try {

            val response = amritApiService.postEcrForm(ecrPostList.toList())
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
            Timber.w("Bad Response from server, need to check $ecrPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postDataToAmritServer(ecrPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }

    suspend fun processNewEct(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = database.userDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val ectList = database.ecrDao.getAllUnprocessedECT()

            val ectPostList = mutableSetOf<EligibleCoupleTrackingCache>()

            ectList.forEach {
                ectPostList.clear()
                val ben = database.benDao.getBen(it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                ectPostList.add(it)
                it.syncState = SyncState.SYNCING
                database.ecrDao.updateEligibleCoupleTracking(it)
                val uploadDone = postEctDataToAmritServer(ectPostList)
                if (uploadDone) {
                    it.processed = "P"
                    it.syncState = SyncState.SYNCED
                } else {
                    it.syncState = SyncState.UNSYNCED
                }
                database.ecrDao.updateEligibleCoupleTracking(it)
            }

            return@withContext true
        }
    }

    private suspend fun postEctDataToAmritServer(ectPostList: MutableSet<EligibleCoupleTrackingCache>): Boolean {
        if (ectPostList.isEmpty()) return false

        try {

            val response = amritApiService.postEctForm(ectPostList.toList())
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
            Timber.w("Bad Response from server, need to check $ectPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postEctDataToAmritServer(ectPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }
}