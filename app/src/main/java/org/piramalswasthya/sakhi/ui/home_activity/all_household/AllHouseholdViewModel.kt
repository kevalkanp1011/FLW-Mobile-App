package org.piramalswasthya.sakhi.ui.home_activity.all_household

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import javax.inject.Inject

@HiltViewModel
class AllHouseholdViewModel @Inject constructor(
    householdRepo: HouseholdRepo

) : ViewModel() {

    val householdList = householdRepo.householdList
}