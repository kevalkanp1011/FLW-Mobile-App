package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInputOld
import org.piramalswasthya.sakhi.model.HBYCCache
import org.piramalswasthya.sakhi.model.InputType

class HBYCFormDataset(context: Context, private val hbyc: HBYCCache? = null) {

    private val month = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Month",
        required = false
    )
    private val subCenterName = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Subcenter name ",
        required = false
    )
    private val year = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Year",
        required = false
    )
    private val primaryHealthCenterName = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Primary Health Center Name ",
        required = false
    )
    private val villagePopulation = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Village Population",
        required = false
    )
    private val infantPopulation = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Total number of children aged 3 to 15 months in village",
        required = false
    )
    private val visitDate = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Visit Date",
        required = false
    )
    private val hbycAgeCategory = FormInputOld(
        inputType = InputType.DROPDOWN,
        title = "H.B.Y.C by age Tour",
        required = false
    )
    private val orsPacketDelivered = FormInputOld(
        inputType = InputType.RADIO,
        title = "O.R.S. Packet delivered ",
        required = false
    )
    private val ironFolicAcidGiven = FormInputOld(
        inputType = InputType.RADIO,
        title = "Iron folic acid syrup given (after six months of childbirth)",
        required = false
    )
    private val isVaccinatedByAge = FormInputOld(
        inputType = InputType.RADIO,
        title = "Child vaccinated by age",
        required = false
    )
    private val wasIll = FormInputOld(
        inputType = InputType.RADIO,
        title = "The child was ill",
        required = false
    )
    private val referred = FormInputOld(
        inputType = InputType.RADIO,
        title = "If yes refer hospital",
        required = false
    )
    private val supplementsGiven = FormInputOld(
        inputType = InputType.RADIO,
        title = "Supplements given to the child according to age",
        required = false
    )
    private val byHeightLength = FormInputOld(
        inputType = InputType.RADIO,
        title = "By height / length (color inscribed in mcp card)",
        required = false
    )
    private val childrenWeighingLessReferred = FormInputOld(
        inputType = InputType.RADIO,
        title = "Number of children weighing less than –3SD orange color indicated in MCCP card referred to VHSND/PHC/NRC",
        required = false
    )
    private val weightAccordingToAge = FormInputOld(
        inputType = InputType.RADIO,
        title = "Weight according to child’s age (color inscribed in MCP card)",
        required = false
    )
    private val delayInDevelopment = FormInputOld(
        inputType = InputType.RADIO,
        title = "Delay / constraint found in the physical and mental development of the child according to age",
        required = false
    )
    private val referredToHealthInstitite = FormInputOld(
        inputType = InputType.RADIO,
        title = "If yes then Health Institute R.B.S.K. Team / A.N.M. referred to",
        required = false
    )
    private val vitaminASupplementsGiven = FormInputOld(
        inputType = InputType.RADIO,
        title = "Vitamin – A supplements given to the child according to age",
        required = false
    )
    private val deathAge = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "age (in month) at the time of death",
        required = false
    )
    private val deathCause = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Cause of death",
        required = false
    )
    private val qmOrAnmInformed = FormInputOld(
        inputType = InputType.RADIO,
        title = "Q.M/A.N.M was informed",
        required = false
    )
    private val deathPlace = FormInputOld(
        inputType = InputType.DROPDOWN,
        title = "Place of death",
        required = false
    )
    private val superVisorOn = FormInputOld(
        inputType = InputType.RADIO,
        title = "Supervisor from block/district/state level in last on",
        required = false
    )
    private val orsShortage = FormInputOld(
        inputType = InputType.RADIO,
        title = "ORS in last one month Packet shortage",
        required = false
    )
    private val ifaDecreased = FormInputOld(
        inputType = InputType.RADIO,
        title = "IFA in last one month Decreased syrup",
        required = false
    )
}