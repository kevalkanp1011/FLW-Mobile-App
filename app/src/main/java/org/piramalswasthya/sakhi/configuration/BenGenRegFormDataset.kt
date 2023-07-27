package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.net.Uri
import android.text.InputType
import androidx.core.text.isDigitsOnly
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Konstants.maxAgeForGenBen
import org.piramalswasthya.sakhi.helpers.Konstants.minAgeForGenBen
import org.piramalswasthya.sakhi.helpers.Konstants.minAgeForMarriage
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.AgeUnit
import org.piramalswasthya.sakhi.model.BenBasicCache
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FormElement
import org.piramalswasthya.sakhi.model.Gender
import org.piramalswasthya.sakhi.model.InputType.DATE_PICKER
import org.piramalswasthya.sakhi.model.InputType.DROPDOWN
import org.piramalswasthya.sakhi.model.InputType.EDIT_TEXT
import org.piramalswasthya.sakhi.model.InputType.IMAGE_VIEW
import org.piramalswasthya.sakhi.model.InputType.RADIO
import org.piramalswasthya.sakhi.model.InputType.TEXT_VIEW
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BenGenRegFormDataset(context: Context, language: Languages) : Dataset(context, language) {
    companion object {

        private fun getCurrentDateString(): String {
            val calendar = Calendar.getInstance()
            val mdFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }

        private fun getMinDateOfReg(): Long {
            return Calendar.getInstance().apply {
                set(Calendar.YEAR, 2020)
                set(Calendar.MONTH, 0)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis

        }

        private fun getMinDobMillis(): Long {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -1 * maxAgeForGenBen)
            return cal.timeInMillis
        }

        private fun getMaxDobMillis(): Long {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -1 * minAgeForGenBen)
            return cal.timeInMillis
        }

    }

    private var familyHeadPhoneNo: String? = null

    //////////////////////////////////First Page////////////////////////////////////
    private val pic = FormElement(
        id = 1,
        inputType = IMAGE_VIEW,
        title = context.getString(R.string.nbr_image),
        arrayId = -1,
        required = false
    )
    private val dateOfReg = FormElement(
        id = 2,
        inputType = DATE_PICKER,
        title = context.getString(R.string.nbr_dor),
        arrayId = -1,
        required = true,
        min = getMinDateOfReg(),
        max = System.currentTimeMillis()
    )
    private val firstName = FormElement(
        id = 3,
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_nb_first_name),
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )
    private val lastName = FormElement(
        id = 4,
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_nb_last_name),
        arrayId = -1,
        required = false,
        allCaps = true,
        hasSpeechToText = true,
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
    )
    private val age = FormElement(
        id = 5,
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_age),
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 2,
        max = maxAgeForGenBen.toLong(),
        min = minAgeForGenBen.toLong(),
    )
    private val dob = FormElement(
        id = 6,
        inputType = DATE_PICKER,
        title = context.getString(R.string.nbr_dob),
        arrayId = -1,
        required = true,
        hasDependants = true,
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
    )
    private val gender = FormElement(
        id = 7,
        inputType = RADIO,
        title = context.getString(R.string.nbr_gender),
        arrayId = -1,
        entries = resources.getStringArray(R.array.nbr_gender_array),
        required = true,
        hasDependants = true,
    )

    private val maritalStatusMale = arrayOf(
        "Unmarried",
        "Married",
        "Divorced",
        "Separated",
        "Widower",
    )

    private val maritalStatusFemale = arrayOf(
        "Unmarried",
        "Married",
        "Divorced",
        "Separated",
        "Widow",
    )
    private val maritalStatus = FormElement(
        id = 8,
        inputType = DROPDOWN,
        title = "Marital Status",
        arrayId = -1,
        entries = maritalStatusMale,
        required = true,
        hasDependants = true,
    )
    private val husbandName = FormElement(
        id = 9,
        inputType = EDIT_TEXT,
        title = "Husband's Name",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,

        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )
    private val wifeName = FormElement(
        id = 10,
        inputType = EDIT_TEXT,
        title = "Wife's Name",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,

        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )
    private val spouseName = FormElement(
        id = 11,
        inputType = EDIT_TEXT,
        title = "Spouse's Name",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,

        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )
    private val ageAtMarriage = FormElement(
        id = 12,
        inputType = EDIT_TEXT,
        title = "Age At Marriage",
        etMaxLength = 2,
        arrayId = -1,
        required = true,
        hasDependants = true,

        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        min = minAgeForMarriage.toLong(),
        max = maxAgeForGenBen.toLong()
    )
    private val dateOfMarriage = FormElement(
        id = 13,
        inputType = DATE_PICKER,
        title = "Date of Marriage",
        arrayId = -1,
        required = true,
        max = System.currentTimeMillis(),
        min = getMinDobMillis(),
    )
    private val fatherName = FormElement(
        id = 14,
        inputType = EDIT_TEXT,
        title = "Father's Name",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,

        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )
    private val motherName = FormElement(
        id = 15,
        inputType = EDIT_TEXT,
        title = "Mother's Name",
        arrayId = -1,
        required = true,
        allCaps = true,
        hasSpeechToText = true,

        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )

    private val mobileNoOfRelation = FormElement(
        id = 16,
        inputType = DROPDOWN,
        title = "Mobile Number Of",
        arrayId = -1,
        entries = arrayOf(
            "Self", "Husband", "Mother", "Father", "Family Head", "Other"
        ),
        required = true,
        hasDependants = true,
    )
    private val otherMobileNoOfRelation = FormElement(
        id = 17,
        inputType = EDIT_TEXT,
        title = "Other - Mobile Number of",
        arrayId = -1,
        required = true
    )
    private val contactNumber = FormElement(
        id = 18,
        inputType = EDIT_TEXT,
        title = "Contact Number",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        isMobileNumber = true,
        etMaxLength = 10,
        max = 9999999999,
        min = 6000000000
    )

    private val contactNumberFamilyHead = FormElement(
        id = 114,
        inputType = TEXT_VIEW,
        title = context.getString(R.string.nrb_contact_number),
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    )


    private val relationToHeadListDefault = arrayOf(
        "Mother",
        "Father",
        "Brother",
        "Sister",
        "Wife",
        "Husband",
        "Nephew",
        "Niece",
        "Son",
        "Daughter",
        "Grand Father",
        "Grand Mother",
        "Father in Law",
        "Mother in Law",
        "Grand Son",
        "Grand Daughter",
        "Son in Law",
        "Daughter in Law",
        "Self",
        "Other"
    )
    private val relationToHeadListMale = arrayOf(
        "Father",
        "Brother",
        "Husband",
        "Nephew",
        "Son",
        "Grand Father",
        "Father in Law",
        "Grand Son",
        "Son in Law",
        "Self",
        "Other",
    )
    private val relationToHeadListFemale = arrayOf(
        "Mother",
        "Sister",
        "Wife",
        "Niece",
        "Daughter",
        "Grand Mother",
        "Mother in Law",
        "Grand Daughter",
        "Daughter in Law",
        "Self",
        "Other"
    )
    private val relationToHead = FormElement(
        id = 19,
        inputType = DROPDOWN,
        title = "Relation with family head",
        arrayId = -1,
        entries = relationToHeadListDefault,
        required = true,
        hasDependants = true,
    )
    private val otherRelationToHead = FormElement(
        id = 20,
        inputType = EDIT_TEXT,
        title = "Other - Enter relation to head",
        arrayId = -1,
        required = true,
        allCaps = true,
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    )
    private val community = FormElement(
        id = 21, inputType = DROPDOWN, title = "Community", arrayId = -1, entries = arrayOf(
            "General", "SC", "ST", "EBC", "OBC", "Not given"
        ), required = true
    )
    private val     religion = FormElement(
        id = 22, inputType = DROPDOWN, title = "Religion", arrayId = -1, entries = arrayOf(
            "Hindu",
            "Muslim",
            "Christian",
            "Sikh",
            "Buddhism",
            "Jainism",
            "Other",
            "Parsi",
            "Not Disclosed"
        ), required = true, hasDependants = true
    )
    private val otherReligion = FormElement(
        id = 23,
        inputType = EDIT_TEXT,
        title = "Other - Enter Religion",
        arrayId = -1,
        required = true
    )

    private val firstPage: List<FormElement> by lazy {
        listOf(
            pic,
            dateOfReg,
            firstName,
            lastName,
            dob,
            age,
            gender,
            maritalStatus,
            fatherName,
            motherName,
            relationToHead,
            mobileNoOfRelation,
            community,
            religion,
        )
    }

    //////////////////////////////////Second Page////////////////////////////////////

    private val hasAadharNo = FormElement(
        id = 24,
        inputType = RADIO,
        title = "Has Aadhar Number",
        arrayId = -1,
        entries = arrayOf("Yes", "No"),
        required = false,
        hasDependants = true
    )

    private val aadharNo = FormElement(
        id = 25,
        inputType = EDIT_TEXT,
        title = "Enter Aadhar Number",
        arrayId = -1,
        required = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        isMobileNumber = true,
        etMaxLength = 12,
        max = 999999999999L,
        min = 100000000000L
    )

    private val rchId = FormElement(
        id = 26,
        inputType = EDIT_TEXT,
        title = "RCH ID",
        arrayId = -1,
        required = false,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        isMobileNumber = true,
        etMaxLength = 12

    )

    private val secondPage = listOf(
        hasAadharNo, rchId
    )


    //////////////////////////////////Third(if any) Page////////////////////////////////////

//    private val lastMenstrualPeriod = FormElement(id = 27,
//        inputType = DATE_PICKER,
//        title = "Last Menstrual Period",
//        arrayId = -1,
//        required = false,
//        hasDependants = true,
//        max = System.currentTimeMillis(),
//        min = age.value?.let { getLongFromDate(it) } ?: 0L
//
//    )

    private val reproductiveStatus = FormElement(
        id = 28,
        inputType = DROPDOWN,
        title = "Reproductive Status",
        arrayId = -1,
        entries = resources.getStringArray(R.array.nbr_reproductive_status_array),
        required = true,
        hasDependants = true
    )

    private val otherReproductiveStatus = FormElement(
        id = 29,
        inputType = EDIT_TEXT,
        title = "Reproductive Status Other",
        arrayId = -1,
        required = true,
        etMaxLength = 100
    )

//    private val nishchayKitDeliveryStatus = FormElement(
//        id = 30,
//        inputType = RADIO,
//        title = "Nishchay Kit Delivery Status",
//        arrayId = -1,
//        entries = arrayOf("Delivered", "Not Delivered"),
//        required = true,
//        hasDependants = true,
//
//        )
//
//    private val pregnancyTestResult = FormElement(
//        id = 31,
//        inputType = RADIO,
//        title = "Pregnancy Test Result",
//        arrayId = -1,
//        entries = arrayOf("Pregnant", "Not Pregnant", "Pending"),
//        required = true,
//    )
//
//    private val expectedDateOfDelivery = FormElement(
//        id = 32,
//        inputType = TEXT_VIEW,
//        title = "Expected Date Of Delivery",
//        arrayId = -1,
//        required = true
//    )
//
//
//    private val numPrevLiveBirthOrPregnancy = FormElement(
//        id = 33,
//        inputType = EDIT_TEXT,
//        title = "No. of Previous Live Birth / Pregnancy",
//        arrayId = -1,
//        required = true,
//        hasDependants = true,
//        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
//        etMaxLength = 2,
//        max = 20,
//        min = 0
//    )
//
//    private val lastDeliveryConducted = FormElement(
//        id = 34,
//        inputType = DROPDOWN,
//        title = "Last Delivery Conducted",
//        arrayId = -1,
//        entries = arrayOf(
//            "Home",
//            "PHC",
//            "HWC",
//            "CHC",
//            "District Hospital",
//            "Medical college Hospital",
//            "Other",
//        ),
//        required = true,
//        hasDependants = true
//    )
//    private val facility = FormElement(
//        id = 35, inputType = EDIT_TEXT, title = "Facility Name", arrayId = -1, required = true
//    )
//    private val otherPlaceOfDelivery = FormElement(
//        id = 36,
//        inputType = EDIT_TEXT,
//        title = " Enter the Place of last delivery conducted",
//        arrayId = -1,
//        required = true
//    )
//    private val whoConductedDelivery = FormElement(
//        id = 37,
//        inputType = DROPDOWN,
//        title = "Who Conducted Delivery",
//        arrayId = -1,
//        entries = arrayOf(
//            "ANM",
//            "LHV",
//            "Doctor",
//            "Staff Nurse",
//            "Relative",
//            "TBA(Non-Skilled Birth Attendant)",
//            "Other",
//        ),
//        required = true,
//        hasDependants = true
//    )
//    private val otherWhoConductedDelivery = FormElement(
//        id = 38,
//        inputType = EDIT_TEXT,
//        title = "Other - Enter who Conducted Delivery",
//        arrayId = -1,
//        required = true
//    )
//
//    private val dateOfDelivery = FormElement(id = 39,
//        inputType = DATE_PICKER,
//        title = "Date Of Delivery",
//        arrayId = -1,
//        required = true,
//        max = System.currentTimeMillis(),
//        min = age.value?.let { getLongFromDate(it) } ?: 0L)


    private val thirdPage = listOf(
        reproductiveStatus,
//        lastMenstrualPeriod,

    )

    suspend fun setFirstPage(ben: BenRegCache?, familyHeadPhoneNo: Long?) {
        val list = firstPage.toMutableList()
        contactNumberFamilyHead.value = familyHeadPhoneNo?.toString()
        this.familyHeadPhoneNo = familyHeadPhoneNo?.toString()
        dateOfReg.value = getCurrentDateString()
        ben?.takeIf { !it.isDraft }?.let { saved ->
            pic.value = saved.userImage
            dateOfReg.value = getDateFromLong(saved.regDate)
            firstName.value = saved.firstName
            lastName.value = saved.lastName
            dob.value = getDateFromLong(saved.dob)
            age.value = BenBasicCache.getAgeFromDob(saved.dob).toString()
            gender.value = gender.getStringFromPosition(saved.genderId)
            fatherName.value = saved.fatherName
            motherName.value = saved.motherName
            saved.genDetails?.spouseName?.let {
                when (saved.genderId) {
                    1 -> wifeName.value = it
                    2 -> husbandName.value = it
                    3 -> spouseName.value = it
                }
            }
            maritalStatus.value =
                maritalStatus.getStringFromPosition(saved.genDetails?.maritalStatusId ?: 0)
            ageAtMarriage.value = saved.genDetails?.ageAtMarriage.toString()
            dateOfMarriage.value = getDateFromLong(
                saved.genDetails?.marriageDate ?: 0
            )
            mobileNoOfRelation.value =
                mobileNoOfRelation.getStringFromPosition(saved.mobileNoOfRelationId)
            otherMobileNoOfRelation.value = saved.mobileOthers
            contactNumber.value = saved.contactNumber.toString()
            relationToHead.value = relationToHeadListDefault[saved.familyHeadRelationPosition - 1]
            otherRelationToHead.value = saved.familyHeadRelationOther
            community.value = community.getStringFromPosition(saved.communityId)
            religion.value = religion.getStringFromPosition(saved.religionId)
            otherReligion.value = saved.religionOthers

        }
        /// Set up fields
        if (maritalStatus.value != maritalStatus.entries!![0] && gender.value != null) {
//            if(maritalStatus.value ==maritalStatus.entries!![1])
//            list.removeAll(listOf(fatherName, motherName))
            fatherName.required = false
            motherName.required = false
            list.add(
                list.indexOf(maritalStatus) + 3, when (gender.value) {
                    gender.entries!![0] -> wifeName
                    gender.entries!![1] -> husbandName
                    gender.entries!![2] -> spouseName
                    else -> throw java.lang.IllegalStateException("Gender unspecified with non empty marital status value!")
                }
            )
            list.add(list.indexOf(maritalStatus) + 4, ageAtMarriage)
        }
        ageAtMarriage.value?.takeIf { it.isNotEmpty() && it == age.value }?.let {
            list.add(list.indexOf(ageAtMarriage) + 1, dateOfMarriage)
        }

        if (mobileNoOfRelation.value == mobileNoOfRelation.entries!!.last()) {
            list.add(list.indexOf(mobileNoOfRelation) + 1, otherMobileNoOfRelation)
        }
        if (mobileNoOfRelation.value == mobileNoOfRelation.entries!![4]) {
            list.add(list.indexOf(mobileNoOfRelation) + 1, contactNumberFamilyHead)
        } else
            list.add(list.indexOf(community), contactNumber)
        if (relationToHead.value == relationToHead.entries!!.last()) {
            list.add(list.indexOf(relationToHead) + 1, otherRelationToHead)
        }
        if (religion.value == religion.entries!![6]) {
            list.add(list.indexOf(religion) + 1, otherReligion)
        }

        setUpPage(list)
    }

    fun getIndexOfRelationToHead() = getIndexOfElement(relationToHead)
    fun getIndexOfAgeAtMarriage() = getIndexOfElement(ageAtMarriage)

    fun getIndexOfFatherName() = getIndexById(fatherName.id)
    fun getIndexOfMotherName() = getIndexById(motherName.id)
    fun getIndexOfSpouseName() = getIndexById(husbandName.id).takeIf { it != -1 }
        ?: getIndexById(wifeName.id).takeIf { it != -1 } ?: getIndexById(spouseName.id)

    fun getIndexOfMaritalStatus() = getIndexById(maritalStatus.id)

//    fun getIndexOfExpectedDateOfDelivery() = getIndexOfElement(expectedDateOfDelivery)


    /*
        fun loadFirstPageOnViewModel(): List<FormElement> {
            val viewList = mutableListOf(
                pic,
                dateOfReg,
                firstName,
                lastName,
                dob,
                age,
                gender,
                maritalStatus,
                fatherName,
                motherName,
                relationToHead,
                mobileNoOfRelation,
                contactNumber,
                community,
                religion,
            )
            ben?.let { benCache ->
                dateOfReg.value = getDateFromLong(benCache.regDate)
                firstName.value = benCache.firstName
                lastName.value = benCache.lastName
                age.value = benCache.age.toString()
                dob.value = getDateFromLong(benCache.dob)
                gender.value =
                    context.resources.getStringArray(R.array.nbr_gender_array)[benCache.genderId - 1]
                benCache.genDetails?.maritalStatusId?.takeIf { it > 0 }?.let {

                    maritalStatus.value = when (benCache.genderId) {
                        2 -> maritalStatusFemale[it - 1]
                        else -> maritalStatusMale[it - 1]
                    }
                }

                fatherName.value = benCache.fatherName
                motherName.value = benCache.motherName
                mobileNoOfRelation.value =
                    mobileNoOfRelation.entries?.get(benCache.mobileNoOfRelationId - 1)
                otherMobileNoOfRelation.value = benCache.mobileOthers
                contactNumber.value = benCache.contactNumber.toString()
                relationToHead.value =
                    relationToHeadListDefault[benCache.familyHeadRelationPosition - 1]
                community.value = community.entries?.get(benCache.communityId - 1)
                religion.value = religion.entries?.get(benCache.religionId - 1)
                otherReligion.value = benCache.religionOthers

                if (benCache.genDetails?.maritalStatusId != 1) {
                    when (benCache.gender) {
                        MALE -> {
                            wifeName.value = benCache.genDetails?.spouseName
                            viewList.add(viewList.indexOf(maritalStatus) + 1, wifeName)
                        }
                        FEMALE -> {
                            husbandName.value = benCache.genDetails?.spouseName
                            viewList.add(viewList.indexOf(maritalStatus) + 1, husbandName)
                        }
                        TRANSGENDER -> {
                            spouseName.value = benCache.genDetails?.spouseName
                            viewList.add(viewList.indexOf(maritalStatus) + 1, spouseName)
                        }
                        null -> {}
                    }
                }

                benCache.genDetails?.ageAtMarriage?.takeIf { it >= 12 }?.let {
                    ageAtMarriage.value = it.toString()
                    viewList.add(viewList.indexOf(maritalStatus) + 2, ageAtMarriage)
                }
                benCache.genDetails?.dateOfMarriage?.takeIf { it > 0L }?.let {
                    dateOfMarriage.value = getDateFromLong(it)
                    viewList.add(viewList.indexOf(ageAtMarriage) + 1, dateOfMarriage)
                }
                otherRelationToHead.value?.let {
                    viewList.add(
                        viewList.indexOf(relationToHead) + 1,
                        otherRelationToHead
                    )
                }
                otherMobileNoOfRelation.value?.let {
                    viewList.add(
                        viewList.indexOf(
                            mobileNoOfRelation
                        ) + 1, otherMobileNoOfRelation
                    )
                }
                otherReligion.value?.let {
                    viewList.add(
                        viewList.indexOf(religion) + 1,
                        otherReligion
                    )
                }

            }

            return viewList
        }
    */
    suspend fun setSecondPage(ben: BenRegCache?) {
        val list = secondPage.toMutableList()
        ben?.takeIf { !it.isDraft }?.let { saved ->
            hasAadharNo.value = hasAadharNo.getStringFromPosition(saved.hasAadharId)
            aadharNo.value = saved.aadharNum
            rchId.value = saved.rchId
        }
        if (hasAadharNo.value == hasAadharNo.entries!!.first()) list.add(
            list.indexOf(hasAadharNo) + 1, aadharNo
        )
        setUpPage(list)
    }

    /*    fun loadSecondPageOnViewModel(): List<FormElement> {
            val viewList = mutableListOf(
                hasAadharNo,
                rchId
            )
            ben?.let { benCache ->

                hasAadharNo.value = if (benCache.hasAadhar == true) "Yes" else "No"
                rchId.value = benCache.rchId
                benCache.hasAadhar?.takeIf { it }?.run {
                    aadharNo.value = benCache.aadharNum
                    viewList.add(1, aadharNo)
                }
            }

            return viewList
        }*/

    suspend fun setThirdPage(ben: BenRegCache?) {
        val list = thirdPage.toMutableList()
        ben?.takeIf { !it.isDraft }?.let { saved ->
//            lastMenstrualPeriod.value = saved.genDetails?.lastMenstrualPeriod?.let {
//                getDateFromLong(
//                    it
//                )
//            }
            reproductiveStatus.value = reproductiveStatus.getStringFromPosition(
                saved.genDetails?.reproductiveStatusId ?: 0
            )
            otherReproductiveStatus.value = saved.genDetails?.reproductiveStatus
//            nishchayKitDeliveryStatus.value =
//                nishchayKitDeliveryStatus.getStringFromPosition(saved.nishchayDeliveryStatusPosition)
//            pregnancyTestResult.value =
//                pregnancyTestResult.getStringFromPosition(saved.nishchayPregnancyStatusPosition)
//            expectedDateOfDelivery.value = saved.genDetails?.expectedDateOfDelivery?.let {
//                getDateFromLong(
//                    it
//                )
//            }
//            dateOfDelivery.value = saved.genDetails?.deliveryDate
//            numPrevLiveBirthOrPregnancy.value = saved.genDetails?.numPreviousLiveBirth?.toString()
//            lastDeliveryConducted.value = saved.genDetails?.lastDeliveryConductedId?.let {
//                lastDeliveryConducted.getStringFromPosition(
//                    it
//                )
//            }
//            facility.value = saved.genDetails?.facilityName
//            otherPlaceOfDelivery.value = saved.genDetails?.otherLastDeliveryConducted
//            whoConductedDelivery.value = saved.genDetails?.whoConductedDeliveryId?.let {
//                whoConductedDelivery.getStringFromPosition(
//                    it
//                )
//            }
//            otherWhoConductedDelivery.value = saved.genDetails?.otherWhoConductedDelivery
        }
        when (reproductiveStatus.value) {
//            reproductiveStatus.entries!![1]/*, reproductiveStatus.entries!![2] */-> {
//                lastMenstrualPeriod.required = true
//                list.addAll(listOf(expectedDateOfDelivery, numPrevLiveBirthOrPregnancy))
//            }
//
//            reproductiveStatus.entries!![3] -> {
//                list.remove(lastMenstrualPeriod)
//                list.add(dateOfDelivery)
//            }

            reproductiveStatus.entries?.last() -> list.add(otherReproductiveStatus)
            else -> {}
        }
//        if (nishchayKitDeliveryStatus.value?.isNotEmpty() == true) {
//            list.add(nishchayKitDeliveryStatus)
//        }
//        if (pregnancyTestResult.value?.isNotEmpty() == true) {
//            list.add(pregnancyTestResult)
//        }
//        if (numPrevLiveBirthOrPregnancy.value?.isDigitsOnly() == true) {
//            if (numPrevLiveBirthOrPregnancy.value!!.toInt() > 0) list.addAll(
//                listOf(
//                    lastDeliveryConducted, whoConductedDelivery
//                )
//            )
//        }
//        if (lastDeliveryConducted.value in lastDeliveryConducted.entries!!.sliceArray(
//                IntRange(
//                    0, 3
//                )
//            )
//        ) {
//            list.add(facility)
//        } else if (lastDeliveryConducted.value in lastDeliveryConducted.entries!!.sliceArray(
//                IntRange(
//                    4, 5
//                )
//            )
//        ) {
//            list.add(otherPlaceOfDelivery)
//        }
//        if (whoConductedDelivery.value == whoConductedDelivery.entries!!.last()) {
//            list.add(otherWhoConductedDelivery)
//        }
        setUpPage(list)
    }


    /*    fun loadThirdPageOnViewModel(): List<FormElement> {
            val viewList = mutableListOf(
                reproductiveStatus
            )
            ben?.let { benCache ->
                benCache.genDetails?.reproductiveStatusId?.takeIf { it > 0 }?.let {
                    reproductiveStatus.value = reproductiveStatus.entries?.get(it - 1)
                }
                when (benCache.genDetails?.reproductiveStatusId) {
                    1 -> {
                        lastMenstrualPeriod.value =
                            benCache.genDetails?.lastMenstrualPeriod?.let { getDateFromLong(it) }
                        benCache.nishchayDeliveryStatusPosition.takeIf { it > 0 }?.let {
                            nishchayKitDeliveryStatus.value = nishchayKitDeliveryStatus.entries?.get(it)
                        }
                        benCache.nishchayPregnancyStatusPosition.takeIf { it > 0 }?.let {
                            pregnancyTestResult.value = pregnancyTestResult.entries?.get(it)
                        }
                        viewList.addAll(
                            listOf(
                                lastMenstrualPeriod, nishchayKitDeliveryStatus, pregnancyTestResult
                            )
                        )
                    }
                    2, 3 -> {
                        lastMenstrualPeriod.value =
                            benCache.genDetails?.lastMenstrualPeriod?.let { getDateFromLong(it) }
                        expectedDateOfDelivery.value =
                            benCache.genDetails?.expectedDateOfDelivery?.let { getDateFromLong(it) }
                        numPrevLiveBirthOrPregnancy.value =
                            benCache.genDetails?.numPreviousLiveBirth?.toString()
                        lastDeliveryConducted.value = benCache.genDetails?.lastDeliveryConducted
                        facility.value = benCache.genDetails?.facilityName
                        otherPlaceOfDelivery.value = benCache.genDetails?.otherLastDeliveryConducted
                        whoConductedDelivery.value = benCache.genDetails?.whoConductedDelivery
                        otherWhoConductedDelivery.value = benCache.genDetails?.otherWhoConductedDelivery
                        viewList.addAll(
                            listOf(
                                lastMenstrualPeriod,
                                expectedDateOfDelivery,
                                numPrevLiveBirthOrPregnancy,
                                lastDeliveryConducted,
                                facility,
                                otherPlaceOfDelivery,
                                whoConductedDelivery,
                                otherWhoConductedDelivery

                            )
                        )
                    }
                    4 -> {
                        dateOfDelivery.value = benCache.genDetails?.deliveryDate
                        viewList.addAll(
                            listOf(
                                dateOfDelivery
                            )
                        )
                    }
                    5, 6 -> {
                        lastMenstrualPeriod.value =
                            benCache.genDetails?.lastMenstrualPeriod?.let { getDateFromLong(it) }
                        viewList.addAll(
                            listOf(
                                lastMenstrualPeriod
                            )
                        )
                    }
                    else -> {
                        otherReproductiveStatus.value = benCache.genDetails?.reproductiveStatus
                        viewList.addAll(
                            listOf(
                                otherReproductiveStatus
                            )
                        )
                    }
                }
            }

            return viewList
        }*/


//    suspend fun getBenForFirstPage(userId: Int, hhId: Long): BenRegCache {
//        if (ben == null) {
//            ben = BenRegCache(
//                ashaId = userId,
//                beneficiaryId = -1L,
//                isKid = false,
//                isAdult = true,
//                householdId = hhId,
//                isDraft = true,
//                genDetails = BenRegGen(),
//                syncState = SyncState.UNSYNCED
//            )
//        }
//        ben?.apply {
//            userImageBlob = ImageUtils.getByteArrayFromImageUri(context, pic.value!!)
//            Timber.d("BenGenReg: $userImageBlob, ${pic.value}")
//            regDate = getLongFromDate(dateOfReg.value!!)
//            firstName = firstName.value
//            lastName = lastName.value
//            dob = getLongFromDate(dob.value!!)
//            age = age.value?.toInt() ?: 0
//            ageUnit = AgeUnit.YEARS
//            ageUnitId = 3
//            gender = when (gender.value) {
//                "Male" -> MALE
//                "Female" -> FEMALE
//                "Transgender" -> TRANSGENDER
//                else -> null
//            }
//            genderId = when (gender.value) {
//                "Male" -> 1
//                "Female" -> 2
//                "Transgender" -> 3
//                else -> 0
//            }
//            this.registrationType = TypeOfList.GENERAL
//            genDetails?.maritalStatus = maritalStatus.value
//            genDetails?.maritalStatusId =
//                (maritalStatus.entries?.indexOf(genDetails?.maritalStatus!!))?.let { it + 1 }
//                    ?: 0
//            genDetails?.spouseName = husbandName.value
//                ?: wifeName.value
//                        ?: spouseName.value
//            genDetails?.ageAtMarriage =
//                ageAtMarriage.value?.toInt() ?: 0
//            genDetails?.marriageDate =
//                dateOfMarriage.value?.let { getLongFromDate(it) }
//                    ?: getDoMFromDoR(genDetails?.ageAtMarriage, regDate!!)
//            fatherName = fatherName.value
//            motherName = motherName.value
//            familyHeadRelation = relationToHead.value
//            familyHeadRelationPosition =
//                relationToHeadListDefault.indexOf(familyHeadRelation) + 1
//            familyHeadRelationOther = otherRelationToHead.value
//            mobileNoOfRelation = mobileNoOfRelation.value
//            mobileNoOfRelationId =
//                (mobileNoOfRelation.entries?.indexOf(mobileNoOfRelation!!))?.let { it + 1 }
//                    ?: 0
//            mobileOthers = otherMobileNoOfRelation.value
//            contactNumber = stringToLong(contactNumber.value!!)
//            community = community.value
//            communityId =
//                (community.entries?.indexOf(community!!))?.let { it + 1 }
//                    ?: 0
//            religion = religion.value
//            religionId =
//                (religion.entries?.indexOf(religion!!))?.let { it + 1 } ?: 0
//            religionOthers = otherReligion.value
//
//            rchId = rchId.value
//        }
//        return ben!!
//
//    }
//

    private fun getDoMFromDoR(ageAtMarriage: Int?, regDate: Long): Long? {
        if (ageAtMarriage == null) return null
        val cal = Calendar.getInstance()
        cal.timeInMillis = regDate
        cal.add(Calendar.YEAR, -1 * ageAtMarriage)
        return cal.timeInMillis

    }


//    suspend fun getBenForSecondPage(userId: Int, hhId: Long): BenRegCache {
//        getBenForFirstPage(userId, hhId)
//
//        ben?.apply {
//            this.hasAadhar = when (hasAadharNo.value) {
//                "Yes" -> true
//                "No" -> false
//                else -> null
//            }
//            this.hasAadharId = when (this.hasAadhar) {
//                true -> 1
//                false -> 2
//                else -> 0
//            }
//            this.aadharNum = aadharNo.value
//            this.rchId = rchId.value
//        }
//        return ben!!
//
//    }

//    suspend fun getBenForThirdPage(userId: Int, hhId: Long): BenRegCache {
//        getBenForSecondPage(userId, hhId)
//        ben?.apply {
//            this.genDetails?.apply {
//                reproductiveStatus = reproductiveStatus.value
//                reproductiveStatusId =
//                    (reproductiveStatus.entries?.indexOf(reproductiveStatus))?.let { it + 1 }
//                        ?: 0
//                lastMenstrualPeriod =
//                    lastMenstrualPeriod.value?.let {
//                        getLongFromDate(
//                            it
//                        )
//                    }
//                nishchayDeliveryStatus =
//                    nishchayKitDeliveryStatus.value
//                nishchayDeliveryStatusPosition =
//                    (nishchayKitDeliveryStatus.entries?.indexOf(
//                        nishchayDeliveryStatus
//                    ))?.let { it + 1 } ?: 0
//                nishchayPregnancyStatus = pregnancyTestResult.value
//                nishchayPregnancyStatusPosition =
//                    (pregnancyTestResult.entries?.indexOf(
//                        nishchayPregnancyStatus
//                    ))?.let { it + 1 } ?: 0
//                expectedDateOfDelivery =
//                    expectedDateOfDelivery.value?.let {
//                        getLongFromDate(
//                            it
//                        )
//                    }
//                numPreviousLiveBirth =
//                    numPrevLiveBirthOrPregnancy.value?.toInt() ?: 0
//                lastDeliveryConducted = lastDeliveryConducted.value
//                lastDeliveryConductedId =
//                    (lastDeliveryConducted.entries?.indexOf(
//                        lastDeliveryConducted
//                    )) ?: 0
//                otherLastDeliveryConducted =
//                    otherPlaceOfDelivery.value
//                facilityName = facility.value
//                whoConductedDelivery = whoConductedDelivery.value
//                whoConductedDeliveryId =
//                    (whoConductedDelivery.entries?.indexOf(
//                        whoConductedDelivery
//                    ))?.let { it + 1 } ?: 0
//                otherWhoConductedDelivery =
//                    otherWhoConductedDelivery.value
//                registrationType = when (reproductiveStatus) {
//                    "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
//                    "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
//                    "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
//                    "Postnatal Mother-Lactating Mother" -> TypeOfList.POSTNATAL_MOTHER
//                    "Menopause Stage" -> TypeOfList.MENOPAUSE
//                    "Teenager" -> TypeOfList.TEENAGER
//                    else -> TypeOfList.OTHER
//                }
//
//
//            }
//        }
//        return ben!!
//    }

//    suspend fun setPic() {
//        pic.value = ben?.userImageBlob?.let {
//            ImageUtils.getUriFromByteArray(
//                context,
//                ben!!.beneficiaryId,
//                it
//            ).toString()
//        }
//    }

    fun hasThirdPage(): Boolean {
        return gender.value == gender.entries!![1] && maritalStatus.value != maritalStatus.entries!!.first()

    }

    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
            firstName.id -> {
                validateEmptyOnEditText(firstName)
                validateAllCapsOrSpaceOnEditText(firstName)
            }

            lastName.id -> {
                validateAllCapsOrSpaceOnEditText(lastName)
            }

            dob.id -> {
                assignValuesToAgeFromDob(getLongFromDate(dob.value), age)
                age.value?.takeIf { it.isNotEmpty() }?.toLong()?.let { ageAtMarriage.max = it }
                -1
            }

            age.id -> {

                if (age.value.isNullOrEmpty()) {
                    validateEmptyOnEditText(age)
                    return -1
                }
                age.min = 15
                age.max = 99
                validateIntMinMax(age)
                if (age.errorText == null) {
                    val cal = Calendar.getInstance()
                    cal.add(
                        Calendar.YEAR, -1 * age.value!!.toInt()
                    )
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH) + 1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    val newDob =
                        "${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year"
                    if (dob.value != newDob) {
                        dob.value = newDob
                        dob.errorText = null
                    }
                }
                ageAtMarriage.value = null
                age.value?.toLong()?.let {
                    ageAtMarriage.max = it
                }
                triggerDependants(
                    source = age, passedIndex = index, triggerIndex = 0, target = dateOfMarriage
                )
            }

            gender.id, maritalStatus.id -> {
                relationToHead.value = null
                if (formId == gender.id) {
                    maritalStatus.entries = when (index) {
                        1 -> maritalStatusFemale
                        else -> maritalStatusMale
                    }
                    relationToHead.entries = when (index) {
                        0 -> relationToHeadListMale
                        1 -> relationToHeadListFemale
                        else -> relationToHeadListDefault
                    }
                    maritalStatus.value = null
                    return triggerDependants(
                        source = gender,
                        removeItems = listOf(otherRelationToHead),
                        addItems = emptyList()
                    )
                } else {
                    when (maritalStatus.value) {
                        maritalStatus.entries!![0] -> {
                            fatherName.required = true
                            motherName.required = true
                            return triggerDependants(
                                source = maritalStatus,
                                addItems = emptyList(),//listOf(fatherName, motherName),
                                removeItems = listOf(
                                    spouseName,
                                    husbandName,
                                    wifeName,
                                    ageAtMarriage
                                )
                            )
                        }

                        maritalStatus.entries!![1] -> {
                            if (gender.value == gender.entries!![1]) {
                                fatherName.required = false
                                motherName.required = false
                            } else {
                                fatherName.required = true
                                motherName.required = true
                            }
                            husbandName.required = true
                            wifeName.required = true
                            return triggerDependants(
                                source = maritalStatus, addItems = when (gender.value) {
                                    gender.entries!![0] -> listOf(wifeName, ageAtMarriage)
                                    gender.entries!![1] -> listOf(husbandName, ageAtMarriage)
                                    else -> listOf(spouseName, ageAtMarriage)
                                }, removeItems = listOf(
                                    wifeName,
                                    husbandName,
                                    spouseName,
                                    ageAtMarriage
                                )
                            )
                        }

                        else -> {
                            husbandName.required = maritalStatus.value != maritalStatus.entries!![2]
                            wifeName.required = maritalStatus.value != maritalStatus.entries!![2]
                            fatherName.required = true
                            motherName.required = true
//                            ().let {
////                            wifeName.required = it
////                            husbandName.required = it
//                                spouseName.required = it
//                            }
                            return triggerDependants(
                                source = maritalStatus, addItems = when (gender.value) {
                                    gender.entries!![0] -> listOf(wifeName, ageAtMarriage)
                                    gender.entries!![1] -> listOf(husbandName, ageAtMarriage)
                                    else -> listOf(spouseName, ageAtMarriage)
                                }, removeItems = listOf(
                                    wifeName,
                                    husbandName,
                                    spouseName,
                                    ageAtMarriage
                                )
                            )
                        }
                    }

                }

            }

            ageAtMarriage.id -> age.value?.takeIf { it.isNotEmpty() && it.isDigitsOnly() && !ageAtMarriage.value.isNullOrEmpty() }
                ?.toInt()?.let {
                    validateEmptyOnEditText(ageAtMarriage)
                    validateIntMinMax(ageAtMarriage)
                    if (it == ageAtMarriage.value?.toInt()) {
                        val cal = Calendar.getInstance()
                        dateOfMarriage.max = cal.timeInMillis
                        cal.add(Calendar.YEAR, -1)
                        dateOfMarriage.min = cal.timeInMillis

                    }
                    triggerDependants(
                        source = ageAtMarriage,
                        passedIndex = ageAtMarriage.value!!.toInt(),
                        triggerIndex = it,
                        target = dateOfMarriage
                    )
                } ?: -1

            fatherName.id -> {
                validateEmptyOnEditText(fatherName)
                validateAllCapsOrSpaceOnEditText(fatherName)
            }

            motherName.id -> {
                validateEmptyOnEditText(motherName)
                validateAllCapsOrSpaceOnEditText(motherName)
            }

            husbandName.id -> {
                validateEmptyOnEditText(husbandName)
                validateAllCapsOrSpaceOnEditText(husbandName)
            }

            wifeName.id -> {
                validateEmptyOnEditText(wifeName)
                validateAllCapsOrSpaceOnEditText(wifeName)
            }

            spouseName.id -> {
                validateEmptyOnEditText(spouseName)
                validateAllCapsOrSpaceOnEditText(spouseName)
            }

            contactNumber.id -> {
                validateEmptyOnEditText(contactNumber)
                validateMobileNumberOnEditText(contactNumber)
            }

            mobileNoOfRelation.id -> {
                when (index) {
                    0, 1, 2, 3 -> triggerDependants(
                        source = mobileNoOfRelation,
                        removeItems = listOf(otherMobileNoOfRelation, contactNumberFamilyHead),
                        addItems = listOf(contactNumber)
                    )

                    4 -> {
                        contactNumberFamilyHead.value = familyHeadPhoneNo
                        triggerDependants(
                            source = mobileNoOfRelation,
                            addItems = listOf(contactNumberFamilyHead),
                            removeItems = listOf(otherMobileNoOfRelation, contactNumber)
                        )
                    }

                    else -> triggerDependants(
                        source = mobileNoOfRelation,
                        removeItems = listOf(contactNumberFamilyHead),
                        addItems = listOf(otherMobileNoOfRelation, contactNumber)
                    )
                }
            }

            otherMobileNoOfRelation.id -> validateEmptyOnEditText(otherMobileNoOfRelation)
            relationToHead.id -> {
                triggerDependants(
                    source = relationToHead,
                    passedIndex = index,
                    triggerIndex = relationToHead.entries!!.lastIndex,
                    target = otherRelationToHead
                )
            }

            otherRelationToHead.id -> validateEmptyOnEditText(otherRelationToHead)
            religion.id -> {
                triggerDependants(
                    source = religion,
                    passedIndex = index,
                    triggerIndex = religion.entries!!.lastIndex,
                    target = otherReligion
                )
            }

            otherReligion.id -> validateEmptyOnEditText(otherReligion)
            hasAadharNo.id -> triggerDependants(
                source = hasAadharNo, passedIndex = index, triggerIndex = 0, target = aadharNo
            )

            aadharNo.id -> validateAadharNoOnEditText(aadharNo)
            rchId.id -> validateRchIdOnEditText(rchId)
//            lastMenstrualPeriod.id -> {
//                lastMenstrualPeriod.value?.let {
//                    val day = it.substring(0, 2).toInt()
//                    val month = it.substring(3, 5).toInt() - 1
//                    val year = it.substring(6).toInt()
//                    val calLmp = Calendar.getInstance()
//                    calLmp.set(year, month, day)
//                    val calNow = Calendar.getInstance()
//                    val monthsDiff = getDiffMonths(calLmp, calNow)
//                    if (reproductiveStatus.value == reproductiveStatus.entries!!.first()) {
//                        if (monthsDiff >= 1) {
//                            triggerDependants(
//                                source = lastMenstrualPeriod,
//                                addItems = listOf(nishchayKitDeliveryStatus),
//                                removeItems = listOf(pregnancyTestResult)
//                            )
//                        } else {
//                            triggerDependants(
//                                source = lastMenstrualPeriod,
//                                addItems = emptyList(),
//                                removeItems = listOf(nishchayKitDeliveryStatus, pregnancyTestResult)
//                            )
//                        }
//                    } else if (reproductiveStatus.value == reproductiveStatus.entries!![1] || reproductiveStatus.value == reproductiveStatus.entries!![2]) {
//                        val calEdd = Calendar.getInstance()
//                        calEdd.timeInMillis = calLmp.timeInMillis
//                        calEdd.add(Calendar.DAY_OF_YEAR, 270)
//                        expectedDateOfDelivery.value = getDateFromLong(calEdd.timeInMillis)
//                        -1
//                    } else
//                        -1
//                } ?: -1
//            }

//            nishchayKitDeliveryStatus.id -> {
//                triggerDependants(
//                    source = nishchayKitDeliveryStatus,
//                    passedIndex = index,
//                    triggerIndex = 0,
//                    target = pregnancyTestResult
//                )
//            }

            reproductiveStatus.id ->
                triggerDependants(
                    source = reproductiveStatus,
                    passedIndex = index,
                    triggerIndex = reproductiveStatus.entries!!.lastIndex,
                    target = otherReproductiveStatus
                )
            /*{
            lastMenstrualPeriod.value = null
            when (index) {
                0 -> {

                    lastMenstrualPeriod.required = false
                    triggerDependants(
                        source = reproductiveStatus,
                        addItems = listOf(lastMenstrualPeriod),
                        removeItems = listOf(
                            nishchayKitDeliveryStatus,
                            pregnancyTestResult,
                            expectedDateOfDelivery,
                            dateOfDelivery,
                            whoConductedDelivery,
                            otherWhoConductedDelivery,
                            lastDeliveryConducted,
                            otherPlaceOfDelivery,
                            facility,
                            numPrevLiveBirthOrPregnancy,
                            otherReproductiveStatus
                        )
                    )
                }

                4, 5 -> {
                    lastMenstrualPeriod.required = false
                    triggerDependants(
                        source = reproductiveStatus,
                        addItems = listOf(lastMenstrualPeriod),
                        removeItems = listOf(
                            nishchayKitDeliveryStatus,
                            pregnancyTestResult,
                            expectedDateOfDelivery,
                            dateOfDelivery,
                            whoConductedDelivery,
                            otherWhoConductedDelivery,
                            lastDeliveryConducted,
                            otherPlaceOfDelivery,
                            facility,
                            numPrevLiveBirthOrPregnancy,
                            otherReproductiveStatus
                        )
                    )
                }

                1, 2 -> {
                    lastMenstrualPeriod.required = true
                    triggerDependants(
                        source = reproductiveStatus, addItems = listOf(
                            lastMenstrualPeriod,
                            expectedDateOfDelivery,
                            numPrevLiveBirthOrPregnancy

                        ), removeItems = listOf(
                            nishchayKitDeliveryStatus,
                            pregnancyTestResult,
                            dateOfDelivery,
                            whoConductedDelivery,
                            otherWhoConductedDelivery,
                            lastDeliveryConducted,
                            otherPlaceOfDelivery,
                            facility,
                            otherReproductiveStatus
                        )
                    )
                }

                3 -> {
                    triggerDependants(
                        source = reproductiveStatus,
                        addItems = listOf(dateOfDelivery, numPrevLiveBirthOrPregnancy),
                        removeItems = listOf(
                            nishchayKitDeliveryStatus,
                            pregnancyTestResult,
                            lastMenstrualPeriod,
                            whoConductedDelivery,
                            otherWhoConductedDelivery,
                            lastDeliveryConducted,
                            otherPlaceOfDelivery,
                            expectedDateOfDelivery,
                            facility,
                            otherReproductiveStatus
                        )
                    )
                }

                6 -> {
                    lastMenstrualPeriod.required = false
                    triggerDependants(
                        source = reproductiveStatus,
                        addItems = listOf(lastMenstrualPeriod, otherReproductiveStatus),
                        removeItems = listOf(
                            nishchayKitDeliveryStatus,
                            pregnancyTestResult,
                            dateOfDelivery,
                            numPrevLiveBirthOrPregnancy,
                            lastMenstrualPeriod,
                            whoConductedDelivery,
                            otherWhoConductedDelivery,
                            lastDeliveryConducted,
                            otherPlaceOfDelivery,
                            facility,
                        )
                    )
                }

                else -> -1
            }
        }*/

            /*numPrevLiveBirthOrPregnancy.id -> {
                numPrevLiveBirthOrPregnancy.value?.takeIf { it.isNotEmpty() }?.toInt()?.let {
                    if (it > 0) triggerDependants(
                        source = numPrevLiveBirthOrPregnancy, removeItems = listOf(
                            facility, otherWhoConductedDelivery, otherPlaceOfDelivery
                        ), addItems = listOf(lastDeliveryConducted, whoConductedDelivery)
                    )
                    else triggerDependants(
                        source = numPrevLiveBirthOrPregnancy,
                        addItems = emptyList(),
                        removeItems = listOf(
                            lastDeliveryConducted,
                            whoConductedDelivery,
                            facility,
                            otherWhoConductedDelivery,
                            otherPlaceOfDelivery
                        ),
                    )
                } ?: -1
            }*/
//
//            lastDeliveryConducted.id -> {
//                when (index) {
//                    0, 1, 2, 3, 4 -> triggerDependants(
//                        source = lastDeliveryConducted,
//                        addItems = listOf(facility),
//                        removeItems = listOf(otherPlaceOfDelivery)
//                    )
//
//                    else -> triggerDependants(
//                        source = lastDeliveryConducted,
//                        addItems = listOf(otherPlaceOfDelivery),
//                        removeItems = listOf(facility)
//                    )
//                }
//            }
//
//            facility.id -> validateEmptyOnEditText(facility)
//            otherPlaceOfDelivery.id -> validateEmptyOnEditText(otherPlaceOfDelivery)
//            whoConductedDelivery.id -> {
//                triggerDependants(
//                    source = whoConductedDelivery,
//                    passedIndex = index,
//                    triggerIndex = whoConductedDelivery.entries!!.lastIndex,
//                    target = otherWhoConductedDelivery
//                )
//            }
//
//            otherWhoConductedDelivery.id -> validateEmptyOnEditText(otherWhoConductedDelivery)


            else -> -1
        }
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        (cacheModel as BenRegCache).let { ben ->
            //      Page 001
            ben.userImage = pic.value
            ben.regDate = getLongFromDate(dateOfReg.value!!)
            ben.firstName = firstName.value
            ben.lastName = lastName.value
            ben.dob = getLongFromDate(dob.value!!)
            ben.age = age.value?.toInt() ?: 0
            ben.ageUnit = AgeUnit.YEARS
            ben.ageUnitId = 3
            ben.genderId = when (gender.value) {
                gender.entries!![0] -> 1
                gender.entries!![1] -> 2
                gender.entries!![2] -> 3
                else -> 0
            }
            ben.gender = when (ben.genderId) {
                1 -> Gender.MALE
                2 -> Gender.FEMALE
                3 -> Gender.TRANSGENDER
                else -> null
            }
//            ben.registrationType = TypeOfList.GENERAL
            ben.genDetails?.maritalStatusId = maritalStatus.getPosition()
            ben.genDetails?.maritalStatus =
                maritalStatus.getStringFromPosition(ben.genDetails?.maritalStatusId ?: 0)
            ben.genDetails?.spouseName = husbandName.value.takeIf { !it.isNullOrEmpty() }
                ?: wifeName.value.takeIf { !it.isNullOrEmpty() }
                        ?: spouseName.value.takeIf { !it.isNullOrEmpty() }
            ben.genDetails?.ageAtMarriage =
                ageAtMarriage.value?.toInt() ?: 0
            ben.genDetails?.marriageDate =
                dateOfMarriage.value?.let { getLongFromDate(it) }
                    ?: getDoMFromDoR(ben.genDetails?.ageAtMarriage, ben.regDate)
            ben.fatherName = fatherName.value
            ben.motherName = motherName.value
            ben.familyHeadRelationPosition =
                relationToHeadListDefault.indexOf(relationToHead.value) + 1
            ben.familyHeadRelation =
                relationToHeadListDefault[ben.familyHeadRelationPosition - 1]
            ben.familyHeadRelationOther = otherRelationToHead.value
            ben.mobileNoOfRelationId = mobileNoOfRelation.getPosition()
            ben.mobileNoOfRelation =
                mobileNoOfRelation.getStringFromPosition(ben.mobileNoOfRelationId)
            ben.mobileOthers = otherMobileNoOfRelation.value
            ben.contactNumber =
                if (ben.mobileNoOfRelationId == 5) familyHeadPhoneNo!!.toLong() else contactNumber.value!!.toLong()
            ben.community = community.value
            ben.communityId = community.getPosition()
            ben.religion = religion.value
            ben.religionId = religion.getPosition()
            ben.religionOthers = otherReligion.value
            ben.rchId = rchId.value

            //Page 002
            ben.hasAadhar = when (hasAadharNo.value) {
                "Yes" -> true
                "No" -> false
                else -> null
            }
            ben.hasAadharId = when (ben.hasAadhar) {
                true -> 1
                false -> 2
                else -> 0
            }
            ben.aadharNum = aadharNo.value
            ben.rchId = rchId.value

            //Page 003
            ben.genDetails?.let { gen ->
                gen.reproductiveStatusId = reproductiveStatus.getPosition()
                gen.reproductiveStatus = reproductiveStatus.value
//                gen.lastMenstrualPeriod =
//                    lastMenstrualPeriod.value?.let {
//                        getLongFromDate(
//                            it
//                        )
//                    }
//                ben.nishchayDeliveryStatus =
//                    nishchayKitDeliveryStatus.value
//                ben.nishchayDeliveryStatusPosition =
//                    (nishchayKitDeliveryStatus.entries?.indexOf(
//                        ben.nishchayDeliveryStatus
//                    ))?.let { it + 1 } ?: 0
//                ben.nishchayPregnancyStatus = pregnancyTestResult.value
//                ben.nishchayPregnancyStatusPosition =
//                    (pregnancyTestResult.entries?.indexOf(
//                        ben.nishchayPregnancyStatus
//                    ))?.let { it + 1 } ?: 0
//                gen.expectedDateOfDelivery =
//                    expectedDateOfDelivery.value?.let {
//                        getLongFromDate(
//                            it
//                        )
//                    }
//                gen.deliveryDate = dateOfDelivery.value
//                gen.numPreviousLiveBirth =
//                    numPrevLiveBirthOrPregnancy.value?.toInt() ?: 0
//                gen.lastDeliveryConducted = lastDeliveryConducted.value
//                gen.lastDeliveryConductedId = lastDeliveryConducted.getPosition()
//                gen.otherLastDeliveryConducted =
//                    otherPlaceOfDelivery.value
//                gen.facilityName = facility.value
//                gen.whoConductedDelivery = whoConductedDelivery.value
//                gen.whoConductedDeliveryId = whoConductedDelivery.getPosition()
//                gen.otherWhoConductedDelivery =
//                    otherWhoConductedDelivery.value
//                ben.registrationType = when (reproductiveStatus.value) {
//                    "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
//                    "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
//                    "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
//                    "Postnatal Mother-Lactating Mother" -> TypeOfList.POSTNATAL_MOTHER
//                    "Menopause Stage" -> TypeOfList.MENOPAUSE
//                    "Teenager" -> TypeOfList.TEENAGER
//                    else -> TypeOfList.OTHER
//                }
            }


        }
    }

    fun setImageUriToFormElement(lastImageFormId: Int, dpUri: Uri) {
        when (lastImageFormId) {
            pic.id -> {
                pic.value = dpUri.toString()
                pic.errorText = null
            }
        }

    }


}