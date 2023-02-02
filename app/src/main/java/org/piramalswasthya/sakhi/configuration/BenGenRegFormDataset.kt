package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import kotlinx.coroutines.flow.MutableStateFlow
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import java.text.SimpleDateFormat
import java.util.*

class BenGenRegFormDataset(context: Context) {

    private var ben: BenRegCache? = null

    constructor(context: Context, ben: BenRegCache? = null) : this(context) {
        this.ben = ben
        //TODO(SETUP THE VALUES)
    }

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

        private fun stringToLong(phNo: String) = phNo.toLong()

        private fun getMinDobMillis(): Long {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -115)
            return cal.timeInMillis
        }

        private fun getMaxDobMillis(): Long {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -15)
            return cal.timeInMillis
        }
    }

    //////////////////////////////////First Page////////////////////////////////////

    private val dateOfReg = FormInput(
        inputType = TEXT_VIEW,
        title = "Date of Registration",
        value = MutableStateFlow(getCurrentDate()),
        required = true
    )
    private val firstName = FormInput(
        inputType = EDIT_TEXT,
        title = "First Name",
        required = true
    )
    private val lastName = FormInput(
        inputType = EDIT_TEXT,
        title = "Last Name / Surname",
        required = false,
    )
    val age = FormInput(
        inputType = EDIT_TEXT,
        title = "Age (in Years)",
        min = 15,
        max = 114,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true,
    )
    val dob = FormInput(
        inputType = DATE_PICKER,
        title = "Date of Birth",
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
        required = true,
    )
    val gender = FormInput(
        inputType = RADIO,
        title = "Gender",
        list = listOf(
            "Male",
            "Female",
            "Transgender"
        ),
        required = true,
    )
    val maritalStatus = FormInput(
        inputType = DROPDOWN,
        title = "Marital Status",
        list = listOf(
            "Unmarried",
            "Married",
            "Divorced",
            "Separated",
            "Widow",
            "Widower",
        ),
        required = true,
    )
    val husbandName = FormInput(
        inputType = EDIT_TEXT,
        title = "Husband's Name",
        required = true
    )
    val wifeName = FormInput(
        inputType = EDIT_TEXT,
        title = "Wife's Name",
        required = true
    )
    val spouseName = FormInput(
        inputType = EDIT_TEXT,
        title = "Spouse's Name",
        required = true
    )
    val ageAtMarriage = FormInput(
        inputType = EDIT_TEXT,
        title = "Age At Marriage",
        min = 12,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true,
    )
    private val fatherName = FormInput(
        inputType = EDIT_TEXT,
        title = "Father's Name",
        required = true
    )
    private val motherName = FormInput(
        inputType = EDIT_TEXT,
        title = "Mother's Name",
        required = true
    )

    val mobileNoOfRelation = FormInput(
        inputType = DROPDOWN,
        title = "Mobile Number Of",
        list = listOf(
            "Mother",
            "Father",
            "Family Head",
            "Other"
        ),
        required = true,
    )
    val contactNumber = FormInput(
        inputType = EDIT_TEXT,
        title = "Contact Number",
        required = true,
        etLength = 10,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    )


    val relationToHeadListDefault = listOf(
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
    val relationToHeadListMale = listOf(
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
    val relationToHeadListFemale = listOf(
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
    val relationToHead = FormInput(
        inputType = DROPDOWN,
        title = "Relation with family head",
        list = relationToHeadListDefault,
        required = true,
    )
    val otherRelationToHead = FormInput(
        inputType = EDIT_TEXT,
        title = "Other - Enter relation to head",
        required = true
    )
    private val community = FormInput(
        inputType = DROPDOWN,
        title = "Community",
        list = listOf(
            "General",
            "SC",
            "ST",
            "BC",
            "OBC",
            "OC",
            "Not given"
        ),
        required = true
    )
    val religion = FormInput(
        inputType = DROPDOWN,
        title = "Religion",
        list = listOf(
            "Hindu",
            "Muslim",
            "Christian",
            "Sikh",
            "Buddism",
            "Jainism",
            "Other",
            "Parsi",
            "Not Disclosed"
        ),
        required = true
    )
    val otherReligion = FormInput(
        inputType = EDIT_TEXT,
        title = "Other - Enter Religion",
        required = true
    )

    val firstPage: List<FormInput> by lazy {
        listOf(
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

    val hasAadharNo = FormInput(
        inputType = RADIO,
        title = "Has Aadhar Number",
        list = listOf("Yes", "No"),
        required = false
    )

    val aadharNo = FormInput(
        inputType = EDIT_TEXT,
        title = "Enter Aadhar Number",
        required = true,
        etLength = 12,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    )

    val rchId = FormInput(
        inputType = EDIT_TEXT,
        title = "RCH ID",
        required = false,
        etLength = 12,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL

    )

    val secondPage: List<FormInput> by lazy {
        listOf(
            hasAadharNo,
            rchId
        )
    }


    //////////////////////////////////Third(if any) Page////////////////////////////////////

    val lastMenstrualPeriod = FormInput(
        inputType = DATE_PICKER,
        title = "Last Menstrual Period",
        required = false,
        min = age.value.value?.let { getLongFromDate(it) } ?: 0L,
        max = System.currentTimeMillis()

    )

    val reproductiveStatus = FormInput(
        inputType = DROPDOWN,
        title = "Reproductive Status",
        list = listOf(
            "Eligible Couple",
            "Antenatal Mother",
            "Delivery Stage",
            "Postnatal Mother-Lactating Mother",
            "Menopause Stage",
            "Teenager",
            "Other",
        ),
        required = true
    )


    val thirdPage by lazy {
        listOf(
            lastMenstrualPeriod,
            reproductiveStatus
        )
    }


    fun getBenForFirstPage(userId: Int, hhId: Long): BenRegCache {
        if (ben == null) {
            ben = BenRegCache(
                ashaId = userId,
                isKid = false,
                isAdult = true,
                householdId = hhId,
                isDraft = true,
                syncState = SyncState.UNSYNCED
            )
        }
        return ben!!

    }


    fun getBenForSecondPage(): BenRegCache {
        return ben!!

    }

    fun getBenForThirdPage(): BenRegCache {
        return ben!!
    }


}