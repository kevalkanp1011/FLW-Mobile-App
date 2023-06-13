package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.EligibleCoupleRegCache
import org.piramalswasthya.sakhi.model.EligibleCoupleTrackingCache
import javax.inject.Inject

class EcrRepo @Inject constructor(
    private val database: InAppDb
)  {

    suspend fun persistRecord(ecrForm: EligibleCoupleRegCache) {
        withContext(Dispatchers.IO){
            database.ecrDao.upsert(ecrForm)
        }
    }

    suspend fun getBenFromId(benId: Long): BenRegCache? {
        return withContext(Dispatchers.IO){
            database.benDao.getBen(benId)
        }
    }

    suspend fun getSavedRecord(benId: Long): EligibleCoupleRegCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getSavedECR(benId)
        }
    }

    suspend fun getEct(benId: Long): EligibleCoupleTrackingCache? {
        return withContext(Dispatchers.IO) {
            database.ecrDao.getEct(benId)
        }
    }

    suspend fun saveEct(eligibleCoupleTrackingCache: EligibleCoupleTrackingCache) {
        withContext(Dispatchers.IO){
            database.ecrDao.saveRecord(eligibleCoupleTrackingCache)
        }
    }

}