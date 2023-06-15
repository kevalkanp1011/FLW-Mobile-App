package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.*
import java.text.SimpleDateFormat
import java.util.*

class EligibleCoupleRegistrationDataset(context: Context, language: Languages) : Dataset(context, language) {

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
        required = false,
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
        etMaxLength = 1,
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
        etMaxLength = 1,
        max = 9,
        min = 0,
    )

    private val numMale = FormElement(
        id = 14,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Male",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
        max = 9,
        min = 0,
    )

    private val numFemale = FormElement(
        id = 15,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Female",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
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
        etMaxLength = 2,
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
        etMaxLength = 2,
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

    private val thirdChildDetails = FormElement(
        id = 26,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 3rd Child",
        arrayId = -1,
        required = false
    )

    private val dob3 = FormElement(
        id = 27,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "3rd Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age3 = FormElement(
        id = 28,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "3rd Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender3 = FormElement(
        id = 29,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "3rd Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val SecondAndThirdChildGap = FormElement(
        id = 30,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 2nd child and 3rd Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val fourthChildDetails = FormElement(
        id = 31,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 4th Child",
        arrayId = -1,
        required = false
    )

    private val dob4 = FormElement(
        id = 32,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "4th Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age4 = FormElement(
        id = 33,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "4th Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender4 = FormElement(
        id = 34,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "4th Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val thirdAndFourthChildGap = FormElement(
        id = 35,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 3rd Child and 4th Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val fifthChildDetails = FormElement(
        id = 36,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 2nd Child",
        arrayId = -1,
        required = false
    )

    private val dob5 = FormElement(
        id = 37,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "5th Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age5 = FormElement(
        id = 38,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "5th Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender5 = FormElement(
        id = 39,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "5th Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val fourthAndFifthChildGap = FormElement(
        id = 40,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 4th child and 5th Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val sixthChildDetails = FormElement(
        id = 41,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 6th Child",
        arrayId = -1,
        required = false
    )

    private val dob6 = FormElement(
        id = 42,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "6th Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age6 = FormElement(
        id = 43,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "6th Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender6 = FormElement(
        id = 44,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "6th Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val fifthAndSixthChildGap = FormElement(
        id = 45,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 5th child and 6th Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val seventhChildDetails = FormElement(
        id = 46,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 7th Child",
        arrayId = -1,
        required = false
    )

    private val dob7 = FormElement(
        id = 47,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "7th Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age7 = FormElement(
        id = 48,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "7th Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender7 = FormElement(
        id = 49,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "7th Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val sixthAndSeventhChildGap = FormElement(
        id = 50,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 6th child and 7th Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val eighthChildDetails = FormElement(
        id = 51,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 8th Child",
        arrayId = -1,
        required = false
    )

    private val dob8 = FormElement(
        id = 52,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "8th Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age8 = FormElement(
        id = 53,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "8th Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender8 = FormElement(
        id = 54,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "8th Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val seventhAndEighthChildGap = FormElement(
        id = 55,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 7th child and 8th Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    private val ninthChildDetails = FormElement(
        id = 56,
        inputType = org.piramalswasthya.sakhi.model.InputType.HEADLINE,
        title = "Details of 9th Child",
        arrayId = -1,
        required = false
    )

    private val dob9 = FormElement(
        id = 57,
        inputType = org.piramalswasthya.sakhi.model.InputType.DATE_PICKER,
        title = "9th Child Date of Birth",
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )

    private val age9 = FormElement(
        id = 58,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "9th Child Age",
        arrayId = -1,
        required = true,
        hasDependants = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = Konstants.maxAgeForAdolescent.toLong(),
        min = 1,
    )

    private val gender9 = FormElement(
        id = 59,
        inputType = org.piramalswasthya.sakhi.model.InputType.RADIO,
        title = "9th Child Sex",
        arrayId = -1,
        entries = resources.getStringArray(R.array.ecr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val eighthAndNinthChildGap = FormElement(
        id = 60,
        inputType = org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT,
        title = "Gap between 8th child and 9th Child",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = 99,
        min = 0,
    )

    suspend fun setUpPage(ben: BenRegCache?, saved: EligibleCoupleRegCache?) {
        val list = mutableListOf(
            dateOfReg, rchId, name, husbandName, age, ageAtMarriage, womanDetails, aadharNo, bankAccount, bankName, branchName, ifsc,
            noOfChildren, noOfLiveChildren, numMale, numFemale, firstChildDetails, dob1, age1, gender1, marriageFirstChildGap,
            secondChildDetails, dob2, age2, gender2, firstAndSecondChildGap
        )
        dateOfReg.value = getDateFromLong(System.currentTimeMillis())
//        dateOfReg.value?.let {
//            val long = Dataset.getLongFromDate(it)
//            dateOfhivTestDone.min = long
//        }

        ben?.let {
            dateOfReg.min = it.regDate
            rchId.value = ben.rchId
            name.value = "${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}"
            husbandName.value = ben.genDetails?.spouseName
            age.value = BenBasicCache.getAgeFromDob(ben.dob).toString()
        }
        setUpPage(list)

    }

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
            ageAtMarriage.id -> {
                if (ageAtMarriage.value.isNullOrEmpty()) {
                    validateEmptyOnEditText(ageAtMarriage)
                    return -1
                }
                ageAtMarriage.min = 14
                ageAtMarriage.max = age.value?.toLong()
                validateIntMinMax(ageAtMarriage)

//                dob1.min = System.currentTimeMillis() -
                -1
            }
            dob1.id -> {
                assignValuesToAgeFromDob(getLongFromDate(dob1.value), age1)
                setMarriageAndFirstChildGap()
                -1
            }
            age1.id -> {

                if (age1.value.isNullOrEmpty()) {
                    validateEmptyOnEditText(age1)
                    return -1
                }
                age1.min = 1
                age1.max = 15
                validateIntMinMax(age1)
                if (age1.errorText == null) {
                    val cal = Calendar.getInstance()
                    cal.add(
                        Calendar.YEAR, -1 * age1.value!!.toInt()
                    )
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH) + 1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    val newDob =
                        "${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year"
                    if (dob1.value != newDob) {
                        dob1.value = newDob
                        dob1.errorText = null
                    }
                }
                -1
            }
            dob2.id -> {
                assignValuesToAgeFromDob(getLongFromDate(dob2.value), age2)
                setFirstChildAndSecondChildGap()
                -1
            }
            age2.id -> {

                if (age2.value.isNullOrEmpty()) {
                    validateEmptyOnEditText(age2)
                    return -1
                }
                age2.min = 1
                age2.max = 15
                validateIntMinMax(age2)
                if (age2.errorText == null) {
                    val cal = Calendar.getInstance()
                    cal.add(
                        Calendar.YEAR, -1 * age2.value!!.toInt()
                    )
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH) + 1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    val newDob =
                        "${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year"
                    if (dob2.value != newDob) {
                        dob2.value = newDob
                        dob2.errorText = null
                    }
                }
//                ageAtMarriage.value = null
//                age.value?.toLong()?.let {
//                    ageAtMarriage.min = it
//                }
                -1
            }
            else -> -1
        }
    }

    private fun setMarriageAndFirstChildGap() {
        marriageFirstChildGap.value =
            (age1.value?.let { age1 ->
                (ageAtMarriage.value?.toInt()?.let { age.value?.toInt()?.minus(it) })?.minus(
                    age1.toInt())
            }).toString() }

    private fun setFirstChildAndSecondChildGap() {
        firstAndSecondChildGap.value =
            (age2.value?.toInt()?.let { age1.value?.toInt()?.minus(it) }).toString() }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        (cacheModel as EligibleCoupleRegCache).let { ecr ->
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
        }
    }
}