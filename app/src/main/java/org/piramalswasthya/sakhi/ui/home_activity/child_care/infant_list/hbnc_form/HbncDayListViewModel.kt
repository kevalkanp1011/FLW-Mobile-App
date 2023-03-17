package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.model.HbncIcon
import org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_list.ImmunizationListFragmentArgs
import javax.inject.Inject


@HiltViewModel
class HbncDayListViewModel @Inject constructor(
    state : SavedStateHandle,

) : ViewModel(){

    private val benId = ImmunizationListFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = ImmunizationListFragmentArgs.fromSavedStateHandle(state).hhId

    val dayList = listOf(
        HbncIcon(hhId, benId, 1),
        HbncIcon(hhId, benId, 3),
        HbncIcon(hhId, benId, 7),
        HbncIcon(hhId, benId, 14),
        HbncIcon(hhId, benId, 21),
        HbncIcon(hhId, benId, 28),
        HbncIcon(hhId, benId, 42),
    )

}