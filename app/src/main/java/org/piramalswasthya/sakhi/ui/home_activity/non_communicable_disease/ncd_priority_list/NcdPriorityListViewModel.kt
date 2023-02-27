package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_priority_list

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.NewBenRegTypeFragment
import javax.inject.Inject

@HiltViewModel
class NcdPriorityListViewModel @Inject constructor(
    private val application: Application,
    benRepo: BenRepo

) : ViewModel() {
    private val ncdPriorityList = benRepo.ncdPriorityList

    //private lateinit var user: UserDomain
    private val _benList = MutableLiveData<List<BenBasicDomain>>()
    val benList: LiveData<List<BenBasicDomain>>
        get() = _benList

    init {
        viewModelScope.launch {
            ncdPriorityList.asFlow().collect {
                _benList.value = it
            }

        }
    }

//    suspend fun loadUser() {
//        withContext(Dispatchers.IO) {
//            user = userRepo.getLoggedInUser()!!
//        }
//    }

    // fun getUserId(): Int = user.userId

    fun filterText(filterText: String) {
        if (filterText == "")
            _benList.value = ncdPriorityList.value
        else
            _benList.value = ncdPriorityList.value?.filter {
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


    fun triggerPushToAmritWorker() {
        NewBenRegTypeFragment.triggerBenDataSendingWorker(application)
    }
}