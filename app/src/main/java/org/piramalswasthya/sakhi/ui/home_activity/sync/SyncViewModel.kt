package org.piramalswasthya.sakhi.ui.home_activity.sync

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.database.room.dao.SyncDao
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    syncDao: SyncDao
) : ViewModel(){

    val syncStatus = syncDao.getSyncStatus()
}