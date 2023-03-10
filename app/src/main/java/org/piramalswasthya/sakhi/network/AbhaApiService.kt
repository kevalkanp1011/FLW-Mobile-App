package org.piramalswasthya.sakhi.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface AbhaApiService {

    @Headers("No-Auth: true")
    @POST
    suspend fun getToken(@Url url : String = "https://dev.abdm.gov.in/gateway/v0.5/sessions", @Body request: AbhaTokenRequest = AbhaTokenRequest()): AbhaTokenResponse

    @POST("generateOtp")
    suspend fun generateAadhaarOtp(@Body aadhaar : AbhaGenerateAadhaarOtpRequest) : AbhaGenerateAadhaarOtpResponse

    @POST("verifyOTP")
    suspend fun verifyAadhaarOtp(@Body request : AbhaVerifyAadhaarOtpRequest) : AbhaVerifyAadhaarOtpResponse

    @POST("generateMobileOTP")
    suspend fun generateMobileOtp(@Body mobile : AbhaGenerateMobileOtpRequest) : AbhaGenerateMobileOtpResponse

    @POST("verifyMobileOTP")
    suspend fun verifyMobileOtp(@Body request : AbhaVerifyMobileOtpRequest) : AbhaVerifyMobileOtpResponse

    @POST("createAbhaId")
    suspend fun createAbhaId(@Body request : CreateAbhaIdRequest) : CreateAbhaIdResponse










}