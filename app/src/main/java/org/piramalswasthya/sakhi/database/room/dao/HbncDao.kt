package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.CDRCache
import org.piramalswasthya.sakhi.model.HBNCCache


@Dao
interface HbncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg hbncCache: HBNCCache)

    @Query("SELECT * FROM HBNC WHERE processed = 'N'")
    suspend fun getAllUnprocessedFPOT(): List<HBNCCache>

    @Query("select count(*) from HBNC")
    suspend fun hbncCount(): Int

    @Query("SELECT * FROM HBNC WHERE benId =:benId AND hhId = :hhId and homeVisitDate = :nthDay LIMIT 1")
    suspend fun getHbnc(hhId: Long, benId: Long, nthDay : Int): HBNCCache?

    @Query("SELECT homeVisitDate FROM HBNC WHERE benId =:benId AND hhId = :hhId ORDER BY homeVisitDate")
    fun gethomeVisitDateList(hhId: Long, benId: Long): Flow<List<Int>>

    @Update
    suspend fun setSynced(it: HBNCCache)
    @Query("SELECT * FROM HBNC WHERE processed = 'N'")
    suspend fun getAllUnprocessedHbnc(): List<HBNCCache>

    @Update
    suspend fun update(it: HBNCCache)

}