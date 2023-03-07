package org.piramalswasthya.sakhi.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.*


object ImageSizeConverter {

    suspend fun getByteArrayFromImageUri(context: Context, uriString: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            val file =
                File(context.cacheDir, uriString.substringAfterLast("/"))
            val compressedFile = Compressor.compress(context, file) {
                size(10_000)
            }
            val iStream = compressedFile.inputStream()
            val byteArray = getBytes(iStream)
            iStream.close()
            byteArray
        }
    }

    private fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    suspend fun compressByteArray(context: Context, benId: Long, inputString: String) : ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                val inputByteArray = Base64.decode(inputString, Base64.DEFAULT)
                val file =
                    File(context.cacheDir, "$benId").also { it.createNewFile() }
                val os: OutputStream =
                    FileOutputStream(file)
                Timber.d("File Created $file ${file.length()} ${inputByteArray.size}")
//                val bitmap = BitmapFactory.decodeByteArray(inputByteArray, 0,inputByteArray.size)
//                bitmap.compress(Bitmap.CompressFormat.JPEG,50,os)
                os.write(inputByteArray)
                os.close()

                val compressedFile = Compressor.compress(context, file, this.coroutineContext) {
                    size(10_000)
                }

                val istream = compressedFile.inputStream()
                val byteArray = getBytes(istream)
                istream.close()
                Timber.d("byte array after compression ${byteArray?.size}")
                byteArray
            }catch (e : java.lang.Exception){
                Timber.d("Compress failed with error $e ${e.localizedMessage} ${e.stackTrace}")
                null
            }
        }
    }


}