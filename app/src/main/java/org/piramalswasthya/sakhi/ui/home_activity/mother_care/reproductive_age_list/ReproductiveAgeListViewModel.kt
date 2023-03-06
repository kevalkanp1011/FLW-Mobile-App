package org.piramalswasthya.sakhi.ui.home_activity.mother_care.reproductive_age_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class ReproductiveAgeListViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    private val reproductiveAgeList = benRepo.reproductiveAgeList
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            reproductiveAgeList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }
        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = reproductiveAgeList.value?.let { filterBenList(it, text) }
    }

}