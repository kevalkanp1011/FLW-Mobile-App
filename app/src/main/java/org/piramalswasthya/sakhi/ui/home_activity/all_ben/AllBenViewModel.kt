package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class AllBenViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {


    val benList = benRepo.benList

    fun manualSync(hhId: Long, benId: Long, locationRecord: LocationRecord) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}