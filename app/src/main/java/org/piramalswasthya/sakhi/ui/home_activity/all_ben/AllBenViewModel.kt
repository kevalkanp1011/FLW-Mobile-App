package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.BenBasicDomain
import javax.inject.Inject

@HiltViewModel
class AllBenViewModel @Inject constructor() : ViewModel() {


    val benList = MutableLiveData(
        listOf(
            BenBasicDomain(
                benId = 1234567890,
                hhId = 91234567890,
                regDate = "30-07-1999",
                benName = "Ben",
                benSurname = "Gen",
                gender = "Not Disclosed",
                age = "4Years 2 days",
                mobileNo = "909090909090",
                fatherName = "Ben Sr.",
                familyHeadName = "Ben Head",
                typeOfList = "Type Unknown",
                rchId = "389242934792374",
                hrpStatus = "No Risk No Rusk",
                syncState = SyncState.SYNCING
            )
        )
    )
}