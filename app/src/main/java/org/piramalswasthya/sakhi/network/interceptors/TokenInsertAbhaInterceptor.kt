package org.piramalswasthya.sakhi.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber

class TokenInsertAbhaInterceptor : Interceptor {
    companion object {
        private var TOKEN: String = ""
        fun setToken(iToken: String) {
            TOKEN = iToken
        }

        fun getToken(): String {
            return TOKEN
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.header("No-Auth") == null) {
            request = request
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer $TOKEN"
                )
                .build()
        }
        Timber.d("Request : $request")
        val response = chain.proceed(request)
        val httpStatus = response.code
        if (httpStatus >= 400) {
            val responseBody = response.body
            if (responseBody != null) {
                val errorMessage = responseBody.string()
                val newResponseBody = errorMessage.toResponseBody(responseBody.contentType())
                return response.newBuilder()
                    .body(newResponseBody)
                    .build()
            }
        }
        return response
    }
}
