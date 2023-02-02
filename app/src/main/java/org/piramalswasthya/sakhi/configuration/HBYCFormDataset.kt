package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HBYCCache

class HBYCFormDataset(context: Context, private val hbyc: HBYCCache? = null) {

    private val month = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Month",
        required = false
    )
    private val subcenterName = FormInput(
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
    private val visitdate = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Visit Date",
        required = false
    )
    private val hbycAgeCategory = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "H.B.Y.C by age Tour",
        required = false
    )
    private val orsPacketDelivered = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "O.R.S. Packet delivered ",
        required = false
    )
    private val ironFolicAcidGiven = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Iron folic acid syrup given (after six months of childbirth)",
        required = false
    )
    private val isVaccinatedByAge = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Child vaccinated by age",
        required = false
    )
    private val wasIll = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "The child was ill",
        required = false
    )
    private val referred = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "If yes refer hospital",
        required = false
    )
    private val supplementsGiven = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Supplements given to the child according to age",
        required = false
    )
    private val byHeightLength = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "By height / length (color inscribed in mcp card)",
        required = false
    )
    private val childrenWeighingLessReferred = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Number of children weighing less than –3SD orange color indicated in MCCP card referred to VHSND/PHC/NRC",
        required = false
    )
    private val weightAccordingToAge = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Weight according to child’s age (color inscribed in MCP card)",
        required = false
    )
    private val delayInDevelopment = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Delay / constraint found in the physical and mental development of the child according to age",
        required = false
    )
    private val referredToHealthInstitite = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "If yes then Health Institute R.B.S.K. Team / A.N.M. referred to",
        required = false
    )
    private val vitaminASupplementsGiven = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Vitamin – A supplements given to the child according to age",
        required = false
    )
    private val deathAge = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "age (in month) at the time of death",
        required = false
    )
    private val deathCause = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Cause of death",
        required = false
    )
    private val qmOrAnmInformed = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Q.M/A.N.M was informed",
        required = false
    )
    private val deathPlace = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "Place of death",
        required = false
    )
    private val superVisorOn = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Supervisor from block/district/state level in last on",
        required = false
    )
    private val orsShortage = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "ORS in last one month Packet shortage",
        required = false
    )
    private val ifaDecreased = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "IFA in last one month Decreased syrup",
        required = false
    )
}