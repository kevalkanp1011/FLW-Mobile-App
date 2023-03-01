package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.CDRCache

@Dao
interface CdrDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg cdrCache: CDRCache)

    @Query("SELECT * FROM CDR WHERE processed = 'N'")
    suspend fun getAllUnprocessedCdr(): List<CDRCache>

    @Query("select count(*) from CDR")
    suspend fun cdrCount(): Int

    @Update
    suspend fun setSynced(it: CDRCache)
}