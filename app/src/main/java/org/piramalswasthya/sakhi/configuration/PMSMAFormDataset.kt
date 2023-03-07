package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.PMSMACache
import java.text.SimpleDateFormat
import java.util.*

class PMSMAFormDataset(context: Context, private val pmsma: PMSMACache? = null) {

    companion object {
        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val mdFormat =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }

        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    private val mctsNumberOrRchNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "MCTS Number/RCH Number",
        required = false
    )
    val haveMCPCard = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Does the beneficiary have an MCP card",
        list = arrayOf("Yes", "No"),
        required = false
    )

    val givenMCPCard = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = ", MCP card is given",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val husbandName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Husband’s Name",
        required = false
    )
    val address = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Address",
        required = false
    )
    val mobileNumber = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Mobile number",
        required = false
    )
    private val numANC = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Number of ANCs done before delivery",
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val weight = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Weight (in Kg)",
        min = 30,
        max = 200,
        etMaxLength = 3,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val systolicBloodPressure = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Systolic Blood Pressure",
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val diastolicBloodPressure = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Diastolic Blood Pressure",
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val abdominalCheckUp = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Abdominal Check-up",
        required = false
    )
    private val fetalHRPM = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Fetal Heart Rate per minute",
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val twinPregnancy = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Twins pregnancy",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val urineAlbumin = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Urine albumin",
        required = false
    )
    private val haemoglobinAndBloodGroup = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Haemoglobin and blood group",
        required = false
    )
    private val hiv = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "HIV",
        required = false
    )
    private val vdrl = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Vdrl",
        required = false
    )
    private val hbsc = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "HBSC (Hepatitis B) ",
        required = false
    )
    private val malaria = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Malaria ",
        required = false
    )
    private val hivTestDuringANC = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Was HIV test done during ANC Check up? ",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val swollenCondtion = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Swollen condition ",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val bloodSugarTest = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Blood sugar test ",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val ultraSound = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Ultrasound ",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val ironFolicAcid = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Iron Folic Acid ",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val calciumSupplementation = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Calcium Supplementation ",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val tetanusToxoid = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Tetanus toxoid ",
        list = arrayOf(
            "First",
            "Second",
            "Booster",
        ),
        required = false
    )
    val lastMenstrualPeriod = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Last Menstrual Period ",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val expectedDateOfDelivery = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Expected Date of Delivery ",
        max = System.currentTimeMillis() + 270 * 24 * 60 * 600000L,
        min = System.currentTimeMillis(),
        required = true
    )
    val highriskSymbols = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Identification of high risk symbols",
        list = arrayOf("Yes", "No"),
        required = false
    )
    val highRiskReason = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "If yes, select the reason give below and write",
        required = false
    )
    private val highRiskPregnant = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Pregnant in high risk category treated",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val highRiskPregnancyReferred = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Pregnant in high risk category was referred",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val birthPrepAndNutritionAndFamilyPlanning = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Preparation for birth and complications, nutrition and family planning",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val medicalOfficerSign = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Signature of medical officer in charge ",
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
            weight,
            systolicBloodPressure,
            diastolicBloodPressure,
            abdominalCheckUp,
            fetalHRPM,
            twinPregnancy,
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
            ironFolicAcid,
            calciumSupplementation,
            tetanusToxoid,
            lastMenstrualPeriod,
            expectedDateOfDelivery,
            highriskSymbols,
            highRiskPregnant,
            highRiskPregnancyReferred,
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


}