package org.piramalswasthya.sakhi.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.piramalswasthya.sakhi.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface D2DApiService {

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
    suspend fun getVillageDataForBihar(
        @Query("ashaId") userId : Int
    ): Response<ResponseBody>

    @GET("AssamVillageData/getAssamVillageDetailsByAshaId")
    suspend fun getVillageDataForAssam(
        @Query("ashaId") userId : Int
    ): Response<ResponseBody>


    @POST("mdsrRegister")
    @Headers("Content-Type: application/json")
    suspend fun postMdsrForm(
        @Body mdsrPostList: List<MdsrPost>
    ): Response<ResponseBody>

    @POST("hbyncRegister")
    @Headers("Content-Type: application/json")
    suspend fun postHbycForm(
        @Body hbycPostList: List<HbycPost>
    ): Response<ResponseBody>

    @POST("cdrRegister")
    @Headers("Content-Type: application/json")
    suspend fun postCdrForm(
        @Body cdrPost: List<CDRPost>
    ): Response<ResponseBody>

    @POST("pmsmaData")
    @Headers("Content-Type: application/json")
    suspend fun postPmsmaForm(
        @Body pmsmaPost: List<PmsmaPost>
    ): Response<ResponseBody>

    @POST("pmjayData")
    @Headers("Content-Type: application/json")
    suspend fun postPmjayForm(
        @Body pmjayPostList: List<PMJAYPost>
    ): Response<ResponseBody>
}