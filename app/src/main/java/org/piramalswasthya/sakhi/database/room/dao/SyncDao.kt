package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.SyncStatusCache

@Dao
interface SyncDao {

    @Query(
        "       SELECT 'Beneficiary' as name, b1.syncState as syncState, count(*) as count from beneficiary b1 group by b1.syncState "
                + "UNION SELECT 'CBAC' as name,  c1.syncState as syncState, count(*) as count from cbac c1 group by c1.syncState "
                + "UNION SELECT 'TB Screening' as name,  tbsn.syncState as syncState, count(*) as count from TB_SCREENING tbsn group by tbsn.syncState "
                + "UNION SELECT 'TB Suspected' as name,  tbsp.syncState as syncState, count(*) as count from TB_SUSPECTED tbsp group by tbsp.syncState "
                + "UNION SELECT 'EC Registration' as name,  ecr.syncState as syncState, count(*) as count from eligible_couple_reg ecr group by ecr.syncState "
                + "UNION SELECT 'EC Tracking' as name,  ect.syncState as syncState, count(*) as count from eligible_couple_tracking ect group by ect.syncState "
                + "UNION SELECT 'PW Registration' as name,  pwr.syncState as syncState, count(*) as count from pregnancy_register pwr group by pwr.syncState "
                + "UNION SELECT 'PW ANC' as name,  pwanc.syncState as syncState, count(*) as count from pregnancy_anc pwanc group by pwanc.syncState "
                + "UNION SELECT 'Delivery Out' as name,  do.syncState as syncState, count(*) as count from delivery_outcome do group by do.syncState "
                + "UNION SELECT 'Infant Reg' as name,  ir.syncState as syncState, count(*) as count from infant_reg ir group by ir.syncState "
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