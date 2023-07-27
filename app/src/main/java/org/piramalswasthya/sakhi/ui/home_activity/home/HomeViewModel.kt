package org.piramalswasthya.sakhi.ui.home_activity.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val database: InAppDb,
    private val pref: PreferenceDao,
    private val userRepo: UserRepo,
) : ViewModel() {


    val currentUser = pref.getLoggedInUser()

    val numBenIdsAvail = database.benIdGenDao.liveCount()

    var profilePicUri : Uri?
        get() = pref.getProfilePicUri()
        set(value) = pref.saveProfilePicUri(value)

    val scope: CoroutineScope
        get() = viewModelScope
    private var _unprocessedRecords: Int = 0
    val unprocessedRecords: Int
        get() = _unprocessedRecords


    val locationRecord: LocationRecord? = pref.getLocationRecord()
    val currentLanguage = pref.getCurrentLanguage()

    private val _navigateToLoginPage = MutableLiveData(false)
    val navigateToLoginPage: MutableLiveData<Boolean>
        get() = _navigateToLoginPage

    init {
        viewModelScope.launch {
//            _user = pref.getLoggedInUser()!!
            launch {
                userRepo.unProcessedRecordCount.collect { value ->
                    _unprocessedRecords = value
                }
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database.clearAllTables()
            }
            pref.deleteForLogout()
            pref.setLastSyncedTimeStamp(Konstants.defaultTimeStamp)
            _navigateToLoginPage.value = true
        }
    }

    fun navigateToLoginPageComplete() {
        _navigateToLoginPage.value = false
    }


}