package org.piramalswasthya.sakhi.work

import android.content.Context
import androidx.work.*

object WorkerUtils {

    private val networkOnlyConstraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun triggerSyncWorker(context : Context){
        val pullWorkRequest = OneTimeWorkRequestBuilder<PullFromAmritWorker>()
            .setConstraints(networkOnlyConstraint)
            .build()
        val pushWorkRequest = OneTimeWorkRequestBuilder<PushToAmritWorker>()
            .setConstraints(networkOnlyConstraint)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager
            .beginUniqueWork(PullFromAmritWorker.name, ExistingWorkPolicy.APPEND_OR_REPLACE, pullWorkRequest)
            .then(pushWorkRequest)
            .enqueue()
    }
}