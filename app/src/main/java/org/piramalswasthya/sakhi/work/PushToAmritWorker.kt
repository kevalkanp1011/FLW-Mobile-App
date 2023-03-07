package org.piramalswasthya.sakhi.work

import android.content.Context
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertTmcInterceptor
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import java.net.SocketTimeoutException

@HiltWorker
class PushToAmritWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val benRepo: BenRepo,
    private val preferenceDao: PreferenceDao,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val name = "PushToAmritWorker"
    }


    override suspend fun doWork(): Result {
        init()
        try {
            val workerResult = benRepo.processNewBen()
            if (workerResult) {
                Timber.d("Worker completed")
                return Result.success()
            } else {
                Timber.d("Worker Failed as usual!")
                return Result.failure()
            }
        } catch (e: SocketTimeoutException) {
            Timber.e("Caught Exception for push amrit worker $e")
            return Result.retry()
        } catch (e : java.lang.Exception) {
            Toast.makeText(applicationContext, "Push to server failed! ", Toast.LENGTH_LONG).show()
            return Result.failure()
        }
    }

    private fun init() {
        if (TokenInsertTmcInterceptor.getToken() == "")
            TokenInsertTmcInterceptor.setToken(preferenceDao.getPrimaryApiToken()!!)
    }
}