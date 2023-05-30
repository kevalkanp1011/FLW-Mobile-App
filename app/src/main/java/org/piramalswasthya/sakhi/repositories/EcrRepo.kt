package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.EligibleCoupleRegCache
import org.piramalswasthya.sakhi.network.D2DApiService
import timber.log.Timber
import javax.inject.Inject

class EcrRepo @Inject constructor(
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val d2DNetworkApiService: D2DApiService
)  {

    suspend fun saveEcrData(ecrCache: EligibleCoupleRegCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
//                ecrCache.apply {
//                    createdBy = user.userName
//                    createdDate = System.currentTimeMillis()
//                }

                database.ecrDao.upsert(ecrCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at saveHbncData")
                false
            }
        }
    }

}