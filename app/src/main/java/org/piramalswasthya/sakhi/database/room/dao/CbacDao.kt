package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.model.CbacCache

@Dao
interface CbacDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg cbacCache: CbacCache)

    @Query("SELECT * FROM CBAC WHERE benId = :benId LIMIT 1")
    suspend fun getCbacFromBenId(benId: Long): CbacCache?


    @Query("SELECT * FROM CBAC WHERE processed = 'N'")
    suspend fun getAllUnprocessedCbac() : List<CbacCache>


    @Query("UPDATE CBAC SET processed = 'P' WHERE benId =:benId")
    suspend fun cbacSyncedWithServer(vararg benId: Long)

}