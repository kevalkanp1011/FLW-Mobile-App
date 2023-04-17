package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.database.room.InAppDb
import javax.inject.Inject

@HiltViewModel
class ImmunizationListViewModel @Inject constructor(
    state: SavedStateHandle,
    private val database: InAppDb
) : ViewModel() {
//
//    private val benId = ImmunizationListFragmentArgs.fromSavedStateHandle(state).benId
//    private val hhId = ImmunizationListFragmentArgs.fromSavedStateHandle(state).hhId
//
//    private var _vaccineList : List<ImmunizationIcon>? = null
//    val vaccineList: List<ImmunizationIcon>?
//        get() = _vaccineList
//
//    private val adolescentVaccines = listOf(ImmunizationIcon(benId, hhId, "Td",0, typeOfList = TypeOfList.ADOLESCENT))
//    private val pregnantWomenVaccines = listOf(
//    ImmunizationIcon(benId, hhId, "Tetanus & Adult Diphtheria (Td)-1",0, typeOfList = TypeOfList.ANTENATAL_MOTHER),
//    ImmunizationIcon(benId, hhId, "Td-2",0, typeOfList = TypeOfList.ANTENATAL_MOTHER),
//    ImmunizationIcon(benId, hhId, "Td-Booster",0, typeOfList = TypeOfList.ANTENATAL_MOTHER)
//    )
//    private val infantVaccines = listOf(
//    ImmunizationIcon(benId, hhId, "Bacillus Calmette Guerin (BCG)",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Oral Polio Vaccine (OPV)-0 dose",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Hepatitis B birth dose",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "OPV-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Pentavalent-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Rotavirus Vaccine (RVV)-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "OPV-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Pentavalent-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Fractional dose of Inactivated Polio Vaccine (fIPV)-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Pneumococcal Conjugate Vaccine(PCV) -1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "OPV-2",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Pentavalent-2",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "RVV-2",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "OPV-3",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Pentavalent-3",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "fIPV-2",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "RVV-3",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "PCV-2",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Measles & Rubella (MR)-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "JE-1",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "PCV-Booster",0,typeOfList = TypeOfList.INFANT),
//    ImmunizationIcon(benId, hhId, "Vitamin A (1 dose)",0,typeOfList = TypeOfList.INFANT)
//    )
//    private val childVaccines = listOf(
//    ImmunizationIcon(benId, hhId, "MR-2",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, "JE-2",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, "Diphtheria",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, "Pertussis & Tetanus (DPT)-Booster-1",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, " OPV – Booster",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, "DPT-Booster-2",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, "Tetanus & adult Diphtheria (Td)",0, typeOfList = TypeOfList.CHILD),
//    ImmunizationIcon(benId, hhId, "Vitamin A (2nd to 9th dose)",0, typeOfList = TypeOfList.CHILD)
//    )
//
//    init {
//        viewModelScope.launch {
//            val ben = database.benDao.getBen(hhId, benId)!!
//            Timber.d("immunization entries: $hhId, $benId: ${ben.beneficiaryId}")
//
//            val typeOfList = ben.registrationType
//            _vaccineList = when (typeOfList) {
//                TypeOfList.INFANT -> infantVaccines
//                TypeOfList.CHILD -> childVaccines
//                TypeOfList.ADOLESCENT -> adolescentVaccines
//                TypeOfList.ANTENATAL_MOTHER -> pregnantWomenVaccines
//                else -> throw IllegalStateException("type of entries is null")
//            }
//        }
//    }
}