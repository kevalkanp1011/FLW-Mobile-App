package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private var lastFilter: String = ""


    init {
        viewModelScope.launch {
            ncdList.asFlow().collect {
                _benList.value = it
            }

        }
    }


    fun filterText(filterText: String) {
        lastFilter = filterText
        if (filterText == "")
            _benList.value = ncdList.value
        else
            _benList.value = ncdList.value?.filter {
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


}