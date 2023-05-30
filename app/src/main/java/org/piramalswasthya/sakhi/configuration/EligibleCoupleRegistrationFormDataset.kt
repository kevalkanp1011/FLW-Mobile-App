package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EligibleCoupleRegistrationFormDataset(context: Context, language: Languages) : Dataset(context, language) {

    companion object {
        private fun getCurrentDateString(): String {
            val calendar = Calendar.getInstance()
            val mdFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }

        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
        private fun getMinDobMillis(): Long {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -15)
            cal.add(Calendar.DAY_OF_MONTH, +1)
            return cal.timeInMillis
        }

        private fun getMaxDobMillis(): Long {
            return System.currentTimeMillis()
        }
    }

    //////////////////////////////// First Page /////////////////////////////////////////

    private val dateOfReg = FormElement(
        id = 0,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "Date of Registration",
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        min = 0L,
    )

    private val rchId = FormElement(
        id = 1,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "RCH ID No. of Woman",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        isMobileNumber = true,
        etMaxLength = 12,
        max = 999999999999L,
        min = 0L
    )

    private val name = FormElement(
        id = 2,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Name of Woman",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )

    private val husbandName = FormElement(
        id = 3,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Name of Husband",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )

    private val age = FormElement(
        id = 4,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Current Age of Woman",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForGenBen.toLong(),
        min = Konstants.minAgeForGenBen.toLong(),
    )

    private val ageAtMarriage = FormElement(
        id = 5,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Age of Woman at Marriage",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForGenBen.toLong(),
        min = Konstants.minAgeForGenBen.toLong(),
    )

    private val womanDetails = FormElement(
        id = 6,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of Woman",
        arrayId = -1,
        required = false
    )

    private val aadharNo = FormElement(
        id = 7,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Aadhaar Number of Woman",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        isMobileNumber = true,
        etMaxLength = 12,
        max = 999999999999L,
        min = 100000000000L
    )

    private val bankAccount = FormElement(
        id = 8,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Bank AC No or Post Office AC No",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        isMobileNumber = true,
        etMaxLength = 18,
        max = 999999999999999999L,
        min = 0L
    )

    private val bankName = FormElement(
        id = 9,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Bank Name",
        arrayId = -1,
        required = false,
        etMaxLength = 100
    )

    private val branchName = FormElement(
        id = 10,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Branch Name",
        arrayId = -1,
        required = false,
        etMaxLength = 100
    )

    private val ifsc = FormElement(
        id = 11,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "IFSC Code",
        arrayId = -1,
        required = false,
        etMaxLength = 11,
    )

    private val noOfChildren = FormElement(
        id = 12,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Total No. of Children Born",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 9,
        min = 0,
    )

    private val noOfLiveChildren = FormElement(
        id = 13,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "No. of Live Children",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 9,
        min = 0,
    )

    private val numMale = FormElement(
        id = 14,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Total No. of Children Born",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 9,
        min = 0,
    )

    private val numFemale = FormElement(
        id = 15,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Total No. of Children Born",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 9,
        min = 0,
    )

    private val firstChildDetails = FormElement(
        id = 16,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 1st Child",
        arrayId = -1,
        required = false
    )

    private val dob1 = FormElement(
        id = 17,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "1st Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age1 = FormElement(
        id = 18,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "1st Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender1 = FormElement(
        id = 19,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "1st Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val marriageFirstChildGap = FormElement(
        id = 20,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between Date of marriage and 1st child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val secondChildDetails = FormElement(
        id = 21,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 2nd Child",
        arrayId = -1,
        required = false
    )

    private val dob2 = FormElement(
        id = 22,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "2nd Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age2 = FormElement(
        id = 23,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "2nd Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender2 = FormElement(
        id = 24,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "2nd Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val firstAndSecondChildGap = FormElement(
        id = 25,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 1st child and 2nd Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )



    private val firstPage by lazy {
        listOf(
            dateOfReg, rchId, name, husbandName, age, ageAtMarriage, womanDetails, aadharNo, bankAccount, bankName, branchName, ifsc,
            noOfChildren, noOfLiveChildren, numMale, numFemale, firstChildDetails, dob1, age1, gender1, marriageFirstChildGap,
            secondChildDetails, dob2, age2, gender2, firstAndSecondChildGap
        )
    }

    suspend fun setPage(ecr: EligibleCoupleRegCache?) {
        setUpPage(firstPage)
        ecr?.let { saved ->
            dateOfReg.value = getDateFromLong(saved.dateOfReg)
        }
    }

    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
//            firstNameHeadOfFamily.id -> {
//                validateEmptyOnEditText(firstNameHeadOfFamily)
//                validateAllCapsOrSpaceOnEditText(firstNameHeadOfFamily)
//            }
//            lastNameHeadOfFamily.id -> validateAllCapsOrSpaceOnEditText(lastNameHeadOfFamily)
//            mobileNoHeadOfFamily.id -> {
//                validateEmptyOnEditText(mobileNoHeadOfFamily)
//                validateMobileNumberOnEditText(mobileNoHeadOfFamily)
//            }
//            residentialArea.id -> triggerDependants(
//                source = residentialArea,
//                passedIndex = index,
//                triggerIndex = residentialArea.entries!!.size - 1,
//                target = otherResidentialArea
//            )
            else -> -1
        }
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        TODO("Not yet implemented")
    }

    fun mapValues(ecr: EligibleCoupleRegCache) {
        ecr.dateOfReg = getLongFromDate(dateOfReg.value!!)
        ecr.rchId = rchId.value?.toLong()
        ecr.name = name.value
        ecr.husbandName = husbandName.value
        ecr.age = age.value?.toInt()
        ecr.ageAtMarriage = ageAtMarriage.value?.toInt()
        ecr.aadharNo = aadharNo.value?.toLong()
        ecr.bankAccount = bankAccount.value?.toLong()
        ecr.bankName = bankName.value
        ecr.branchName = branchName.value
        ecr.ifsc = ifsc.value
        ecr.noOfChildren = noOfChildren.value?.toInt()
        ecr.noOfLiveChildren = noOfLiveChildren.value?.toInt()
        ecr.noOfMaleChildren = numMale.value?.toInt()
        ecr.noOfFemaleChildren = numFemale.value?.toInt()
        ecr.dob1 = getLongFromDate(dob1.value)
        ecr.age1 = age1.value?.toInt()
        ecr.gender1 = when (gender1.value) {
            gender1.entries!![0] -> Gender.MALE
            gender1.entries!![1] -> Gender.FEMALE
            else -> null
        }
        ecr.marriageFirstChildGap = marriageFirstChildGap.value?.toInt()
        ecr.dob2 = getLongFromDate(dob2.value)
        ecr.age2 = age2.value?.toInt()
        ecr.gender2 = when (gender2.value) {
            gender2.entries!![0] -> Gender.MALE
            gender2.entries!![1] -> Gender.FEMALE
            else -> null
        }
        ecr.firstAndSecondChildGap = firstAndSecondChildGap.value?.toInt()
//        (cacheModel as HouseholdCache).family = family
    }
}