package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class NcdEligibleListViewModel @Inject constructor(
    private val benRepo: BenRepo,
    private val userRepo: UserRepo,
) : ViewModel() {

    private val ncdEligibleList = benRepo.ncdEligibleList

    //private lateinit var user: UserDomain
    private val _benList = MutableLiveData<List<BenBasicDomainForForm>>()
    val benList: LiveData<List<BenBasicDomainForForm>>
        get() = _benList

    private var lastFilter = ""

    init {
        viewModelScope.launch {
            ncdEligibleList.asFlow().collect {
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
        _benList.value = ncdEligibleList.value?.let { filterBenList(it, text) }
    }


    fun manualSync(/*hhId: Long, benId: Long, locationRecord: LocationRecord*/) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}