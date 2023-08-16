package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.EcrPost
import org.piramalswasthya.sakhi.model.EligibleCoupleRegCache
import org.piramalswasthya.sakhi.model.EligibleCoupleTrackingCache
import org.piramalswasthya.sakhi.network.AmritApiService
import org.piramalswasthya.sakhi.network.GetDataPaginatedRequest
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class EcrRepo @Inject constructor(
    private val amritApiService: AmritApiService,
    private val userRepo: UserRepo,
    private val database: InAppDb,
    private val preferenceDao: PreferenceDao,
    private val tmcNetworkApiService: AmritApiService
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

    suspend fun getEct(benId: Long, visitDate : Long): EligibleCoupleTrackingCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getEct(benId, visitDate)
        }
    }

    suspend fun saveEct(eligibleCoupleTrackingCache: EligibleCoupleTrackingCache) {
        withContext(Dispatchers.IO){
            database.ecrDao.saveRecord(eligibleCoupleTrackingCache)
        }
    }

    suspend fun processUnsyncedEcr(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = preferenceDao.getLoggedInUser()
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

        val user =
            preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

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
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
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
            val user = preferenceDao.getLoggedInUser()
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

        val user = preferenceDao.getLoggedInUser()
            ?: throw IllegalStateException("No user logged in!!")
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
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
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

    suspend fun getECRDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            val lastTimeStamp = preferenceDao.getLastSyncedTimeStamp()
            try {
                val response = tmcNetworkApiService.getEcrFormData(
                    GetDataPaginatedRequest(
                        user.userId,
                        0,
                        getCurrentDate(lastTimeStamp),
                        getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit eligible couple register data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
//                                    saveECRCacheFromResponse(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("TB Screening entries not synced $e")
                                    return@withContext 0
                                }

                                return@withContext 1
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
                                        user.userName, user.password
                                    )
                                ) throw SocketTimeoutException("Refreshed Token!")
                                else throw IllegalStateException("User Logged out!!")
                            }

                            5000 -> {
                                if (errorMessage == "No record found") return@withContext 0
                            }

                            else -> {
                                throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                            }
                        }
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_tb error : $e")
                return@withContext -2

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_tb error : $e")
                return@withContext -1
            }
            -1
        }
    }

//    private suspend fun saveECRCacheFromResponse(dataObj: String): MutableList<EligibleCoupleRegCache> {
//        val tbScreeningList = mutableListOf<TBScreeningCache>()
//        var requestDTO = Gson().fromJson(dataObj, TBScreeningRequestDTO::class.java)
//        requestDTO?.tbScreeningList?.forEach { tbScreeningDTO ->
//            tbScreeningDTO.visitDate?.let {
//                var tbScreeningCache: TBScreeningCache? =
//                    tbDao.getTbScreening(tbScreeningDTO.benId,
//                        TBRepo.getLongFromDate(tbScreeningDTO.visitDate)
//                    )
//                if (tbScreeningCache == null) {
//                    tbDao.saveTbScreening(tbScreeningDTO.toCache())
//                }
//            }
//        }
//        return tbScreeningList
//    }
//
//    suspend fun getTbSuspectedDetailsFromServer(): Int {
//        return withContext(Dispatchers.IO) {
//            val user =
//                preferenceDao.getLoggedInUser()
//                    ?: throw IllegalStateException("No user logged in!!")
//            val lastTimeStamp = preferenceDao.getLastSyncedTimeStamp()
//            try {
//                val response = tmcNetworkApiService.getTBSuspectedData(
//                    GetBenRequest(
//                        user.userId,
//                        0,
//                        TBRepo.getCurrentDate(lastTimeStamp),
//                        TBRepo.getCurrentDate()
//                    )
//                )
//                val statusCode = response.code()
//                if (statusCode == 200) {
//                    val responseString = response.body()?.string()
//                    if (responseString != null) {
//                        val jsonObj = JSONObject(responseString)
//
//                        val errorMessage = jsonObj.getString("errorMessage")
//                        val responseStatusCode = jsonObj.getInt("statusCode")
//                        Timber.d("Pull from amrit tb suspected data : $responseStatusCode")
//                        when (responseStatusCode) {
//                            200 -> {
//                                try {
//                                    val dataObj = jsonObj.getString("data")
//                                    saveTBSuspectedCacheFromResponse(dataObj)
//                                } catch (e: Exception) {
//                                    Timber.d("TB Suspected entries not synced $e")
//                                    return@withContext 0
//                                }
//
//                                return@withContext 1
//                            }
//
//                            5002 -> {
//                                if (userRepo.refreshTokenTmc(
//                                        user.userName, user.password
//                                    )
//                                ) throw SocketTimeoutException("Refreshed Token!")
//                                else throw IllegalStateException("User Logged out!!")
//                            }
//
//                            5000 -> {
//                                if (errorMessage == "No record found") return@withContext 0
//                            }
//
//                            else -> {
//                                throw IllegalStateException("$responseStatusCode received, don't know what todo!?")
//                            }
//                        }
//                    }
//                }
//
//            } catch (e: SocketTimeoutException) {
//                Timber.d("get_tb error : $e")
//                return@withContext -2
//
//            } catch (e: java.lang.IllegalStateException) {
//                Timber.d("get_tb error : $e")
//                return@withContext -1
//            }
//            -1
//        }
//    }
//
//    private suspend fun saveTBSuspectedCacheFromResponse(dataObj: String): MutableList<TBSuspectedCache> {
//        val tbSuspectedList = mutableListOf<TBSuspectedCache>()
//        val requestDTO = Gson().fromJson(dataObj, TBSuspectedRequestDTO::class.java)
//        requestDTO?.tbSuspectedList?.forEach { tbSuspectedDTO ->
//            tbSuspectedDTO.visitDate?.let {
//                val tbSuspectedCache: TBSuspectedCache? =
//                    tbDao.getTbSuspected(
//                        tbSuspectedDTO.benId,
//                        TBRepo.getLongFromDate(tbSuspectedDTO.visitDate)
//                    )
//                if (tbSuspectedCache == null) {
//                    tbDao.saveTbSuspected(tbSuspectedDTO.toCache())
//                }
//            }
//        }
//        return tbSuspectedList
//    }

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        private fun getCurrentDate(millis: Long = System.currentTimeMillis()): String {
            val dateString = dateFormat.format(millis)
            val timeString = timeFormat.format(millis)
            return "${dateString}T${timeString}.000Z"
        }

        private fun getLongFromDate(dateString: String): Long {
            //Jul 22, 2023 8:17:23 AM"
            val f = SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }
}