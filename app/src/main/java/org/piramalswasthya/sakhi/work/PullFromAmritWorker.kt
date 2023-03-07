package org.piramalswasthya.sakhi.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class PullFromAmritWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val benRepo: BenRepo,
    private val preferenceDao: PreferenceDao,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val name = "PullFromAmritWorker"

        const val n = 4 // Number of threads!
    }

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager



    override suspend fun doWork(): Result {
        return try {
            setForeground(createForegroundInfo("Downloading"))
            withContext(Dispatchers.IO) {
                val startTime = System.currentTimeMillis()
                var numPages: Int
                do {
                    numPages = benRepo.getBeneficiariesFromServerForWorker(0)
                } while (numPages == -2)
//                for (i in 1 until numPages)
//                    benRepo.getBeneficiariesFromServerForWorker(i)
                val result1 =
                    awaitAll(
                        async { getBenForPage(numPages, 0) },
                        async { getBenForPage(numPages, 1) },
                        async { getBenForPage(numPages, 2) },
                        async { getBenForPage(numPages, 3) },
                    )
                val endTime = System.currentTimeMillis()
                val timeTaken = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                Timber.d("Full load took $timeTaken seconds for $numPages pages  $result1")

                if (result1.all { it }) {
                    preferenceDao.setLastSyncedTimeStamp(System.currentTimeMillis())

                    return@withContext Result.success()
                }
                return@withContext Result.failure()


//                for (j in 0 until n) {
//                    if (j < numPages)
//                        getBenForPage(numPages, j)
//                }


            }

        } catch (e: java.lang.Exception) {
            Timber.d("Error occurred in PullFromAmritFullLoadWorker $e")
            Result.failure()
        }
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        val notification = NotificationCompat.Builder(appContext, appContext.getString(org.piramalswasthya.sakhi.R.string.notification_sync_channel_id))
            .setContentTitle("Syncing Data")
            .setContentText(progress)
            .setSmallIcon(org.piramalswasthya.sakhi.R.drawable.ic_launcher_foreground)
            .setProgress(100, 0, true)
            .setOngoing(true)
            .build()

        return ForegroundInfo(0, notification)
    }


    private suspend fun getBenForPage(numPages: Int, rem: Int): Boolean {
        return withContext(Dispatchers.IO) {
            var page: Int = rem
            while (page < numPages) {
                val ret = benRepo.getBeneficiariesFromServerForWorker(page)
                if (ret == -1)
                    throw IllegalStateException("benRepo.getBeneficiariesFromServerForWorker(page) returned -1 ")
                if (ret != -2)
                    page += n
            }
            true
        }
    }

}