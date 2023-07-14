package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.*

class DeliveryOutcomeDataset(
    context: Context, currentLanguage: Languages
) : Dataset(context, currentLanguage) {

    private val dateOfDelivery = FormElement(
        id = 1,
        inputType = InputType.DATE_PICKER,
        title = "Date of Delivery",
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = true
    )

    private val timeOfDelivery = FormElement(
        id = 2,
        inputType = InputType.TIME_PICKER,
        title = "Time of Delivery",
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = true
    )

    private var placeOfDelivery = FormElement(
        id = 3,
        inputType = InputType.DROPDOWN,
        title = "Place of Delivery",
        entries = arrayOf("District Hospital", "Community Health Centre", "Primary Health Centre", "Sub Centre",
                "Other Public Facility", "Accredited Private Hospital", "Other Private Hospital", "Home",
                "Sub District Hospital", "Medical College Hospital", "In Transit"),
        required = false,
        hasDependants = false
    )

    private var typeOfDelivery = FormElement(
        id = 4,
        inputType = InputType.RADIO,
        title = "Type of Delivery",
        entries = arrayOf("Normal", "Caesarean", "Assisted"),
        required = false,
        hasDependants = false
    )

    private var hadComplications = FormElement(
        id = 5,
        inputType = InputType.RADIO,
        title = "Complications during Delivery?",
        entries = arrayOf("Yes", "No"),
        required = false,
        hasDependants = true
    )

    private var complication = FormElement(
        id = 6,
        inputType = InputType.DROPDOWN,
        title = "Delivery Complication",
        entries = arrayOf("PPH", "RETAINED PLACENTA", "OBSTRUCTED DELIVERY", "PROLAPSED CORD", "TWINS PREGNANCY",
                    "CONVULSIONS", "DEATH", "ANY OTHER (SPECIFY)", "DON'T KNOW"),
        required = true,
        hasDependants = true
    )

    private var causeOfDeath = FormElement(
        id = 7,
        inputType = InputType.DROPDOWN,
        title = "Probable Cause of Death",
        entries = arrayOf("Eclampcia", "Haemorrahge", "Obstructed Labour", "Prolonged Labour", "Other", "High Fever"),
        required = true,
        hasDependants = true
    )

    private var otherCauseOfDeath = FormElement(
        id = 8,
        inputType = InputType.EDIT_TEXT,
        title = "Other Cause of Death",
        required = true,
        hasDependants = false
    )

    private var otherComplication = FormElement(
        id = 9,
        inputType = InputType.EDIT_TEXT,
        title = "Other Delivery Complication",
        required = true,
        hasDependants = false
    )

    private var deliveryOutcome = FormElement(
        id = 10,
        inputType = InputType.EDIT_TEXT,
        title = "Outcomes of Delivery",
        required = false,
        hasDependants = false,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
        max = 6,
        min = 0,
    )

    private var liveBirth = FormElement(
        id = 11,
        inputType = InputType.EDIT_TEXT,
        title = "Live Birth",
        required = false,
        hasDependants = false,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
        max = 6,
        min = 0,
    )

    private var stillBirth = FormElement(
        id = 12,
        inputType = InputType.EDIT_TEXT,
        title = "Still Birth",
        required = false,
        hasDependants = false,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
        max = 6,
        min = 0,
    )

    private val dateOfDischarge = FormElement(
        id = 13,
        inputType = InputType.DATE_PICKER,
        title = "Date of Discharge",
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = true

    )

    private val timeOfDischarge = FormElement(
        id = 14,
        inputType = InputType.TIME_PICKER,
        title = "Time of Discharge",
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = true
    )

    private var isJSYBenificiary = FormElement(
        id = 15,
        inputType = InputType.RADIO,
        title = "Is the Pregnant Woman a JSY Beneficiary?",
        entries = arrayOf("Yes", "No"),
        required = false,
        hasDependants = false
    )

    suspend fun setUpPage(ben: BenRegCache?, saved: DeliveryOutcomeCache?) {
        var list = mutableListOf(
            dateOfDelivery,
            timeOfDelivery,
            placeOfDelivery,
            typeOfDelivery,
            hadComplications,
//            complication,
//            causeOfDeath,
//            otherCauseOfDeath,
//            otherComplication,
            deliveryOutcome,
            liveBirth,
            stillBirth,
            dateOfDischarge,
            timeOfDischarge,
            isJSYBenificiary
        )
        if (saved == null) {
            dateOfDelivery.value = getDateFromLong(System.currentTimeMillis())
            dateOfDischarge.value = getDateFromLong(System.currentTimeMillis())
        } else {
            list = mutableListOf(
                dateOfDelivery,
                timeOfDelivery,
                placeOfDelivery,
                typeOfDelivery,
                hadComplications,
//                complication,
//                causeOfDeath,
//                otherCauseOfDeath,
//                otherComplication,
                deliveryOutcome,
                liveBirth,
                stillBirth,
                dateOfDischarge,
                timeOfDischarge,
                isJSYBenificiary
            )
            dateOfDelivery.value = getDateFromLong(saved.dateOfDelivery)
            timeOfDelivery.value = saved.timeOfDelivery
            placeOfDelivery.value = saved.placeOfDelivery
            typeOfDelivery.value = saved.typeOfDelivery
            hadComplications.value = if (saved.hadComplications == true) "Yes" else "No"
            complication.value = saved.complication
            causeOfDeath.value = saved.causeOfDeath
            otherCauseOfDeath.value = saved.otherCauseOfDeath
            otherComplication.value = saved.otherComplication
            deliveryOutcome.value = saved.deliveryOutcome.toString()
            liveBirth.value = saved.liveBirth.toString()
            stillBirth.value = saved.stillBirth.toString()
            dateOfDischarge.value = getDateFromLong(saved.dateOfDischarge)
            timeOfDischarge.value = saved.timeOfDischarge
            isJSYBenificiary.value = if (saved.isJSYBenificiary == true) "Yes" else "No"
        }
        ben?.let {
            dateOfDelivery.min = it.regDate
        }
        setUpPage(list)

    }
    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
            dateOfDelivery.id -> {
                dateOfDischarge.min = getLongFromDate(dateOfDelivery.value)
                -1
            }
            hadComplications.id -> {
                triggerDependants(
                    source = hadComplications,
                    passedIndex = index,
                    triggerIndex = 0,
                    target = complication,
                    targetSideEffect = listOf(causeOfDeath, otherComplication, otherCauseOfDeath)
                )
            }
            complication.id -> {
                if(index == 6) {
                    triggerDependants(
                        source = complication,
                        addItems = listOf(causeOfDeath),
                        removeItems = listOf(otherComplication, otherCauseOfDeath)
                    )
                } else if(index == 7) {
                    triggerDependants(
                        source = complication,
                        addItems = listOf(otherComplication),
                        removeItems = listOf(causeOfDeath, otherCauseOfDeath)
                    )
                } else {
                    triggerDependants(
                        source = complication,
                        addItems = listOf(),
                        removeItems = listOf(otherComplication, otherCauseOfDeath, causeOfDeath)
                    )
                }
            }

            causeOfDeath.id -> {
                triggerDependants(
                    source = causeOfDeath,
                    passedIndex = index,
                    triggerIndex = 4,
                    target = otherCauseOfDeath
                )
            }
            deliveryOutcome.id -> {
                validateIntMinMax(deliveryOutcome)
                validateMaxDeliveryOutcome()
            }
            liveBirth.id -> {
                validateIntMinMax(liveBirth)
                validateMaxDeliveryOutcome()
            }
            stillBirth.id -> {
                validateIntMinMax(stillBirth)
                validateMaxDeliveryOutcome()
            }
            else -> -1
        }
    }

    private fun validateMaxDeliveryOutcome() : Int {
        if(!liveBirth.value.isNullOrEmpty() && !stillBirth.value.isNullOrEmpty() && !deliveryOutcome.value.isNullOrEmpty()) {
            if(deliveryOutcome.value!!.toInt() !== liveBirth.value!!.toInt() + stillBirth.value!!.toInt()) {
                deliveryOutcome.errorText = "Outcome of Delivery should equal to sum of Live and Still births"
            }
        }
        return -1
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        (cacheModel as DeliveryOutcomeCache).let { form ->
            form.dateOfDelivery = getLongFromDate(dateOfDelivery.value)
            form.timeOfDelivery = timeOfDelivery.value
            form.placeOfDelivery = placeOfDelivery.value
            form.typeOfDelivery = typeOfDelivery.value
            form.hadComplications = hadComplications.value == "Yes"
            form.complication = complication.value
            form.causeOfDeath = causeOfDeath.value
            form.otherCauseOfDeath = otherCauseOfDeath.value
            form.otherComplication = otherComplication.value
            form.deliveryOutcome = deliveryOutcome.value?.toInt()
            form.liveBirth = liveBirth.value?.toInt()
            form.stillBirth = stillBirth.value?.toInt()
            form.dateOfDischarge = getLongFromDate(dateOfDischarge.value)
            form.timeOfDischarge = timeOfDischarge.value
            form.isJSYBenificiary = isJSYBenificiary.value == "Yes"
        }
    }
}