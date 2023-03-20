package org.piramalswasthya.sakhi.repositories

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.network.*
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AbhaIdRepo @Inject constructor(
    private val abhaApiService: AbhaApiService
) {
    suspend fun getAccessToken(): NetworkResult<AbhaTokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.getToken()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result = Gson().fromJson(responseBody, AbhaTokenResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun generateOtpForAadhaar(req: AbhaGenerateAadhaarOtpRequest): NetworkResult<AbhaGenerateAadhaarOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.generateAadhaarOtp(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(responseBody, AbhaGenerateAadhaarOtpResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun generateOtpForAadhaarDummy(req: AbhaGenerateAadhaarOtpRequest): NetworkResult<AbhaGenerateAadhaarOtpResponse> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(2000)
            NetworkResult.Success(AbhaGenerateAadhaarOtpResponse("XYZ"))
        }
    }

    suspend fun resendOtpForAadhaar(req: AbhaResendAadhaarOtpRequest): NetworkResult<AbhaGenerateAadhaarOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.resendAadhaarOtp(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(responseBody, AbhaGenerateAadhaarOtpResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun verifyOtpForAadhaar(req: AbhaVerifyAadhaarOtpRequest): NetworkResult<AbhaVerifyAadhaarOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.verifyAadhaarOtp(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(responseBody, AbhaVerifyAadhaarOtpResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }

    }

    suspend fun verifyOtpForAadhaarDummy(req: AbhaVerifyAadhaarOtpRequest): NetworkResult<AbhaVerifyAadhaarOtpResponse> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(2000)
            NetworkResult.Success(AbhaVerifyAadhaarOtpResponse("XYZ"))
        }

    }

    suspend fun generateOtpForMobileNumber(req: AbhaGenerateMobileOtpRequest): NetworkResult<AbhaGenerateMobileOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.generateMobileOtp(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(responseBody, AbhaGenerateMobileOtpResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun generateOtpForMobileNumberDummy(req: AbhaGenerateMobileOtpRequest): NetworkResult<AbhaGenerateMobileOtpResponse> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(2000)
            NetworkResult.Success(AbhaGenerateMobileOtpResponse("XYZ"))
        }
    }

    suspend fun checkAndGenerateOtpForMobileNumber(req: AbhaGenerateMobileOtpRequest): NetworkResult<AbhaCheckAndGenerateMobileOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.checkAndGenerateMobileOtp(request = req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(
                            responseBody,
                            AbhaCheckAndGenerateMobileOtpResponse::class.java
                        )
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun checkAndGenerateOtpForMobileNumberDummy(req: AbhaGenerateMobileOtpRequest): NetworkResult<AbhaCheckAndGenerateMobileOtpResponse> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(2000)
            NetworkResult.Success(AbhaCheckAndGenerateMobileOtpResponse(true, "XYZ"))
        }
    }

    suspend fun verifyOtpForMobileNumber(req: AbhaVerifyMobileOtpRequest): NetworkResult<AbhaVerifyMobileOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.verifyMobileOtp(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(responseBody, AbhaVerifyMobileOtpResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun verifyOtpForMobileNumberDummy(req: AbhaVerifyMobileOtpRequest): NetworkResult<AbhaVerifyMobileOtpResponse> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(2000)
            NetworkResult.Success(AbhaVerifyMobileOtpResponse("XYZ"))
        }
    }

    suspend fun generateAbhaId(req: CreateAbhaIdRequest): NetworkResult<CreateAbhaIdResponse> {
        return withContext((Dispatchers.IO)) {
            try {
                val response = abhaApiService.createAbhaId(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result = Gson().fromJson(responseBody, CreateAbhaIdResponse::class.java)
                    NetworkResult.Success(result)
                } else {
                    sendErrorResponse(response)
                }
            } catch (e: IOException) {
                NetworkResult.Error(-1, "Unable to connect to Internet!")
            } catch (e: JSONException) {
                NetworkResult.Error(-2, "Invalid response! Please try again!")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error(-3, "Request Timed out! Please try again!")
            } catch (e: java.lang.Exception) {
                NetworkResult.Error(-4, e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun generateAbhaIdDummy(req: CreateAbhaIdRequest): NetworkResult<CreateAbhaIdResponse> {
        return withContext((Dispatchers.IO)) {
            Thread.sleep(2000)
            NetworkResult.Success(
                CreateAbhaIdResponse(
                    "",
                    "",
                    "87-457-7451-784-784",
                    "Dummy Name",
                    "",
                    "",
                    "",
                    ", ",
                    "",
                    "",
                    ", ",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    arrayOf(),
                    "",
                    mapOf(),
                    "",
                    true
                )
            )
        }
    }

    suspend fun getPdfCard(context: Context) {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.getPdfCard()
                val fileName = "myPdf.pdf"
                val directory = context.applicationContext.getExternalFilesDir(null)
                val file = File(directory, fileName)
                val responseBody = response.body()!!
                val inputStream = responseBody.byteStream()
                val outputStream = FileOutputStream(file)

                val buffer = ByteArray(1024)
                var bytesRead = inputStream?.read(buffer)
                while (bytesRead != -1) {
                    outputStream.write(buffer, 0, bytesRead!!)
                    bytesRead = inputStream?.read(buffer)
                }
                outputStream.close()
                inputStream.close()
                Timber.d("PDF Download Completed")
            } catch (e: java.lang.Exception) {
                Timber.d("Get Pdf Card download failed")
            }
        }
    }

    suspend fun getPngCard() {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.getPngCard()
            } catch (e: java.lang.Exception) {
            }
        }
    }

    private fun sendErrorResponse(response: Response<ResponseBody>): NetworkResult.Error {
        return when (response.code()) {
            503 -> NetworkResult.Error(503, "Service Unavailable! Please try later!")
            else -> {
                val errorBody = response.errorBody()?.string()
                val errorJson = JSONObject(errorBody)
                val detailsArray = errorJson.getJSONArray("details")
                val detailsObject = detailsArray.getJSONObject(0)
                val errorMessage = detailsObject.getString("message")
                NetworkResult.Error(response.code(), errorMessage)
            }
        }
    }

}