package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.HBYCCache
import org.piramalswasthya.sakhi.model.HbycPost
import org.piramalswasthya.sakhi.network.D2DApiService
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class HbycRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DApiService
) {

    suspend fun saveHbycData(hbycCache: HBYCCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                hbycCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                }

                database.hbycDao.upsert(hbycCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveHbncData")
                false
            }
        }
    }

    suspend fun processNewHbyc(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = database.userDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val hbycList = database.hbycDao.getAllUnprocessedHBYC()

            val hbycPostList = mutableSetOf<HbycPost>()

            hbycList.forEach {
                hbycPostList.clear()
                val household = database.householdDao.getHousehold(it.hhId)
                    ?: throw IllegalStateException("No household exists for hhId: ${it.hhId}!!")
                val ben = database.benDao.getBen(it.hhId, it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                val hbycCount = database.hbycDao.hbycCount()
                hbycPostList.add(it.asPostModel(user, household, ben, hbycCount))
                it.syncState = SyncState.SYNCING
                database.hbycDao.setSynced(it)
                val uploadDone = postDataToD2dServer(hbycPostList)
                if (uploadDone) {
                    it.processed = "P"
                    it.syncState = SyncState.SYNCED
                } else {
                    it.syncState = SyncState.UNSYNCED
                }
                database.hbycDao.setSynced(it)
            }

            return@withContext true
        }
    }

    private suspend fun postDataToD2dServer(hbycPostList: MutableSet<HbycPost>): Boolean {
        if (hbycPostList.isEmpty()) return false

        try {

            val response = d2DNetworkApiService.postHbycForm(hbycPostList.toList())
            val statusCode = response.code()

            if (statusCode == 200) {
                try {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errormessage = jsonObj.getString("message")
                        if (jsonObj.isNull("status")) throw IllegalStateException("D2d server not responding properly, Contact Service Administrator!!")
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
            Timber.w("Bad Response from server, need to check $hbycPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postDataToD2dServer(hbycPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }
}