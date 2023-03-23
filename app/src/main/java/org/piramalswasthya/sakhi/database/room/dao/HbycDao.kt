package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.HBYCCache

@Dao
interface HbycDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg hbycCache: HBYCCache)

    @Query("SELECT * FROM HBYC WHERE processed = 'N'")
    suspend fun getAllUnprocessedHBYC(): List<HBYCCache>

    @Query("select count(*) from HBYC")
    suspend fun hbycCount(): Int

    @Query("SELECT * FROM HBYC WHERE benId =:benId AND hhId = :hhId LIMIT 1")
    suspend fun getHbyc(hhId: Long, benId: Long): HBYCCache?

    @Update
    suspend fun setSynced(it: HBYCCache)
}