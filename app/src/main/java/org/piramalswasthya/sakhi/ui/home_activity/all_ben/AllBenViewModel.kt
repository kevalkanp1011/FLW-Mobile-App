package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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


    init {
        viewModelScope.launch {
            allBenList.asFlow().collect {
                _benList.value = it
            }
        }
    }


    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            Timber.d("manual sync called!")
            benRepo.processNewBen()
        }
    }

    fun filterText(filterText: String) {
        if (filterText == "")
            _benList.value = allBenList.value
        else
            _benList.value = allBenList.value?.filter {
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