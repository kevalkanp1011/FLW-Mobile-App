package org.piramalswasthya.sakhi.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.WorkerParameters
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
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val benRepo: BenRepo,
    private val preferenceDao: PreferenceDao,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val name = "BenDataSendingWorker"
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        const val n = 4
    }


    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                val startTime = System.currentTimeMillis()
                val numPages = benRepo.getBeneficiariesFromServerForWorker(0)
                if(numPages == -1) {
                 Timber.d("Page size returned -1")
                }
                for (j in 0 until n) {
                    if (j < numPages)
                        getBenForPage(numPages, j)
                }
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

    private suspend fun getBenForPage(numPages: Int, rem: Int) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                var page: Int = rem
                while (page < numPages) {
                    if (numPages % n == rem) {
                        benRepo.getBeneficiariesFromServerForWorker(page)
                        page += n
                    }

                }
            }
        }
    }
}