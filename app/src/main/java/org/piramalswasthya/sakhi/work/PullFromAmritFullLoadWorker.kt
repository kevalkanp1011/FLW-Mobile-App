package org.piramalswasthya.sakhi.work

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class PullFromAmritFullLoadWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val benRepo: BenRepo,
    private val preferenceDao: PreferenceDao,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val name = "BenDataSendingWorker"
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
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
                val numPages = benRepo.getBeneficiariesFromServerForWorker(0)
                for (i in 1 until numPages)
                    benRepo.getBeneficiariesFromServerForWorker(i)
//                for (j in 0 until n) {
//                    if (j < numPages)
//                        getBenForPage(numPages, j)
//                }
                val endTime = System.currentTimeMillis()
                val timeTaken = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                Timber.d("Full load took $timeTaken seconds for $numPages pages")
            }
            preferenceDao.setFullLoadStatus(true)
            Result.success()
        } catch (e: java.lang.Exception) {
            Timber.d("Error occurred in PullFromAmritFullLoadWorker $e")
            preferenceDao.setFullLoadStatus(false)
            Result.failure()
        }
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        val notification = NotificationCompat.Builder(appContext, appContext.getString(org.piramalswasthya.sakhi.R.string.notification_sync_channel_id))
            .setContentTitle("Syncing Data")
            .setContentText(progress)
            .setSmallIcon(org.piramalswasthya.sakhi.R.drawable.ic_launcher_foreground)
            .setProgress(0,100,true)
            .build()

        return ForegroundInfo(0, notification)
    }


    private suspend fun getBenForPage(numPages: Int, rem: Int) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                var page: Int = rem
                while (page < numPages) {
                    if ((numPages % n) == rem) {
                        benRepo.getBeneficiariesFromServerForWorker(page)
                        page += n
                    }

                }
            }
        }
    }
}