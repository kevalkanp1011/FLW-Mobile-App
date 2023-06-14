package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.TBDao
import org.piramalswasthya.sakhi.model.TBScreeningCache
import javax.inject.Inject

class TBRepo @Inject constructor(
    private val tbDao: TBDao) {

    suspend fun getTbsn(benId: Long): TBScreeningCache? {
        return withContext(Dispatchers.IO){
            tbDao.getTbsn(benId)
        }
    }

    suspend fun saveTbsn(tbScreeningCache: TBScreeningCache) {
        withContext(Dispatchers.IO) {
            tbDao.saveTbsn(tbScreeningCache)
        }
    }


}