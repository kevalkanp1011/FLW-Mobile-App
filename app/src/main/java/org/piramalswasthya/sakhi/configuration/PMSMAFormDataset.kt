package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.FormInputOld
import org.piramalswasthya.sakhi.model.PMSMACache
import java.text.SimpleDateFormat
import java.util.*

class PMSMAFormDataset(
    context: Context, currentLanguage: Languages
) : Dataset(context, currentLanguage) {

    companion object {
        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    private val mctsNumberOrRchNumber = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_mcts_rch_number),
        required = false
    )
    val haveMCPCard = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_have_mcp_card),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )

    val givenMCPCard = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_given_mcp_card),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    val husbandName = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_husband_name),
        required = false
    )
    val address = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_address),
        required = false
    )
    val mobileNumber = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.TEXT_VIEW,
        title = resources.getString(R.string.pmsma_mobile_number),
        required = false
    )
    private val numANC = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_num_anc),
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val motherStatus = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_mother_status),
        required = false
    )
    private val weight = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_weight),
        min = 30,
        max = 200,
        etMaxLength = 3,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val systolicBloodPressure = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_sys_bp),
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val diastolicBloodPressure = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_dia_bp),
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val abdominalCheckUp = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_abdominal_check_up),
        required = false
    )
    private val fetalStatus = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_fetal_status),
        required = false
    )
    private val fetalHRPM = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_fetal_hrpm),
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val twinPregnancy = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_twin_pregnancy),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val investigations = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_investigation),
        required = false
    )
    private val urineAlbumin = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_urine_albumin),
        required = false
    )
    private val haemoglobinAndBloodGroup = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_haemoglobin_and_blood_group),
        required = false
    )
    private val hiv = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_hiv),
        required = false
    )
    private val vdrl = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_vdrl),
        required = false
    )
    private val hbsc = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_hbsc),
        required = false
    )
    private val malaria = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_malaria),
        required = false
    )
    private val hivTestDuringANC = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_hiv_test_during_anc),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val swollenCondtion = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_swollen_condition),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val bloodSugarTest = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_blood_sugar_test),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val ultraSound = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_ultrasound),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val interventionDetails = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_intervention_details),
        required = false
    )
    private val ironFolicAcid = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_iron_folic_acid),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val calciumSupplementation = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_calcium_supplementation),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val tetanusToxoid = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_tetanus_toxoid),
        entries = resources.getStringArray(R.array.pmsma_tetanus_toxoid_array),
        required = false
    )
    private val lmp = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_lmp),
        required = false
    )
    val lastMenstrualPeriod = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = resources.getString(R.string.pmsma_last_mestrual_period),
        min = 0L,
        max = System.currentTimeMillis(),
        required = true
    )
    val expectedDateOfDelivery = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.TEXT_VIEW,
        title = resources.getString(R.string.pmsma_expected_delivery_date),
        required = true
    )
    private val highRiskFactors = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_high_risk_factors),
        required = false
    )
    val highriskSymbols = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_high_risk_symbols),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    val highRiskReason = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_high_risk_reason),
        required = false
    )
    private val highRiskPregnant = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_high_risk_pregnant),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val highRiskPregnancyReferred = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_high_risk_pregnancy_referred),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val advice = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = resources.getString(R.string.pmsma_advice),
        required = false
    )
    private val birthPrepAndNutritionAndFamilyPlanning = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = resources.getString(R.string.pmsma_birth_complications_and_planning),
        entries = resources.getStringArray(R.array.pmsma_confirmation_array),
        required = false
    )
    private val medicalOfficerSign = FormInputOld(
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = resources.getString(R.string.pmsma_medical_officer_sign),
        required = false
    )

    val firstPage by lazy {
        listOf(
            mctsNumberOrRchNumber,
            haveMCPCard,
            husbandName,
            address,
            mobileNumber,
            numANC,
            motherStatus,
            weight,
            systolicBloodPressure,
            diastolicBloodPressure,
            abdominalCheckUp,
            fetalStatus,
            fetalHRPM,
            twinPregnancy,
            investigations,
            urineAlbumin,
            haemoglobinAndBloodGroup,
            hiv,
            vdrl,
            hbsc,
            malaria,
            hivTestDuringANC,
            swollenCondtion,
            bloodSugarTest,
            ultraSound,
            interventionDetails,
            ironFolicAcid,
            calciumSupplementation,
            tetanusToxoid,
            lmp,
            lastMenstrualPeriod,
            expectedDateOfDelivery,
            highRiskFactors,
            highriskSymbols,
            highRiskPregnant,
            highRiskPregnancyReferred,
            advice,
            birthPrepAndNutritionAndFamilyPlanning,
            medicalOfficerSign,

            )
    }

    fun mapValues(pmsmaCache: PMSMACache) {
        pmsmaCache.mctsNumberOrRchNumber = mctsNumberOrRchNumber.value.value
        pmsmaCache.haveMCPCard = haveMCPCard.value.value == "Yes"
        pmsmaCache.husbandName = husbandName.value.value
        pmsmaCache.address = address.value.value
        pmsmaCache.mobileNumber = mobileNumber.value.value?.toLong() ?: 0L
        pmsmaCache.numANC = numANC.value.value?.toInt() ?: 0
        pmsmaCache.weight = weight.value.value?.toInt() ?: 0
        pmsmaCache.systolicBloodPressure = systolicBloodPressure.value.value
        pmsmaCache.bloodPressure = diastolicBloodPressure.value.value
        pmsmaCache.abdominalCheckUp = abdominalCheckUp.value.value
        pmsmaCache.fetalHRPM = fetalHRPM.value.value?.toInt() ?: 0
        pmsmaCache.twinPregnancy = twinPregnancy.value.value == "Yes"
        pmsmaCache.urineAlbumin = urineAlbumin.value.value
        pmsmaCache.haemoglobinAndBloodGroup = haemoglobinAndBloodGroup.value.value
        pmsmaCache.hiv = hiv.value.value
        pmsmaCache.vdrl = vdrl.value.value
        pmsmaCache.hbsc = hbsc.value.value
        pmsmaCache.malaria = malaria.value.value
        pmsmaCache.hivTestDuringANC = hivTestDuringANC.value.value == "Yes"
        pmsmaCache.swollenCondtion = swollenCondtion.value.value == "Yes"
        pmsmaCache.bloodSugarTest = bloodSugarTest.value.value == "Yes"
        pmsmaCache.ultraSound = ultraSound.value.value == "Yes"
        pmsmaCache.ironFolicAcid = ironFolicAcid.value.value == "Yes"
        pmsmaCache.calciumSupplementation = calciumSupplementation.value.value == "Yes"
        pmsmaCache.tetanusToxoid = tetanusToxoid.value.value
        pmsmaCache.lastMenstrualPeriod =
            lastMenstrualPeriod.value.value?.let { getLongFromDate(it) } ?: 0L
        pmsmaCache.expectedDateOfDelivery = getLongFromDate(expectedDateOfDelivery.value.value!!)
        pmsmaCache.highriskSymbols = highriskSymbols.value.value == "Yes"
        pmsmaCache.highRiskReason = highRiskReason.value.value
        pmsmaCache.highRiskPregnant = highRiskPregnant.value.value == "Yes"
        pmsmaCache.highRiskPregnancyReferred = highRiskPregnancyReferred.value.value == "Yes"
        pmsmaCache.birthPrepAndNutritionAndFamilyPlanning =
            birthPrepAndNutritionAndFamilyPlanning.value.value == "Yes"
        pmsmaCache.medicalOfficerSign = medicalOfficerSign.value.value
    }

    fun setExistingValues(pmsma: PMSMACache?) {
        mctsNumberOrRchNumber.value.value = pmsma?.mctsNumberOrRchNumber
        haveMCPCard.value.value = if (pmsma?.haveMCPCard == true) "Yes" else "No"
        husbandName.value.value = pmsma?.husbandName
        address.value.value = pmsma?.address
        mobileNumber.value.value = pmsma?.mobileNumber.toString()
        numANC.value.value = pmsma?.numANC.toString()
        weight.value.value = pmsma?.weight.toString()
        systolicBloodPressure.value.value = pmsma?.systolicBloodPressure
        diastolicBloodPressure.value.value = pmsma?.bloodPressure
        abdominalCheckUp.value.value = pmsma?.abdominalCheckUp
        fetalHRPM.value.value = pmsma?.fetalHRPM.toString()
        twinPregnancy.value.value = if (pmsma?.twinPregnancy == true) "Yes" else "No"
        urineAlbumin.value.value = pmsma?.urineAlbumin
        haemoglobinAndBloodGroup.value.value = pmsma?.haemoglobinAndBloodGroup
        hiv.value.value = pmsma?.hiv
        vdrl.value.value = pmsma?.vdrl
        hbsc.value.value = pmsma?.hbsc
        malaria.value.value = pmsma?.malaria
        hivTestDuringANC.value.value = if (pmsma?.hivTestDuringANC == true) "Yes" else "No"
        swollenCondtion.value.value = if (pmsma?.swollenCondtion == true) "Yes" else "No"
        bloodSugarTest.value.value = if (pmsma?.bloodSugarTest == true) "Yes" else "No"
        ultraSound.value.value = if (pmsma?.ultraSound == true) "Yes" else "No"
        ironFolicAcid.value.value = if (pmsma?.ironFolicAcid == true) "Yes" else "No"
        calciumSupplementation.value.value =
            if (pmsma?.calciumSupplementation == true) "Yes" else "No"
        tetanusToxoid.value.value = pmsma?.tetanusToxoid
        lastMenstrualPeriod.value.value = getDateFromLong(pmsma?.lastMenstrualPeriod)
        expectedDateOfDelivery.value.value = getDateFromLong(pmsma?.expectedDateOfDelivery)
        highriskSymbols.value.value = if (pmsma?.highriskSymbols == true) "Yes" else "No"
        highRiskReason.value.value = pmsma?.highRiskReason
        highRiskPregnant.value.value = if (pmsma?.highRiskPregnant == true) "Yes" else "No"
        highRiskPregnancyReferred.value.value =
            if (pmsma?.highRiskPregnancyReferred == true) "Yes" else "No"
        birthPrepAndNutritionAndFamilyPlanning.value.value =
            if (pmsma?.birthPrepAndNutritionAndFamilyPlanning == true) "Yes" else "No"
        medicalOfficerSign.value.value = pmsma?.medicalOfficerSign
    }

    private fun getDateFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        dateLong?.let {
            return dateFormat.format(dateLong)
        } ?: run {
            return null
        }
    }

    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        TODO("Not yet implemented")
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        TODO("Not yet implemented")
    }


}