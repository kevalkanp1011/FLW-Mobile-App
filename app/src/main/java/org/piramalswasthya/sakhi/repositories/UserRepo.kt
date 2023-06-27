package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.ImmunizationDao
import org.piramalswasthya.sakhi.database.room.dao.UserDao
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.BIRTH
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.MONTH_16_24
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.MONTH_9_12
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.WEEK_10
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.WEEK_14
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.WEEK_6
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.YEAR_5_6
import org.piramalswasthya.sakhi.model.ImmunizationCategory.CHILD
import org.piramalswasthya.sakhi.model.ImmunizationCategory.MOTHER
import org.piramalswasthya.sakhi.model.LocationEntity
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.model.UserNetwork
import org.piramalswasthya.sakhi.model.Vaccine
import org.piramalswasthya.sakhi.network.AmritApiService
import org.piramalswasthya.sakhi.network.D2DApiService
import org.piramalswasthya.sakhi.network.D2DAuthUserRequest
import org.piramalswasthya.sakhi.network.TmcAuthUserRequest
import org.piramalswasthya.sakhi.network.TmcLocationDetailsRequest
import org.piramalswasthya.sakhi.network.TmcUserDetailsRequest
import org.piramalswasthya.sakhi.network.TmcUserVanSpDetailsRequest
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertD2DInterceptor
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertTmcInterceptor
import org.piramalswasthya.sakhi.ui.login_activity.sign_in.SignInViewModel.State
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val userDao: UserDao,
    benDao: BenDao,
    private val vaccineDao: ImmunizationDao,
    private val preferenceDao: PreferenceDao,
    private val d2dNetworkApi: D2DApiService,
    private val tmcNetworkApiService: AmritApiService
) {


    private var user: UserNetwork? = null

    val unProcessedRecordCount: Flow<Int> = benDao.getUnProcessedRecordCount()

    suspend fun checkAndAddVaccines() {
        if (vaccineDao.vaccinesLoaded())
            return
        val vaccineList = arrayOf(
            ////------------------CHILD-----------------///////////////
            //Birth
            Vaccine(
                id = 1,
                category = CHILD,
                childCategory = BIRTH,
                name = "OPV 0",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(15),
                overdueDurationSinceMinInMillis = TimeUnit.DAYS.toMillis(1),
            ),
            Vaccine(
                id = 2,
                category = CHILD,
                childCategory = BIRTH,
                name = "BCG 0",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
                overdueDurationSinceMinInMillis = TimeUnit.DAYS.toMillis(1),

            ),
            Vaccine(
                id = 3,
                category = CHILD,
                childCategory = BIRTH,
                name = "Hepatitis B 0",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1),

            ),
            Vaccine(
                id = 4,
                category = CHILD,
                childCategory = BIRTH,
                name = "Vit K",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1),
            ),
            //Week 6
            Vaccine(
                id = 5,
                category = CHILD,
                childCategory = WEEK_6,
                name = "OPV 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*2),
//                overdueDurationSinceMinInMillis =
            ),
            Vaccine(
                id = 6,
                category = CHILD,
                childCategory = WEEK_6,
                name = "Pentavalent 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
            ),
            Vaccine(
                id = 7,
                category = CHILD,
                childCategory = WEEK_6,
                name = "ROTA 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
            ),
            Vaccine(
                id = 8,
                category = CHILD,
                childCategory = WEEK_6,
                name = "IPV 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(6*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
            ),
            //Week 10
            Vaccine(
                id = 9,
                category = CHILD,
                childCategory = WEEK_10,
                name = "OPV 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(10*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*2),
                dependantVaccineId = 5,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 10,
                category = CHILD,
                childCategory = WEEK_10,
                name = "Pentavalent 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(10*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
                dependantVaccineId = 5,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 11,
                category = CHILD,
                childCategory = WEEK_14,
                name = "OPV 3",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(14*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*2),
                dependantVaccineId = 9,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 12,
                category = CHILD,
                childCategory = WEEK_14,
                name = "Pentavalent 3",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(14*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
                dependantVaccineId =10,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 13,
                category = CHILD,
                childCategory = WEEK_10,
                name = "ROTA 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(10*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*1),
                dependantVaccineId = 7,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 14,
                category = CHILD,
                childCategory = WEEK_14,
                name = "ROTA 3",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(14*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*1),
                dependantVaccineId = 7,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 15,
                category = CHILD,
                childCategory = WEEK_14,
                name = "IPV 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(8*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*1),
                dependantVaccineId = 8,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(28)
            ),
            Vaccine(
                id = 16,
                category = CHILD,
                childCategory = MONTH_16_24,
                name = "OPV Booster 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*2),
            ),
            Vaccine(
                id = 17,
                category = CHILD,
                childCategory = MONTH_16_24,
                name = "DPT Booster 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*7),
            ),
            Vaccine(
                id = 18,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "DPT Booster 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(356*5),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*7),
            ),

            Vaccine(
                id = 19,
                category = CHILD,
                childCategory = MONTH_9_12,
                name = "Measles 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(274),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
            ),
            Vaccine(
                id = 20,
                category = CHILD,
                childCategory = MONTH_16_24,
                name = "Measles 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
            ),
            Vaccine(
                id = 21,
                category = CHILD,
                childCategory = MONTH_9_12,
                name = "JE Vaccine – 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(274),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365),
            ),
            Vaccine(
                id = 22,
                category = CHILD,
                childCategory = MONTH_16_24,
                name = "JE Vaccine – 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
            ),
            Vaccine(
                id = 23,
                category = CHILD,
                childCategory = MONTH_9_12,
                name = "Vitamin A – 1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(274),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
            ),
            Vaccine(
                id = 24,
                category = CHILD,
                childCategory = MONTH_16_24,
                name = "Vitamin A – 2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(487),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 23,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(274)
            ),
            Vaccine(
                id = 25,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 3",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*2),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 24,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            Vaccine(
                id = 26,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 4",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(913),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 25,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            Vaccine(
                id = 27,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 5",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1095),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 26,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            Vaccine(
                id = 28,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 6",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1278),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 27,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            Vaccine(
                id = 29,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 7",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1460),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 28,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            Vaccine(
                id = 30,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 8",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(1643),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                dependantVaccineId = 29,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            Vaccine(
                id = 31,
                category = CHILD,
                childCategory = YEAR_5_6,
                name = "Vitamin A – 9",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*5),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(365*7),
                dependantVaccineId = 30,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(183)
            ),
            ////------------------MOTHER-----------------///////////////
            Vaccine(
                id = 32,
                category = MOTHER,
                childCategory = YEAR_5_6,
                name = "Td-1",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(36*7),
            ),
            Vaccine(
                id = 33,
                category = MOTHER,
                childCategory = YEAR_5_6,
                name = "Td-2",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(4*7),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(36*7),
                dependantVaccineId = 32,
                dependantCoolDuration = TimeUnit.DAYS.toMillis(4*7)
            ),
            Vaccine(
                id = 34,
                category = MOTHER,
                childCategory = YEAR_5_6,
                name = "Td-Booster",
                minAllowedAgeInMillis = TimeUnit.DAYS.toMillis(0),
                maxAllowedAgeInMillis = TimeUnit.DAYS.toMillis(36*7),
            ),

            )
        vaccineDao.addVaccine(*vaccineList)
    }


    suspend fun getLoggedInUser(): UserDomain? {
        return withContext(Dispatchers.IO) {
            userDao.getLoggedInUser()?.asDomainModel()
        }
    }


    suspend fun authenticateUser(userName: String, password: String, state: String): State {
        return withContext(Dispatchers.IO) {
            val loggedInUser = userDao.getLoggedInUser()
            loggedInUser?.let {
                if (it.userName == userName && it.password == password) {
                    val tokenA = preferenceDao.getD2DApiToken()
                    val tokenB = preferenceDao.getPrimaryApiToken()
                    TokenInsertD2DInterceptor.setToken(
                        tokenA
                            ?: throw IllegalStateException("User logging offline without pref saved token A!")
                    )
                    TokenInsertTmcInterceptor.setToken(
                        tokenB
                            ?: throw IllegalStateException("User logging offline without pref saved token B!")
                    )
                    Timber.w("User Logged in!")

                    return@withContext State.SUCCESS
                }
            }

            try {
                if (getTokenD2D(userName, password)) {
                    getTokenTmc(userName, password)
                    if (user != null) {
                        val result = getUserDetails(state)
                        if (result) {
                            Timber.d("User Auth Complete!!!!")
                            user?.loggedIn = true
                            if (userDao.getLoggedInUser()?.userName == userName) {
                                userDao.update(user!!.asCacheModel())
                            } else {
                                userDao.resetAllUsersLoggedInState()
                                userDao.insert(user!!.asCacheModel())
                            }
                            return@withContext State.SUCCESS
                        }
                    }
                    return@withContext State.ERROR_SERVER
                }
                return@withContext State.ERROR_INPUT
            } catch (se: SocketTimeoutException) {
                return@withContext State.ERROR_SERVER
            } catch (ce: ConnectException) {
                return@withContext State.ERROR_NETWORK
            } catch (ue: UnknownHostException) {
                return@withContext State.ERROR_NETWORK
            } catch (ce: ConnectException) {
                return@withContext State.ERROR_NETWORK
            }
        }
    }

    private suspend fun getUserDetails(stateToggle: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response =
                tmcNetworkApiService.getUserDetailsById(TmcUserDetailsRequest(user!!.userId))
            Timber.d("User Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                //pDialog.dismiss();

                //pDialog.dismiss();
                val responseString = response.body()?.string() ?: return@withContext false
                val responseJson = JSONObject(responseString)
                val data = responseJson.getJSONObject("data")
                val user = data.getJSONObject("user")
                val emergencyContactNo = user.getString("emergencyContactNo")
                this@UserRepo.user?.emergencyContactNo = emergencyContactNo
                val userId = user.getInt("userID")
                //val userName = user.getString("userName")

                val healthInstitution = data.getJSONObject("healthInstitution")
                val state = healthInstitution.getJSONObject("state")
                val stateId = state.getInt("stateID")
                val stateName = state.getString("stateName")
                val district = healthInstitution.getJSONArray("districts").getJSONObject(0)
                val districtId = district.getInt("districtID")
                val districtName = district.getString("districtName")
                val block = healthInstitution.getJSONArray("blockids").getJSONObject(0)
                val blockId = block.getInt("blockID")
                val blockName = block.getString("blockName")
                val countryId = state.getInt("countryID")
                this@UserRepo.user?.apply {
                    if (stateToggle != "Assam") {
                        this.states.add(
                            LocationEntity(
                                stateId,
                                stateName,
                            )
                        )
                        this.districts.add(
                            LocationEntity(
                                districtId,
                                districtName,
                            )
                        )
                        this.blocks.add(
                            LocationEntity(
                                blockId,
                                blockName,
                            )
                        )
                    }
                    this.country = LocationEntity(
                        countryId, "India"
                    )
                }
                val roleJsonArray = data.getJSONArray("roleids")
                val role =
                    if (roleJsonArray.getJSONObject(0)["designationName"].toString()
                            .equals("Nurse", ignoreCase = true)
                    ) {
                        "ANM"
                    } else {
                        "ASHA"
                    }
                this@UserRepo.user?.userType = role
                getUserVillageDetails(userId, stateToggle)
                // usertype = role
            } else
                false
        }
    }

    private suspend fun getUserVillageDetails(userId: Int, state: String): Boolean {
        return when (state) {
            "Bihar" -> getVillageDetailsForBihar(userId)
            "Assam" -> getVillageDetailsForAssam(userId)
            else -> throw IllegalStateException("No State Passed to Repo ! ! !")
        }
    }

    private suspend fun getVillageDetailsForAssam(userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = d2dNetworkApi.getVillageDataForAssam(userId)
            Timber.d("Village Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string() ?: return@withContext false
                val responseJson = JSONObject(responseString)
                val data = responseJson.getJSONArray("data")
                //Uncomment all commented lines for ASSAM within for loop
                for (i in 0 until data.length()) {
                    val dataEntry = data.getJSONObject(i)
                    val state = dataEntry.getJSONObject("state")
                    val stateId = state.getInt("id")
                    val stateNameEnglish = state.getString("stateNameInEnglish")
                    val stateNameHindi = state.getString("stateNameInhindi")
                    user?.states?.add(
                        LocationEntity(
                            stateId, stateNameEnglish, stateNameHindi
                        )
                    )
//                    user?.stateIds?.add(stateId)
//                    user?.stateEnglish?.add(stateNameEnglish)
//                    user?.stateHindi?.add(stateNameHindi)
                    val districts = dataEntry.getJSONArray("district")
                    for (j in 0 until districts.length()) {
                        val district = districts.getJSONObject(j)
//                        val stateId2 = district.getInt("stateId")
                        val districtId = district.getInt("id")
                        val districtNameEnglish = district.getString("districtNameInEnglish")
                        val districtNameHindi = district.getString("districtNameInHindi")
                        user?.districts?.add(
                            LocationEntity(
                                districtId, districtNameEnglish, districtNameHindi
                            )
                        )
                        //TODO(Save Above data somewhere)
                    }
                    val blocks = dataEntry.getJSONArray("block")
                    for (k in 0 until blocks.length()) {
                        val block = blocks.getJSONObject(k)
                        val blockId = block.getInt("blockId")
                        val blockNameEnglish = block.getString("blockNameInEnglish")
                        val blockNameHindi = block.getString("blockNameIndHindi")
                        user?.blocks?.add(
                            LocationEntity(
                                blockId, blockNameEnglish, blockNameHindi
                            )
                        )
                        //TODO(Save Above data somewhere)
                    }
//                val villages = data.getJSONArray("villages")
                    for (l in 0 until data.length()) {
                        val village =
                            data.getJSONObject(l).getJSONArray("villages").getJSONObject(0)
                        val villageId = village.getInt("villageid")
                        val villageNameEnglish = village.getString("villageNameEnglish")
                        val villageNameHindi = village.getString("villageNameHindi")
                        user?.villages?.add(
                            LocationEntity(
                                villageId, villageNameEnglish, villageNameHindi
                            )
                        )
                        //TODO(Save Above data somewhere)
                    }
                }
                getUserVanSpDetails()
            } else {
                false
            }

        }
    }

    private suspend fun getVillageDetailsForBihar(userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = d2dNetworkApi.getVillageDataForBihar(userId)
            Timber.d("Village Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string() ?: return@withContext false
                val responseJson = JSONObject(responseString)
                val data = responseJson.getJSONArray("data")
                for (l in 0 until data.length()) {
                    val village = data.getJSONObject(l).getJSONArray("villages").getJSONObject(0)
                    val villageId = village.getInt("villageid")
                    val villageNameEnglish = village.getString("villageNameEnglish")
                    val villageNameHindi = village.getString("villageNameHindi")
                    user?.villages?.add(
                        LocationEntity(
                            villageId, villageNameEnglish, villageNameHindi
                        )
                    )
//                    user?.villageIds?.add(villageId)
//                    user?.villageEnglish?.add(villageNameEnglish)
//                    user?.villageHindi?.add(villageNameHindi)
                    //TODO(Save Above data somewhere)
                }
                getUserVanSpDetails()
            } else {
                false
            }

        }
    }

    private suspend fun getUserVanSpDetails(): Boolean {
        return withContext(Dispatchers.IO) {
            val response = tmcNetworkApiService.getTMVanSpDetails(
                TmcUserVanSpDetailsRequest(
                    user!!.userId,
                    user!!.serviceMapId
                )
            )
            Timber.d("User Van Sp Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string() ?: return@withContext false
                val responseJson = JSONObject(responseString)
                val data = responseJson.getJSONObject("data")
                val vanSpDetailsArray = data.getJSONArray("UserVanSpDetails")
//                val vanId =
//                    vanSpDetailsArray.getJSONObject(0).getInt("vanID")
//                val servicePointID =
//                    vanSpDetailsArray.getJSONObject(0).getInt("servicePointID")
//                val servicePointName =
//                    vanSpDetailsArray.getJSONObject(0).getString("servicePointName")
//                val parkingPlaceID =
//                    vanSpDetailsArray.getJSONObject(0).getInt("parkingPlaceID")
                for (i in 0 until vanSpDetailsArray.length()) {
                    val vanSp = vanSpDetailsArray.getJSONObject(i)
                    val vanId = vanSp.getInt("vanID")
                    user?.vanId = vanId
                    //val name = vanSp.getString("vanNoAndType")
                    val servicePointId = vanSp.getInt("servicePointID")
                    user?.servicePointId = servicePointId
                    val servicePointName = vanSp.getString("servicePointName")
                    user?.servicePointName = servicePointName
                    user?.parkingPlaceId = vanSp.getInt("parkingPlaceID")

                }
                getLocationDetails()
            } else {
                false
            }
        }
    }

    private suspend fun getLocationDetails(): Boolean {
        return withContext(Dispatchers.IO) {
            val response = tmcNetworkApiService.getLocationDetails(
                TmcLocationDetailsRequest(
                    user!!.servicePointId,
                    user!!.serviceMapId
                )
            )
            Timber.d("User Van Sp Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string() ?: return@withContext false
                val responseJson = JSONObject(responseString)
                val dataJson = responseJson.getJSONObject("data")
                val otherLocation = dataJson.getJSONObject("otherLoc")
                val parkingPlaceName = otherLocation.getString("parkingPlaceName")
                val zoneId = otherLocation.getInt("zoneID")
                val parkingPlaceId = otherLocation.getInt("parkingPlaceID")
                val zoneName = otherLocation.getString("zoneName")
                this@UserRepo.user?.apply {
                    this.parkingPlaceId = parkingPlaceId
                    this.parkingPlaceName = parkingPlaceName
                    this.zoneName = zoneName
                    this.zoneId = zoneId
                }
                true
            } else
                false
        }
    }

    private suspend fun getTokenD2D(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    d2dNetworkApi.getJwtToken(D2DAuthUserRequest(userName, password))
                Timber.d("JWT : $response")
                TokenInsertD2DInterceptor.setToken(response.jwt)
                preferenceDao.registerD2DApiToken(response.jwt)
                //saveUserD2D()
                true
            } catch (e: retrofit2.HttpException) {
                Timber.d("Auth Failed!")
                false
            }

        }
    }

    /*    private suspend fun saveUserD2D() {

        }*/

    suspend fun refreshTokenTmc(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    tmcNetworkApiService.getJwtToken(TmcAuthUserRequest(userName, password))
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
                    preferenceDao.registerPrimaryApiToken(token)
                    return@withContext true
                } else {
                    val errorMessage = responseBody.getString("errorMessage")
                    Timber.d("Error Message $errorMessage")
                }
                return@withContext false
            } catch (se: SocketTimeoutException) {
                return@withContext refreshTokenTmc(userName, password)
            } catch (e: retrofit2.HttpException) {
                Timber.d("Auth Failed!")
                return@withContext false
            }


        }

    }

    suspend fun refreshTokenD2d(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    d2dNetworkApi.getJwtToken(D2DAuthUserRequest(userName, password))
                Timber.d("JWT : $response")
                TokenInsertD2DInterceptor.setToken(response.jwt)
                preferenceDao.registerD2DApiToken(response.jwt)
                //saveUserD2D()
                true
            } catch (e: java.lang.Exception) {
                Timber.d("Auth Failed!")
                false
            }

        }
    }

    private suspend fun getTokenTmc(userName: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    tmcNetworkApiService.getJwtToken(TmcAuthUserRequest(userName, password))
                Timber.d("JWT : $response")
                if (!response.isSuccessful) {
                    return@withContext
                }
                val responseBody = JSONObject(
                    response.body()?.string()
                        ?: throw IllegalStateException("Response success but data missing @ $response")
                )
                val responseStatusCode = responseBody.getInt("statusCode")
                if (responseStatusCode == 200) {
                    val data = responseBody.getJSONObject("data")
                    val token = data.getString("key")
                    val userId = data.getInt("userID")

                    val privilegesArray = data.getJSONArray("previlegeObj")
                    val privilegesObject = privilegesArray.getJSONObject(0)

                    user = UserNetwork(userId, userName, password)
                    val serviceId = privilegesObject.getInt("serviceID")
                    user?.serviceId = serviceId
                    val serviceMapId =
                        privilegesObject.getInt("providerServiceMapID")
                    user?.serviceMapId = serviceMapId
                    TokenInsertTmcInterceptor.setToken(token)
                    preferenceDao.registerPrimaryApiToken(token)
                } else {
                    val errorMessage = responseBody.getString("errorMessage")
                    Timber.d("Error Message $errorMessage")
                }
            } catch (e: retrofit2.HttpException) {
                Timber.d("Auth Failed!")
            }


        }

    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            val loggedInUser = userDao.getLoggedInUser()!!
            userDao.logout(loggedInUser)
        }
    }


}