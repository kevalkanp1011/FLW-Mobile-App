package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple.tracking.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenFormList
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm
import org.piramalswasthya.sakhi.repositories.EcrRepo
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class EligibleCoupleTrackingListViewModel @Inject constructor(
    recordsRepo: RecordsRepo,
    private var ecrRepo: EcrRepo
) : ViewModel() {

    val scope : CoroutineScope
        get() = viewModelScope

    private val allBenList = recordsRepo.eligibleCoupleTrackingList
    private val filter = MutableStateFlow("")
    val benList = allBenList.combine(filter) { list, filter ->
        filterBenFormList(list, filter)
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }
    }


}