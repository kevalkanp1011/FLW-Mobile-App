package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.PMSMACache

@Dao
interface PmsmaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg pmsmaCache: PMSMACache)

    @Query("SELECT * FROM PMSMA WHERE processed = 'N'")
    suspend fun getAllUnprocessedPmsma(): List<PMSMACache>

    @Query("select count(*) from PMSMA")
    suspend fun pmsmaCount(): Int

    @Update
    abstract fun updatePmsmaRecord(it: PMSMACache)

}