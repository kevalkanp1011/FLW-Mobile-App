package org.piramalswasthya.sakhi.ui.home_activity.child_care.adolescent_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class AdolescentListViewModel @Inject constructor(
    recordsRepo: RecordsRepo
) : ViewModel() {
    private val allBenList = recordsRepo.adolescentList
    private val filter = MutableStateFlow("")
    val benList = allBenList.combine(filter){
            list, filter -> filterBenList(list, filter)
    }


    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }
}