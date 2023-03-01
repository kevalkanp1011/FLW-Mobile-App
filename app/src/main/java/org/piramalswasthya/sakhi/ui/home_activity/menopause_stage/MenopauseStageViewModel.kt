package org.piramalswasthya.sakhi.ui.home_activity.menopause_stage

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class MenopauseStageViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    val menopauseStageList = benRepo.menopauseList
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            menopauseStageList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }
        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = menopauseStageList.value?.let { filterBenList(it, text) }
    }


    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}