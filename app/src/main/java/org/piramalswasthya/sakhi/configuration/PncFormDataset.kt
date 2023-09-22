package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.DeliveryOutcomeCache
import org.piramalswasthya.sakhi.model.FormElement
import org.piramalswasthya.sakhi.model.InputType

class PncFormDataset(
    context: Context, currentLanguage: Languages
) : Dataset(context, currentLanguage) {

    private val pncPeriod = FormElement(
        id = 1,
        inputType = InputType.DROPDOWN,
        title = resources.getString(R.string.pnc_period),
        entries = resources.getStringArray(R.array.pnc_period_array),
        arrayId = -1,
        required = true,
        hasDependants = false
    )

    private val visitDate = FormElement(
        id = 2,
        inputType = InputType.DATE_PICKER,
        title = resources.getString(R.string.pnc_visit_date),
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = false
    )

    private var ifaTabsGiven = FormElement(
        id = 3,
        inputType = InputType.EDIT_TEXT,
        title = resources.getString(R.string.pnc_ifa_tabs_given),
        required = false,
        hasDependants = false,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 3,
        max = 400,
        min = 0,
    )

    private var anyContraceptionMethod = FormElement(
        id = 4,
        inputType = InputType.RADIO,
        title = resources.getString(R.string.pnc_any_contraception_method),
        entries = resources.getStringArray(R.array.pnc_confirmation_array),
        required = false,
        hasDependants = false
    )

    private var contraceptionMethod = FormElement(
        id = 5,
        inputType = InputType.DROPDOWN,
        title = resources.getString(R.string.pnc_contraception_method),
        entries = resources.getStringArray(R.array.pnc_contraception_method_array),
        required = false,
        hasDependants = true
    )

    private var otherPpcMethod = FormElement(
        id = 6,
        inputType = InputType.EDIT_TEXT,
        title = resources.getString(R.string.pnc_other_ppc_method),
        required = true,
        hasDependants = true
    )

    private var motherDangerSign = FormElement(
        id = 7,
        inputType = InputType.DROPDOWN,
        title = resources.getString(R.string.pnc_mother_danger_sign),
        entries = resources.getStringArray(R.array.pnc_mother_danger_sign_array),
        required = true,
        hasDependants = true
    )

    private var otherDangerSign = FormElement(
        id = 8,
        inputType = InputType.EDIT_TEXT,
        title = resources.getString(R.string.pnc_other_danger_sign),
        required = true,
        hasDependants = false
    )

    private var referralFacility = FormElement(
        id = 9,
        inputType = InputType.DROPDOWN,
        title = resources.getString(R.string.pnc_referral_facility),
        entries = resources.getStringArray(R.array.pnc_referral_facility_array),
        required = true,
        hasDependants = false
    )

    private var motherDeath = FormElement(
        id = 10,
        inputType = InputType.RADIO,
        title = resources.getString(R.string.pnc_mother_death),
        entries = resources.getStringArray(R.array.pnc_confirmation_array),
        required = false,
        hasDependants = false,
    )

    private var deathDate = FormElement(
        id = 11,
        inputType = InputType.DATE_PICKER,
        title = resources.getString(R.string.pnc_death_date),
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = false
    )

    private var causeOfDeath = FormElement(
        id = 12,
        inputType = InputType.DROPDOWN,
        title = resources.getString(R.string.pnc_death_cause),
        entries = resources.getStringArray(R.array.pnc_death_cause_array),
        required = false,
        hasDependants = false,
    )

    private var otherDeathCause = FormElement(
        id = 13,
        inputType = InputType.EDIT_TEXT,
        title = resources.getString(R.string.pnc_other_death_cause),
        required = true,
        hasDependants = false
    )

    private var placeOfDeath = FormElement(
        id = 14,
        inputType = InputType.DROPDOWN,
        title = resources.getString(R.string.pnc_death_place),
        entries = resources.getStringArray(R.array.pnc_death_place_array),
        required = false,
        hasDependants = false,
    )

    private var remarks = FormElement(
        id = 15,
        inputType = InputType.EDIT_TEXT,
        title = resources.getString(R.string.pnc_remarks),
        required = true,
        hasDependants = false
    )

    suspend fun setUpPage(ben: BenRegCache?, saved: DeliveryOutcomeCache?) {
//        var list = mutableListOf(
//            dateOfDelivery,
//            timeOfDelivery,
//            placeOfDelivery,
//            typeOfDelivery,
//            hadComplications,
////            complication,
////            causeOfDeath,
////            otherCauseOfDeath,
////            otherComplication,
//            deliveryOutcome,
//            liveBirth,
//            stillBirth,
//            dateOfDischarge,
//            timeOfDischarge,
//            isJSYBenificiary
//        )
//        if (saved == null) {
//            dateOfDelivery.value = Dataset.getDateFromLong(System.currentTimeMillis())
//            dateOfDischarge.value = Dataset.getDateFromLong(System.currentTimeMillis())
//        } else {
//            list = mutableListOf(
//                dateOfDelivery,
//                timeOfDelivery,
//                placeOfDelivery,
//                typeOfDelivery,
//                hadComplications,
////                complication,
////                causeOfDeath,
////                otherCauseOfDeath,
////                otherComplication,
//                deliveryOutcome,
//                liveBirth,
//                stillBirth,
//                dateOfDischarge,
//                timeOfDischarge,
//                isJSYBenificiary
//            )
//            dateOfDelivery.value = Dataset.getDateFromLong(saved.dateOfDelivery)
//            timeOfDelivery.value = saved.timeOfDelivery
//            placeOfDelivery.value = saved.placeOfDelivery
//            typeOfDelivery.value = saved.typeOfDelivery
//            hadComplications.value = if (saved.hadComplications == true) "Yes" else "No"
//            complication.value = saved.complication
//            causeOfDeath.value = saved.causeOfDeath
//            otherCauseOfDeath.value = saved.otherCauseOfDeath
//            otherComplication.value = saved.otherComplication
//            deliveryOutcome.value = saved.deliveryOutcome.toString()
//            liveBirth.value = saved.liveBirth.toString()
//            stillBirth.value = saved.stillBirth.toString()
//            dateOfDischarge.value = Dataset.getDateFromLong(saved.dateOfDischarge)
//            timeOfDischarge.value = saved.timeOfDischarge
//            isJSYBenificiary.value = if (saved.isJSYBenificiary == true) "Yes" else "No"
//        }
//        ben?.let {
//            dateOfDelivery.min = it.regDate
//        }
//        setUpPage(list)

    }
    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
//            dateOfDelivery.id -> {
//                dateOfDischarge.min = Dataset.getLongFromDate(dateOfDelivery.value)
//                -1
//            }
//            hadComplications.id -> {
//                triggerDependants(
//                    source = hadComplications,
//                    passedIndex = index,
//                    triggerIndex = 0,
//                    target = complication,
//                    targetSideEffect = listOf(causeOfDeath, otherComplication, otherCauseOfDeath)
//                )
//            }
//            complication.id -> {
//                if(index == 6) {
//                    triggerDependants(
//                        source = complication,
//                        addItems = listOf(causeOfDeath),
//                        removeItems = listOf(otherComplication, otherCauseOfDeath)
//                    )
//                } else if(index == 7) {
//                    triggerDependants(
//                        source = complication,
//                        addItems = listOf(otherComplication),
//                        removeItems = listOf(causeOfDeath, otherCauseOfDeath)
//                    )
//                } else {
//                    triggerDependants(
//                        source = complication,
//                        addItems = listOf(),
//                        removeItems = listOf(otherComplication, otherCauseOfDeath, causeOfDeath)
//                    )
//                }
//            }
//
//            causeOfDeath.id -> {
//                triggerDependants(
//                    source = causeOfDeath,
//                    passedIndex = index,
//                    triggerIndex = 4,
//                    target = otherCauseOfDeath
//                )
//            }
//            deliveryOutcome.id -> {
//                validateIntMinMax(deliveryOutcome)
//                validateMaxDeliveryOutcome()
//            }
//            liveBirth.id -> {
//                validateIntMinMax(liveBirth)
//                validateMaxDeliveryOutcome()
//            }
//            stillBirth.id -> {
//                validateIntMinMax(stillBirth)
//                validateMaxDeliveryOutcome()
//            }
            else -> -1
        }
    }

//    private fun validateMaxDeliveryOutcome() : Int {
//        if(!liveBirth.value.isNullOrEmpty() && !stillBirth.value.isNullOrEmpty() &&
//            !deliveryOutcome.value.isNullOrEmpty() && deliveryOutcome.errorText.isNullOrEmpty()) {
//            if(deliveryOutcome.value!!.toInt() != liveBirth.value!!.toInt() + stillBirth.value!!.toInt()) {
//                deliveryOutcome.errorText = "Outcome of Delivery should equal to sum of Live and Still births"
//            }
//        }
//        return -1
//    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        (cacheModel as DeliveryOutcomeCache).let { form ->
//            form.dateOfDelivery = Dataset.getLongFromDate(dateOfDelivery.value)
//            form.timeOfDelivery = timeOfDelivery.value
//            form.placeOfDelivery = placeOfDelivery.value
//            form.typeOfDelivery = typeOfDelivery.value
//            form.hadComplications = hadComplications.value == "Yes"
//            form.complication = complication.value
//            form.causeOfDeath = causeOfDeath.value
//            form.otherCauseOfDeath = otherCauseOfDeath.value
//            form.otherComplication = otherComplication.value
//            form.deliveryOutcome = deliveryOutcome.value?.toInt()
//            form.liveBirth = liveBirth.value?.toInt()
//            form.stillBirth = stillBirth.value?.toInt()
//            form.dateOfDischarge = Dataset.getLongFromDate(dateOfDischarge.value)
//            form.timeOfDischarge = timeOfDischarge.value
//            form.isJSYBenificiary = isJSYBenificiary.value == "Yes"
        }
    }
}