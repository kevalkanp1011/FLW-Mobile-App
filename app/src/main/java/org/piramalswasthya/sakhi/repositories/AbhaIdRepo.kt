package org.piramalswasthya.sakhi.repositories

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

    suspend fun generateOtpForAadhaar(aadhaarNo: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.generateAadhaarOtp(AbhaGenerateAadhaarOtpRequest(aadhaarNo)).txnId
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun generateOtpForAadhaarDummy(aadhaarNo: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(3000)
                "XYZ"
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun verifyOtpForAadhaar(otp: String, txnIdFromArgs: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.verifyAadhaarOtp(
                    AbhaVerifyAadhaarOtpRequest(
                        otp,
                        txnIdFromArgs
                    )
                ).txnId
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun verifyOtpForAadhaarDummy(otp: String, txnIdFromArgs: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(4000)
                "XYZ"
            } catch (e: java.lang.Exception) {
                null
            }
        }

    }

    suspend fun generateOtpForMobileNumber(mobileNumber: String, txnId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.generateMobileOtp(
                    AbhaGenerateMobileOtpRequest(
                        mobileNumber,
                        txnId
                    )
                ).txnId
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun generateOtpForMobileNumberDummy(
        mobileNumber: String,
        txnIdFromArgs: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(4000)
                "XYZ"
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun verifyOtpForMobileNumber(otp: String, txnId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                abhaApiService.verifyMobileOtp(
                    AbhaVerifyMobileOtpRequest(
                        otp,
                        txnId
                    )
                ).txnId
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun verifyOtpForMobileNumberDummy(otp: String, txnIdFromArgs: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Thread.sleep(4000)
                "XYZ"
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

}