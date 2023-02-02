package org.piramalswasthya.sakhi.repositories

import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HouseholdFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.LocationRecord
import javax.inject.Inject

class HouseholdRepo @Inject constructor(
    private val database: InAppDb,
//    private val tmcNetworkApiService: TmcNetworkApiService
) {
    val householdList by lazy {
        Transformations.map(database.householdDao.getAllHouseholds()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    suspend fun getDraftForm(): HouseholdCache? {
        return withContext(Dispatchers.IO) {
            database.householdDao.getDraftHousehold()
        }
    }

    suspend fun persistFirstPage(form: HouseholdFormDataset) {
        //TODO(Delete this dummy after checking)
        //database.dummyDao.insert(BeneficiaryIdsAvail(data = "Entry 1", sync = SyncState.UNSYNCED))

        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        //val draftHousehold = database.householdDao.getDraftHousehold()
        val household = form.getHouseholdForFirstPage(user.userId)
        database.householdDao.upsert(household)
    }

    suspend fun persistSecondPage(form: HouseholdFormDataset) {

//        val draftHousehold = database.householdDao.getDraftHousehold()
//            ?: throw IllegalStateException("no draft saved!!")
        val household =
            form.getHouseholdForSecondPage()
        database.householdDao.upsert(household)
    }

    suspend fun persistThirdPage(form: HouseholdFormDataset, locationRecord: LocationRecord) : Long {

//        val draftHousehold = database.householdDao.getDraftHousehold()
//            ?: throw IllegalStateException("no draft saved!!")
        val household =
            form.getHouseholdForThirdPage()
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        household.apply {
            if(createdTimeStamp==null) {
                createdTimeStamp = System.currentTimeMillis()
                createdBy = user.userName
            }
            else {
                updatedTimeStamp = System.currentTimeMillis()
                updatedBy = user.userName
            }
            stateId = locationRecord.stateId
            state = locationRecord.state
            districtId = locationRecord.districtId
            district = locationRecord.district
            blockId = locationRecord.blockId
            block = locationRecord.block
            villageId = locationRecord.villageId
            village = locationRecord.village
        }
        database.householdDao.upsert(household)
        return household.householdId
    }

    suspend fun deleteHouseholdDraft() {
        withContext(Dispatchers.IO) {
            database.householdDao.deleteDraftHousehold()
        }
    }


}