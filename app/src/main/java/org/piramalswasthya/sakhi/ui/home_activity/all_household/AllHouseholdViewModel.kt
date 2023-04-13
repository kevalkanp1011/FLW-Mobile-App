package org.piramalswasthya.sakhi.ui.home_activity.all_household

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.HouseHoldBasicDomain
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class AllHouseholdViewModel @Inject constructor(
    private val householdRepo: HouseholdRepo,
    recordsRepo: RecordsRepo

) : ViewModel() {

    private val _hasDraft = MutableLiveData(false)
    val hasDraft: LiveData<Boolean>
        get() = _hasDraft

    private val filter = MutableStateFlow("")

    private val _navigateToNewHouseholdRegistration = MutableLiveData(false)
    val navigateToNewHouseholdRegistration: LiveData<Boolean>
        get() = _navigateToNewHouseholdRegistration

    val householdList = recordsRepo.hhList.combine(filter) { list, filter ->
        filterHH(list, filter)
    }

    fun checkDraft() {
        viewModelScope.launch {
            _hasDraft.value = householdRepo.getDraftForm() != null
        }
    }

    fun navigateToNewHouseholdRegistration(delete: Boolean) {

        viewModelScope.launch {
            if (delete)
                householdRepo.deleteHouseholdDraft()
            _navigateToNewHouseholdRegistration.value = true
        }
    }

    fun navigateToNewHouseholdRegistrationCompleted() {
        _navigateToNewHouseholdRegistration.value = false
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }

    private fun filterHH(list: List<HouseHoldBasicDomain>, filter: String): List<HouseHoldBasicDomain> {
        return if (filter == "")
            list
        else {
            val filterText = filter.lowercase()
            list.filter {
                it.hhId.toString().contains(filterText) ||
                        it.headName.lowercase().contains(filterText) ||
                        it.headSurname.lowercase().contains((filterText))
            }
        }
    }
}