package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.piramalswasthya.sakhi.model.DeliveryOutcomeCache

@Dao
interface DeliveryOutcomeDao {

    @Query("SELECT * FROM DELIVERY_OUTCOME WHERE benId =:benId limit 1")
    fun getDeliveryOutcome(benId: Long) : DeliveryOutcomeCache?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveDeliveryOutcome(deliveryOutcomeCache: DeliveryOutcomeCache)

    @Query("SELECT * FROM DELIVERY_OUTCOME WHERE processed = 'N'")
    suspend fun getAllUnprocessedDeliveryOutcomes(): List<DeliveryOutcomeCache>

    @Update
    suspend fun updateDeliveryOutcome(it: DeliveryOutcomeCache)
}