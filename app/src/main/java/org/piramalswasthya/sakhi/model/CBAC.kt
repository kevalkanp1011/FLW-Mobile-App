package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CBAC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = UserCache::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("ashaId"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class CbacCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    val benId: Long,
    @ColumnInfo(index = true)
    val hhId: Long,
    @ColumnInfo(index = true)
    val ashaId: Int,
    var cbac_age: String? = null,
    var cbac_age_posi: Int = 0,
    var cbac_smoke: String? = null,
    var cbac_smoke_posi: Int = 0,
    var cbac_alcohol: String? = null,
    var cbac_alcohol_posi: Int = 0,
    var cbac_waist: String? = null,
    var cbac_waist_posi: Int = 0,
    var cbac_pa: String? = null,
    var cbac_pa_posi: Int = 0,
    var cbac_familyhistory: String? = null,
    var cbac_familyhistory_posi: Int = 0,
    var total_score: Int = 0,
    var cbac_sufferingtb: String? = null,
    var cbac_sufferingtb_pos: Int = 0,
    var cbac_antitbdrugs: String? = null,
    var cbac_antitbdrugs_pos: Int = 0,
    var cbac_tbhistory: String? = null,
    var cbac_tbhistory_pos: Int = 0,
    var cbac_sortnesofbirth: String? = null,
    var cbac_sortnesofbirth_pos: Int = 0,
    var cbac_coughing: String? = null,
    var cbac_coughing_pos: Int = 0,
    var cbac_bloodsputum: String? = null,
    var cbac_bloodsputum_pos: Int = 0,
    var cbac_fivermore: String? = null,
    var cbac_fivermore_pos: Int = 0,
    var cbac_loseofweight: String? = null,
    var cbac_loseofweight_pos: Int = 0,
    var cbac_nightsweats: String? = null,
    var cbac_nightsweats_pos: Int = 0,
    var cbac_historyoffits: String? = null,
    var cbac_historyoffits_pos: Int = 0,
    var cbac_difficultyinmouth: String? = null,
    var cbac_difficultyinmouth_pos: Int = 0,
    var cbac_uicers: String? = null,
    var cbac_uicers_pos: Int = 0,
    var cbac_toneofvoice: String? = null,
    var cbac_toneofvoice_pos: Int = 0,
    var cbac_lumpinbreast: String? = null,
    var cbac_lumpinbreast_pos: Int = 0,
    var cbac_blooddischage: String? = null,
    var cbac_blooddischage_pos: Int = 0,
    var cbac_changeinbreast: String? = null,
    var cbac_changeinbreast_pos: Int = 0,
    var cbac_bleedingbtwnperiods: String? = null,
    var cbac_bleedingbtwnperiods_pos: Int = 0,
    var cbac_bleedingaftermenopause: String? = null,
    var cbac_bleedingaftermenopause_pos: Int = 0,
    var cbac_bleedingafterintercourse: String? = null,
    var cbac_bleedingafterintercourse_pos: Int = 0,
    var cbac_foulveginaldischarge: String? = null,
    var cbac_foulveginaldischarge_pos: Int = 0,
    var cbac_referpatient_mo: String? = null,
    var cbac_tracing_all_fm: String? = null,
    var cbac_sputemcollection: String? = null,
    var serverUpdatedStatus: Int = 0,
    var createdBy: String? = null,
    var createdDate: String? = null,
    var ProviderServiceMapID: Int = 0,
    var VanID: Int = 0,
    var Processed: String? = null,
    var Countyid: Int = 0,
    var stateid: Int = 0,
    var districtid: Int = 0,
    var districtname: String? = null,
    var villageid: Int = 0,
    var hrp_suspected: Boolean? = null,
    var suspected_hrp: String? = null,
    var ncd_suspected: String? = null,
    var suspected_ncd: String? = null,
    var suspected_tb: String? = null,
    var suspected_ncd_diseases: String? = null,
    var cbac_reg_i: Int = 0,


    )