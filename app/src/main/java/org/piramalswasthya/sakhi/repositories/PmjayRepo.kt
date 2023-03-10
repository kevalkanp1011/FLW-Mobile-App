package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.PMJAYCache
import org.piramalswasthya.sakhi.network.D2DNetworkApiService
import timber.log.Timber
import javax.inject.Inject

class PmjayRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DNetworkApiService
) {

    suspend fun savePmjayData(pmjayCache: PMJAYCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                pmjayCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                }

                database.pmjayDao.upsert(pmjayCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveCdrData")
                false
            }
        }
    }
}