package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.model.UserNetwork
import org.piramalswasthya.sakhi.network.*
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertD2DInterceptor
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertTmcInterceptor
import timber.log.Timber
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val database: InAppDb,
    private val d2dNetworkApi: D2DNetworkApiService,
    private val tmcNetworkApiService: TmcNetworkApiService
) {
    private var user : UserNetwork? = null


    suspend fun getLoggedInUser() : UserDomain? {
        return withContext(Dispatchers.IO){
            database.userDao.getLoggedInUser()?.asDomainModel()
        }
    }


    suspend fun authenticateUser(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            if (getTokenD2D(userName, password)) {
                getTokenTmc(userName, password)
                if (user != null) {
                    val result = getUserDetails()
                    if(result) {
                        Timber.d("User Auth Complete!!!!")
                        user?.loggedIn = true
                        database.userDao.resetAllUsersLoggedInState()
                        database.userDao.upsert(user!!.asCacheModel())
                        return@withContext true
                    }
                }
            }
            false
        }
    }

    private suspend fun getUserDetails(): Boolean {
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
                val contactNo = user.getString("emergencyContactNo")
                this@UserRepo.user?.contactNo = contactNo
                val userId = user.getInt("userID")
                val userName = user.getString("userName")
                val healthInstitution = data.getJSONObject("healthInstitution")
                val state = healthInstitution.getJSONObject("state")
                val countryId = state.getInt("countryID")
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
                getUserVillageDetails(userId)
                // usertype = role
            } else
                false
        }
    }

    private suspend fun getUserVillageDetails(userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = d2dNetworkApi.getVillageData(userId)
            Timber.d("Village Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string() ?: return@withContext false
                val responseJson = JSONObject(responseString)
                val data = responseJson.getJSONArray("data")
                for (i in 0 until data.length()) {
                    val dataEntry = data.getJSONObject(i)
                    val state = dataEntry.getJSONObject("state")
                    val stateId = state.getInt("id")
                    val stateNameEnglish = state.getString("stateNameInEnglish")
                    val stateNameHindi = state.getString("stateNameInhindi")
                    user?.stateEnglish?.add(stateNameEnglish)
                    user?.stateHindi?.add(stateNameHindi)
                    val districts = dataEntry.getJSONArray("district")
                    for (j in 0 until districts.length()) {
                        val district = districts.getJSONObject(j)
                        val stateId2 = district.getInt("stateId")
                        val districtId = district.getInt("id")
                        val districtNameEnglish = district.getString("districtNameInEnglish")
                        val districtNameHindi = district.getString("districtNameInHindi")
                        user?.districtEnglish?.add(districtNameEnglish)
                        user?.districtHindi?.add(districtNameHindi)
                        //TODO(Save Above data somewhere)
                    }
                    val blocks = dataEntry.getJSONArray("block")
                    for (k in 0 until blocks.length()) {
                        val block = blocks.getJSONObject(k)
                        val blockId = block.getInt("blockId")
                        val blockNameEnglish = block.getString("blockNameInEnglish")
                        val blockNameHindi = block.getString("blockNameIndHindi")
                        user?.blockEnglish?.add(blockNameEnglish)
                        user?.blockHindi?.add(blockNameHindi)
                        //TODO(Save Above data somewhere)
                    }
                    val villages = dataEntry.getJSONArray("villages")
                    for (l in 0 until villages.length()) {
                        val village = villages.getJSONObject(l)
                        val villageId = village.getInt("villageid")
                        val villageNameEnglish = village.getString("villageNameEnglish")
                        val villageNameHindi = village.getString("villageNameHindi")
                        user?.villageEnglish?.add(villageNameEnglish)
                        user?.villageHindi?.add(villageNameHindi)
                        //TODO(Save Above data somewhere)
                    }
                }
                getUserVanSpDetails()
            } else {
                false
            }

        }
    }

    private suspend fun getUserVanSpDetails()  : Boolean{
        return withContext(Dispatchers.IO) {
            val response = tmcNetworkApiService.getTMVanSpDetails(TmcUserVanSpDetailsRequest(user!!.userId,user!!.serviceMapId))
            Timber.d("User Van Sp Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string()?: return@withContext false
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
                for(i in 0 until vanSpDetailsArray.length()){
                    val vanSp = vanSpDetailsArray.getJSONObject(i)
                    val id = vanSp.getInt("vanID")
                    val name = vanSp.getString("vanNoAndType")
                    val servicePointId = vanSp.getInt("servicePointID")
                    user?.servicePointId = servicePointId
                    val servicePointName = vanSp.getString("servicePointName")
                }
                getLocationDetails()
            }
            else{
                false
            }
        }
    }

    private suspend fun getLocationDetails() : Boolean{
        return withContext(Dispatchers.IO){
            val response = tmcNetworkApiService.getLocationDetails(TmcLocationDetailsRequest(user!!.servicePointId,user!!.serviceMapId))
            Timber.d("User Van Sp Details : $response")
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string()?:return@withContext false
                val responseJson = JSONObject(responseString)
                val dataJson = responseJson.getJSONObject("data")
                val otherLocation = dataJson.getJSONObject("otherLoc")
                val parkingPlaceName = otherLocation.getString("parkingPlaceName")
                val zoneId = otherLocation.getInt("zoneID")
                val parkingPlaceId = otherLocation.getInt("parkingPlaceID")
                val zoneName = otherLocation.getString("zoneName")
                true
            }
            else
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
                saveUserD2D()
                true
            } catch (e: retrofit2.HttpException) {
                Timber.d("Auth Failed!")
                false
            }

        }
    }

    private suspend fun saveUserD2D() {

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
                    val serviceID = privilegesObject.getString("serviceID")
                    val serviceMapId =
                        privilegesObject.getInt("providerServiceMapID")
                    user = UserNetwork(userId,userName, password)
                    user?.serviceMapId = serviceMapId
                    TokenInsertTmcInterceptor.setToken(token)
                } else {
                    val errorMessage = responseBody.getString("errorMessage")
                    Timber.d("Error Message $errorMessage")
                }
            } catch (e: retrofit2.HttpException) {
                Timber.d("Auth Failed!")
            }

        }

    }


}