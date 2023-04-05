package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import android.widget.LinearLayout
import org.piramalswasthya.sakhi.model.CDRCache
import org.piramalswasthya.sakhi.model.FormInput
import java.text.SimpleDateFormat
import java.util.*

class CDRFormDataset(context: Context, private val childDeathReview: CDRCache? = null) {

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


    fun mapValues(cdrCache: CDRCache) {
        cdrCache.childName = childName.value.value
        cdrCache.dateOfBirth = getLongFromDate(dateOfBirth.value.value!!)
        cdrCache.visitDate = getLongFromDate(visitDate.value.value!!)
        cdrCache.gender = gender.value.value
        cdrCache.motherName = motherName.value.value
        cdrCache.fatherName = fatherName.value.value
        cdrCache.address = address.value.value
        cdrCache.houseNumber = houseNumber.value.value
        cdrCache.mohalla = mohalla.value.value
        cdrCache.landmarks = landmarks.value.value
        cdrCache.landline = landline.value.value?.toLong() ?: 0L
        cdrCache.mobileNumber = mobileNumber.value.value?.toLong() ?: 0L
        cdrCache.dateOfDeath = getLongFromDate(dateOfDeath.value.value!!)
        cdrCache.timeOfDeath = timeOfDeath.value.value
        cdrCache.placeOfDeath = placeOfDeath.value.value
        cdrCache.firstInformant = firstInformant.value.value
        cdrCache.ashaSign = ashaSign.value.value
        cdrCache.pincode = pincode.value.value?.toInt()?:0
        cdrCache.dateOfNotification = dateOfNotification.value.value?.let { getLongFromDate(it) }?: System.currentTimeMillis()
    }

    val childName = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Name of the Child",
        required = false
    )
    val dateOfBirth = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Date of Birth",
        required = false
    )
    val age = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Age",
        required = false
    )
    val visitDate = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        min = 0L,
        max = System.currentTimeMillis(),
        title = "Visit Date",
        required = true
    )
    val gender = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Gender",
        required = false
    )
    val motherName = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Mother’s Name",
        required = false
    )
    val fatherName = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Father’s Name",
        required = false
    )
    val address = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Address",
        required = false
    )
    val houseNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "House number",
        required = false
    )
    val mohalla = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Mohalla/Colony ",
        required = false
    )
    val landmarks = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Landmarks, if any ",
        required = false
    )
    val pincode = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Pincode",
        etMaxLength = 6,
        min=100000,
        max=999999,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val landline = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Landline",
        etMaxLength = 12,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val mobileNumber = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Mobile number",
        required = false
    )
    val dateOfDeath = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Date of death",
        min = 0L,
        max = System.currentTimeMillis(),
        required = true
    )
    val timeOfDeath = FormInput(
        inputType = FormInput.InputType.TIME_PICKER,
        title = "Time",
        required = false
    )
    val placeOfDeath = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Place of Death",
        required = true,
        orientation = LinearLayout.VERTICAL,
        entries = arrayOf("Home", "Hospital", "In transit")
    )
    val hospitalName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Hospital Name",
        required = false
    )
    val firstInformant = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Name of First Informant ",
        required = false
    )
    val ashaSign = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Signature/Name of ASHA",
        required = false
    )
    val dateOfNotification = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Date of Notification ",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )

    val firstPage = listOf(childName, dateOfBirth, age, visitDate, gender, motherName, fatherName, address, houseNumber, mohalla, landmarks, pincode,
        landline, mobileNumber, dateOfDeath, timeOfDeath, placeOfDeath, firstInformant, ashaSign, dateOfNotification)
}