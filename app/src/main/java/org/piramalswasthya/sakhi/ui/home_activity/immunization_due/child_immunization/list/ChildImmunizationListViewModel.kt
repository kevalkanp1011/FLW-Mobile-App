package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.child_immunization.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.ImmunizationDao
import org.piramalswasthya.sakhi.helpers.filterImmunList
import org.piramalswasthya.sakhi.model.ImmunizationCategory
import org.piramalswasthya.sakhi.model.ImmunizationDetailsDomain
import org.piramalswasthya.sakhi.model.Vaccine
import org.piramalswasthya.sakhi.model.VaccineDomain
import org.piramalswasthya.sakhi.model.VaccineState
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ChildImmunizationListViewModel @Inject constructor(
    vaccineDao: ImmunizationDao

) : ViewModel() {
    private val pastRecords = vaccineDao.getBenWithImmunizationRecords(
        minDob = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.YEAR, -16)
        }.timeInMillis,
        maxDob = System.currentTimeMillis(),
    )
    private lateinit var vaccinesList: List<Vaccine>

    private val filter = MutableStateFlow("")

    var selectedPosition = 0

    val benWithVaccineDetails = pastRecords.map { vaccineIdList ->
        vaccineIdList.map { cache ->
            val ageMillis = System.currentTimeMillis() - cache.ben.dob
            ImmunizationDetailsDomain(ben = cache.ben.asBasicDomainModel(),
                vaccineStateList = vaccinesList.filter {
                    it.minAllowedAgeInMillis < ageMillis
                }.map { vaccine ->
                    VaccineDomain(
                        vaccine.vaccineId,
                        vaccine.vaccineName,
                        vaccine.immunizationService,
                        if (cache.givenVaccines.any { it.vaccineId == vaccine.vaccineId }) VaccineState.DONE
                        else if (ageMillis <= (vaccine.minAllowedAgeInMillis)) {
                            VaccineState.PENDING
                        } else if (ageMillis <= (vaccine.maxAllowedAgeInMillis)) {
                            VaccineState.OVERDUE
                        } else VaccineState.MISSED
                    )
                })
        }
    }

    val immunizationBenList = benWithVaccineDetails.combine(filter) { list, filter ->
        filterImmunList(list, filter)
    }

    fun filterText(text: String) {
        viewModelScope.launch {
            filter.emit(text)
        }

    }

    private val clickedBenId = MutableStateFlow(0L)

    val bottomSheetContent = clickedBenId.combine(benWithVaccineDetails) { a, b ->
        b.firstOrNull { it.ben.benId == a }

    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                vaccinesList = vaccineDao.getVaccinesForCategory(ImmunizationCategory.CHILD)
            }
        }
    }

    fun updateBottomSheetData(benId: Long) {
        viewModelScope.launch {
            clickedBenId.emit(benId)
        }
    }


    private val catList = ArrayList<String>()

    fun categoryData() : ArrayList<String> {

        catList.clear()
        catList.add("ALL")
        catList.add("6 WEEKS")
        catList.add("10 WEEKS")
        catList.add("14 WEEKS")
        catList.add("9-12 MONTHS")
        catList.add("16-24 MONTHS")
        catList.add("5-6 YEARS")
        catList.add("10 YEARS")
        catList.add("16 YEARS")

        return catList

    }

    private fun navigateForClicked(benId: Long, vaccineId: Int) {
        Timber.d("Hello Me! clicked for $benId, vaccineId : $vaccineId")
    }

    fun getSelectedBenId(): Long {
        return clickedBenId.value
    }


}