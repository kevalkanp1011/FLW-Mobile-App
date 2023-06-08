package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.PregnantWomanRegistrationCache
import javax.inject.Inject

class MaternalHealthRepo @Inject constructor(
    private val maternalHealthDao: MaternalHealthDao,
    private val benDao: BenDao
) {

    suspend fun getSavedRecord(benId: Long): PregnantWomanRegistrationCache? {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getSavedRecord(benId)
        }
    }

    suspend fun getBenFromId(benId: Long): BenRegCache? {
        return withContext(Dispatchers.IO){
            benDao.getBen(benId)
        }
    }

    suspend fun persistRecord(pregnancyRegistrationForm: PregnantWomanRegistrationCache) {
        withContext(Dispatchers.IO){
            maternalHealthDao.saveRecord(pregnancyRegistrationForm)
        }
    }

}