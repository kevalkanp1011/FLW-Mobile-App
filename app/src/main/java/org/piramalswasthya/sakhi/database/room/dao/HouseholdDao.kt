package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.HouseholdCache

@Dao
interface HouseholdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg household : HouseholdCache)

    @Query("SELECT * FROM HOUSEHOLD WHERE isDraft = 1 LIMIT 1")
    suspend fun getDraftHousehold(): HouseholdCache?

    @Query("SELECT * FROM HOUSEHOLD WHERE isDraft = 0")
    fun getAllHouseholds(): LiveData<List<HouseholdCache>>

    @Query("SELECT * FROM HOUSEHOLD WHERE householdId =:hhId LIMIT 1")
    suspend fun getHousehold(hhId: Long): HouseholdCache?

    @Query("DELETE  FROM HOUSEHOLD WHERE isDraft=1")
    suspend fun deleteDraftHousehold()

    @Query("UPDATE BENEFICIARY SET processed = \"P\" WHERE householdId =:hhId")
    suspend fun householdSyncedWithServer(vararg hhId: Long)
}