package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.SyncStatusCache

@Dao
interface SyncDao {

    @Query(
        "       SELECT 1 as id, 'Beneficiary' as name, b1.syncState as syncState, count(*) as count from beneficiary b1 group by b1.syncState "
                + "UNION SELECT 2 as id, 'EC Registration' as name,  ecr.syncState as syncState, count(*) as count from eligible_couple_reg ecr group by ecr.syncState "
                + "UNION SELECT 3 as id, 'EC Tracking' as name,  ect.syncState as syncState, count(*) as count from eligible_couple_tracking ect group by ect.syncState "
                + "UNION SELECT 4 as id, 'PW Registration' as name,  pwr.syncState as syncState, count(*) as count from pregnancy_register pwr group by pwr.syncState "
                + "UNION SELECT 5 as id, 'PW ANC' as name,  pwanc.syncState as syncState, count(*) as count from pregnancy_anc pwanc group by pwanc.syncState "
                + "UNION SELECT 6 as id, 'PMSMA' as name,  pmsma.syncState as syncState, count(*) as count from pmsma pmsma group by pmsma.syncState "
                + "UNION SELECT 7 as id, 'Delivery Outcome' as name,  do.syncState as syncState, count(*) as count from delivery_outcome do group by do.syncState "
                + "UNION SELECT 8 as id, 'PNC' as name,  pnc.syncState as syncState, count(*) as count from pnc_visit pnc group by pnc.syncState "
                + "UNION SELECT 9 as id, 'CBAC' as name,  c1.syncState as syncState, count(*) as count from cbac c1 group by c1.syncState "
                + "UNION SELECT 10 as id, 'TB Screening' as name,  tbsn.syncState as syncState, count(*) as count from TB_SCREENING tbsn group by tbsn.syncState "
                + "UNION SELECT 11 as id, 'TB Suspected' as name,  tbsp.syncState as syncState, count(*) as count from TB_SUSPECTED tbsp group by tbsp.syncState "
                + "UNION SELECT 12 as id, 'HRP Assess' as name,  hrpa.syncState as syncState, count(*) as count from HRP_PREGNANT_ASSESS hrpa group by hrpa.syncState "
                + "UNION SELECT 13 as id, 'HRP Track' as name,  hrpt.syncState as syncState, count(*) as count from HRP_PREGNANT_TRACK hrpt group by hrpt.syncState "
                + "UNION SELECT 14 as id, 'HR NonPreg Assess' as name,  hrnpa.syncState as syncState, count(*) as count from HRP_NON_PREGNANT_ASSESS hrnpa group by hrnpa.syncState "
                + "UNION SELECT 15 as id, 'HR NonPreg Track' as name,  hrnpt.syncState as syncState, count(*) as count from HRP_NON_PREGNANT_TRACK hrnpt group by hrnpt.syncState "
                + "UNION SELECT 16 as id, 'Infant Reg' as name,  ir.syncState as syncState, count(*) as count from infant_reg ir group by ir.syncState "
                + "UNION SELECT 17 as id, 'Immunization' as name,  imm.syncState as syncState, count(*) as count from IMMUNIZATION imm group by imm.syncState "
                 + " ORDER by id")
    fun getSyncStatus(): Flow<List<SyncStatusCache>>
}