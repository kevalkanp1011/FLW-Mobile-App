package org.piramalswasthya.sakhi.repositories

import android.content.Context
import androidx.lifecycle.Transformations
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import timber.log.Timber
import javax.inject.Inject

class BenRepo @Inject constructor(
    private val database: InAppDb,
    private val tmcNetworkApiService: TmcNetworkApiService
) {

    val benList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllBen()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    suspend fun getDraftForm(context: Context): BenKidRegFormDataset? {
        return null
    }

    suspend fun persistFirstPage(form: BenKidRegFormDataset, hhId: Long) {
        Timber.d("Persisting first page!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val draftBen = database.benDao.getDraftBenForHousehold(hhId)
        val ben = if (draftBen == null)
            form.getBenForFirstPage(
                user.userId,
                hhId
            )
        else
            form.getBenForFirstPage(draftBen.ashaId, draftBen.householdId)
        database.benDao.upsert(ben)
    }

    suspend fun persistSecondPage(form: BenKidRegFormDataset, hhId: Long) {
        val draftBen = database.benDao.getDraftBenForHousehold(hhId)
            ?: throw IllegalStateException("no draft saved!!")
        val ben =
            form.getBenForSecondPage(draftBen.ashaId, draftBen.householdId)
        database.benDao.upsert(ben)
        return
    }

    suspend fun getBenHousehold(hhId: Long): HouseholdCache {
        return database.householdDao.getHousehold(hhId)

    }

    private suspend fun extractBenId(){


    }

    suspend fun genBen(count : Int = 300){

    }

}