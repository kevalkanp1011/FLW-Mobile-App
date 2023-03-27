package org.piramalswasthya.sakhi.repositories

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Base64
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.network.*
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class AbhaIdRepo @Inject constructor(
    private val abhaApiService: AbhaApiService,
    private val prefDao: PreferenceDao
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

    suspend fun getAuthCert(): NetworkResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = abhaApiService.getAuthCert()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    var key = responseBody!!
                    key = key.replace("-----BEGIN PUBLIC KEY-----\n", "")
                    key = key.replace("-----END PUBLIC KEY-----", "")
                    key = key.trim()
                    NetworkResult.Success(key)
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

    suspend fun generateOtpForAadhaarV2(req: AbhaGenerateAadhaarOtpRequest): NetworkResult<AbhaGenerateAadhaarOtpResponseV2> {
        return withContext(Dispatchers.IO) {
            try {
                req.aadhaar = encryptData(req.aadhaar)
                val response = abhaApiService.generateAadhaarOtpV2(req)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val result =
                        Gson().fromJson(responseBody, AbhaGenerateAadhaarOtpResponseV2::class.java)
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

    private fun encryptData(txt: String): String {
        var encryptedTextBase64 = ""
        val publicKeyString = prefDao.getPublicKeyForAbha()!!
        Timber.d(publicKeyString)
        try {
            val publicKeyBytes = Base64.decode(publicKeyString, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
            val publicKey = keyFactory.generatePublic(publicKeySpec)

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val encryptedText = cipher.doFinal(txt.toByteArray())
            encryptedTextBase64 =
                Base64.encodeToString(encryptedText, Base64.DEFAULT).replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encryptedTextBase64
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

    suspend fun getPdfCard(context: Context, fileName: String) {
        return withContext(Dispatchers.IO) {
            try {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "pdf_download_channel"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelName = "PDF Download"
                    val channel = NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationId = 1
                val notificationBuilder = NotificationCompat.Builder(
                    context,
                    channelId
                )
                    .setSmallIcon(R.drawable.ic_download)
                    .setContentTitle(fileName)
                    .setContentText("Downloading in progess")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .setProgress(0, 0, true)
                notificationManager.notify(notificationId, notificationBuilder.build())

                val response = abhaApiService.getPdfCard()
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(directory, fileName)
                val responseBody = response.body()!!
                val inputStream = responseBody.byteStream()
                val outputStream = FileOutputStream(file)

                val buffer = ByteArray(1024)
                var bytesRead = inputStream.read(buffer)
                var totalBytesRead = bytesRead.toLong()
                var progress = 0
                while (bytesRead != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead
                    progress =
                        ((totalBytesRead.toDouble() / responseBody.contentLength()) * 100).toInt()
                    notificationBuilder
                        .setProgress(100, progress, false)
                        .setContentText("$progress")
                    notificationManager.notify(notificationId, notificationBuilder.build())
                    bytesRead = inputStream.read(buffer)
                }
                Timber.d("$progress")
                outputStream.close()
                inputStream.close()

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.toString()),
                    null,
                ) { _, uri ->
                    run {
                        // Update the notification after the file has been scanned
                        notificationBuilder.setContentTitle(fileName)
                            .setContentText("Download Completed")
                            .setProgress(0, 0, false)
                            .setOngoing(false)
                            .setAutoCancel(true)
                            .setContentIntent(
                                PendingIntent.getActivity(
                                    context,
                                    0,
                                    Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, "application/pdf")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    },
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                            )
                        notificationManager.notify(notificationId, notificationBuilder.build())

                    }
                }

//                notificationManager.cancel(notificationId)
            } catch (e: Exception) {
                e.printStackTrace()
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