package org.piramalswasthya.sakhi.ui.home_activity.child_care.child_list.hbyc

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.transform
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.HbycIcon
import org.piramalswasthya.sakhi.repositories.HbycRepo
import javax.inject.Inject

@HiltViewModel
class HbycMonthListViewModel @Inject constructor(
    hbycRepo: HbycRepo,
    state: SavedStateHandle,

    ) : ViewModel() {

    private val benId = HbycMonthListFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbycMonthListFragmentArgs.fromSavedStateHandle(state).hhId

    val dayList = hbycRepo.hbycList(benId, hhId).transform { dateList ->
        val daysList = dateList.map { it.month }

        val headerList = mutableListOf(3, 6, 9, 12, 15).map { month ->
            HbycIcon(
                hhId = hhId,
                benId = benId,
                count = month,
                isFilled = false,
                syncState = SyncState.UNSYNCED,
                destination = HbycMonthListFragmentDirections.actionHbycMonthListFragmentToHbycFragment(
                    hhId, benId, month
                )
            )
        }
        emit(headerList)
    }

}