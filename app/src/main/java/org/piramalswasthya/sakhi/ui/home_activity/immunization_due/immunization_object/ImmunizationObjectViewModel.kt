package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_object

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.BenRegCache
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImmunizationObjectViewModel @Inject constructor(
    private val context: Application,
    state: SavedStateHandle,
    private val database: InAppDb
) : ViewModel() {

    private val benId = ImmunizationObjectFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = ImmunizationObjectFragmentArgs.fromSavedStateHandle(state).hhId
    val vaccine = ImmunizationObjectFragmentArgs.fromSavedStateHandle(state).vaccine
    val dosage = ImmunizationObjectFragmentArgs.fromSavedStateHandle(state).dosageTaken
    var ben: BenRegCache? = null

    init {
        viewModelScope.launch {
           ben = database.benDao.getBen(hhId, benId)!!
            Timber.d("immunization details: $hhId, $benId: ${ben?.beneficiaryId}")
        }
    }

    fun getBenName(): String = ben?.firstName?: "N/A"
    fun getAgeGender(): String = "${ben?.age} ${ben?.ageUnit?.name} | ${ben?.gender?.name}"
}