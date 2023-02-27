package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EligibleCoupleViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    private val eligibleCoupleList = benRepo.eligibleCoupleList
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    init {
        viewModelScope.launch {
            eligibleCoupleList.asFlow().collect {
                _benList.value = it
            }
        }
    }

    fun filterText(filterText: String) {
        Timber.d("filter called!!")
        if (filterText == "")
            _benList.value = eligibleCoupleList.value
        else
            _benList.value = eligibleCoupleList.value?.filter {
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