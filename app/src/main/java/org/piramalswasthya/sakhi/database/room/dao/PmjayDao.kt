package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.PMJAYCache

@Dao
interface PmjayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg pmjayCache: PMJAYCache)

    @Query("SELECT * FROM CDR WHERE processed = 'N'")
    suspend fun getAllUnprocessedPMJAY(): List<PMJAYCache>

    @Query("select count(*) from CDR")
    suspend fun pmjayCount(): Int

    @Query("SELECT * FROM CDR WHERE benId =:benId AND hhId = :hhId LIMIT 1")
    suspend fun getPmjay(hhId: Long, benId: Long): PMJAYCache?

    @Update
    suspend fun setSynced(it: PMJAYCache)
}