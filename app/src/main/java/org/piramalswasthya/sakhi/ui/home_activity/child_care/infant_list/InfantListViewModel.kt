package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class InfantListViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    val infantList = benRepo.infantList
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    init {
        viewModelScope.launch {
            infantList.asFlow().collect {
                _benList.value = it
            }
        }
    }

    fun filterText(filterText: String) {
        if (filterText == "")
            _benList.value = infantList.value
        else
            _benList.value = infantList.value?.filter {
                it.hhId.toString().contains(filterText) ||
                        it.benId.toString().contains(filterText) ||
                        it.regDate.contains((filterText)) ||
                        it.age.contains(filterText) ||
                        it.benName.lowercase().contains(filterText) ||
                        it.familyHeadName.contains(filterText) ||
                        it.benSurname?.contains(filterText) ?: false ||
                        it.typeOfList.contains(filterText) ||
                        it.mobileNo.contains(filterText) ||
                        it.gender.contains(filterText)

            }
    }


    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}