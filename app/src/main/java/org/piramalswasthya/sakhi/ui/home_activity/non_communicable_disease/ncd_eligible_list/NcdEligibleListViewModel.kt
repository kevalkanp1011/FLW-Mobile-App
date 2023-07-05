package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenFormList
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class NcdEligibleListViewModel @Inject constructor(
    recordsRepo: RecordsRepo,
    userRepo : UserRepo
) : ViewModel() {

    private lateinit var asha : UserDomain

    private val allBenList = recordsRepo.getNcdEligibleList()
    private val filter = MutableStateFlow("")
    val benList = allBenList.combine(filter){
            list, filter -> filterBenFormList(list.map { it.ben.asBenBasicDomainModelForCbacForm() }, filter)
    }

    init {
        viewModelScope.launch {
            asha = userRepo.getLoggedInUser()!!
        }
    }

    fun getAshaId() = asha.userId

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }



}