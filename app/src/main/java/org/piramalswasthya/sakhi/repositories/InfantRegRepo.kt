package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.InfantRegDao
import org.piramalswasthya.sakhi.model.InfantRegCache
import javax.inject.Inject

class InfantRegRepo @Inject constructor(
    private val infantRegDao: InfantRegDao
) {

    suspend fun getInfantReg(benId: Long): InfantRegCache? {
        return withContext(Dispatchers.IO) {
            infantRegDao.getInfantReg(benId)
        }
    }

    suspend fun saveInfantReg(infantRegCache: InfantRegCache) {
        withContext(Dispatchers.IO) {
            infantRegDao.saveInfantReg(infantRegCache)
        }
    }
}