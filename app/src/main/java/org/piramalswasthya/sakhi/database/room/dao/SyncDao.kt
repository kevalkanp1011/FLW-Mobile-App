package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.SyncStatusCache

@Dao
interface SyncDao {

    @Query(
        "       SELECT 'BENEFICIARY' as name, b1.syncState as syncState, count(*) as count from beneficiary b1 group by b1.syncState union " +
                "       SELECT 'CBAC' as name,  c1.syncState as syncState, count(*) as count from cbac c1 group by c1.syncState "
    )
//    @Query(
//        "       SELECT count(*) as ben_synced from beneficiary b1 where b1.syncState = 2 union " +
//                "SELECT count(*) as ben_not_synced from beneficiary b2 where b2.syncState = 0 union " +
//                "SELECT count(*) as ben_syncing from beneficiary b3 where b3.syncState = 1 union " +
//                "SELECT count(*) as cbac_synced from cbac c1 where c1.syncState = 2 union " +
//                "SELECT count(*) as cbac_not_synced from cbac c2 where c2.syncState = 0 union " +
//                "SELECT count(*) as cbac_syncing from cbac c3 where c3.syncState = 1 "
//    )
    fun getSyncStatus(): Flow<List<SyncStatusCache>>
}