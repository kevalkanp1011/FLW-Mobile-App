package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.PMSMACache
import org.piramalswasthya.sakhi.model.PmsmaPost
import org.piramalswasthya.sakhi.network.D2DApiService
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class PmsmaRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DApiService
) {
    suspend fun savePmsmaData(pmsmaCache: PMSMACache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                pmsmaCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                }
                database.pmsmaDao.upsert(pmsmaCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at savePmsmaData")
                false
            }
        }
    }

    suspend fun processNewPmsma(): Boolean {
        return withContext(Dispatchers.IO) {
            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val pmsmaList = database.pmsmaDao.getAllUnprocessedPmsma()

            val pmsmaPostList = mutableSetOf<PmsmaPost>()

            pmsmaList.forEach {
                pmsmaPostList.clear()
                val household =
                    database.householdDao.getHousehold(it.hhId)
                        ?: throw IllegalStateException("No household exists for hhId: ${it.hhId}!!")
                val ben =
                    database.benDao.getBen(it.hhId, it.benId)
                        ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                val pmsma = database.pmsmaDao.pmsmaCount()
                pmsmaPostList.add(it.asPostModel(user, ben))
                val uploadDone = postDataToD2dServer(pmsmaPostList)
                if (uploadDone) {
                    it.processed = "P"
                    database.pmsmaDao.updatePmsmaRecord(it)
                }
            }

            return@withContext true
        }
    }

    private suspend fun postDataToD2dServer(pmsmaPostList: MutableSet<PmsmaPost>): Boolean {
        if (pmsmaPostList.isEmpty())
            return false

        try {
            val response = d2DNetworkApiService.postPmsmaRegister(pmsmaPostList.toList())
            val statusCode = response.code()

            if (statusCode == 200) {
                try {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("message")
                        if (jsonObj.isNull("status"))
                            throw IllegalStateException("D2d server not responding properly, Contact Service Administrator!!")

                        when (jsonObj.getInt("status")) {
                            200 -> {
                                Timber.d("Saved Successfully to server")
                                return true
                            }
                            5002 -> {
                                val user = userRepo.getLoggedInUser()
                                    ?: throw IllegalStateException("User seems to be logged out!!")
                                if (userRepo.refreshTokenD2d(user.userName, user.password))
                                    throw SocketTimeoutException()
                            }
                            else -> {
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
            Timber.w("Bad Response from server, need to check $pmsmaPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postDataToD2dServer(pmsmaPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }

}