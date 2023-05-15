package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.HouseholdCache

@Dao
interface HouseholdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg household : HouseholdCache)

    @Query("SELECT * FROM HOUSEHOLD WHERE isDraft = 1 LIMIT 1")
    suspend fun getDraftHousehold(): HouseholdCache?

    @Query("SELECT * FROM HOUSEHOLD WHERE isDraft = 0 and loc_villageId = :selectedVillage")
    fun getAllHouseholds(selectedVillage: Int): Flow<List<HouseholdCache>>

    @Query("SELECT * FROM HOUSEHOLD WHERE householdId =:hhId LIMIT 1")
    suspend fun getHousehold(hhId: Long): HouseholdCache?

    @Query("DELETE  FROM HOUSEHOLD WHERE isDraft=1")
    suspend fun deleteDraftHousehold()

    @Query("UPDATE HOUSEHOLD SET processed = 'P' WHERE householdId =:hhId")
    suspend fun householdSyncedWithServer(vararg hhId: Long)

    @Query("UPDATE HOUSEHOLD SET householdId  = :newId WHERE householdId = :oldId ")
    suspend fun substituteHouseholdId(oldId: Long, newId: Long)

    @Query("SELECT * FROM household WHERE isDraft = 0 AND processed = 'N'")
    suspend fun getAllUnprocessedHousehold() : List<HouseholdCache>

}