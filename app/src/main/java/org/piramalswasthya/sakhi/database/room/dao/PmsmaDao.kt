package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.PMSMACache

@Dao
interface PmsmaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg pmsmaCache: PMSMACache)

    @Query("SELECT * FROM PMSMA WHERE processed = 'N'")
    suspend fun getAllUnprocessedPmsma(): List<PMSMACache>
}