package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInputOld
import org.piramalswasthya.sakhi.model.ImmunisationCache
import org.piramalswasthya.sakhi.model.InputType

class ImmunisationFormDataset(context: Context, private val immunisation: ImmunisationCache? = null) {

    private val motherName = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Mother's Name",
        required = false
    )
    private val dateOfBirth = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Date Of Birth",
        required = false
    )
    private val dateOfPrevVaccination = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Date of vaccination",
        required = false
    )
    private val numDoses = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "No. of Doses Taken",
        required = false
    )
    private val vaccineName = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Vaccine Name",
        required = false
    )
    private val doseNumber = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Dose Number",
        required = false
    )
    private val expectedDate = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Expected Date",
        required = false
    )
    private val dateOfCurrentVaccination = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Date of Vaccination",
        required = false
    )
    private val vaccinatedAt = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Vaccinated Place",
        required = false
    )
    private val vaccinatedBy = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Vaccinated By",
        required = false
    )
}