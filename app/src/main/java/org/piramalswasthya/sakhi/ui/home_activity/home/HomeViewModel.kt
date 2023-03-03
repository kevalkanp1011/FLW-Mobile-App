package org.piramalswasthya.sakhi.ui.home_activity.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    database: InAppDb,
    private val pref: PreferenceDao,
    private val userRepo: UserRepo
) : ViewModel() {

    val currentUser = database.userDao.getLoggedInUserLiveData()

    val iconCount = Transformations.switchMap(currentUser) {
        it?.let {
            database.userDao.getRecordCounts(it.userId)
        }
    }


    private var locationRecord: LocationRecord? = null

    fun setLocationDetails(
        state: String,
        district: String,
        block: String,
        village: String
    ) {
        val stateId = user.stateIds[user.stateEnglish.indexOf(state)]
        val districtId = user.districtIds[user.districtEnglish.indexOf(district)]
        val blockId = user.blockIds[user.blockEnglish.indexOf(block)]
        val villageId = user.villageIds[user.villageEnglish.indexOf(village)]
        this.locationRecord = LocationRecord(
            stateId,
            state,
            districtId,
            district,
            blockId,
            block,
            villageId,
            village,
            user.countryId
        )
        pref.saveLocationRecord(locationRecord!!)
    }

    fun getLocationRecord() = locationRecord!!


    fun isLocationSet(): Boolean {
        return locationRecord != null
    }

    private lateinit var _user: UserDomain
    val user: UserDomain
        get() = _user

    private val _navigateToLoginPage = MutableLiveData(false)
    val navigateToLoginPage: MutableLiveData<Boolean>
        get() = _navigateToLoginPage

    init {
        viewModelScope.launch {
            _user = getUserFromRepo()
        }
    }

    private suspend fun getUserFromRepo(): UserDomain {
        return withContext(Dispatchers.IO) {
            userRepo.getLoggedInUser()
                ?: throw IllegalStateException("No Logged in user found in DB!!")
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
            pref.deleteLoginCred()
            pref.setLastSyncedTimeStamp(1603132200000)
            _navigateToLoginPage.value = true
        }
    }

    fun navigateToLoginPageComplete() {
        _navigateToLoginPage.value = false
    }

    fun checkIfFullLoadCompletedBefore(): Long {
        return pref.getLastSyncedTimeStamp()

    }


}