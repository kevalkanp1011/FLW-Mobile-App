package org.piramalswasthya.sakhi.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface AbhaApiService {

    @Headers("No-Auth: true")
    @POST
    suspend fun getToken(@Url url : String = "https://dev.abdm.gov.in/gateway/v0.5/sessions", @Body request: AbhaTokenRequest = AbhaTokenRequest()): AbhaTokenResponse



}