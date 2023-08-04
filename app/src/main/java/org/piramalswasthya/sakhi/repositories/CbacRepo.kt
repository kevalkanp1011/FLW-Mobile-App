package org.piramalswasthya.sakhi.repositories

import android.content.Context
import android.content.res.Resources
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.CbacCache
import org.piramalswasthya.sakhi.network.AmritApiService
import org.piramalswasthya.sakhi.network.GetDataPaginatedRequest
import org.piramalswasthya.sakhi.network.getLongFromDate
import timber.log.Timber
import javax.inject.Inject

class CbacRepo @Inject constructor(
    @ApplicationContext context: Context,
    private val database: InAppDb,
    private val amritApiService: AmritApiService,
    private val prefDao: PreferenceDao
) {

    private val resources: Resources

    init {
        resources = context.resources
    }

    suspend fun saveCbacData(cbacCache: CbacCache, ben: BenRegCache): Boolean {
        return withContext(Dispatchers.IO) {

            val user =
                prefDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                cbacCache.apply {
                    createdBy = user.userName
                    createdDate = System.currentTimeMillis()
                    serverUpdatedStatus = 0
                    cbac_tracing_all_fm =
                        if (cbac_sufferingtb_pos == 1 || cbac_antitbdrugs_pos == 1)
                            "1"
                        else
                            "0"
                    cbac_sputemcollection = if (cbac_tbhistory_pos == 1 ||
                        cbac_coughing_pos == 1 ||
                        cbac_bloodsputum_pos == 1 ||
                        cbac_fivermore_pos == 1 ||
                        cbac_loseofweight_pos == 1 ||
                        cbac_nightsweats_pos == 1
                    )
                        "1"
                    else
                        "0"
                    Processed = "N"
//                    ProviderServiceMapID = user.serviceMapId
//                    VanID = user.vanId

                }

                database.cbacDao.upsert(cbacCache)
                database.benDao.updateBen(ben)
                true
            } catch (e: java.lang.Exception) {
                Timber.d("Error : $e raised at saveCbacData")
                false
            }
        }
    }

    suspend fun getCbacCacheFromId(cbacId: Int): CbacCache {
        return withContext(Dispatchers.IO) {
            database.cbacDao.getCbacFromBenId(cbacId)
                ?: throw IllegalStateException("No CBAC entry found!")
        }

    }

    suspend fun pullAndPersistCbacRecord(page: Int) {
        val userId = prefDao.getLoggedInUser()?.userId!!
        val response = amritApiService.getCbacs(
            userDetail =
            GetDataPaginatedRequest(
                ashaId = userId,
                pageNo = page,
                fromDate = BenRepo.getCurrentDate(Konstants.defaultTimeStamp),
                toDate = BenRepo.getCurrentDate(System.currentTimeMillis())
            )
        )
        val cbacList = response.body()?.string()?.let { getCbacCacheFromServerResponse(it) }
        cbacList?.let { database.cbacDao.upsert(*it.toTypedArray()) }
    }

    suspend fun pushAndUpdateCbacRecord() {
        val unProcessedList = database.cbacDao.getAllUnprocessedCbac()
        val list = unProcessedList
            .map { it.cbac.asPostModel(it.hhId, it.benGender, resources = resources) }
        val response = list.takeIf { it.isNotEmpty() }?.let {
            amritApiService.postCbacs(list = list)
        }
        response?.body()?.string()?.let {
            if (JSONObject(it).getString("status")=="Success") {
                val cbacList = unProcessedList.map { it.cbac }
                cbacList.forEach {cbac ->
                    cbac.Processed = "P"
                    cbac.syncState = SyncState.SYNCED
                }
                database.cbacDao.update(*cbacList.toTypedArray())
            }
            else {

            }
        }
    }

    private fun getCbacCacheFromServerResponse(response: String): MutableList<CbacCache> {
        val jsonObj = JSONObject(response)
        val result = mutableListOf<CbacCache>()

        val responseStatusCode = jsonObj.getInt("statusCode")
        if (responseStatusCode == 200) {
            val jsonArray = jsonObj.getJSONArray("data")

            if (jsonArray.length() != 0) {
                for (i in 0 until jsonArray.length()) {
                    val cbacDataObj = jsonArray.getJSONObject(i)
//                    val cbacDataObj = jsonObject.getJSONObject("cbacDetails")
                    val benId =
                        if (cbacDataObj.has("beneficiaryId")) cbacDataObj.getLong("beneficiaryId") else 0L
//                    val hhId =
//                        if (jsonObject.has("houseoldId")) jsonObject.getLong("houseoldId") else -1L
//                    if (benId == -1L || hhId == -1L) continue
                    if (benId==0L ||cbacDataObj.length() == 0) continue


                    try {
                        result.add(
                            CbacCache(
                                benId = cbacDataObj.getLong("beneficiaryId"),
//                                hhId = ben.householdId,
                                ashaId = prefDao.getLoggedInUser()?.userId
                                    ?: throw IllegalStateException("logged in user not found!"),
//                                gender = ben.gender!!,
                                fillDate = getLongFromDate(cbacDataObj.getString("createdDate")),
                                cbac_age_posi = cbacDataObj.getInt("cbacAgePosi"),
                                cbac_smoke_posi = cbacDataObj.getInt("cbacSmokePosi"),
                                cbac_alcohol_posi = cbacDataObj.getInt("cbacAlcoholPosi"),
                                cbac_waist_posi = cbacDataObj.getInt("cbacWaistPosi"),
                                cbac_pa_posi = cbacDataObj.getInt("cbacPaPosi"),
                                cbac_familyhistory_posi = cbacDataObj.getInt("cbacFamilyhistoryPosi"),
                                total_score = cbacDataObj.getInt("totalScore"),
                                cbac_sufferingtb_pos = cbacDataObj.getInt("cbacSufferingtbPos"),
                                cbac_antitbdrugs_pos = cbacDataObj.getInt("cbacAntitbdrugsPos"),
                                cbac_tbhistory_pos = cbacDataObj.getInt("cbacTbhistoryPos"),
                                cbac_sortnesofbirth_pos = cbacDataObj.getInt("cbacSortnesofbirthPos"),
                                cbac_coughing_pos = cbacDataObj.getInt("cbacCoughingPos"),
                                cbac_bloodsputum_pos = cbacDataObj.getInt("cbacBloodsputumPos"),
                                cbac_fivermore_pos = cbacDataObj.getInt("cbacFivermorePos"),
                                cbac_loseofweight_pos = cbacDataObj.getInt("cbacLoseofweightPos"),
                                cbac_nightsweats_pos = cbacDataObj.getInt("cbacNightsweatsPos"),
                                cbac_historyoffits_pos = cbacDataObj.getInt("cbacHistoryoffitsPos"),
                                cbac_difficultyinmouth_pos = cbacDataObj.getInt("cbacDifficultyinmouthPos"),
                                cbac_uicers_pos = cbacDataObj.getInt("cbacUicersPos"),
                                cbac_toneofvoice_pos = cbacDataObj.getInt("cbacToneofvoicePos"),
                                cbac_lumpinbreast_pos = cbacDataObj.getInt("cbacLumpinbreastPos"),
                                cbac_blooddischage_pos = cbacDataObj.getInt("cbacBlooddischagePos"),
                                cbac_changeinbreast_pos = cbacDataObj.getInt("cbacChangeinbreastPos"),
                                cbac_bleedingbtwnperiods_pos = cbacDataObj.getInt("cbacBleedingbtwnperiodsPos"),
                                cbac_bleedingaftermenopause_pos = cbacDataObj.getInt("cbacBleedingaftermenopausePos"),
                                cbac_bleedingafterintercourse_pos = cbacDataObj.getInt("cbacBleedingafterintercoursePos"),
                                cbac_foulveginaldischarge_pos = cbacDataObj.getInt("cbacFoulveginaldischargePos"),
                                cbac_growth_in_mouth_posi = cbacDataObj.getInt("cbacGrowthInMouthPosi"),
                                cbac_Pain_while_chewing_posi = cbacDataObj.getInt("cbacPainWhileChewingPosi"),
                                cbac_hyper_pigmented_patch_posi = cbacDataObj.getInt("cbacHyperPigmentedPatchPosi"),
                                cbac_any_thickend_skin_posi = cbacDataObj.getInt("cbacAnyThickendSkinPosi"),
                                cbac_nodules_on_skin_posi = cbacDataObj.getInt("cbacNodulesOnSkinPosi"),
                                cbac_numbness_on_palm_posi = cbacDataObj.getInt("cbacNumbnessOnPalmPosi"),
                                cbac_clawing_of_fingers_posi = cbacDataObj.getInt("cbacClawingOfFingersPosi"),
                                cbac_tingling_or_numbness_posi = cbacDataObj.getInt("cbacTinglingOrNumbnessPosi"),
                                cbac_inability_close_eyelid_posi = cbacDataObj.getInt("cbacInabilityCloseEyelidPosi"),
                                cbac_diff_holding_obj_posi = cbacDataObj.getInt("cbacDiffHoldingObjPosi"),
                                cbac_weekness_in_feet_posi = cbacDataObj.getInt("cbacWeeknessInFeetPosi"),
                                cbac_fuel_used_posi = cbacDataObj.getInt("cbacFuelUsedPosi"),
                                cbac_occupational_exposure_posi = cbacDataObj.getInt("cbacOccupationalExposurePosi"),
                                cbac_little_interest_posi = cbacDataObj.getInt("cbacLittleInterestPosi"),
                                cbac_feeling_down_posi = cbacDataObj.getInt("cbacFeelingDownPosi"),
                                cbac_little_interest_score = cbacDataObj.getInt("cbacLittleInterestScore"),
                                cbac_feeling_down_score = cbacDataObj.getInt("cbacFeelingDownScore"),
                                cbac_referpatient_mo = cbacDataObj.getInt("cbacReferpatientMo")
                                    .toString(),
                                cbac_tracing_all_fm = cbacDataObj.getInt("cbacTracingAllFm")
                                    .toString(),
                                cbac_sputemcollection = cbacDataObj.getInt("cbacSputemcollection")
                                    .toString(),
                                serverUpdatedStatus = cbacDataObj.getInt("serverUpdatedStatus"),
                                createdBy = cbacDataObj.getString("createdBy"),
                                createdDate = getLongFromDate(cbacDataObj.getString("createdDate")),
                                ProviderServiceMapID = cbacDataObj.getInt("providerServiceMapId"),
//                                VanID = if (cbacDataObj.has("vanID")) cbacDataObj.getInt("vanID") else user.vanId,
                                Countyid = cbacDataObj.getInt("countryid"),
                                stateid = cbacDataObj.getInt("stateid"),
                                districtid = cbacDataObj.getInt("districtid"),
                                villageid = cbacDataObj.getInt("villageid"),
                                cbac_reg_id = if (cbacDataObj.has("BenRegId")) cbacDataObj.getLong("BenRegId") else 1L,
//                                suspected_hrp = cbacDataObj.getString("suspectedHrp"),
//                                confirmed_hrp = cbacDataObj.getString("confirmedHrp"),
//                                suspected_ncd = cbacDataObj.getString("suspectedNcd"),
//                                confirmed_ncd = cbacDataObj.getString("confirmedNcd"),
//                                suspected_tb = cbacDataObj.getString("suspectedTb"),
//                                confirmed_tb = cbacDataObj.getString("confirmedTb"),
//                                suspected_ncd_diseases = cbacDataObj.getString("suspectedNcdDiseases"),
//                                diagnosis_status = cbacDataObj.getString("confirmedTb"),
                                Processed = "P",//cbacDataObj.getString("Processed"),
                                syncState = SyncState.SYNCED,
                            )
                        )
                    } catch (e: JSONException) {
                        Timber.i("Cbac skipped: ${cbacDataObj.getLong("beneficiaryId")} with error $e")
                    }
                }
            }
        }
        return result
    }


}