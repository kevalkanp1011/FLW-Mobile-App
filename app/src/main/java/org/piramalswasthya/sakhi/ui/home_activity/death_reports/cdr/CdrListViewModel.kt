package org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class CdrListViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    val cdrList = benRepo.cdrList
    private val _benList = MutableLiveData<List<BenBasicDomainForForm>>()
    val benList: LiveData<List<BenBasicDomainForForm>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            cdrList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }
        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = cdrList.value?.let { filterBenList(it, text) }
    }


    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}