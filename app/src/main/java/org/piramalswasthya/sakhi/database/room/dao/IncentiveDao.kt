package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.IncentiveActivityCache
import org.piramalswasthya.sakhi.model.IncentiveCache
import org.piramalswasthya.sakhi.model.IncentiveRecordCache

@Dao
interface IncentiveDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg incentiveActivity: IncentiveActivityCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg incentiveRecord: IncentiveRecordCache)

    @Query("select * from `incentive-records`")
    fun getAllRecords(): Flow<List<IncentiveCache>>


}