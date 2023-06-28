package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.*

class SuspectedTBDataset(
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

    private val isSputumCollected = FormElement(
        id = 2,
        inputType = InputType.RADIO,
        title = "Is Sputum sample collected?",
        entries = arrayOf("Yes", "No"),
        required = true,
        hasDependants = true
    )

    private var sputumSubmittedAt = FormElement(
        id = 3,
        inputType = InputType.RADIO,
        title = "Sputum sample submitted at",
        entries = arrayOf("DMC", "SC", "PHC"),
        required = false,
        hasDependants = false
    )

    private var nikshayId = FormElement(
        id = 4,
        inputType = InputType.EDIT_TEXT,
        title = "Nikshay ID",
        required = false,
        hasDependants = false
    )

    private var sputumTestResult = FormElement(
        id = 5,
        inputType = InputType.RADIO,
        title = "Sputum Test Result",
        entries = arrayOf("Positive", "Negative"),
        required = false,
        hasDependants = false
    )

    private var referred = FormElement(
        id = 6,
        inputType = InputType.RADIO,
        title = "Referred to Facility",
        entries = arrayOf("Yes", "No"),
        required = true,
        hasDependants = false
    )

    private var followUps = FormElement(
        id = 7,
        inputType = InputType.RADIO,
        title = "Facility Referral follow-ups",
        entries = arrayOf("Yes", "No"),
        required = false,
        hasDependants = false
    )

    suspend fun setUpPage(ben: BenRegCache?, saved: TBSuspectedCache?) {
        var list = mutableListOf(
            dateOfVisit,
            isSputumCollected,
//            sputumSubmittedAt,
//            nikshayId,
//            sputumTestResult,
            referred,
            followUps
        )
        if (saved == null) {
            dateOfVisit.value = getDateFromLong(System.currentTimeMillis())
        } else {
            list = mutableListOf(
                dateOfVisit,
                isSputumCollected,
                sputumSubmittedAt,
                nikshayId,
                sputumTestResult,
                referred,
                followUps
            )
            dateOfVisit.value = getDateFromLong(saved.visitDate)
            isSputumCollected.value = if (saved.isSputumCollected == true) "Yes" else "No"
            sputumSubmittedAt.value = saved.sputumSubmittedAt
            nikshayId.value = saved.nikshayId
            sputumTestResult.value = saved.sputumTestResult
            referred.value = if (saved.referred == true) "Yes" else "No"
            followUps.value = saved.followUps
        }


        ben?.let {
            dateOfVisit.min = it.regDate
        }
        setUpPage(list)

    }
    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
            isSputumCollected.id -> {
                triggerDependants(
                    source = isSputumCollected,
                    passedIndex = index,
                    triggerIndex = 0,
                    target = sputumSubmittedAt
                )
                triggerDependants(
                    source = isSputumCollected,
                    passedIndex = index,
                    triggerIndex = 0,
                    target = sputumTestResult
                )
                triggerDependants(
                    source = isSputumCollected,
                    passedIndex = index,
                    triggerIndex = 0,
                    target = nikshayId
                )
            }
            else -> -1
        }
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        (cacheModel as TBSuspectedCache).let { form ->
            form.visitDate = getLongFromDate(dateOfVisit.value)
            form.isSputumCollected = isSputumCollected.value == "Yes"
            form.sputumSubmittedAt = sputumSubmittedAt.value
            form.nikshayId = nikshayId.value
            form.sputumTestResult = sputumTestResult.value
            form.referred = referred.value == "Yes"
            form.followUps = followUps.value
        }
    }


//    fun updateBen(benRegCache: BenRegCache) {
//        benRegCache.genDetails?.let {
//            it.reproductiveStatus =
//                englishResources.getStringArray(R.array.nbr_reproductive_status_array)[1]
//            it.reproductiveStatusId = 2
//        }
//        benRegCache.processed = "U"
//    }
//
//    fun isTbSuspected(): String? {
//        return if ( isCoughing.value == "Yes" ||
//            bloodInSputum.value == "Yes" ||
//            isFever.value == "Yes" ||
//            nightSweats.value == "Yes" ||
//            historyOfTB.value == "Yes")
//            resources.getString(R.string.tb_suspected_alert) else null
//    }
//
//    fun isTbSuspectedFamily(): String? {
//        return if (currentlyTakingDrugs.value == "Yes" || familyHistoryTB.value == "Yes")
//            resources.getString(R.string.tb_suspected_family_alert) else null
//    }
}