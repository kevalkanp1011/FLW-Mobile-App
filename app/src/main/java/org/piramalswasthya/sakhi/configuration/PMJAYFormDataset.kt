package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.PMJAYCache
import java.text.SimpleDateFormat
import java.util.*

class PMJAYFormDataset(context: Context) {

    companion object {
        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    fun mapValues(pmjayCache: PMJAYCache) {
        pmjayCache.registrationDate = registrationDate.value.value?.let { getLongFromDate(it) }
        pmjayCache.registeredHospital = registeredHospital.value.value
        pmjayCache.contactNumber = contactNumber.value.value?.let { it.toLong() } ?: 0L
        pmjayCache.communicationContactNumber = communicationContactNumber.value.value?.let { it.toLong() } ?: 0L
        pmjayCache.patientAddress = patientAddress.value.value
        pmjayCache.communicationAddress = communicationAddress.value.value
        pmjayCache.hospitalAddress = hospitalAddress.value.value
        pmjayCache.familyId = familyId.value.value?.let { it.toLong() } ?: 0L
        pmjayCache.isAadhaarBeneficiary = isAadhaarBeneficiary.value.value?.let { it.toLong() } ?: 0L
        pmjayCache.memberType = memberType.value.value
        pmjayCache.patientType = patientType.value.value
        pmjayCache.scheme = scheme.value.value
    }

    val id = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Id Number",
        required = false
    )
    val registrationDate = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Registration Date",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val registeredHospital = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Registered Hospital",
        required = false
    )
    val contactNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Contact No",
        required = false
    )
    val communicationContactNumber = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Communication Contact No",
        required = false
    )
    val patientAddress = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Patient Address",
        required = false
    )
    val communicationAddress = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Communication Address",
        required = false
    )
    val hospitalAddress = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Hospital Address",
        required = false
    )
    val familyId = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Family Id",
        required = false
    )
    val isAadhaarBeneficiary = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Is Aadhaar Beneficiary",
        required = false,
    )
    val memberType = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Member Type",
        required = false
    )
    val patientType = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "Patient Type",
        required = false,
        list = arrayOf("Select Type", "General OP", "IP")
    )
    val scheme = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = "Scheme",
        required = false,
        list = arrayOf("Select Scheme", "AB-PMJAY(S)")
    )

    val firstPage by lazy {
        listOf(id, registrationDate, registeredHospital, contactNumber, communicationContactNumber, patientAddress,
            communicationAddress, hospitalAddress, familyId, isAadhaarBeneficiary, memberType, patientType, scheme)
    }
}