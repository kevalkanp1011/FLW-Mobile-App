package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

class EligibleCoupleViewModel @Inject constructor(
    private val benRepo: BenRepo
) : ViewModel() {

    val eligibleCoupleList = benRepo.eligibleCoupleList

    fun manualSync(hhId: Long, benId: Long, locationRecord: LocationRecord) {
        viewModelScope.launch {
            benRepo.processNewBen()
        }
    }
}