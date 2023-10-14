package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.IncentiveDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.IncentiveActivityListRequest
import org.piramalswasthya.sakhi.model.IncentiveRecordListRequest
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.model.getDateTimeStringFromLong
import org.piramalswasthya.sakhi.network.AmritApiService
import timber.log.Timber
import javax.inject.Inject

class IncentiveRepo @Inject constructor(
    private val amritApiService: AmritApiService,
    private val incentiveDao: IncentiveDao

) {

    suspend fun pullAndSaveAllIncentiveActivities(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {

                val requestBody = IncentiveActivityListRequest(0, 0)
                val activityListNetwork =
                    amritApiService.getAllIncentiveActivities(requestBody = requestBody)
                val activityList = activityListNetwork.data.map { it.asCacheModel() }
                incentiveDao.insert(*activityList.toTypedArray())
                true

            } catch (e: Exception) {
                Timber.d("Caught $e at incentives!")
                false
            }
        }
    }

    suspend fun pullAndSaveAllIncentiveRecords(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {

                val requestBody = IncentiveRecordListRequest(
                    user.userId, getDateTimeStringFromLong(
                        Konstants.defaultTimeStamp)!!,
                        getDateTimeStringFromLong(System.currentTimeMillis())!!
                    )
                val recordListNetwork =
                    amritApiService.getAllIncentiveRecords(requestBody = requestBody)
                val list = recordListNetwork.data.map { it.asCacheModel() }
                incentiveDao.insert(*list.toTypedArray())
                true

            } catch (e: Exception) {
                Timber.d("Caught $e at incentives!")
                false
            }
        }
    }
}