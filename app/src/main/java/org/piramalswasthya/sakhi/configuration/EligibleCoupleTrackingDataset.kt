package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.*

class EligibleCoupleTrackingDataset (
    context: Context, currentLanguage: Languages
    ) : Dataset(context, currentLanguage) {

    private val dateOfVisit = FormElement(
        id = 1,
        inputType = InputType.DATE_PICKER,
        title = context.getString(R.string.tracking_date),
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        hasDependants = true

    )

    private val financialYear = FormElement(
        id = 2,
        inputType = InputType.EDIT_TEXT,
        title = "Financial Year",
        required = false,
    )

    private val month = FormElement(
        id = 3,
        inputType = InputType.DROPDOWN,
        title = "Month",
        arrayId = R.array.visit_months,
        entries = resources.getStringArray(R.array.visit_months),
        required = false
    )

    private val isPregnancyTestDone = FormElement(
        id = 4,
        inputType = InputType.RADIO,
        title = "Is Pregnancy Test done?",
        entries = arrayOf("Yes", "No", "Don't Know"),
        required = true,
        hasDependants = true
    )

    private val pregnancyTestResult = FormElement(
        id = 5,
        inputType = InputType.RADIO,
        title = "Pregnancy Test Result",
        entries = arrayOf("Positive", "Negative"),
        required = true,
        hasDependants = false
    )

    private val isPregnant = FormElement(
        id = 6,
        inputType = InputType.RADIO,
        title = "Is the woman pregnant?",
        entries = arrayOf("Yes", "No", "Don't Know"),
        required = true,
        hasDependants = true
    )

    private val usingFamilyPlanning = FormElement(
        id = 7,
        inputType = InputType.RADIO,
        title = "Are you using Family Planning Method?",
        entries = arrayOf("Yes", "No"),
        required = true,
        hasDependants = true
    )

    private val methodOfContraception = FormElement(
        id = 8,
        inputType = InputType.DROPDOWN,
        title = "Method of Contraception",
        arrayId = R.array.method_of_contraception,
        entries = resources.getStringArray(R.array.method_of_contraception),
        required = true,
        hasDependants = true

    )

    private val anyOtherMethod = FormElement(
        id = 9,
        inputType = InputType.EDIT_TEXT,
        title = "Any Other Method",
        required = true,
    )


    suspend fun setUpPage(ben: BenRegCache?, saved: EligibleCoupleTrackingCache?) {
        val list = mutableListOf(
            dateOfVisit,
            financialYear,
            month,
            isPregnancyTestDone,
            isPregnant,
            usingFamilyPlanning,
        )
        dateOfVisit.value = getDateFromLong(System.currentTimeMillis())
        dateOfVisit.value?.let {
            financialYear.value = getFinancialYear(it)
            month.value = resources.getStringArray(R.array.visit_months)[Companion.getMonth(it)!!]
        }

        ben?.let {
            dateOfVisit.min = it.regDate
        }
        setUpPage(list)

    }
    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
            dateOfVisit.id -> {
                financialYear.value = Companion.getFinancialYear(dateOfVisit.value)
                month.value = resources.getStringArray(R.array.visit_months)[Companion.getMonth(dateOfVisit.value)!!]
                -1
            }
            isPregnancyTestDone.id -> {
                triggerDependants(
                    source = isPregnancyTestDone,
                    passedIndex = index,
                    triggerIndex = 0,
                    target = pregnancyTestResult
                )
            }

            usingFamilyPlanning.id -> {
                triggerDependants(
                    source = usingFamilyPlanning,
                    passedIndex = index,
                    triggerIndex = 0,
                    target = methodOfContraception,
                    targetSideEffect = listOf(anyOtherMethod)
                )
            }

            methodOfContraception.id -> {
                triggerDependants(
                    source = methodOfContraception,
                    passedIndex = index,
                    triggerIndex = methodOfContraception.entries!!.lastIndex,
                    target = anyOtherMethod
                )
            }

            else -> -1
        }
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        (cacheModel as EligibleCoupleTrackingCache).let { form ->
            form.visitDate = getLongFromDate(dateOfVisit.value)
            form.isPregnancyTestDone = isPregnancyTestDone.value
            form.pregnancyTestResult= pregnancyTestResult.value
            form.isPregnant= isPregnant.value
            form.usingFamilyPlanning = usingFamilyPlanning.value == "Yes"
            if (methodOfContraception.value == "Any Other Method") {
                form.methodOfContraception = anyOtherMethod.value
            } else {
                form.methodOfContraception = methodOfContraception.value
            }
        }
    }

    fun updateBen(benRegCache: BenRegCache) {
        benRegCache.genDetails?.let {
            it.reproductiveStatus =
                englishResources.getStringArray(R.array.nbr_reproductive_status_array)[1]
            it.reproductiveStatusId = 2
        }
        benRegCache.processed = "U"
    }
}