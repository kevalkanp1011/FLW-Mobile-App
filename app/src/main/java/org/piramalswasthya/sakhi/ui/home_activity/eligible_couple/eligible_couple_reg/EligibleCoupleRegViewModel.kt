package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple.eligible_couple_reg

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.HbycRepo
import javax.inject.Inject

@HiltViewModel
class EligibleCoupleRegViewModel @Inject constructor(
    state: SavedStateHandle,
    @ApplicationContext context : Context,
    private val database: InAppDb,
    private val hbycRepo: HbycRepo,
    private val benRepo: BenRepo
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS,
        FAIL
    }

    // TODO: Implement the ViewModel
}