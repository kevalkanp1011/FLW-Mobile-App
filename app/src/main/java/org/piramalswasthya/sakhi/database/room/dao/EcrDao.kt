package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.EligibleCoupleRegCache

@Dao
interface EcrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg ecrCache: EligibleCoupleRegCache)

    @Query("SELECT * FROM EligibleCoupleReg WHERE processed = 'N'")
    suspend fun getAllUnprocessedECR(): List<EligibleCoupleRegCache>

    @Query("select count(*) from EligibleCoupleReg")
    suspend fun ecrCount(): Int

    @Query("SELECT * FROM EligibleCoupleReg WHERE benId =:benId AND hhId = :hhId")
    suspend fun getECR(hhId: Long, benId: Long): EligibleCoupleRegCache?

    @Update
    suspend fun setSynced(it: EligibleCoupleRegCache)

    @Query("SELECT * FROM EligibleCoupleReg WHERE processed = 'N'")
    suspend fun getAllUnprocessedEcr(): List<EligibleCoupleRegCache>

    @Update
    suspend fun update(it: EligibleCoupleRegCache)

}