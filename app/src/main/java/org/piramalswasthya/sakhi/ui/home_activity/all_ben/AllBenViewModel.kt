package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AllBenViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {


    private val allBenList = benRepo.benList
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            allBenList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }
        }
    }

    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            Timber.d("manual sync called!")
            benRepo.processNewBen()
        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = allBenList.value?.let { filterBenList(it, text) }
    }
}