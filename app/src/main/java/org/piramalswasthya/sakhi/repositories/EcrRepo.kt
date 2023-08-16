package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
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
) {

    suspend fun persistRecord(ecrForm: EligibleCoupleRegCache) {
        withContext(Dispatchers.IO) {
            database.ecrDao.upsert(ecrForm)
        }
    }

    suspend fun getBenFromId(benId: Long): BenRegCache? {
        return withContext(Dispatchers.IO) {
            database.benDao.getBen(benId)
        }
    }

    suspend fun getSavedRecord(benId: Long): EligibleCoupleRegCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getSavedECR(benId)
        }
    }

    suspend fun getEct(benId: Long, visitDate: Long): EligibleCoupleTrackingCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getEct(benId, visitDate)
        }
    }

    suspend fun saveEct(eligibleCoupleTrackingCache: EligibleCoupleTrackingCache) {
        withContext(Dispatchers.IO) {
            database.ecrDao.upsert(eligibleCoupleTrackingCache)
        }
    }

    suspend fun pushAndUpdateEcrRecord(): Boolean {
        return withContext(Dispatchers.IO) {
            val ecrList = database.ecrDao.getAllUnprocessedECR()
            val ecrPostList = mutableSetOf<EcrPost>()

            ecrList.forEach {
                ecrPostList.clear()
                val ben = database.benDao.getBen(it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                ecrPostList.add(it.asPostModel())
                it.syncState = SyncState.SYNCING
                database.ecrDao.update(it)
                val uploadDone = postECRDataToAmritServer(ecrPostList)
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

    private suspend fun postECRDataToAmritServer(ecrPostList: MutableSet<EcrPost>): Boolean {
        if (ecrPostList.isEmpty()) return false

        val user =
            preferenceDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")

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
            return postECRDataToAmritServer(ecrPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }

    suspend fun pushAndUpdateEctRecord(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val ectList = database.ecrDao.getAllUnprocessedECT()

            val ectPostList = mutableSetOf<EligibleCoupleTrackingCache>()

            ectList.forEach {
                ectPostList.clear()
                ectPostList.add(it)
                it.syncState = SyncState.SYNCING
                database.ecrDao.updateEligibleCoupleTracking(it)
                val uploadDone = postECTDataToAmritServer(ectPostList)
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

    private suspend fun postECTDataToAmritServer(ectPostList: MutableSet<EligibleCoupleTrackingCache>): Boolean {
        if (ectPostList.isEmpty()) return false

        val user =
            preferenceDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        try {

            val response =
                amritApiService.postEctForm(ectPostList.toList().map { it.asNetworkModel() })
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
            return postECTDataToAmritServer(ectPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }

    suspend fun pullAndPersistEcrRecord(): Int {
        return withContext(Dispatchers.IO) {
            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")
            val lastTimeStamp = Konstants.defaultTimeStamp
            try {
                val response = tmcNetworkApiService.getEcrFormData(
                    GetDataPaginatedRequest(
                        user.userId, 0, getCurrentDate(lastTimeStamp), getCurrentDate()
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
                                    val dataObj = jsonObj.getJSONArray("data")
                                    val ecrList = getEcrCacheFromServerResponse(dataObj)
                                    ecrList.filter {
                                        database.benDao.getBen(
                                            it.benId
                                        ) != null
                                    }.takeIf { it.isNotEmpty() }?.let {
                                        database.ecrDao.upsert(*it.toTypedArray())
                                    }
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
                Timber.d("get_ect error : $e")
                pullAndPersistEcrRecord()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_ect error : $e")
                return@withContext -1
            }
            -1
        }
    }


    suspend fun pullAndPersistEctRecord(): Int {
        return withContext(Dispatchers.IO) {
            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")
            val lastTimeStamp = Konstants.defaultTimeStamp
            try {
                val response = tmcNetworkApiService.getEcrFormData(
                    GetDataPaginatedRequest(
                        user.userId, 0, getCurrentDate(lastTimeStamp), getCurrentDate()
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
                                    val dataObj = jsonObj.getJSONArray("data")
                                    val ecrList = getEctCacheFromServerResponse(dataObj)
                                    ecrList.filter {
                                        !database.ecrDao.ectWithsameCreateDateExists(it.createdDate) && database.benDao.getBen(
                                            it.benId
                                        ) != null
                                    }.takeIf { it.isNotEmpty() }?.let {
                                        database.ecrDao.upsert(*it.toTypedArray())
                                    }
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
                Timber.d("get_ect error : $e")
                pullAndPersistEctRecord()

            } catch (e: java.lang.IllegalStateException) {
                Timber.d("get_ect error : $e")
                return@withContext -1
            }
            -1
        }
    }

    private fun getEcrCacheFromServerResponse(dataObj: JSONArray): List<EligibleCoupleRegCache> {
//        TODO()
        val list = mutableListOf<EligibleCoupleRegCache>()
        for (i in 0 until dataObj.length()) {
            val ecrJson = dataObj.getJSONObject(i)
            val ecr = EligibleCoupleRegCache(
                benId = ecrJson.getLong("benId"),
//                dateOfReg = if (ecrJson.has("dateOfReg")) getLongFromDate(ecrJson.getString("dateOfReg")) else getLongFromDate(
//                    ecrJson.getString("createdDate")),
//                    rchId = null,
//                    name =null,
//                    husbandName = null,
//                    age = null,
//                    ageAtMarriage = null,
//                    aadharNo = null,
//                    bankAccount = null,
//                    bankName = null,
//                    branchName = null,
//                    ifsc =,
//                    noOfChildren =,
//                    noOfLiveChildren =,
//                    noOfMaleChildren =,
//                    noOfFemaleChildren =,
//                    dob1 =,
//                    age1 =,
//                    gender1 =,
//                    marriageFirstChildGap =,
//                    dob2 =,
//                    age2 =,
//                    gender2 =,
//                    firstAndSecondChildGap =,
//                    dob3 =,
//                    age3 =,
//                    gender3 =,
//                    secondAndThirdChildGap =,
//                    dob4 =,
//                    age4 =,
//                    gender4 =,
//                    thirdAndFourthChildGap =,
//                    dob5 =,
//                    age5 =,
//                    gender5 =,
//                    fourthAndFifthChildGap =,
//                    dob6 =,
//                    age6 =,
//                    gender6 =,
//                    fifthANdSixthChildGap =,
//                    dob7 =,
//                    age7 =,
//                    gender7 =,
//                    sixthAndSeventhChildGap =,
//                    dob8 =,
//                    age8 =,
//                    gender8 =,
//                    seventhAndEighthChildGap =,
//                    dob9 =,
//                    age9 =,
//                    gender9 =,
//                    eighthAndNinthChildGap =,
//                    processed =,
                createdBy = ecrJson.getString("createdBy"),
                createdDate = getLongFromDate(
                    ecrJson.getString("createdDate")
                ),
                updatedBy = if (ecrJson.has("updatedBy")) ecrJson.getString("updatedBy") else ecrJson.getString(
                    "createdBy"
                ),
                updatedDate = getLongFromDate(
                    if (ecrJson.has("updatedDate")) ecrJson.getString(
                        "updatedDate"
                    ) else ecrJson.getString("createdDate")
                ),
                syncState = SyncState.SYNCED
            )
            list.add(ecr)

        }
        return list
    }

    private fun getEctCacheFromServerResponse(dataObj: JSONArray): List<EligibleCoupleTrackingCache> {
//        TODO()
        return emptyList()
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