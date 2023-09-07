package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.CDRCache
import org.piramalswasthya.sakhi.model.PMSMACache

@Dao
interface PmsmaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg pmsmaCache: PMSMACache)

    @Query("SELECT * FROM PMSMA WHERE processed = 'N'")
    suspend fun getAllUnprocessedPmsma(): List<PMSMACache>

    @Query("select count(*) from PMSMA")
    suspend fun pmsmaCount(): Int

    @Query("SELECT * FROM PMSMA WHERE benId =:benId LIMIT 1")
    suspend fun getPmsma(benId: Long): PMSMACache?

    @Update
    abstract fun updatePmsmaRecord(it: PMSMACache)

}