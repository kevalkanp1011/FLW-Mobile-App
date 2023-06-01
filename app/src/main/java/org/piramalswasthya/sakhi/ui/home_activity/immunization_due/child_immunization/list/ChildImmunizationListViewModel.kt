package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.child_immunization.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.piramalswasthya.sakhi.database.room.dao.ImmunizationDao
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.BIRTH
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.CATCH_UP
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.MONTH_16_24
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.MONTH_9_12
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.WEEK_10
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.WEEK_14
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.WEEK_6
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.YEAR_10
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.YEAR_16
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory.YEAR_5_6
import org.piramalswasthya.sakhi.model.ImmunizationDetailsDomain
import org.piramalswasthya.sakhi.model.VaccineDomain
import org.piramalswasthya.sakhi.model.VaccineState
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ChildImmunizationListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, vaccineDao: ImmunizationDao

) : ViewModel() {
    private val category =
        ChildImmunizationListFragmentArgs.fromSavedStateHandle(savedStateHandle).category
    private val minDob = Calendar.getInstance().apply {
        set(Calendar.SECOND, 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }.timeInMillis - when (category) {
        BIRTH -> TimeUnit.DAYS.toMillis(6 * 7)
        WEEK_6 -> TimeUnit.DAYS.toMillis(10 * 7)
        WEEK_10 -> TimeUnit.DAYS.toMillis(14 * 7)
        WEEK_14 -> TimeUnit.DAYS.toMillis(9 * 30)
        MONTH_9_12 -> TimeUnit.DAYS.toMillis(16 * 30)
        MONTH_16_24 -> TimeUnit.DAYS.toMillis(5 * 365)
        YEAR_5_6 -> TimeUnit.DAYS.toMillis(10 * 365)
        YEAR_10 -> TimeUnit.DAYS.toMillis(16 * 365)
        YEAR_16 -> TimeUnit.DAYS.toMillis(17 * 365)
        CATCH_UP -> 0
    }


    private val maxDob = Calendar.getInstance().apply {
        if (category != BIRTH) {
            set(Calendar.SECOND, 1)
            set(Calendar.MINUTE, 0)
            set(Calendar.HOUR_OF_DAY, 0)
        }
    }.timeInMillis - when (category) {
        BIRTH -> 0
        WEEK_6 -> TimeUnit.DAYS.toMillis(6 * 7)
        WEEK_10 -> TimeUnit.DAYS.toMillis(10 * 7)
        WEEK_14 -> TimeUnit.DAYS.toMillis(14 * 7)
        MONTH_9_12 -> TimeUnit.DAYS.toMillis(9 * 30)
        MONTH_16_24 -> TimeUnit.DAYS.toMillis(16 * 30)
        YEAR_5_6 -> TimeUnit.DAYS.toMillis(5 * 365)
        YEAR_10 -> TimeUnit.DAYS.toMillis(10 * 365)
        YEAR_16 -> TimeUnit.DAYS.toMillis(16 * 365)
        CATCH_UP -> 0
    }
    private val vaccinesList = vaccineDao.getVaccinesForCategory(category)

    private val vaccineIdList = vaccinesList.map {
        it.map { it.id }
    }

    val headerList = vaccinesList.map { list ->
        list.map { "${it.name} ${it.dosage}" }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val benWithVaccineDetails = vaccineIdList.flatMapLatest { vaccineIdList ->
        val list = vaccineDao.getBenWithImmunizationRecords(minDob, maxDob /*vaccineIdList*/)
        list.map { cacheList ->
            Timber.d("List in viewModel : ${cacheList.map { it.benId }}")
            cacheList.map { element ->
                ImmunizationDetailsDomain(benId = element.benId, name = element.benName,
                    vaccineStateList = vaccineIdList.map {
                        VaccineDomain(
//                            benId = element.ben.benId,
                            vaccineId = it,
                            state = if (element.vaccineList.map { it.vaccineId }.contains(it)) {
                                VaccineState.DONE
                            } else VaccineState.MISSED,
                        )
                    },
//                    onClick = { a: Long, b: Int -> navigateForClicked(a, b) }

                )
            }

        }
    }

    private fun navigateForClicked(benId: Long, vaccineId: Int) {
        Timber.d("Hello Me! clicked for $benId, vaccineId : $vaccineId")
    }
//
//
//    val headerList = ImmunizationDetailsHeader(
//        when (category) {
//            BIRTH -> listOf("OPV 0", "BCG", "Hepatitis B 0", "Vitamin K")
//            WEEK_6 -> listOf()
//            WEEK_10 -> listOf()
//            WEEK_14 -> listOf()
//            MONTH_9_12 -> listOf()
//            MONTH_16_24 -> listOf()
//            YEAR_5_6 -> listOf()
//            YEAR_10 -> listOf()
//            YEAR_16 -> listOf()
//            CATCH_UP -> listOf()
//        }
//    )
}