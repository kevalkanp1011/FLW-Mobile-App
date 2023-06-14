package org.piramalswasthya.sakhi.ui.home_activity.maternal_health.delivery_stage_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterPregnantWomanList
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class DeliveryStageListViewModel @Inject constructor(
    recordsRepo: RecordsRepo,
) : ViewModel() {

    private val allBenList = recordsRepo.deliveryList
    private val filter = MutableStateFlow("")
    val benList = allBenList.combine(filter){
            list, filter -> filterPregnantWomanList(list, filter)
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }
}