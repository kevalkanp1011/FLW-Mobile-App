package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HBYCCache
import java.text.SimpleDateFormat
import java.util.*

class HBYCFormDataset(context: Context, private val hbyc: HBYCCache? = null) {

    companion object {
        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    fun mapValues(hbycCache: HBYCCache) {
        hbycCache.month = month.value.value
        hbycCache.subcenterName = subCenterName.value.value
        hbycCache.year = year.value.value
        hbycCache.primaryHealthCenterName = primaryHealthCenterName.value.value
        hbycCache.villagePopulation = villagePopulation.value.value
        hbycCache.infantPopulation = infantPopulation.value.value
        hbycCache.visitdate = visitDate.value.value?.let { getLongFromDate(it) }
        hbycCache.hbycAgeCategory = hbycAgeCategory.value.value
        hbycCache.orsPacketDelivered = orsPacketDelivered.value.value == "Yes"
        hbycCache.ironFolicAcidGiven = ironFolicAcidGiven.value.value == "Yes"
        hbycCache.isVaccinatedByAge = isVaccinatedByAge.value.value == "Yes"
        hbycCache.wasIll = wasIll.value.value == "Yes"
        hbycCache.referred = referred.value.value == "Yes"
        hbycCache.supplementsGiven = supplementsGiven.value.value == "Yes"
        hbycCache.byHeightLength = byHeightLength.value.value == "Yes"
        hbycCache.childrenWeighingLessReferred = childrenWeighingLessReferred.value.value == "Yes"
        hbycCache.weightAccordingToAge = weightAccordingToAge.value.value == "Yes"
        hbycCache.delayInDevelopment = delayInDevelopment.value.value == "Yes"
        hbycCache.referredToHealthInstitite = referredToHealthInstitite.value.value == "Yes"
        hbycCache.vitaminASupplementsGiven = vitaminASupplementsGiven.value.value == "Yes"
        hbycCache.deathAge = deathAge.value.value
        hbycCache.deathCause = deathCause.value.value
        hbycCache.qmOrAnmInformed = qmOrAnmInformed.value.value == "Yes"
        hbycCache.deathPlace = deathPlace.value.value
        hbycCache.superVisorOn = superVisorOn.value.value == "Yes"
        hbycCache.orsShortage = orsShortage.value.value == "Yes"
        hbycCache.ifaDecreased = ifaDecreased.value.value == "Yes"
    }

    private val month = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Month",
        required = false
    )
    private val subCenterName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Subcenter name ",
        required = false
    )
    private val year = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Year",
        required = false
    )
    private val primaryHealthCenterName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Primary Health Center Name ",
        required = false
    )
    private val villagePopulation = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Village Population",
        required = false
    )
    private val infantPopulation = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total number of children aged 3 to 15 months in village",
        required = false
    )
    private val visitDate = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Visit Date",
        required = false,
        min = 0L,
        max = System.currentTimeMillis()
    )
    private val hbycAgeCategory = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "H.B.Y.C by age Tour",
        required = false,
        entries = arrayOf("3", "6", "9", "12", "15")
    )
    private val orsPacketDelivered = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "O.R.S. Packet delivered ",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val ironFolicAcidGiven = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Iron folic acid syrup given (after six months of childbirth)",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val isVaccinatedByAge = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Child vaccinated by age",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val wasIll = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "The child was ill",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val referred = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "If yes refer hospital",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val supplementsGiven = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Supplements given to the child according to age",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val byHeightLength = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "By height / length (color inscribed in mcp card)",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val childrenWeighingLessReferred = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Number of children weighing less than –3SD orange color indicated in MCCP card referred to VHSND/PHC/NRC",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val weightAccordingToAge = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Weight according to child’s age (color inscribed in MCP card)",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val delayInDevelopment = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Delay / constraint found in the physical and mental development of the child according to age",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val referredToHealthInstitite = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "If yes then Health Institute R.B.S.K. Team / A.N.M. referred to",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val vitaminASupplementsGiven = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Vitamin – A supplements given to the child according to age",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val deathAge = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "Mark age (in month) at the time of death",
        required = false,
        entries = arrayOf("3 Month", "4 Month", "5 Month", "6 Month", "7 Month", "8 Month", "9 Month", "10 Month", "11 Month",
            "12 Month", "13 Month", "14 Month", "15 Month", )
    )
    private val deathCause = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Cause of death",
        required = false
    )
    private val qmOrAnmInformed = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Q.M/A.N.M was informed",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val deathPlace = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "Place of death",
        required = false,
        entries = arrayOf("Home", "Health Center", "On the Way")
    )
    private val superVisorOn = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Supervisor from block/district/state level in last on",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val orsShortage = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "ORS in last one month Packet shortage",
        required = false,
        entries = arrayOf("Yes", "No")
    )
    private val ifaDecreased = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "IFA in last one month Decreased syrup",
        required = false,
        entries = arrayOf("Yes", "No")
    )

    val firstPage by lazy {
        listOf(month, subCenterName, year, primaryHealthCenterName, villagePopulation, infantPopulation, visitDate, hbycAgeCategory,
        orsPacketDelivered, ironFolicAcidGiven, isVaccinatedByAge, wasIll, referred, supplementsGiven, byHeightLength,
        childrenWeighingLessReferred, weightAccordingToAge, delayInDevelopment, referredToHealthInstitite, vitaminASupplementsGiven,
        deathAge, deathCause, qmOrAnmInformed, deathPlace, superVisorOn, orsShortage, ifaDecreased)
    }
}