package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class NcdEligibleListViewModel @Inject constructor(
    recordsRepo: RecordsRepo,
    userRepo: UserRepo,
) : ViewModel(

) {

    private lateinit var asha : UserDomain
    private val allBenList = recordsRepo.getNcdList()
    private val filter = MutableStateFlow("")
    private val selectedBenId = MutableStateFlow(0L)
    val benList = allBenList.combine(filter) { cacheList, filter ->
        val list = cacheList.map { it.asDomainModel() }
        val benBasicDomainList = list.map { it.ben}
        val filteredBenBasicDomainList = filterBenList(benBasicDomainList, filter)
        list.filter { it.ben.benId in filteredBenBasicDomainList.map { it.benId } }

    }

    val ncdDetails = allBenList.combineTransform(selectedBenId) { list, benId ->
        if (benId > 0) {
            val list = list.firstOrNull { it.ben.benId == benId }?.savedCbacRecords?.map { it.asDomainModel() }
            if (!list.isNullOrEmpty()) emit(list)
        }
    }

    init {
        viewModelScope.launch {
            asha = userRepo.getLoggedInUser()!!
        }
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }

    fun setSelectedBenId(benId: Long) {
        viewModelScope.launch {
            selectedBenId.emit(benId)
        }
    }

    fun getSelectedBenId(): Long = selectedBenId.value
    fun getAshaId(): Int  = asha.userId


}