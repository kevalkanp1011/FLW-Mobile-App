package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.EligibleCoupleRegCache

@Dao
interface EcrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg ecrCache: EligibleCoupleRegCache)

    @Query("SELECT * FROM ELIGIBLE_COUPLE_REG WHERE processed = 'N'")
    suspend fun getAllUnprocessedECR(): List<EligibleCoupleRegCache>

    @Query("select count(*) from ELIGIBLE_COUPLE_REG")
    suspend fun ecrCount(): Int

    @Query("SELECT * FROM ELIGIBLE_COUPLE_REG WHERE benId =:benId limit 1")
    suspend fun getSavedECR(benId: Long): EligibleCoupleRegCache?

    @Update
    suspend fun update(it: EligibleCoupleRegCache)

}