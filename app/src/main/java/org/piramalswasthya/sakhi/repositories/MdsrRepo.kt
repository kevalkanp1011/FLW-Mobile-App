package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.D2DNetworkApiService
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MdsrRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DNetworkApiService
) {

    suspend fun saveMdsrData(mdsrCache: MDSRCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                mdsrCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                }
                database.mdsrDao.upsert(mdsrCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveCbacData")
                false
            }
        }
    }

    suspend fun processNewMdsr(): Boolean {
        return withContext(Dispatchers.IO) {
            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val mdsrList = database.mdsrDao.getAllUnprocessedMdsr()

            val mdsrPostList = mutableSetOf<MdsrPost>()

            mdsrList.forEach {
                mdsrPostList.clear()
                val household =
                    database.householdDao.getHousehold(it.hhId)
                        ?: throw IllegalStateException("No household exists for hhId: ${it.hhId}!!")
                val ben =
                    database.benDao.getBen(it.hhId, it.benId)
                        ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                val mdsrCount = database.mdsrDao.mdsrCount()
                mdsrPostList.add(it.asPostModel(user, household, ben, mdsrCount))
                    val uploadDone = postDataToD2dServer(mdsrPostList)
//
//                    if (!uploadDone)
//                        database.benDao.benSyncWithServerFailed(*benNetworkPostList.map { ben -> ben.benId }
//                            .toTypedArray().toLongArray())
//                }
            }

//            val updateBenList = database.benDao.getAllBenForSyncWithServer()
//            updateBenList.forEach {
//                database.benDao.setSyncState(it.householdId, it.beneficiaryId, SyncState.SYNCING)
//                benNetworkPostList.add(it.asNetworkPostModel(user))

//                val uploadDone = postDataToAmritServer(
//                    benNetworkPostList,
//                    householdNetworkPostList,
//                    kidNetworkPostList,
//                    cbacPostList
//                )
//                if (!uploadDone)
//                    database.benDao.benSyncWithServerFailed(*benNetworkPostList.map { ben -> ben.benId }
//                        .toTypedArray().toLongArray())

//            }
            return@withContext true
        }
    }

    suspend fun postDataToD2dServer(mdsrPostList: MutableSet<MdsrPost>): Boolean {
        if(mdsrPostList.isEmpty())
            return false

        try {
            val response = d2DNetworkApiService.postMdsrDataRegister(mdsrPostList.toList())
            val statusCode = response.code()

            if (statusCode == 200) {
                var responseString: String? = null
                try {
                    responseString = response.body()!!.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        // Log.d("dsfsdfse", "onResponse: "+jsonObj);
                        val errormessage = jsonObj.getString("message")
                        if(jsonObj.isNull("status"))
                            throw IllegalStateException("D2d server not responding properly, Contact Service Administrator!!")
                        val responsestatuscode = jsonObj.getInt("status")

                        if (responsestatuscode == 200) {
                            Timber.d("Saved Successfully to server")
                        } else if (responsestatuscode == 5002) {
                            val user = userRepo.getLoggedInUser()
                                ?: throw IllegalStateException("User seems to be logged out!!")
//                            throw SocketTimeoutException("Refreshed Token!")
                            //mdialog.AlertDialog(getActivity(), resources.getString(R.string.text_Alert), resources.getString(R.string.session_expired), resources.getString(R.string.text_ok), 5);
                        } else {
//                                    lay_recy.setVisibility(View.GONE);
//                                    lay_no_ben.setVisibility(View.VISIBLE);
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
            Timber.w("Bad Response from server, need to check $mdsrPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return false
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }
}