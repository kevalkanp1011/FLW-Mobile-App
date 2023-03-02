package org.piramalswasthya.sakhi.configuration

import android.content.Context
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
        required = true
    )
    private val haveMCPCard = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Does the beneficiary have an MCP card",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val husbandName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Husband’s Name",
        required = true
    )
    val address = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Address",
        required = true
    )
    val mobileNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Mobile number",
        isMobileNumber = true,
        min = 6000000000,
        max = 9999999999,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true
    )
    private val numANC = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Number of ANCs done before delivery",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true
    )
    private val weight = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Weight (in Kg)",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true
    )
    private val systolicBloodPressure = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Systolic Blood Pressure",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true
    )
    private val bloodPressure = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Blood Pressure",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true
    )
    private val abdominalCheckUp = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Abdominal Check-up",
        required = true
    )
    private val fetalHRPM = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Fetal Heart Rate per minute",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true
    )
    private val twinPregnancy = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Twins pregnancy",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val urineAlbumin = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Urine albumin",
        required = true
    )
    private val haemoglobinAndBloodGroup = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Haemoglobin and blood group",
        required = true
    )
    private val hiv = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "HIV",
        required = true
    )
    private val vdrl = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Vdrl",
        required = true
    )
    private val hbsc = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "HBSC (Hepatitis B) ",
        required = true
    )
    private val malaria = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Malaria ",
        required = true
    )
    private val hivTestDuringANC = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Was HIV test done during ANC Check up? ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val swollenCondtion = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Swollen condition ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val bloodSugarTest = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Blood sugar test ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val ultraSound = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Ultrasound ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val ironFolicAcid = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Iron Folic Acid ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val calciumSupplementation = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Calcium Supplementation ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val tetanusToxoid = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Tetanus toxoid ",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val lastMenstrualPeriod = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Last Menstrual Period ",
        min = 0L,
        max = System.currentTimeMillis(),
        required = true
    )
    private val expectedDateOfDelivery = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Expected Date of Delivery ",
        min = 0L,
        max = System.currentTimeMillis(),
        required = true
    )
    private val highriskSymbols = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Identification of high risk symbols",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val highRiskReason = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "If yes, select the reason give below and write",
        required = true
    )
    private val highRiskPregnant = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Pregnant in high risk category treated",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val highRiskPregnancyReferred = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Pregnant in high risk category was referred",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val birthPrepAndNutritionAndFamilyPlanning = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Preparation for birth and complications, nutrition and family planning",
        list = arrayOf("Yes", "No"),
        required = true
    )
    private val medicalOfficerSign = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Signature of medical officer in charge ",
        required = true
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
            bloodPressure,
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
            highRiskReason,
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
        pmsmaCache.bloodPressure = bloodPressure.value.value
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
        pmsmaCache.tetanusToxoid = tetanusToxoid.value.value == "Yes"
        pmsmaCache.lastMenstrualPeriod = getLongFromDate(lastMenstrualPeriod.value.value!!)
        pmsmaCache.expectedDateOfDelivery = getLongFromDate(expectedDateOfDelivery.value.value!!)
        pmsmaCache.highriskSymbols = highriskSymbols.value.value == "Yes"
        pmsmaCache.highRiskReason = highRiskReason.value.value
        pmsmaCache.highRiskPregnant = highRiskPregnant.value.value == "Yes"
        pmsmaCache.highRiskPregnancyReferred = highRiskPregnancyReferred.value.value == "Yes"
        pmsmaCache.birthPrepAndNutritionAndFamilyPlanning = birthPrepAndNutritionAndFamilyPlanning.value.value == "Yes"
        pmsmaCache.medicalOfficerSign = medicalOfficerSign.value.value
    }


}