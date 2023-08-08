package org.piramalswasthya.sakhi.ui.home_activity.maternal_health.child_reg.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenFormList
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChildRegListViewModel @Inject constructor(
    recordsRepo: RecordsRepo
) : ViewModel() {
    private val allBenList =
        recordsRepo.getRegisteredInfants().map { it.map { it.asBenBasicDomainModelForInfantRegistrationForm() } }
    private val filter = MutableStateFlow("")
    val benList = allBenList.combine(filter) { list, filter ->
        filterBenFormList(list, filter)
    }

    init {
        viewModelScope.launch {
            allBenList.collect{
                Timber.d("Collected : $it")
            }
        }
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }

}