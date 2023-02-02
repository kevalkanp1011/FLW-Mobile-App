package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.childDeathReviewCache

class ChildDeathReviewFormDataset(context: Context, private val childDeathReview: childDeathReviewCache? = null) {

    private val childName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of the Child",
        required = false
    )
    private val dateOfBirth = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Date of Birth",
        required = false
    )
    private val age = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Age",
        required = false
    )
    private val visitDate = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Visit Date",
        required = false
    )
    private val gender = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "Gender",
        required = false
    )
    private val motherName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Mother’s Name",
        required = false
    )
    private val fatherName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Father’s Name",
        required = false
    )
    private val address = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Address",
        required = false
    )
    private val houseNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "House number",
        required = false
    )
    private val mohalla = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Mohalla/Colony ",
        required = false
    )
    private val landmarks = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Landmarks, if any ",
        required = false
    )
    private val pincode = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Pincode",
        required = false
    )
    private val landline = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Landline",
        required = false
    )
    private val mobileNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Mobile number",
        required = false
    )
    private val dateOfDeath = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Date of death",
        required = false
    )
    private val timeOfDeath = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Time",
        required = false
    )
    private val placeOfDeath = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Place of Death",
        required = false
    )
    private val firstInformant = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Name of First Informant ",
        required = false
    )
    private val ashaSign = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Signature/Name of ASHA",
        required = false
    )
    private val dateOfNotification = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Date of Notification ",
        required = false
    )
}