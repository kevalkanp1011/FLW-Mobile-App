package org.piramalswasthya.sakhi.repositories

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.helpers.hasPendingAncVisit
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.AmritApiService
import org.piramalswasthya.sakhi.network.GetDataPaginatedRequest
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MaternalHealthRepo @Inject constructor(
    private val amritApiService: AmritApiService,
    private val maternalHealthDao: MaternalHealthDao,
    private val userRepo: UserRepo,
    private val benDao: BenDao,
    private val preferenceDao: PreferenceDao,
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
            val regis = it.key
            Timber.d(
                "Values emitted : ${
                    it.value.map {
                        AncStatus(
                            it.benId,
                            it.visitNumber,
                            AncFormState.ALREADY_FILLED,
                            (TimeUnit.MILLISECONDS.toDays(regis.lmpDate - it.ancDate) / 7).toInt()

                        )
                    }
                }"
            )

            val visitPending = hasPendingAncVisit(
                it.value.map {
                    AncStatus(
                        it.benId,
                        it.visitNumber,
                        AncFormState.ALREADY_FILLED,
                        (TimeUnit.MILLISECONDS.toDays(regis.lmpDate - it.ancDate) / 7).toInt()

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
            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val ancList = maternalHealthDao.getAllUnprocessedAncVisits()

            val ancPostList = mutableSetOf<ANCPost>()

            ancList.forEach {
                ancPostList.clear()
                val ben = benDao.getBen(it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                ancPostList.add(it.asPostModel())
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

    private suspend fun postDataToAmritServer(ancPostList: MutableSet<ANCPost>): Boolean {
        if (ancPostList.isEmpty()) return false
        val user =
            preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

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
                                if (userRepo.refreshTokenTmc(
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

    suspend fun processNewPwr(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

            val pwrList = maternalHealthDao.getAllUnprocessedPWRs()

            val pwrPostList = mutableSetOf<PwrPost>()

            pwrList.forEach {
                pwrPostList.clear()
                val ben = benDao.getBen(it.benId)
                    ?: throw IllegalStateException("No beneficiary exists for benId: ${it.benId}!!")
                pwrPostList.add(it.asPwrPost())
                it.syncState = SyncState.SYNCING
                maternalHealthDao.updatePwr(it)
                val uploadDone = postPwrToAmritServer(pwrPostList)
                if (uploadDone) {
                    it.processed = "P"
                    it.syncState = SyncState.SYNCED
                } else {
                    it.syncState = SyncState.UNSYNCED
                }
                maternalHealthDao.updatePwr(it)
            }

            return@withContext true
        }
    }

    private suspend fun postPwrToAmritServer(pwrPostList: MutableSet<PwrPost>): Boolean {
        if (pwrPostList.isEmpty()) return false
        val user =
            preferenceDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")

        try {

            val response = amritApiService.postPwrForm(pwrPostList.toList())
            val statusCode = response.code()

            if (statusCode == 200) {
                try {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errormessage = jsonObj.getString("errorMessage")
                        if (jsonObj.isNull("statusCode")) throw IllegalStateException("Amrit server not responding properly, Contact Service Administrator!!")

                        when (jsonObj.getInt("statusCode")) {
                            200 -> {
                                Timber.d("Saved Successfully to server")
                                return true
                            }

                            5002 -> {
                                if (userRepo.refreshTokenTmc(
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
            Timber.w("Bad Response from server, need to check $pwrPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postPwrToAmritServer(pwrPostList)
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
        }
    }

    suspend fun getPwrDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            val lastTimeStamp = Konstants.defaultTimeStamp
            try {
                val response = amritApiService.getPwrData(
                    GetDataPaginatedRequest(
                        ashaId = user.userId,
                        pageNo = 0,
                        fromDate = getCurrentDate(lastTimeStamp),
                        toDate = getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit Pregnant women data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    savePwrCacheFromResponse(dataObj)
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

    private suspend fun savePwrCacheFromResponse(dataObj: String): List<PwrPost> {
        var pwrList = Gson().fromJson(dataObj, Array<PwrPost>::class.java).toList()
        pwrList.forEach { pwrDTO ->
            pwrDTO.createdDate?.let {
                var pwrCache: PregnantWomanRegistrationCache? =
                    maternalHealthDao.getSavedRecord(pwrDTO.benId)
                if (pwrCache == null) {
                    maternalHealthDao.saveRecord(pwrDTO.toPwrCache())
                }
            }
        }
        return pwrList
    }

    suspend fun getAncVisitDetailsFromServer(): Int {
        return withContext(Dispatchers.IO) {
            val user =
                preferenceDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            val lastTimeStamp = Konstants.defaultTimeStamp
            try {
                val response = amritApiService.getAncVisitsData(
                    GetDataPaginatedRequest(
                        ashaId = user.userId,
                        pageNo = 0,
                        fromDate = getCurrentDate(lastTimeStamp),
                        toDate = getCurrentDate()
                    )
                )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        Timber.d("Pull from amrit ANC Visit data : $responseStatusCode")
                        when (responseStatusCode) {
                            200 -> {
                                try {
                                    val dataObj = jsonObj.getString("data")
                                    saveANCCacheFromResponse(dataObj)
                                } catch (e: Exception) {
                                    Timber.d("ANC visit entries not synced $e")
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
                                throw IllegalStateException("$responseStatusCode received, don't know what todo!?")
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

    private suspend fun saveANCCacheFromResponse(dataObj: String): List<ANCPost> {
        var ancList = Gson().fromJson(dataObj, Array<ANCPost>::class.java).toList()
        ancList.forEach { ancDTO ->
            ancDTO.createdDate?.let {
                var pwrCache: PregnantWomanRegistrationCache? =
                    maternalHealthDao.getSavedRecord(ancDTO.benId)
                if (pwrCache == null) {
                    maternalHealthDao.saveRecord(ancDTO.toAncCache())
                }
            }
        }
        return ancList
    }

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        fun getCurrentDate(millis: Long = System.currentTimeMillis()): String {
            val dateString = dateFormat.format(millis)
            val timeString = timeFormat.format(millis)
            return "${dateString}T${timeString}.000Z"
        }
    }
}