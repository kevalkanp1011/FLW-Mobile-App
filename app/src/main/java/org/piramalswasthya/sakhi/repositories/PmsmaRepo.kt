package org.piramalswasthya.sakhi.repositories

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.PMSMACache
import org.piramalswasthya.sakhi.network.NcdNetworkApiService
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import timber.log.Timber
import javax.inject.Inject

class PmsmaRepo @Inject constructor(
    private val context: Application,
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val tmcNetworkApiService: TmcNetworkApiService,
    private val ncdNetworkApiService: NcdNetworkApiService
) {
    suspend fun savePmsmaData(pmsmaCache: PMSMACache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                pmsmaCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                }
                database.pmsmaDao.upsert(pmsmaCache)

                true
            } catch (e: Exception) {
                Timber.d("Error : $e raised at savePmsmaData")
                false
            }
        }
    }

}