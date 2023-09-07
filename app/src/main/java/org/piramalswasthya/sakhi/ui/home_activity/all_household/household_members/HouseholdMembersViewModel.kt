package org.piramalswasthya.sakhi.ui.home_activity.all_household.household_members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject


@HiltViewModel
class HouseholdMembersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val benRepo: BenRepo,

    ) : ViewModel() {

    val hhId = HouseholdMembersFragmentArgs.fromSavedStateHandle(savedStateHandle).hhId

    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _benList.postValue(
                    benRepo.getBenBasicListFromHousehold(hhId).map { it.asBasicDomainModel() })
            }
        }
    }


}