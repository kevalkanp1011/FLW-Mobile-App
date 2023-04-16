package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.helpers.filterBenList
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import javax.inject.Inject

@HiltViewModel
class AllBenViewModel @Inject constructor(
    recordsRepo: RecordsRepo
) : ViewModel() {


    private val allBenList = recordsRepo.benList
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