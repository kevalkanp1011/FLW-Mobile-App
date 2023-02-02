package org.piramalswasthya.sakhi.ui.home_activity.all_household

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import javax.inject.Inject

@HiltViewModel
class AllHouseholdViewModel @Inject constructor(
    private val context: Application,
    private val householdRepo: HouseholdRepo

) : ViewModel() {

    private val _hasDraft = MutableLiveData(false)
    val hasDraft : LiveData<Boolean>
        get() = _hasDraft

    private val _navigateToNewHouseholdRegistration = MutableLiveData(false)
    val navigateToNewHouseholdRegistration : LiveData<Boolean>
        get() = _navigateToNewHouseholdRegistration

    val householdList = householdRepo.householdList

    fun checkDraft() {
        viewModelScope.launch {
            _hasDraft.value = householdRepo.getDraftForm() !=null
        }
    }

    fun navigateToNewHouseholdRegistration(delete : Boolean){

        viewModelScope.launch {
            if(delete)
                householdRepo.deleteHouseholdDraft()
            _navigateToNewHouseholdRegistration.value = true
        }
    }
    fun navigateToNewHouseholdRegistrationCompleted(){
        _navigateToNewHouseholdRegistration.value = false
    }
}