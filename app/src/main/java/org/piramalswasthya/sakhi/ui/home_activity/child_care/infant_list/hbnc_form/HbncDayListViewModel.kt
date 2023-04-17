package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.transform
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.HbncIcon
import org.piramalswasthya.sakhi.repositories.HbncRepo
import javax.inject.Inject


@HiltViewModel
class HbncDayListViewModel @Inject constructor(
    hbncRepo: HbncRepo,
    state: SavedStateHandle,

    ) : ViewModel() {

    private val benId = HbncDayListFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbncDayListFragmentArgs.fromSavedStateHandle(state).hhId

    val dayList = hbncRepo.hbncList(benId, hhId).transform { dateList ->
        val daysList = dateList.map { it.homeVisitDate }
        val headerList = mutableListOf(
            HbncIcon(
                hhId,
                benId,
                Konstants.hbncCardDay,
                daysList.contains(Konstants.hbncCardDay),
                dateList.firstOrNull { it.homeVisitDate == Konstants.hbncCardDay }?.syncState,
                "Visit Card",
                HbncDayListFragmentDirections.actionHbncDayListFragmentToVisitCardFragment(
                    hhId = hhId, benId = benId,
                )
            ),
            HbncIcon(
                hhId,
                benId,
                Konstants.hbncPart1Day,
                daysList.contains(Konstants.hbncPart1Day),
                dateList.firstOrNull { it.homeVisitDate == Konstants.hbncPart1Day }?.syncState,
                "Part I",
                HbncDayListFragmentDirections.actionHbncDayListFragmentToHbncPartIFragment(
                    hhId, benId,
                )
            ),
            HbncIcon(
                hhId,
                benId,
                Konstants.hbncPart2Day,
                daysList.contains(Konstants.hbncPart2Day),
                dateList.firstOrNull { it.homeVisitDate == Konstants.hbncPart2Day }?.syncState,
                "Part II",
                HbncDayListFragmentDirections.actionHbncDayListFragmentToHbncPartIIFragment(
                    hhId, benId
                )
            ),
        )
        headerList.addAll(listOf(1, 3, 7, 14, 21, 28, 42).map { day ->
            HbncIcon(
                hhId = hhId,
                benId = benId,
                count = day,
                isFilled = daysList.contains(day),
                syncState = dateList.firstOrNull { it.homeVisitDate == day }?.syncState,
                destination = HbncDayListFragmentDirections.actionHbncDayListFragmentToHbncFragment(
                    hhId, benId, day
                )
            )
        })
        emit(headerList)
    }

}