package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.network.AbhaApiService
import org.piramalswasthya.sakhi.network.AbhaTokenResponse
import javax.inject.Inject


class AbhaIdRepo @Inject constructor(
    private val abhaApiService: AbhaApiService
){

    suspend fun getAccessToken() : AbhaTokenResponse?{
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.getToken()
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

}