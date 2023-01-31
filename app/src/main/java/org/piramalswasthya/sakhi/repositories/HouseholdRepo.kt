package org.piramalswasthya.sakhi.repositories

import android.content.Context
import androidx.lifecycle.Transformations
import org.piramalswasthya.sakhi.configuration.HouseholdFormDataset
import org.piramalswasthya.sakhi.database.room.BeneficiaryIdsAvail
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import javax.inject.Inject

class HouseholdRepo @Inject constructor(
    private val database: InAppDb,
    private val tmcNetworkApiService: TmcNetworkApiService
) {
    val householdList by lazy {
        Transformations.map(database.householdDao.getAllHouseholds()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    suspend fun getDraftForm(context: Context): HouseholdFormDataset? {
        return HouseholdFormDataset(context)
    }

    suspend fun persistFirstPage(form: HouseholdFormDataset) {
        //TODO(Delete this dummy after checking)
        //database.dummyDao.insert(BeneficiaryIdsAvail(data = "Entry 1", sync = SyncState.UNSYNCED))

        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val draftHousehold = database.householdDao.getDraftHousehold()
        val household = if (draftHousehold == null)
            form.getHouseholdForFirstPage(
                user.userId,
                HouseholdFormDataset.getHHidFromUserId(user.userId)
            )
        else
            form.getHouseholdForFirstPage(draftHousehold.ashaId, draftHousehold.householdId)
        database.householdDao.upsert(household)
    }

    suspend fun persistSecondPage(form: HouseholdFormDataset) {

        val draftHousehold = database.householdDao.getDraftHousehold()
            ?: throw IllegalStateException("no draft saved!!")
        val household =
            form.getHouseholdForSecondPage(draftHousehold.ashaId, draftHousehold.householdId)
        database.householdDao.upsert(household)
    }

    suspend fun persistThirdPage(form: HouseholdFormDataset) : Long {

        val draftHousehold = database.householdDao.getDraftHousehold()
            ?: throw IllegalStateException("no draft saved!!")
        val household =
            form.getHouseholdForThirdPage(draftHousehold.ashaId, draftHousehold.householdId)
        database.householdDao.upsert(household)
        return household.householdId
    }


}