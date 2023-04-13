package org.piramalswasthya.sakhi.repositories

import android.content.Context
import androidx.lifecycle.Transformations
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HouseholdFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.LocationRecord
import javax.inject.Inject

class HouseholdRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: InAppDb
) {
    suspend fun getDraftForm(): HouseholdFormDataset? {
        return withContext(Dispatchers.IO) {
           val it =  database.householdDao.getDraftHousehold()
            it?.let { HouseholdFormDataset(context, it) }
        }
    }


    suspend fun getHouseholdForm(hhId: Long): HouseholdFormDataset {
        return withContext(Dispatchers.IO) {
            val it = database.householdDao.getHousehold(hhId)
            HouseholdFormDataset(context, it!!)
        }
    }

    suspend fun persistFirstPage(form: HouseholdFormDataset) {
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val household = form.getHouseholdForFirstPage(user.userId)
        database.householdDao.upsert(household)
    }

    suspend fun persistSecondPage(form: HouseholdFormDataset) {
        val household =
            form.getHouseholdForSecondPage()
        database.householdDao.upsert(household)
    }

    suspend fun persistThirdPage(form: HouseholdFormDataset, locationRecord: LocationRecord): Long {
        val household =
            form.getHouseholdForThirdPage()
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        household.apply {
            if (householdId == 0L) {
                val newId = HouseholdFormDataset.getHHidFromUserId(ashaId)
                database.householdDao.substituteBenId(householdId, newId)
                householdId = newId
                serverUpdatedStatus = 1
                processed = "N"
            } else {
                serverUpdatedStatus = 2
            }

            if (createdTimeStamp == null) {
                createdTimeStamp = System.currentTimeMillis()
                createdBy = user.userName
            }
            updatedTimeStamp = System.currentTimeMillis()
            updatedBy = user.userName
            stateId = locationRecord.stateId
            state = locationRecord.state
            districtId = locationRecord.districtId
            district = locationRecord.district
            blockId = locationRecord.blockId
            block = locationRecord.block
            villageId = locationRecord.villageId
            village = locationRecord.village
            countyId = locationRecord.countryId
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