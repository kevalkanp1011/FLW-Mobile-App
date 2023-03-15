package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType
import org.piramalswasthya.sakhi.model.HBNCCache
import java.text.SimpleDateFormat
import java.util.*

class HBNCFormDataset(context: Context, private val hbnc: HBNCCache? = null) {

    companion object {
        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    fun mapValues(hbnc: HBNCCache) {

    }

    private val titleHomeVisit = FormInput(
        inputType = InputType.HEADLINE,
        title = "Home Visit Form for newborn and mother care",
        required = false
    )
    private val healthSubCenterName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Health Subcenter Name ",
        required = false
    )
    private val phcName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "P.H.C. Name ",
        required = false
    )
    private val motherName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Mother Name",
        required = false
    )
    private val fatherName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Father Name",
        required = false
    )

    private val dateOfDelivery = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of Delivery",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val placeOfDelivery = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Place of Delivery",
        entries = arrayOf(
            "House",
            "Health center",
            "CHC",
            "PHC",
        ),
        required = false
    )
    private val gender = FormInput(
        inputType = InputType.RADIO,
        title = "Baby Gender",
        entries = arrayOf(
            "Male",
            "Female",
            "Transgender",
        ),
        required = false
    )

    private val typeOfDelivery = FormInput(
        inputType = InputType.RADIO,
        title = "Type of Delivery",
        entries = arrayOf(
            "Cesarean",
            "Normal",
        ),
        required = false
    )
    private val startedBreastFeeding = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Started Breastfeeding",
        entries = arrayOf(
            "Within an hour ",
            "An hour later ",
            "After 24 hours"
        ),
        required = false
    )
    private val weightAtBirth = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Weight at birth ( grams )",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfDischargeFromHospital = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Discharge date from hospital",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val motherStatus = FormInput(
        inputType = InputType.RADIO,
        title = "Mother Status",
        entries = arrayOf(
            "Living",
            "Dead",
        ),
        required = false
    )
    private val registrationOfBirth = FormInput(
        inputType = InputType.RADIO,
        title = "Mother Status",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val childStatus = FormInput(
        inputType = InputType.RADIO,
        title = "Child Status",
        entries = arrayOf(
            "Living",
            "Dead",
        ),
        required = false
    )
    private val homeVisitDate = FormInput(
        inputType = InputType.RADIO,
        title = "Home Visit Date",
        entries = arrayOf(
            "1st Day",
            "3rd Day",
        ),
        required = false
    )
    private val childImmunizationStatus = FormInput(
        inputType = InputType.CHECKBOXES,
        title = "Child Immunization Status",
        entries = arrayOf(
            "BCG",
            "Polio",
            "DPT 1",
            "Hepatitis-B"
        ),
        required = false
    )
    private val birthWeightRecordedInCard = FormInput(
        inputType = InputType.RADIO,
        title = "Birth weight of the newborn recorded in Mother and Child Protection Card",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )



















    val firstPage by lazy {
        listOf(
           titleHomeVisit,
           healthSubCenterName,
           phcName,
           motherName,
           fatherName,
           dateOfDelivery,
           placeOfDelivery,
           gender,
           typeOfDelivery,
           startedBreastFeeding,
           weightAtBirth,
           dateOfDischargeFromHospital,
           motherStatus,
           registrationOfBirth,
           childStatus,
           homeVisitDate,
           childImmunizationStatus,
           birthWeightRecordedInCard,

        )
    }
}