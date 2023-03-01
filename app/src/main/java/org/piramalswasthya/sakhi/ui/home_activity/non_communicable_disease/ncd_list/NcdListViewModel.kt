package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class NcdListViewModel @Inject constructor(
    private val benRepo: BenRepo,
) : ViewModel(

) {
    private val ncdList = benRepo.ncdEligibleList

    //private lateinit var user: UserDomain
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            ncdList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }

        }
    }

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = ncdList.value?.let { filterBenList(it, text) }
    }


}