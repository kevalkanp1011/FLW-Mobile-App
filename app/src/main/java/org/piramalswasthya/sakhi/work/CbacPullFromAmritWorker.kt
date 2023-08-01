package org.piramalswasthya.sakhi.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.piramalswasthya.sakhi.repositories.CbacRepo

@HiltWorker
class CbacPullFromAmritWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val cbacRepo: CbacRepo
) : CoroutineWorker(appContext, params) {
    companion object {
        const val name = "Cbac-Pull"
    }
    override suspend fun doWork(): Result {
        cbacRepo.pullAndPersistCbacRecord(page = 1)
        return Result.success()
    }
}