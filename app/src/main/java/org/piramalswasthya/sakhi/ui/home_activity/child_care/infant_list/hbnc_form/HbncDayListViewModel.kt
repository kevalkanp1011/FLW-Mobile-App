package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.transform
import org.piramalswasthya.sakhi.model.HbncIcon
import org.piramalswasthya.sakhi.repositories.HbncRepo
import org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_list.ImmunizationListFragmentArgs
import javax.inject.Inject


@HiltViewModel
class HbncDayListViewModel @Inject constructor(
    hbncRepo: HbncRepo,
    state: SavedStateHandle,

    ) : ViewModel() {

    private val benId = ImmunizationListFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = ImmunizationListFragmentArgs.fromSavedStateHandle(state).hhId

    val dayList = hbncRepo.hbncList(benId, hhId).transform { dateList ->
        val list = listOf(1, 3, 7, 14, 21, 28, 42).map {
            HbncIcon(hhId, benId, it, dateList.contains(it))
        }
        emit(list)
    }

}