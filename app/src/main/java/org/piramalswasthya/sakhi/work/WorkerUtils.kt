package org.piramalswasthya.sakhi.work

import android.content.Context
import androidx.work.*

object WorkerUtils {

    const val syncWorkerUniqueName  = "SYNC-WITH-AMRIT"

    private val networkOnlyConstraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun triggerAmritSyncWorker(context : Context){
        val pullWorkRequest = OneTimeWorkRequestBuilder<PullFromAmritWorker>()
            .setConstraints(networkOnlyConstraint)
            .build()
        val pushWorkRequest = OneTimeWorkRequestBuilder<PushToAmritWorker>()
            .setConstraints(networkOnlyConstraint)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager
            .beginUniqueWork(syncWorkerUniqueName, ExistingWorkPolicy.APPEND_OR_REPLACE, pullWorkRequest)
            .then(pushWorkRequest)
            .enqueue()
    }

    fun triggerD2dSyncWorker(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<PushToD2DWorker>()
            .setConstraints(PushToD2DWorker.constraint)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                PushToD2DWorker.name,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                workRequest
            )
    }

    fun triggerGenBenIdWorker(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<GenerateBenIdsWorker>()
            .setConstraints(GenerateBenIdsWorker.constraint)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(GenerateBenIdsWorker.name, ExistingWorkPolicy.KEEP, workRequest)
    }

    fun cancelAllWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()
    }
}