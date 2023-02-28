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
        required = false
    )
    private val haveMCPCard = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Does the beneficiary have an MCP card",
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
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Mobile number",
        required = false
    )
    private val numANC = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Number of ANCs done before delivery",
        required = false
    )
    private val weight = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Weight",
        required = false
    )
    private val systolicBloodPressure = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Systolic Blood Pressure",
        required = false
    )
    private val bloodPressure = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Blood Pressure",
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
        title = "Hemoglobin and blood group ",
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
        required = false
    )
    private val lastMenstrualPeriod = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Last Menstrual Period ",
        required = false
    )
    private val expectedDateOfDelivery = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Expected Date of Delivery ",
        required = false
    )
    private val highriskSymbols = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Identification of high risk symbols",
        list = arrayOf("Yes", "No"),
        required = false
    )
    private val highRiskReason = FormInput(
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

    }


}