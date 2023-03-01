package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
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

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            eligibleCoupleList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }
        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = eligibleCoupleList.value?.let { filterBenList(it, text) }
    }


    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}