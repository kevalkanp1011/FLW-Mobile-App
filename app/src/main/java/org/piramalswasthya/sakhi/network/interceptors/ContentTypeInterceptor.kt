package org.piramalswasthya.sakhi.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class ContentTypeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
        Timber.d("Request : $request")
        return chain.proceed(request)
    }
}