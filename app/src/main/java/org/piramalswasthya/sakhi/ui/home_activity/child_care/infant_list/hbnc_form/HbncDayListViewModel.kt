package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.transform
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.HbncIcon
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.HbncRepo
import javax.inject.Inject


@HiltViewModel
class HbncDayListViewModel @Inject constructor(
    benRepo : BenRepo,
    hbncRepo: HbncRepo,
    state: SavedStateHandle,

    ) : ViewModel() {

    private val benId = HbncDayListFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbncDayListFragmentArgs.fromSavedStateHandle(state).hhId

    val dayList = hbncRepo.hbncList(benId, hhId).transform { dateList ->
        val daysList = dateList.map { it.homeVisitDate }
        val visitCardIcon = HbncIcon(
            hhId,
            benId,
            Konstants.hbncCardDay,
            daysList.contains(Konstants.hbncCardDay),
            dateList.firstOrNull { it.homeVisitDate == Konstants.hbncCardDay }?.syncState,
            "Visit Card",
            HbncDayListFragmentDirections.actionHbncDayListFragmentToVisitCardFragment(
                hhId = hhId, benId = benId,
            )
        )
        val partIIcon = HbncIcon(
            hhId,
            benId,
            Konstants.hbncPart1Day,
            daysList.contains(Konstants.hbncPart1Day),
            dateList.firstOrNull { it.homeVisitDate == Konstants.hbncPart1Day }?.syncState,
            "Part I",
            HbncDayListFragmentDirections.actionHbncDayListFragmentToHbncPartIFragment(
                hhId, benId,
            )
        )
        val partIIIcon = HbncIcon(
            hhId,
            benId,
            Konstants.hbncPart2Day,
            daysList.contains(Konstants.hbncPart2Day),
            dateList.firstOrNull { it.homeVisitDate == Konstants.hbncPart2Day }?.syncState,
            "Part II",
            HbncDayListFragmentDirections.actionHbncDayListFragmentToHbncPartIIFragment(
                hhId, benId
            )
        )
        val headerList = mutableListOf(visitCardIcon)
        if(visitCardIcon.isFilled) headerList.add(partIIcon)
        if(partIIcon.isFilled) headerList.add(partIIIcon)
        val ben = benRepo.getBeneficiaryRecord(benId,hhId)!!
        val allDayList = listOf(1, 3, 7, 14, 21, 28, 42)
        val dayList = allDayList.filter { it<=ben.age }

        if(partIIIcon.isFilled)
            headerList.addAll(dayList.map { day ->
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