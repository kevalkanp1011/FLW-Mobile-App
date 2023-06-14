package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.TBScreeningCache

@Dao
interface TBDao {

    @Query("SELECT * FROM TB_SCREENING WHERE benId =:benId limit 1")
    fun getTbsn(benId: Long) : TBScreeningCache?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveTbsn(tbScreeningCache: TBScreeningCache)

}