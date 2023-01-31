package org.piramalswasthya.sakhi.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UploadSyncService : Service() {

    @Inject
    lateinit var networkApiService: TmcNetworkApiService

    @Inject
    lateinit var database : InAppDb

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate called")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createServiceNotification()
        serviceScope.launch {
            if(unsyncedEntries()) {
                //Call network api here
                delay(30000)
                stopSelf()
            }
            else
                stopSelf()


        }









        return super.onStartCommand(intent, flags, startId)

    }

    private suspend fun unsyncedEntries(): Boolean {
        return true
    }

    private fun createServiceNotification() {
        val notification = NotificationCompat.Builder(this, getString(R.string.notification_sync_channel_id))
            .setContentTitle("Syncing Data")
            .setContentText("uploading")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setProgress(0,100,true)
            .build()
        startForeground(1, notification)// 2
    }
}