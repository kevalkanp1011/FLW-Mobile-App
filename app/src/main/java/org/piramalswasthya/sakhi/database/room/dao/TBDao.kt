package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.TBScreeningCache
import org.piramalswasthya.sakhi.model.TBSuspectedCache

@Dao
interface TBDao {

    @Query("SELECT * FROM TB_SCREENING WHERE benId =:benId limit 1")
    fun getTbsn(benId: Long) : TBScreeningCache?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveTbsn(tbScreeningCache: TBScreeningCache)

    @Query("SELECT * FROM TB_SUSPECTED WHERE benId =:benId limit 1")
    fun getTbSuspected(benId: Long) : TBSuspectedCache?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveTbSuspected(tbSuspectedCache: TBSuspectedCache)

}