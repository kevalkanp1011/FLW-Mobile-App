package org.piramalswasthya.sakhi.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.network.*
import javax.inject.Inject


class AbhaIdRepo @Inject constructor(
    private val abhaApiService: AbhaApiService
) {

    suspend fun getAccessToken(): AbhaTokenResponse? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.getToken()
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun getAccessTokenDummy(): AbhaTokenResponse? {
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(3000)
                AbhaTokenResponse("ABC", 0, 0, "RRV", "Buller")
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun generateOtpForAadhaar(req: AbhaGenerateAadhaarOtpRequest): AbhaGenerateAadhaarOtpResponse? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.generateAadhaarOtp(req)
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun generateOtpForAadhaarDummy(req: AbhaGenerateAadhaarOtpRequest): AbhaGenerateAadhaarOtpResponse? {
        Log.i("AbhaIdRepo", req.aadhaar)

        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(3000)
                AbhaGenerateAadhaarOtpResponse("XYZ")
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun verifyOtpForAadhaar(req: AbhaVerifyAadhaarOtpRequest): AbhaVerifyAadhaarOtpResponse? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.verifyAadhaarOtp(req)
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun verifyOtpForAadhaarDummy(req: AbhaVerifyAadhaarOtpRequest): AbhaVerifyAadhaarOtpResponse? {
        Log.i("AbhaIdRepo", req.txnId)
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(4000)
                AbhaVerifyAadhaarOtpResponse("XYZ")
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun generateOtpForMobileNumber(req: AbhaGenerateMobileOtpRequest): AbhaGenerateMobileOtpResponse? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.generateMobileOtp(req)
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun generateOtpForMobileNumberDummy(req: AbhaGenerateMobileOtpRequest): AbhaGenerateMobileOtpResponse? {
        Log.i("AbhaIdRepo", req.txnId)
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(4000)
                AbhaGenerateMobileOtpResponse("XYZ")
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun verifyOtpForMobileNumber(req: AbhaVerifyMobileOtpRequest): AbhaVerifyMobileOtpResponse? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.verifyMobileOtp(req)
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun verifyOtpForMobileNumberDummy(req: AbhaVerifyMobileOtpRequest): AbhaVerifyMobileOtpResponse? {
        Log.i("AbhaIdRepo", req.txnId)
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(4000)
                AbhaVerifyMobileOtpResponse("XYZ")
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun generateAbhaId(req: CreateAbhaIdRequest): CreateAbhaIdResponse? {
        return withContext((Dispatchers.IO)) {
            try {
                val res = abhaApiService.createAbhaId(req)
                Log.i("AbhaIdRepo", res.toString())
                res
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun generateAbhaIdDummy(req: CreateAbhaIdRequest): CreateAbhaIdResponse? {
        Log.i("AbhaIdRepo", req.txnId)
        return withContext((Dispatchers.IO)) {
            try {
                Thread.sleep(4000)
                CreateAbhaIdResponse(
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                    "",
                    "43-4221-5105-6749",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "akash",
                    "",
                    "",
                    "",
                    "PUNE",
                    "401",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "AADHAAR_OTP",
                    "",
                    mutableMapOf<String, String>(),
                    "",
                    ""
                )
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

}