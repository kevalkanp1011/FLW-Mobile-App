package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.DeliveryOutcomeDao
import org.piramalswasthya.sakhi.model.DeliveryOutcomeCache
import javax.inject.Inject

class DeliveryOutcomeRepo @Inject constructor(
    private val deliveryOutcomeDao: DeliveryOutcomeDao
) {

    suspend fun getDeliveryOutcome(benId: Long): DeliveryOutcomeCache? {
        return withContext(Dispatchers.IO) {
            deliveryOutcomeDao.getDeliveryOutcome(benId)
        }
    }

    suspend fun saveDeliveryOutcome(deliveryOutcomeCache: DeliveryOutcomeCache) {
        withContext(Dispatchers.IO) {
            deliveryOutcomeDao.saveDeliveryOutcome(deliveryOutcomeCache)
        }
    }
}