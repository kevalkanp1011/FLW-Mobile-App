package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.HouseholdDao
import org.piramalswasthya.sakhi.model.HouseholdCache
import javax.inject.Inject

class HouseholdRepo @Inject constructor(
    private val dao: HouseholdDao
) {
    suspend fun getDraftRecord(): HouseholdCache? {
        return withContext(Dispatchers.IO) {
            dao.getDraftHousehold()
        }
    }


    suspend fun getRecord(hhId: Long): HouseholdCache? {
        return withContext(Dispatchers.IO) {
            dao.getHousehold(hhId)
        }
    }

    suspend fun persistRecord(householdCache: HouseholdCache?, isFinal : Boolean = false) {
        withContext(Dispatchers.IO) {
            householdCache?.let {
                dao.upsert(it)
            }
        }
    }

    suspend fun substituteHouseholdIdForDraft(it: HouseholdCache) {
        dao.substituteHouseholdId(0, it.householdId)
        it.isDraft = false
    }
//
//    suspend fun persistSecondPage(form: HouseholdFormDataset) {
//        val household =
//            form.getHouseholdForSecondPage()
//        dao.upsert(household)
//    }
//
//    suspend fun persistThirdPage(form: HouseholdFormDataset, locationRecord: LocationRecord): Long {
//        val household =
//            form.getHouseholdForThirdPage()
//        val user =
//            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
//        household.apply {
//            if (householdId == 0L) {
//                val newId = HouseholdFormDataset.getHHidFromUserId(ashaId)
//                dao.substituteHouseholdId(householdId, newId)
//                householdId = newId
//                serverUpdatedStatus = 1
//                processed = "N"
//            } else {
//                serverUpdatedStatus = 2
//            }
//
//            if (createdTimeStamp == null) {
//                createdTimeStamp = System.currentTimeMillis()
//                createdBy = user.userName
//            }
//            updatedTimeStamp = System.currentTimeMillis()
//            updatedBy = user.userName
//            stateId = locationRecord.stateId
//            state = locationRecord.state
//            districtId = locationRecord.districtId
//            district = locationRecord.district
//            blockId = locationRecord.blockId
//            block = locationRecord.block
//            villageId = locationRecord.villageId
//            village = locationRecord.village
//            countyId = locationRecord.countryId
//        }
//        dao.upsert(household)
//        return household.householdId
//    }

    suspend fun deleteHouseholdDraft() {
        withContext(Dispatchers.IO) {
            dao.deleteDraftHousehold()
        }
    }


}