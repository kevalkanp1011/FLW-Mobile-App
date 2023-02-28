package org.piramalswasthya.sakhi.model

import android.content.res.Resources
import androidx.room.*
import org.piramalswasthya.sakhi.R

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
        )],
    indices = [Index(name = "ind", value = ["benId", "hhId"])]
)
data class CbacCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    @ColumnInfo(index = true)
    val ashaId: Int,
    var cbac_age_posi: Int = 0,
    var cbac_smoke_posi: Int = 0,
    var cbac_alcohol_posi: Int = 0,
    var cbac_waist_posi: Int = 0,
    var cbac_pa_posi: Int = 0,
    var cbac_familyhistory_posi: Int = 0,
    var total_score: Int = 0,
    var cbac_sufferingtb_pos: Int = 0,
    var cbac_antitbdrugs_pos: Int = 0,
    var cbac_tbhistory_pos: Int = 0,
    var cbac_sortnesofbirth_pos: Int = 0,
    var cbac_coughing_pos: Int = 0,
    var cbac_bloodsputum_pos: Int = 0,
    var cbac_fivermore_pos: Int = 0,
    var cbac_loseofweight_pos: Int = 0,
    var cbac_nightsweats_pos: Int = 0,
    var cbac_historyoffits_pos: Int = 0,
    var cbac_difficultyinmouth_pos: Int = 0,
    var cbac_uicers_pos: Int = 0,
    var cbac_toneofvoice_pos: Int = 0,
    var cbac_lumpinbreast_pos: Int = 0,
    var cbac_blooddischage_pos: Int = 0,
    var cbac_changeinbreast_pos: Int = 0,
    var cbac_bleedingbtwnperiods_pos: Int = 0,
    var cbac_bleedingaftermenopause_pos: Int = 0,
    var cbac_bleedingafterintercourse_pos: Int = 0,
    var cbac_foulveginaldischarge_pos: Int = 0,
    //Extras
    var cbac_cloudy_posi: Int = 0,
    var cbac_diffreading_posi: Int = 0,
    var cbac_pain_ineyes_posi: Int = 0,
    var cbac_diff_inhearing_posi: Int = 0,
    var cbac_redness_ineyes_posi: Int = 0,
    var cbac_growth_in_mouth_posi: Int = 0,
    var cbac_white_or_red_patch_posi: Int = 0,
    var cbac_Pain_while_chewing_posi: Int = 0,
    var cbac_hyper_pigmented_patch_posi: Int = 0,
    var cbac_any_thickend_skin_posi: Int = 0,
    var cbac_nodules_on_skin_posi: Int = 0,
    var cbac_numbness_on_palm_posi: Int = 0,
    var cbac_clawing_of_fingers_posi: Int = 0,
    var cbac_tingling_or_numbness_posi: Int = 0,
    var cbac_inability_close_eyelid_posi: Int = 0,
    var cbac_diff_holding_obj_posi: Int = 0,
    var cbac_weekness_in_feet_posi: Int = 0,
    var cbac_fuel_used_posi: Int = 0,
    var cbac_occupational_exposure_posi: Int = 0,

    var cbac_feeling_unsteady_posi: Int = 0,
    var cbac_suffer_physical_disability_posi: Int = 0,
    var cbac_needing_help_posi: Int = 0,
    var cbac_forgetting_names_posi: Int = 0,

    var cbac_little_interest_posi: Int = 0,
    var cbac_feeling_down_posi: Int = 0,

    var cbac_little_interest_score: Int = 0,
    var cbac_feeling_down_score: Int = 0,

    var cbac_referpatient_mo: String? = null,
    var cbac_tracing_all_fm: String? = null,
    var cbac_sputemcollection: String? = null,
    var serverUpdatedStatus: Int = 0,
    var createdBy: String? = null,
    var createdDate: Long = 0L,
    var ProviderServiceMapID: Int = 0,
    var VanID: Int = 0,
    var Processed: String? = null,
    var Countyid: Int = 0,
    var stateid: Int = 0,
    var districtid: Int = 0,
    var districtname: String? = null,
    var villageid: Int = 0,
    var cbac_reg_id: Long = 0,
    var hrp_suspected: Boolean? = null,
    var suspected_hrp: String? = null,
    var confirmed_hrp: String? = null,
    var ncd_suspected: String? = null,
    var suspected_ncd: String? = null,
    var ncd_confirmed: Boolean? = null,
    var confirmed_ncd: String? = null,
    var suspected_tb: String? = null,
    var confirmed_tb: String? = null,
    var suspected_ncd_diseases: String? = null,
    var confirmed_ncd_diseases: String? = null,
    var diagnosis_status: String? = null


) {
    fun asPostModel(gender: Gender, resources: Resources): CbacPost {
        return CbacPost(
            houseoldId = hhId,
            benficieryid = benId,
            ashaid = ashaId,
            cbac_age = resources.getStringArray(R.array.cbac_age)[cbac_age_posi - 1],
            cbac_age_posi = cbac_age_posi,
            cbac_smoke = resources.getStringArray(R.array.cbac_smoke)[cbac_smoke_posi - 1],
            cbac_smoke_posi = cbac_smoke_posi,
            cbac_alcohol = resources.getStringArray(R.array.cbac_alcohol)[cbac_alcohol_posi - 1],
            cbac_alcohol_posi = cbac_alcohol_posi,
            cbac_waist = if (gender == Gender.MALE)
                resources.getStringArray(R.array.cbac_waist_mes_male)[cbac_waist_posi - 1]
            else
                resources.getStringArray(R.array.cbac_waist_mes_female)[cbac_waist_posi - 1],
            cbac_waist_posi = cbac_waist_posi,
            cbac_pa = resources.getStringArray(R.array.cbac_pa)[cbac_pa_posi - 1],
            cbac_pa_posi = cbac_pa_posi,
            cbac_familyhistory = resources.getStringArray(R.array.cbac_fh)[cbac_familyhistory_posi - 1],
            cbac_familyhistory_posi = cbac_familyhistory_posi,
            total_score = total_score,
            cbac_sufferingtb = when (cbac_sufferingtb_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_fh_tb)
            },
            cbac_sufferingtb_pos = cbac_sufferingtb_pos,
            cbac_antitbdrugs = when (cbac_antitbdrugs_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_taking_tb_drug)
            },
            cbac_antitbdrugs_pos = cbac_antitbdrugs_pos,
            cbac_tbhistory = when (cbac_tbhistory_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_histb)
            },
            cbac_tbhistory_pos = cbac_tbhistory_pos,
            cbac_sortnesofbirth = when (cbac_sortnesofbirth_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_breath)
            },
            cbac_sortnesofbirth_pos = cbac_sortnesofbirth_pos,
            cbac_coughing = when (cbac_coughing_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_coughing)
            },
            cbac_coughing_pos = cbac_coughing_pos,
            cbac_bloodsputum = when (cbac_bloodsputum_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_blsputum)
            },
            cbac_bloodsputum_pos = cbac_bloodsputum_pos,
            cbac_fivermore = when (cbac_fivermore_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_feverwks)
            },
            cbac_fivermore_pos = cbac_fivermore_pos,
            cbac_loseofweight = when (cbac_loseofweight_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_lsweight)
            },
            cbac_loseofweight_pos = cbac_loseofweight_pos,
            cbac_nightsweats = when (cbac_nightsweats_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_ntswets)
            },
            cbac_nightsweats_pos = cbac_nightsweats_pos,
            cbac_historyoffits = when (cbac_historyoffits_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_hifits)
            },
            cbac_historyoffits_pos = cbac_historyoffits_pos,
            cbac_difficultyinmouth = when (cbac_difficultyinmouth_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_difmouth)
            },
            cbac_difficultyinmouth_pos = cbac_difficultyinmouth_pos,
            cbac_uicers = when (cbac_uicers_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_recurrent_ulceration)
            },
            cbac_uicers_pos = cbac_uicers_pos,
            cbac_toneofvoice = when (cbac_toneofvoice_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_voice)
            },
            cbac_toneofvoice_pos = cbac_toneofvoice_pos,
            cbac_lumpinbreast = when (cbac_lumpinbreast_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_lumpbrest)
            },
            cbac_lumpinbreast_pos = cbac_lumpinbreast_pos,
            cbac_blooddischage = when (cbac_blooddischage_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_nipple)
            },
            cbac_blooddischage_pos = cbac_blooddischage_pos,
            cbac_changeinbreast = when (cbac_changeinbreast_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_breast)
            },
            cbac_changeinbreast_pos = cbac_changeinbreast_pos,
            cbac_bleedingbtwnperiods = when (cbac_bleedingbtwnperiods_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_blperiods)
            },
            cbac_bleedingbtwnperiods_pos = cbac_bleedingbtwnperiods_pos,
            cbac_bleedingaftermenopause = when (cbac_bleedingaftermenopause_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_blmenopause)
            },
            cbac_bleedingaftermenopause_pos = cbac_bleedingaftermenopause_pos,
            cbac_bleedingafterintercourse = when (cbac_bleedingafterintercourse_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_blintercorse)
            },
            cbac_bleedingafterintercourse_pos = cbac_bleedingafterintercourse_pos,
            cbac_foulveginaldischarge = when (cbac_foulveginaldischarge_pos) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_fouldis)
            },
            cbac_foulveginaldischarge_pos = cbac_foulveginaldischarge_pos,
            cbac_referpatient_mo = cbac_referpatient_mo ?: "0",
            cbac_tracing_all_fm = cbac_tracing_all_fm ?: "0",
            cbac_sputemcollection = cbac_sputemcollection ?: "0",
            serverUpdatedStatus = serverUpdatedStatus,
            createdBy = createdBy!!,
            createdDate = getDateTimeStringFromLong(createdDate)!!,
            ProviderServiceMapID = ProviderServiceMapID,
            VanID = VanID,
            Processed = Processed!!,
            Countyid = Countyid,
            stateid = stateid,
            districtid = districtid,
            districtname = districtname,
            villageid = villageid,
            hrp_suspected = hrp_suspected!!,
            suspected_hrp = suspected_hrp!!,
            ncd_suspected = ncd_suspected!!,
            suspected_ncd = suspected_ncd!!,
            suspected_tb = suspected_tb!!,
            suspected_ncd_diseases = suspected_ncd_diseases!!,
            cbac_reg_id = cbac_reg_id,
            ncd_suspected_cancer = false,
            ncd_suspected_hypertension = false,
            ncd_suspected_breastCancer = false,
            ncd_suspected_diabettis = false,
            ncd_confirmed = ncd_confirmed ?: false,
            confirmed_ncd = confirmed_ncd ?: "No",
            confirmed_hrp = null,
            confirmed_tb = null,
            suspected_confirmed_tb = false,
            confirmed_ncd_diseases = null,
            diagnosis_status = null,
            cbac_growth_in_mouth = when (cbac_growth_in_mouth_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Any_Growth)
            },
            cbac_growth_in_mouth_posi = cbac_growth_in_mouth_posi,
            cbac_white_or_red_patch = when (cbac_white_or_red_patch_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Any_white)
            },
            cbac_white_or_red_patch_posi = cbac_white_or_red_patch_posi,
            cbac_Pain_while_chewing = when (cbac_Pain_while_chewing_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Pain_while_chewing)
            },
            cbac_Pain_while_chewing_posi = cbac_Pain_while_chewing_posi,
            cbac_hyper_pigmented_patch = when (cbac_hyper_pigmented_patch_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Any_hyper_pigmented)
            },
            cbac_hyper_pigmented_patch_posi = cbac_hyper_pigmented_patch_posi,
            cbac_any_thickend_skin = when (cbac_any_thickend_skin_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_any_thickend_skin)
            },
            cbac_any_thickend_skin_posi = cbac_any_thickend_skin_posi,
            cbac_nodules_on_skin = when (cbac_nodules_on_skin_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_any_nodules_skin)
            },
            cbac_nodules_on_skin_posi = cbac_nodules_on_skin_posi,
            cbac_numbness_on_palm = when (cbac_numbness_on_palm_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Recurrent_numbness)
            },
            cbac_numbness_on_palm_posi = cbac_numbness_on_palm_posi,
            cbac_clawing_of_fingers = when (cbac_clawing_of_fingers_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Clawing_of_fingers)
            },
            cbac_clawing_of_fingers_posi = cbac_clawing_of_fingers_posi,
            cbac_tingling_or_numbness = when (cbac_tingling_or_numbness_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Tingling_or_Numbness)
            },
            cbac_tingling_or_numbness_posi = cbac_tingling_or_numbness_posi,
            cbac_cloudy = when (cbac_cloudy_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_recurrent_cloudy)
            },
            cbac_cloudy_posi = cbac_cloudy_posi,
            cbac_diffreading = when (cbac_diffreading_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_recurrent_diffculty_reading)
            },
            cbac_diffreading_posi = cbac_diffreading_posi,
            cbac_pain_ineyes = when (cbac_pain_ineyes_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_recurrent_pain_eyes)
            },
            cbac_pain_ineyes_posi = cbac_pain_ineyes_posi,
            cbac_redness_ineyes = when (cbac_redness_ineyes_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_recurrent_redness_eyes)
            },
            cbac_redness_ineyes_posi = cbac_redness_ineyes_posi,
            cbac_diff_inhearing = when (cbac_diff_inhearing_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_recurrent_diff_hearing)
            },
            cbac_diff_inhearing_posi = cbac_diff_inhearing_posi,
            cbac_inability_close_eyelid = when (cbac_inability_close_eyelid_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Inability_close_eyelid)
            },
            cbac_inability_close_eyelid_posi = cbac_inability_close_eyelid_posi,
            cbac_diff_holding_obj = when (cbac_diff_holding_obj_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_diff_holding_objects)
            },
            cbac_diff_holding_obj_posi = cbac_diff_holding_obj_posi,
            cbac_weekness_in_feet = when (cbac_weekness_in_feet_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_Weekness_in_feet)
            },
            cbac_weekness_in_feet_posi = cbac_weekness_in_feet_posi,
            cbac_feeling_unsteady = when (cbac_feeling_unsteady_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_unsteady)
            },
            cbac_feeling_unsteady_posi = cbac_feeling_unsteady_posi,
            cbac_suffer_physical_disability = when (cbac_suffer_physical_disability_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_pd_rm)
            },
            cbac_suffer_physical_disability_posi = cbac_suffer_physical_disability_posi,
            cbac_needing_help = when (cbac_needing_help_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_nhop)
            },
            cbac_fuel_used = resources.getStringArray(R.array.cbac_type_Cooking_fuel)[cbac_fuel_used_posi - 1],
            cbac_fuel_used_posi = cbac_fuel_used_posi,
            cbac_occupational_exposure = resources.getStringArray(R.array.cbac_type_occupational_exposure)[cbac_occupational_exposure_posi - 1],
            cbac_occupational_exposure_posi = cbac_occupational_exposure_posi,
            cbac_little_interest = resources.getStringArray(R.array.cbac_li)[cbac_little_interest_posi - 1],
            cbac_little_interest_posi = cbac_little_interest_posi,
            cbac_feeling_down = resources.getStringArray(R.array.cbac_fd)[cbac_feeling_down_posi - 1],
            cbac_feeling_down_posi = cbac_feeling_down_posi,
            cbac_little_interest_score = cbac_little_interest_score,
            cbac_feeling_down_score = cbac_feeling_down_score,
            cbac_needing_help_posi = cbac_needing_help_posi,
            cbac_forgetting_names = when (cbac_forgetting_names_posi) {
                1 -> "Yes"
                2 -> "No"
                else -> resources.getString(R.string.cbac_forget_names)
            },
            cbac_forgetting_names_posi = cbac_forgetting_names_posi,

            )
    }
}

data class CbacPost(
    val id: Int = 1,
    val houseoldId: Long,
    val benficieryid: Long,
    val ashaid: Int,
    val cbac_age: String,
    val cbac_age_posi: Int,
    val cbac_smoke: String,
    val cbac_smoke_posi: Int,
    val cbac_alcohol: String,
    val cbac_alcohol_posi: Int,
    val cbac_waist: String,
    val cbac_waist_posi: Int,
    val cbac_pa: String,
    val cbac_pa_posi: Int,
    val cbac_familyhistory: String,
    val cbac_familyhistory_posi: Int,
    val total_score: Int,
    val cbac_sufferingtb: String,
    val cbac_sufferingtb_pos: Int,
    val cbac_antitbdrugs: String,
    val cbac_antitbdrugs_pos: Int,
    val cbac_tbhistory: String,
    val cbac_tbhistory_pos: Int,
    val cbac_sortnesofbirth: String,
    val cbac_sortnesofbirth_pos: Int,
    val cbac_coughing: String,
    val cbac_coughing_pos: Int,
    val cbac_bloodsputum: String,
    val cbac_bloodsputum_pos: Int,
    val cbac_fivermore: String,
    val cbac_fivermore_pos: Int,
    val cbac_loseofweight: String,
    val cbac_loseofweight_pos: Int,
    val cbac_nightsweats: String,
    val cbac_nightsweats_pos: Int,
    val cbac_historyoffits: String,
    val cbac_historyoffits_pos: Int,
    val cbac_difficultyinmouth: String,
    val cbac_difficultyinmouth_pos: Int,
    val cbac_uicers: String,
    val cbac_uicers_pos: Int,
    val cbac_toneofvoice: String,
    val cbac_toneofvoice_pos: Int,
    val cbac_lumpinbreast: String,
    val cbac_lumpinbreast_pos: Int,
    val cbac_blooddischage: String,
    val cbac_blooddischage_pos: Int,
    val cbac_changeinbreast: String,
    val cbac_changeinbreast_pos: Int,
    val cbac_bleedingbtwnperiods: String,
    val cbac_bleedingbtwnperiods_pos: Int,
    val cbac_bleedingaftermenopause: String,
    val cbac_bleedingaftermenopause_pos: Int,
    val cbac_bleedingafterintercourse: String,
    val cbac_bleedingafterintercourse_pos: Int,
    val cbac_foulveginaldischarge: String,
    val cbac_foulveginaldischarge_pos: Int,
    val cbac_referpatient_mo: String,
    val cbac_tracing_all_fm: String,
    val cbac_sputemcollection: String,
    val serverUpdatedStatus: Int,
    val createdBy: String,
    val createdDate: String,
    val ProviderServiceMapID: Int,
    val VanID: Int,
    val Processed: String,
    val Countyid: Int,
    val stateid: Int,
    val districtid: Int,
    val districtname: String?,
    val villageid: Int,
    val hrp_suspected: Boolean,
    val suspected_hrp: String,
    val ncd_suspected: String,
    val suspected_ncd: String,
    val suspected_tb: String,
    val suspected_ncd_diseases: String,
    val cbac_reg_id: Long,
    val ncd_suspected_cancer: Boolean,
    val ncd_suspected_hypertension: Boolean,
    val ncd_suspected_breastCancer: Boolean,
    val ncd_suspected_diabettis: Boolean,
    val ncd_confirmed: Boolean,
    val confirmed_ncd: String,
    val confirmed_hrp: String?,
    val confirmed_tb: String?,
    val suspected_confirmed_tb: Boolean,
    val confirmed_ncd_diseases: String?,
    val diagnosis_status: String?,
    val cbac_growth_in_mouth: String,
    val cbac_growth_in_mouth_posi: Int,
    val cbac_white_or_red_patch: String,
    val cbac_white_or_red_patch_posi: Int,
    val cbac_Pain_while_chewing: String,
    val cbac_Pain_while_chewing_posi: Int,
    val cbac_hyper_pigmented_patch: String,
    val cbac_hyper_pigmented_patch_posi: Int,
    val cbac_any_thickend_skin: String,
    val cbac_any_thickend_skin_posi: Int,
    val cbac_nodules_on_skin: String,
    val cbac_nodules_on_skin_posi: Int,
    val cbac_numbness_on_palm: String,
    val cbac_numbness_on_palm_posi: Int,
    val cbac_clawing_of_fingers: String,
    val cbac_clawing_of_fingers_posi: Int,
    val cbac_tingling_or_numbness: String,
    val cbac_tingling_or_numbness_posi: Int,
    val cbac_cloudy: String,
    val cbac_cloudy_posi: Int,
    val cbac_diffreading: String,
    val cbac_diffreading_posi: Int,
    val cbac_pain_ineyes: String,
    val cbac_pain_ineyes_posi: Int,
    val cbac_redness_ineyes: String,
    val cbac_redness_ineyes_posi: Int,
    val cbac_diff_inhearing: String,
    val cbac_diff_inhearing_posi: Int,
    val cbac_inability_close_eyelid: String,
    val cbac_inability_close_eyelid_posi: Int,
    val cbac_diff_holding_obj: String,
    val cbac_diff_holding_obj_posi: Int,
    val cbac_weekness_in_feet: String,
    val cbac_weekness_in_feet_posi: Int,
    val cbac_feeling_unsteady: String,
    val cbac_feeling_unsteady_posi: Int,
    val cbac_suffer_physical_disability: String,
    val cbac_suffer_physical_disability_posi: Int,
    val cbac_needing_help: String,
    val cbac_fuel_used: String,
    val cbac_fuel_used_posi: Int,
    val cbac_occupational_exposure: String,
    val cbac_occupational_exposure_posi: Int,
    val cbac_little_interest: String,
    val cbac_little_interest_posi: Int,
    val cbac_feeling_down: String,
    val cbac_feeling_down_posi: Int,
    val cbac_little_interest_score: Int,
    val cbac_feeling_down_score: Int,
    val cbac_needing_help_posi: Int,
    val cbac_forgetting_names: String,
    val cbac_forgetting_names_posi: Int,
)