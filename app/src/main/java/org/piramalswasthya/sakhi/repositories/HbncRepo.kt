package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.HBNCCache
import org.piramalswasthya.sakhi.model.HBNCPost
import org.piramalswasthya.sakhi.model.HbncHomeVisit
import org.piramalswasthya.sakhi.model.HbncVisitCard
import org.piramalswasthya.sakhi.network.D2DApiService
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class HbncRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DApiService
) {


    fun hbncList(benId: Long, hhId: Long) = database.hbncDao.getAllHbncEntries(hhId, benId)

    suspend fun getHbncCard(benId: Long, hhId: Long): HbncVisitCard? {
        return withContext(Dispatchers.IO) {
            database.hbncDao.getHbnc(hhId, benId, Konstants.hbncCardDay)?.visitCard
        }
    }

    suspend fun getHbncRecord(benId: Long, hhId: Long, day : Int): HBNCCache? {
        return withContext(Dispatchers.IO) {
            database.hbncDao.getHbnc(benId, hhId, day)
        }
    }


    suspend fun getFirstHomeVisit(hhId: Long, benId: Long): HbncHomeVisit? {
        return withContext(Dispatchers.IO) {
            database.hbncDao.getHbnc(hhId, benId, 1)?.homeVisitForm
        }
    }

    suspend fun saveHbncData(hbncCache: HBNCCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
//                hbncCache.apply {
//                    createdBy = user.userName
//                    createdDate = System.currentTimeMillis()
//                }

                database.hbncDao.upsert(hbncCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveHbncData")
                false
            }
        }
    }

    suspend fun processNewHbnc(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = database.userDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val hbncList = database.hbncDao.getAllUnprocessedHbnc()

            val hbncPostSet = mutableSetOf<HBNCPost>()

            hbncList.forEach {
                hbncPostSet.clear()
                val household = database.householdDao.getHousehold(it.hhId)
                    ?: throw IllegalStateException("No household exists for hhId: ${it.hhId}!!")
                val ben = database.benDao.getBen(it.hhId, it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                val hbncCount = database.hbncDao.hbncCount()
//                hbncPostSet.add(it.asPostModel(user, household, hbncCount))
                it.syncState = SyncState.SYNCING
                database.hbncDao.update(it)
                val uploadDone = postDataToD2dServer(hbncPostSet)
                if (uploadDone) {
                    it.processed = "P"
                    it.syncState = SyncState.SYNCED
                } else {
                    it.syncState = SyncState.UNSYNCED
                }
                database.hbncDao.update(it)
            }
            return@withContext true
        }
    }

    private suspend fun postDataToD2dServer(hbncPostSet: Set<HBNCPost>): Boolean {
        if (hbncPostSet.isEmpty())
            return false

        try {
            val response = d2DNetworkApiService.postHbncForm(hbncPostSet.toList())
            val statusCode = response.code()

            if (statusCode == 200) {
                try {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        // Log.d("dsfsdfse", "onResponse: "+jsonObj);
                        val errormessage = jsonObj.getString("message")
                        if (jsonObj.isNull("status"))
                            throw IllegalStateException("D2d server not responding properly, Contact Service Administrator!!")
                        val responsestatuscode = jsonObj.getInt("status")

                        if (responsestatuscode == 200) {
                            Timber.d("Saved Successfully to server")
                            return true
                        } else if (responsestatuscode == 5002) {
                            val user = userRepo.getLoggedInUser()
                                ?: throw IllegalStateException("User seems to be logged out!!")
                            if (userRepo.refreshTokenD2d(user.userName, user.password))
                                throw SocketTimeoutException()
                        } else {
                            throw IOException("Throwing away IO eXcEpTiOn")
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
            Timber.w("Bad Response from server, need to check $hbncPostSet $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postDataToD2dServer(hbncPostSet)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }


}