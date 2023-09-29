package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.database.room.dao.PncDao
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.PNCVisitCache
import org.piramalswasthya.sakhi.network.AmritApiService
import javax.inject.Inject

class PncRepo @Inject constructor(
    private val amritApiService: AmritApiService,
    private val maternalHealthDao: MaternalHealthDao,
    private val pncDao: PncDao,
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val benDao: BenDao,
    private val preferenceDao: PreferenceDao,
) {
    suspend fun getSavedPncRecord(benId: Long, visitNumber: Int): PNCVisitCache? {
        return withContext(Dispatchers.IO) {
            pncDao.getSavedRecord(benId, visitNumber)
        }
    }

    suspend fun persistPncRecord(pncCache: PNCVisitCache) {
        withContext(Dispatchers.IO){
            pncDao.insert(pncCache)
        }

    }
}