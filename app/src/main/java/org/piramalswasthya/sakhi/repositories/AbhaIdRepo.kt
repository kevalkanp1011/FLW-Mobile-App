package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.network.AbhaApiService
import org.piramalswasthya.sakhi.network.AbhaGenerateAadharOtpRequest
import org.piramalswasthya.sakhi.network.AbhaTokenResponse
import org.piramalswasthya.sakhi.network.AbhaVerifyAadharOtpRequest
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
    suspend fun getAccessTokenDummy() : AbhaTokenResponse?{
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(3000)
                AbhaTokenResponse("ABC",0,0,"RRV","Buller")
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun generateOtpForAadhar(aadharNo: String) : String?{
        return withContext(Dispatchers.IO){
            try{
                abhaApiService.generateAadharOtp(AbhaGenerateAadharOtpRequest(aadharNo)).txnId
            }catch (e: java.lang.Exception) {
                null
            }
        }

    }
    suspend fun generateOtpForAadharDummy(aadharNo: String) : String?{
        return withContext(Dispatchers.IO){
            try{
                Thread.sleep(3000)
                "XYZ"
            }catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun verifyOtpForAadhar(otp: String, txnIdFromArgs: String): String? {
        return withContext(Dispatchers.IO){
            try{
                abhaApiService.verifyAadharOtp(AbhaVerifyAadharOtpRequest(otp, txnIdFromArgs)).txnId
            }
            catch (e : java.lang.Exception){
                null
            }
        }

    }

    suspend fun verifyOtpForAadharDummy(otp: String, txnIdFromArgs: String): String? {
        return withContext(Dispatchers.IO){
            try{
                Thread.sleep(4000)
                "JKL"
            }
            catch (e : java.lang.Exception){
                null
            }
        }

    }

}