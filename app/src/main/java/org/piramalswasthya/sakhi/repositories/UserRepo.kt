package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.piramalswasthya.sakhi.crypt.CryptoUtil
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.ImmunizationDao
import org.piramalswasthya.sakhi.database.room.dao.SyncDao
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.NetworkResponse
import org.piramalswasthya.sakhi.model.SyncStatusCache
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.network.AmritApiService
import org.piramalswasthya.sakhi.network.TmcAuthUserRequest
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertTmcInterceptor
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class UserRepo @Inject constructor(
    benDao: BenDao,
    private val db : InAppDb,
    private val vaccineDao: ImmunizationDao,
    private val preferenceDao: PreferenceDao,
    private val syncDao: SyncDao,
    private val amritApiService: AmritApiService
) {

    val unProcessedRecordCount: Flow<List<SyncStatusCache>> = syncDao.getSyncStatus()


    suspend fun checkAndAddVaccines() {
        if (vaccineDao.vaccinesLoaded())
            return
//        val vaccineList = arrayOf(
//            ////------------------CHILD-----------------///////////////
//            //Birth
//            Vaccine(
//                vaccineId = 1,
//                category = CHILD,
//                immunizationService = BIRTH,
//                vaccineName = "OPV 0",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(15),
//                overdueDurationSinceMinInMillis = TimeUnit.DAYS.toMillis(1),
//            ),
//            Vaccine(
//                vaccineId = 2,
//                category = CHILD,
//                immunizationService = BIRTH,
//                vaccineName = "BCG 0",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//                overdueDurationSinceMinInMillis = TimeUnit.DAYS.toMillis(1),
//
//                ),
//            Vaccine(
//                vaccineId = 3,
//                category = CHILD,
//                immunizationService = BIRTH,
//                vaccineName = "Hepatitis B 0",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1),
//
//                ),
//            Vaccine(
//                vaccineId = 4,
//                category = CHILD,
//                immunizationService = BIRTH,
//                vaccineName = "Vit K",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1),
//            ),
//            //Week 6
//            Vaccine(
//                vaccineId = 5,
//                category = CHILD,
//                immunizationService = WEEK_6,
//                vaccineName = "OPV 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 2),
////                overdueDurationSinceMinInMillis =
//            ),
//            Vaccine(
//                vaccineId = 6,
//                category = CHILD,
//                immunizationService = WEEK_6,
//                vaccineName = "Pentavalent 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//            ),
//            Vaccine(
//                vaccineId = 7,
//                category = CHILD,
//                immunizationService = WEEK_6,
//                vaccineName = "ROTA 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//            ),
//            Vaccine(
//                vaccineId = 8,
//                category = CHILD,
//                immunizationService = WEEK_6,
//                vaccineName = "IPV 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//            ),
//            //Week 10
//            Vaccine(
//                vaccineId = 9,
//                category = CHILD,
//                immunizationService = WEEK_10,
//                vaccineName = "OPV 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(10 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 2),
//                dependantVaccineId = 5,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 10,
//                category = CHILD,
//                immunizationService = WEEK_10,
//                vaccineName = "Pentavalent 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(10 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//                dependantVaccineId = 5,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 11,
//                category = CHILD,
//                immunizationService = WEEK_14,
//                vaccineName = "OPV 3",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(14 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 2),
//                dependantVaccineId = 9,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 12,
//                category = CHILD,
//                immunizationService = WEEK_14,
//                vaccineName = "Pentavalent 3",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(14 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//                dependantVaccineId = 10,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 13,
//                category = CHILD,
//                immunizationService = WEEK_10,
//                vaccineName = "ROTA 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(10 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 1),
//                dependantVaccineId = 7,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 14,
//                category = CHILD,
//                immunizationService = WEEK_14,
//                vaccineName = "ROTA 3",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(14 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 1),
//                dependantVaccineId = 7,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 15,
//                category = CHILD,
//                immunizationService = WEEK_14,
//                vaccineName = "IPV 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(8 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 1),
//                dependantVaccineId = 8,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
//            ),
//            Vaccine(
//                vaccineId = 16,
//                category = CHILD,
//                immunizationService = MONTH_16_24,
//                vaccineName = "OPV Booster 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 2),
//            ),
//            Vaccine(
//                vaccineId = 17,
//                category = CHILD,
//                immunizationService = MONTH_16_24,
//                vaccineName = "DPT Booster 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 7),
//            ),
//            Vaccine(
//                vaccineId = 18,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "DPT Booster 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(356 * 5),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 7),
//            ),
//
//            Vaccine(
//                vaccineId = 19,
//                category = CHILD,
//                immunizationService = MONTH_9_12,
//                vaccineName = "Measles 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(274),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//            ),
//            Vaccine(
//                vaccineId = 20,
//                category = CHILD,
//                immunizationService = MONTH_16_24,
//                vaccineName = "Measles 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//            ),
//            Vaccine(
//                vaccineId = 21,
//                category = CHILD,
//                immunizationService = MONTH_9_12,
//                vaccineName = "JE Vaccine – 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(274),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
//            ),
//            Vaccine(
//                vaccineId = 22,
//                category = CHILD,
//                immunizationService = MONTH_16_24,
//                vaccineName = "JE Vaccine – 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//            ),
//            Vaccine(
//                vaccineId = 23,
//                category = CHILD,
//                immunizationService = MONTH_9_12,
//                vaccineName = "Vitamin A – 1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(274),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//            ),
//            Vaccine(
//                vaccineId = 24,
//                category = CHILD,
//                immunizationService = MONTH_16_24,
//                vaccineName = "Vitamin A – 2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 23,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(274)
//            ),
//            Vaccine(
//                vaccineId = 25,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 3",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 2),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 24,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 26,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 4",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(913),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 25,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 27,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 5",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1095),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 26,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 28,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 6",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1278),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 27,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 29,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 7",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1460),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 28,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 30,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 8",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1643),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                dependantVaccineId = 29,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 31,
//                category = CHILD,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Vitamin A – 9",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 5),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 7),
//                dependantVaccineId = 30,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
//            ),
//            Vaccine(
//                vaccineId = 32,
//                category = CHILD,
//                immunizationService = YEAR_10,
//                vaccineName = "TD Dose",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 10),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 16),
//            ),
//            Vaccine(
//                vaccineId = 33,
//                category = CHILD,
//                immunizationService = YEAR_16,
//                vaccineName = "TD Dose",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 16),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365 * 17),
//            ),
//            ////------------------MOTHER-----------------///////////////
//            Vaccine(
//                vaccineId = 34,
//                category = MOTHER,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Td-1",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(36 * 7),
//            ),
//            Vaccine(
//                vaccineId = 35,
//                category = MOTHER,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Td-2",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(4 * 7),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(36 * 7),
//                dependantVaccineId = 32,
//                dependantCoolDuration = TimeUnit.DAYS.toMillis(4 * 7)
//            ),
//            Vaccine(
//                vaccineId = 36,
//                category = MOTHER,
//                immunizationService = YEAR_5_6,
//                vaccineName = "Td-Booster",
//                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
//                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(36 * 7),
//            ),
//
//            )
//        vaccineDao.addVaccine(*vaccineList)
    }


    suspend fun authenticateUser(userName: String, password: String): NetworkResponse<User?> {
        return withContext(Dispatchers.IO) {
//            val offlineLoginResult = offlineLogin(userName, password)
//            if (offlineLoginResult)
//                return@withContext NetworkResponse.Success(null)
            try {
                val userId = getTokenAmrit(userName, password)
                val user = setUserRole(userId, password)
                return@withContext NetworkResponse.Success(user)
            } catch (se: SocketTimeoutException) {
                return@withContext NetworkResponse.Error(message = "Server timed out !")
            } catch (se: HttpException) {
                return@withContext NetworkResponse.Error(message = "Unable to connect to server !")
            } catch (ce: ConnectException) {
                return@withContext NetworkResponse.Error(message = "Server refused connection !")
            } catch (ue: UnknownHostException) {
                return@withContext NetworkResponse.Error(message = "Unable to connect to server !")
            } catch (ie: Exception) {
                if (ie.message == "Invalid username / password")
                    return@withContext NetworkResponse.Error(message = "Invalid Username/password")
                else
                    return@withContext NetworkResponse.Error(message = "Something went wrong... Try again later")

            }
        }
    }

    private suspend fun setUserRole(userId: Int, password: String): User {
        val response = amritApiService.getUserDetailsById(userId = userId)
        val user = response.data.toUser(password)
        preferenceDao.registerUser(user)
        return user
    }

//    private suspend fun setVanId(user: User): State {
//        return withContext(Dispatchers.IO) {
//            val response = amritApiService.getTMVanSpDetails(
//                TmcUserVanSpDetailsRequest(
//                    user.userId,
//                    user.serviceMapId
//                )
//            )
//            Timber.d("User Van Sp Details : $response")
//            val statusCode = response.code()
//            if (statusCode == 200) {
//                val responseString = response.body()?.string()
//                    ?: return@withContext State.ERROR_SERVER
//                val responseJson = JSONObject(responseString)
//                val data = responseJson.getJSONObject("data")
//                val vanSpDetailsArray = data.getJSONArray("UserVanSpDetails")
//
//                for (i in 0 until vanSpDetailsArray.length()) {
//                    val vanSp = vanSpDetailsArray.getJSONObject(i)
//                    val vanId = vanSp.getInt("vanID")
//                    user.vanId = vanId
//                }
//                return@withContext State.SUCCESS
//
//            } else {
//                return@withContext State.ERROR_SERVER
//            }
//        }
//    }

    private fun offlineLogin(userName: String, password: String): Boolean {
        val loggedInUser = preferenceDao.getLoggedInUser()
        loggedInUser?.let {
            if (it.userName == userName && it.password == password) {
                val amritToken = preferenceDao.getAmritToken()
                TokenInsertTmcInterceptor.setToken(
                    amritToken
                        ?: throw IllegalStateException("User logging offline without pref saved token B!")
                )
                Timber.w("User Logged in!")

                return true
            } else if (it.userName == userName) {
                throw IllegalStateException("Invalid Username/password")
                Timber.w("Invalid Username/password")
                return false
            }
        }
        return false
    }

    private fun encrypt(password: String): String {
        val util = CryptoUtil()
        return util.encrypt(password)
    }

    suspend fun refreshTokenTmc(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    amritApiService.getJwtToken(json = TmcAuthUserRequest(userName, encrypt(password)))
                Timber.d("JWT : $response")
                if (!response.isSuccessful) {
                    return@withContext false
                }
                val responseBody = JSONObject(
                    response.body()?.string()
                        ?: throw IllegalStateException("Response success but data missing @ $response")
                )
                val responseStatusCode = responseBody.getInt("statusCode")
                if (responseStatusCode == 200) {
                    val data = responseBody.getJSONObject("data")
                    val token = data.getString("key")
                    TokenInsertTmcInterceptor.setToken(token)
                    preferenceDao.registerAmritToken(token)
                    return@withContext true
                } else {
                    val errorMessage = responseBody.getString("errorMessage")
                    Timber.d("Error Message $errorMessage")
                }
                return@withContext false
            } catch (se: SocketTimeoutException) {
                return@withContext refreshTokenTmc(userName, password)
            } catch (e: HttpException) {
                Timber.d("Auth Failed!")
                return@withContext false
            } catch (e: Exception) {
                return@withContext false
            }


        }

    }

    private suspend fun getTokenAmrit(userName: String, password: String): Int {
        return withContext(Dispatchers.IO) {
            val encryptedPassword = encrypt(password)
            val response =
                amritApiService.getJwtToken(
                    json = TmcAuthUserRequest(
                        userName,
//                        password,
                        encryptedPassword
                    )
                )
            Timber.d("JWT : $response")
            val responseBody = JSONObject(
                response.body()?.string()
                    ?: throw IllegalStateException("Response success but data missing @ $response")
            )
            val statusCode = responseBody.getInt("statusCode")
            if (statusCode == 5002)
                throw IllegalStateException("Invalid username / password")
            val data = responseBody.getJSONObject("data")
            val token = data.getString("key")
            val userId = data.getInt("userID")
            db.clearAllTables()
            TokenInsertTmcInterceptor.setToken(token)
            preferenceDao.registerAmritToken(token)
            preferenceDao.lastAmritTokenFetchTimestamp = System.currentTimeMillis()
            return@withContext userId
        }
    }


}