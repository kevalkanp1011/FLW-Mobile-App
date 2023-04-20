package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.ImmunisationCache
import org.piramalswasthya.sakhi.model.InputType

class ImmunisationFormDataset(context: Context, private val immunisation: ImmunisationCache? = null) {

    private val motherName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Mother's Name",
        required = false
    )
    private val dateOfBirth = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Date Of Birth",
        required = false
    )
    private val dateOfPrevVaccination = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Date of vaccination",
        required = false
    )
    private val numDoses = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "No. of Doses Taken",
        required = false
    )
    private val vaccineName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Vaccine Name",
        required = false
    )
    private val doseNumber = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Dose Number",
        required = false
    )
    private val expectedDate = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Expected Date",
        required = false
    )
    private val dateOfCurrentVaccination = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Date of Vaccination",
        required = false
    )
    private val vaccinatedAt = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Vaccinated Place",
        required = false
    )
    private val vaccinatedBy = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Vaccinated By",
        required = false
    )
}