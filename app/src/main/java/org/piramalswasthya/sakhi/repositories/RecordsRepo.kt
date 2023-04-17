package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.flow.map
import org.piramalswasthya.sakhi.database.room.InAppDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordsRepo @Inject constructor(
    database: InAppDb,
) {

    val hhList = database.householdDao.getAllHouseholds()
        .map { list -> list.map { it.asBasicDomainModel() } }
    val hhCount = hhList.map { it.size }

    val benList = database.benDao.getAllBen().map { list -> list.map { it.asBasicDomainModel() } }
    val benListCount = benList.map { it.size }

    val eligibleCoupleList = database.benDao.getAllEligibleCoupleList()
        .map { list -> list.map { it.asBasicDomainModel() } }
    val eligibleCoupleListCount = eligibleCoupleList.map { it.size }

    val pregnantList = database.benDao.getAllPregnancyWomenList()
        .map { list -> list.map { it.asBenBasicDomainModelForPmsmaForm() } }
    val pregnantListCount = pregnantList.map { it.size }

    val deliveryList = database.benDao.getAllDeliveryStageWomenList()
        .map { list -> list.map { it.asBenBasicDomainModelForPmsmaForm() } }
    val deliveryListCount = deliveryList.map { it.size }

    val ncdList = database.benDao.getAllNCDList()
        .map { list -> list.map { it.asBasicDomainModel() } }
    val ncdListCount = ncdList.map { it.size }

    val ncdEligibleList = database.benDao.getAllNCDEligibleList()
        .map { list -> list.map { it.asBenBasicDomainModelForCbacForm() } }
    val ncdEligibleListCount = ncdEligibleList.map { it.size }

    val ncdPriorityList = database.benDao.getAllNCDPriorityList()
        .map { list -> list.map { it.asBenBasicDomainModelForCbacForm() } }
    val ncdPriorityListCount = ncdPriorityList.map { it.size }

    val ncdNonEligibleList = database.benDao.getAllNCDNonEligibleList()
        .map { list -> list.map { it.asBenBasicDomainModelForCbacForm() } }
    val ncdNonEligibleListCount = ncdNonEligibleList.map { it.size }

    val menopauseList = database.benDao.getAllMenopauseStageList()
        .map { list -> list.map { it.asBasicDomainModel() } }
    val menopauseListCount = menopauseList.map { it.size }

    val reproductiveAgeList = database.benDao.getAllReproductiveAgeList()
        .map { list -> list.map { it.asBasicDomainModelForFpotForm() } }
    val reproductiveAgeListCount = reproductiveAgeList.map { it.size }

    val infantList = database.benDao.getAllInfantList()
        .map { list -> list.map { it.asBenBasicDomainModelForHbncForm() } }
    val infantListCount = infantList.map { it.size }

    val childList = database.benDao.getAllChildList()
        .map { list -> list.map { it.asBasicDomainModel() } }
    val childListCount = childList.map { it.size }

    val adolescentList =
        database.benDao.getAllAdolescentList()
            .map { list -> list.map { it.asBasicDomainModel() } }
    val adolescentListCount = adolescentList.map { it.size }

    val immunizationList = database.benDao.getAllImmunizationDueList()
        .map { list -> list.map { it.asBasicDomainModel() } }
    val immunizationListCount = menopauseList.map { it.size }

    val hrpList =
        database.benDao.getAllHrpCasesList().map { list -> list.map { it.asBasicDomainModel() } }
    val hrpListCount = menopauseList.map { it.size }

    val pncMotherList = database.benDao.getAllPNCMotherList()
        .map { list -> list.map { it.asBasicDomainModelForPmjayForm() } }
    val pncMotherListCount = pncMotherList.map { it.size }

    val cdrList = database.benDao.getAllCDRList()
        .map { list -> list.map { it.asBenBasicDomainModelForCdrForm() } }
//    val cdrListCount = cdrList.map { it.size }

    val mdsrList = database.benDao.getAllMDSRList()
        .map { list -> list.map { it.asBenBasicDomainModelForMdsrForm() } }
//    val mdsrListCount = mdsrList.map { it.size }


//    private fun getNcdTimestamp() = Calendar.getInstance().run {
//        add(Calendar.YEAR, -30)
//        timeInMillis
//    }
//
//    private fun maxReproductiveAge() = Calendar.getInstance().run {
//        add(Calendar.YEAR, -15)
//        timeInMillis
//    }
//
//    private fun minAdolescentAge(): Long {
//        val minAdolescentAge = Calendar.getInstance().run {
//            add(Calendar.YEAR, -15)
//            timeInMillis
//        }
//        return minAdolescentAge
//    }
//
//    private fun cdrTimestamp(): Long {
//        val cdrTimestamp = Calendar.getInstance().run {
//            add(Calendar.YEAR, -15)
//            timeInMillis
//        }
//        return cdrTimestamp
//    }
//
//    private fun minReproductiveAge() = Calendar.getInstance().run {
//        add(Calendar.YEAR, -60)
//        timeInMillis
//    }
//
//    private fun infantTimestamp(): Long {
//        val infantTimestamp = Calendar.getInstance().run {
//            add(Calendar.YEAR, -2)
//            timeInMillis
//        }
//        return infantTimestamp
//    }
//
//    private fun minChildAge(): Long {
//        val minChildAge = Calendar.getInstance().run {
//            add(Calendar.YEAR, -6)
//            timeInMillis
//        }
//        return minChildAge
//    }
//
//    private fun maxAdolescentAge(): Long {
//        val maxAdolescentAge = Calendar.getInstance().run {
//            add(Calendar.YEAR, -6)
//            timeInMillis
//        }
//        return maxAdolescentAge
//    }
//
//    private fun maxChildAge(): Long {
//        val maxChildAge = Calendar.getInstance().run {
//            add(Calendar.YEAR, -2)
//            timeInMillis
//        }
//        return maxChildAge
//    }

}