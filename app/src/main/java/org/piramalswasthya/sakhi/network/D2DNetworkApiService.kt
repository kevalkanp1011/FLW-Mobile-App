package org.piramalswasthya.sakhi.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface D2DNetworkApiService {

    @Headers("No-Auth: true")
    @POST("user/authenticate")
    suspend fun getJwtToken(
        @Body userInput: D2DAuthUserRequest,
    ): D2DAuthUserResponse

    @POST("saveuser")
    suspend fun saveUserD2d(
        @Body userInput: D2DSaveUserRequest
    ): D2DSaveUserResponse

    @GET("VillageData/getVillageDetailsByAshaId")
    suspend fun getVillageData(
        @Query("ashaId") userId : Int
    ): Response<ResponseBody>


}