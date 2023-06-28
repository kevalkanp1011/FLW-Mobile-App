package org.piramalswasthya.sakhi.repositories

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.HouseholdDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import javax.inject.Inject

@ActivityRetainedScoped
class RecordsRepo @Inject constructor(
    private val householdDao: HouseholdDao,
    private val benDao: BenDao,
    private val maternalHealthDao: MaternalHealthDao,
    preferenceDao: PreferenceDao
) {
    private val selectedVillage = preferenceDao.getLocationRecord()!!.village.id

    fun getHhList() = householdDao.getAllHouseholds(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModel() } }
    fun getHhListCount() = householdDao.getAllHouseholdsCount(selectedVillage)

    fun getBenList() = benDao.getAllBen(selectedVillage).map { list -> list.map { it.asBasicDomainModel() } }
    fun getBenListCount() = benDao.getAllBenCount(selectedVillage)

    val pregnantList = benDao.getAllPregnancyWomenList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForPmsmaForm() } }
    val pregnantListCount = pregnantList.map { it.size }

    val deliveryList = benDao.getAllDeliveryStageWomenList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForPmsmaForm() } }
    val deliveryListCount = deliveryList.map { it.size }

    fun getNcdList() = benDao.getBenWithCbac(selectedVillage)
    fun getNcdListCount() = benDao.getBenWithCbacCount(selectedVillage)

    fun getNcdEligibleList() = getNcdList()
    fun getNcdEligibleListCount() = getNcdListCount()
    fun getNcdPriorityList() = getNcdList().map {
        it.filter { it.savedCbacRecords.isNotEmpty() && it.savedCbacRecords.maxBy { it.id }.total_score>=4 }
    }
    fun getNcdPriorityListCount() = getNcdListCount()
    fun getNcdNonEligibleList() = getNcdList().map {
        it.filter { it.savedCbacRecords.isNotEmpty() && it.savedCbacRecords.maxBy { it.id }.total_score<4 }
    }
    fun getNcdNonEligibleListCount() = getNcdListCount()

//    val ncdPriorityList = benDao.getAllNCDPriorityList(selectedVillage)
//        .map { list -> list.map { it.asBenBasicDomainModelForCbacForm() } }
//    val ncdPriorityListCount = ncdPriorityList.map { it.size }

//    val ncdNonEligibleList = benDao.getAllNCDNonEligibleList(selectedVillage)
//        .map { list -> list.map { it.asBenBasicDomainModelForCbacForm() } }
//    val ncdNonEligibleListCount = ncdNonEligibleList.map { it.size }

    val tbScreeningList = benDao.getAllBen(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForTbsnForm() } }
    val tbScreeningListCount = tbScreeningList.map { it.size }

    val tbSuspectedList = benDao.getScreeningList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForTbspForm() } }
    val tbSuspectedListCount = tbSuspectedList.map { it.size }

    val menopauseList = benDao.getAllMenopauseStageList(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModel() } }
    val menopauseListCount = menopauseList.map { it.size }

    val reproductiveAgeList = benDao.getAllReproductiveAgeList(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModelForFpotForm() } }
    val reproductiveAgeListCount = reproductiveAgeList.map { it.size }

    val infantList = benDao.getAllInfantList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForHbncForm() } }
    val infantListCount = infantList.map { it.size }

    val childList = benDao.getAllChildList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForHbycForm() } }
    val childListCount = childList.map { it.size }

    val adolescentList =
        benDao.getAllAdolescentList(selectedVillage)
            .map { list -> list.map { it.asBasicDomainModel() } }
    val adolescentListCount = adolescentList.map { it.size }

    val immunizationList = benDao.getAllImmunizationDueList(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModel() } }
    val immunizationListCount = menopauseList.map { it.size }

    val hrpList =
        benDao.getAllHrpCasesList(selectedVillage).map { list -> list.map { it.asBasicDomainModel() } }
    val hrpListCount = menopauseList.map { it.size }

    val pncMotherList = benDao.getAllPNCMotherList(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModelForPmjayForm() } }
    val pncMotherListCount = pncMotherList.map { it.size }

    val cdrList = benDao.getAllCDRList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForCdrForm() } }
//    val cdrListCount = cdrList.map { it.size }

    val mdsrList = benDao.getAllMDSRList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForMdsrForm() } }

    val childrenImmunizationList = benDao.getAllChildrenImmunizationList(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModel() } }
    val childrenImmunizationListCount = childrenImmunizationList.map { it.size }

    val motherImmunizationList = benDao.getAllMotherImmunizationList(selectedVillage)
        .map { list -> list.map { it.asBasicDomainModel() } }
    val motherImmunizationListCount = motherImmunizationList.map { it.size }

    val eligibleCoupleList = benDao.getAllEligibleCoupleList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForEligibleCoupleRegistrationForm() } }
    val eligibleCoupleListCount = eligibleCoupleList.map { it.size }

    val eligibleCoupleTrackingList = benDao.getAllEligibleTrackingList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelECTForm() } }
    val eligibleCoupleTrackingListCount = eligibleCoupleTrackingList.map { it.size }

    val deliveredWomenList = benDao.getAllEligibleTrackingList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelECTForm() } }
    val deliveredWomenListCount = deliveredWomenList.map { it.size }

    fun getPregnantWomenList() = benDao.getAllPregnancyWomenList(selectedVillage)
        .map { list -> list.map { it.asBenBasicDomainModelForPregnantWomanRegistrationForm() } }
    fun getPregnantWomenListCount() = benDao.getAllPregnancyWomenListCount(selectedVillage)
    fun getRegisteredPregnantWomanList() = benDao.getAllRegisteredPregnancyWomenList(selectedVillage)
        .map { list -> list.map { it.asDomainModel() } }
    fun getRegisteredPregnantWomanListCount() = benDao.getAllRegisteredPregnancyWomenListCount(selectedVillage)

    fun getHrpCases() = benDao.getHrpCases(selectedVillage)
        .map { list -> list.distinctBy { it.benId }.map { it.asBasicDomainModel() }}

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getHrpCount(): Flow<Int> {
        return maternalHealthDao.getAllPregnancyRecords().transformLatest {
            var count = 0
            it.map {
                val regis = it.key
                val anc = it.value
                if (regis.isHrp || anc.any { it.hrpConfirmed == true })
                    count++
            }
            emit(count)
        }
    }

}