package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class NcdListViewModel @Inject constructor(
    recordsRepo: RecordsRepo
) : ViewModel(

) {
    private val allBenList = recordsRepo.getNcdList()
    private val filter = MutableStateFlow("")
    private val selectedBenId = MutableStateFlow(0L)
    val benList = allBenList.combine(filter) { list, filter ->
        filterBenList(list.map { it.ben.asBasicDomainModel() }, filter)
    }

    val ncdDetails = allBenList.combine(selectedBenId) { list, benId ->
        if (benId > 0) {
            list.first { it.ben.benId==benId }.savedCbacRecords
        }
        else
            null
    }.flowOn(viewModelScope.coroutineContext)

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }

    fun setSelectedBenId(benId : Long){
        viewModelScope.launch {
            selectedBenId.emit(benId)
        }
    }


}