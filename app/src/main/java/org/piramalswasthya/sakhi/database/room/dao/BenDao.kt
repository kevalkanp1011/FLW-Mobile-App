package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.BenRegCache

@Dao
interface BenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(ben : BenRegCache)

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 1 and householdId =:hhId LIMIT 1")
    suspend fun getDraftBenForHousehold(hhId: Long) : BenRegCache?

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0")
    fun getAllBen() : LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE beneficiaryId =:benId LIMIT 1")
    suspend fun getBen(benId  : Long) : BenRegCache
}