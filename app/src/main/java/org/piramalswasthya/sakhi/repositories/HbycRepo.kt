package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.HBYCCache
import org.piramalswasthya.sakhi.network.D2DApiService
import timber.log.Timber
import javax.inject.Inject

class HbycRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DApiService
) {

    suspend fun saveHbycData(hbycCache: HBYCCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
//                hbncCache.apply {
//                    createdBy = user.userName
//                    createdDate = System.currentTimeMillis()
//                }

                database.hbycDao.upsert(hbycCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveHbncData")
                false
            }
        }
    }
}