package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_priority_list

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm
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
    private val _benList = MutableLiveData<List<BenBasicDomainForForm>>()
    val benList: LiveData<List<BenBasicDomainForForm>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            ncdPriorityList.asFlow().collect {
                _benList.value = it?.let { filterBenList(it, lastFilter) }
            }

        }
    }

//    suspend fun loadUser() {
//        withContext(Dispatchers.IO) {
//            user = userRepo.getLoggedInUser()!!
//        }
//    }

    // fun getUserId(): Int = user.userId

    fun filterText(text: String) {
        lastFilter = text
        _benList.value = ncdPriorityList.value?.let { filterBenList(it, text) }
    }
}