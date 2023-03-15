package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.HBNCCache


@Dao
interface HbncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg hbncCache: HBNCCache)

    @Query("SELECT * FROM HBNC WHERE processed = 'N'")
    suspend fun getAllUnprocessedFPOT(): List<HBNCCache>

    @Query("select count(*) from HBNC")
    suspend fun fpotCount(): Int

    @Query("SELECT * FROM HBNC WHERE benId =:benId AND hhId = :hhId LIMIT 1")
    suspend fun getFpot(hhId: Long, benId: Long): HBNCCache?

    @Update
    suspend fun setSynced(it: HBNCCache)
}