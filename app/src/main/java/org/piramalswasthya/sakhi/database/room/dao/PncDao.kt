package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.PNCVisitCache

@Dao
interface PncDao {

    @Query("select * from pnc_visit where benId = :benId and pncPeriod = :visitNumber limit 1")
    fun getSavedRecord(benId: Long, visitNumber: Int): PNCVisitCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pncCache: PNCVisitCache)
}