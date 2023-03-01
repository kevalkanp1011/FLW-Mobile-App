package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_non_eligible_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class NcdNonEligibleListViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    private val ncdNonEligibleList = benRepo.ncdNonEligibleList
    private val _benList = MutableLiveData<List<BenBasicDomainForForm>>()
    val benList: LiveData<List<BenBasicDomainForForm>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            ncdNonEligibleList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }
        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = ncdNonEligibleList.value?.let { filterBenList(it, text) }
    }

    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}