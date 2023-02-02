package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.SurveyRegisterCache

class SurveyRegisterFormDataset(context: Context, private val surveyRegister: SurveyRegisterCache? = null) {

    private val facilitatorSupervisor = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of Facilitator Supervisor",
        required = false
    )
    private val from = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "From",
        required = false
    )
    private val till = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Till",
        required = false
    )
    private val firstHouseLandlord = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of the landlord of First House",
        required = false
    )
    private val fisrtHomeIdentificationSymbol = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Home identification Symbol",
        required = false
    )
    private val middleHouseLandlord = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of the landlord of Middle House",
        required = false
    )
    private val middleHomeIdentificationSymbol = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Home Identification Symbol",
        required = false
    )
    private val lastHouseLandlord = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of the landlord of Last House",
        required = false
    )
    private val lastHomeIdentificationSymbol = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Home Identification Symbol",
        required = false
    )
    private val householdNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Household number",
        required = false
    )
    private val headOfHouse = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of Head of House",
        required = false
    )
    private val numFamilyMembers = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total members in the family",
        required = false
    )
    private val numEligibleCouples = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total number of eligible couples in the family",
        required = false
    )
    private val numPregnantWomen = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total number of pregnant women in the house",
        required = false
    )
    private val numChildrenLessThanOneMonth = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total number of children (0 – 1 month)",
        required = false
    )
    private val numChildrenLessThanOneYear = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total no. of children (1 month – 1 year)",
        required = false
    )
    private val numChildrenLessThanFiveYear = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Total no. of children (2 yr – 5 yr)",
        required = false
    )
}