package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.CDRCache
import org.piramalswasthya.sakhi.model.CDRPost
import timber.log.Timber
import javax.inject.Inject

class CdrRepo @Inject constructor(
    private val database: InAppDb,
    private val preferenceDao: PreferenceDao,
) {

    suspend fun saveCdrData(cdrCache: CDRCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")
            try {
                cdrCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                }
                database.cdrDao.upsert(cdrCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveCdrData")
                false
            }
        }
    }

    suspend fun processNewCdr(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val cdrList = database.cdrDao.getAllUnprocessedCdr()

            val cdrPostList = mutableSetOf<CDRPost>()

            cdrList.forEach {
                cdrPostList.clear()
                val household = database.householdDao.getHousehold(it.hhId)
                    ?: throw IllegalStateException("No household exists for hhId: ${it.hhId}!!")
                val ben = database.benDao.getBen(it.hhId, it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                val cdrCount = database.cdrDao.cdrCount()
                cdrPostList.add(it.asPostModel(user, household, ben, cdrCount))
                it.syncState = SyncState.SYNCING
                database.cdrDao.update(it)
//                val uploadDone = postDataToD2dServer(cdrPostList)
//                if (uploadDone) {
//                    it.processed = "P"
//                    it.syncState = SyncState.SYNCED
//                } else {
//                    it.syncState = SyncState.UNSYNCED
//                }
//                database.cdrDao.update(it)
            }
            return@withContext true
        }
    }

//    private suspend fun postDataToD2dServer(cdrPostList: MutableSet<CDRPost>): Boolean {
//        if (cdrPostList.isEmpty()) return false
//
//        try {
//
//            val response = d2DNetworkApiService.postCdrForm(cdrPostList.toList())
//
//            val statusCode = response.code()
//            if (statusCode == 200) {
//                var responseString: String? = null
//                try {
//                    responseString = response.body()?.string()
//                    //  Log.d("hgyfgdufhf", "onResponse: "+responseString);
//                    if (responseString != null) {
//                        val jsonObj = JSONObject(responseString)
//
//                        // Log.d("dsfsdfse", "onResponse: "+jsonObj);
//                        val errormessage = jsonObj.getString("message")
//
//                        if (jsonObj.isNull("status")) throw IllegalStateException("D2d server not responding properly, Contact Service Administrator!!")
//
//                        val responsestatuscode = jsonObj.getInt("status")
//                        when (responsestatuscode) {
//                            200 -> {
//                                Timber.d("Saved Successfully to server")
//
//                                return true
//                            }
//                            5002 -> {
//                                val user = userRepo.getLoggedInUser()
//                                    ?: throw IllegalStateException("User seems to be logged out!!")
//                                if (userRepo.refreshTokenD2d(
//                                        user.userName,
//                                        user.password
//                                    )
//                                ) throw SocketTimeoutException()
//                            }
//                            else -> {
//                                //                                    lay_recy.setVisibility(View.GONE);
//                                //                                    lay_no_ben.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            } else {
//                //server_resp5();
//            }
//        } catch (e: SocketTimeoutException) {
//            Timber.d("Caught exception $e here")
//            return postDataToD2dServer(cdrPostList)
//        } catch (e: JSONException) {
//            Timber.d("Caught exception $e here")
//            return false
//        }
//        return false
//    }
}