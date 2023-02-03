package org.piramalswasthya.sakhi.configuration

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import java.text.SimpleDateFormat
import java.util.*

class BenKidRegFormDataset(context: Context) {

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
            cal.add(Calendar.YEAR, -15)
            cal.add(Calendar.DAY_OF_MONTH, +1)
            return cal.timeInMillis
        }

        private fun getMaxDobMillis(): Long {
            return System.currentTimeMillis()
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
    val ageUnit = FormInput(
        inputType = DROPDOWN,
        title = "Age Unit",
        list = listOf(
            "Year",
            "Month",
            "Day"
        ),
        required = true,
    )
    val age = FormInput(
        inputType = EDIT_TEXT,
        title = "Age",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
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
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
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

    val childRegisteredAtAwc = FormInput(
        inputType = DROPDOWN,
        title = "Is the Child registered at AWC",
        listOf(
            "Yes",
            "No"
        ),
        required = true
    )

    val childRegisteredAtSchool = FormInput(
        inputType = DROPDOWN,
        title = "Is the Child registered at School",
        listOf(
            "Yes",
            "No"
        ),
        required = true
    )


    val typeOfSchool = FormInput(
        inputType = DROPDOWN,
        title = "Type of School/Institute",
        listOf(
            "Aganwadi",
            "Primary",
            "Secondary",
            "Private"
        ),
        required = false
    )


    val rchId = FormInput(
        inputType = EDIT_TEXT,
        title = "RCH ID",
        required = false,
        etLength = 12,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL

    )
    val firstPage: List<FormInput> by lazy {
        listOf(
            dateOfReg,
            firstName,
            lastName,
            dob,
            age,
            ageUnit,
            gender,
            fatherName,
            motherName,
            relationToHead,
            mobileNoOfRelation,
            community,
            religion,
            rchId,
        )
    }

    //////////////////////////////////////////Second Page///////////////////////////////////////////

    val placeOfBirth = FormInput(
        inputType = DROPDOWN,
        title = "Place of birth",
        list = listOf(
            "Home",
            "Health Facility",
            "Any other Place"
        ),
        required = true
    )
    val facility = FormInput(
        inputType = DROPDOWN,
        title = "Facility Selection",
        list = listOf(
            "Sub Centre",
            "PHC",
            "CHC",
            "Sub District Hospital",
            "District Hospital",
            "Medical College Hospital",
            "In Transit",
            "Private Hospital",
            "Accredited Private Hospital",
            "Other",
        ),
        required = true
    )
    val otherPlaceOfBirth = FormInput(
        inputType = EDIT_TEXT,
        title = "Other Place of Birth",
        required = true
    )
    val whoConductedDelivery = FormInput(
        inputType = DROPDOWN,
        title = "Who Conducted Delivery",
        list = listOf(
            "ANM",
            "LHV",
            "Doctor",
            "Staff Nurse",
            "Relative",
            "TBA(Non-Skilled Birth Attendant)",
            "Other",
        ),
        required = true
    )
    val otherWhoConductedDelivery = FormInput(
        inputType = EDIT_TEXT,
        title = "Other - Enter who Conducted Delivery",
        required = true
    )
    val typeOfDelivery = FormInput(
        inputType = DROPDOWN,
        title = "Type of Delivery",
        listOf(
            "Normal Delivery",
            "C - Section",
            "Assisted"
        ),
        required = true
    )
    val complicationsDuringDelivery = FormInput(
        inputType = DROPDOWN,
        title = "Complications during delivery",
        listOf(
            "PPH",
            "Retained Placenta",
            "Obstructed Delivery",
            "Prolapsed cord",
            "Death",
            "None",
        ),
        required = true
    )
    val breastFeedWithin1Hr = FormInput(
        inputType = DROPDOWN,
        title = "Breast feeding started within 1 hr of birth",
        listOf(
            "Yes",
            "No",
            "Don't Know"
        ),
        required = true
    )
    val birthDose = FormInput(
        inputType = DROPDOWN,
        title = "Birth Dose",
        listOf(
            "Given",
            "Not Given",
            "Don't Know"
        ),
        required = true
    )
    val birthDoseGiven = FormInput(
        inputType = CHECKBOXES,
        title = "Given birth dose details",
        listOf(
            "BCG",
            "Hepatitis",
            "OPV"
        ),
        required = true
    )

    val term = FormInput(
        inputType = DROPDOWN,
        title = "Term",
        listOf(
            "Full-Term",
            "Pre-Term",
            "Don't Know"
        ),
        required = true
    )

    val termGestationalAge = FormInput(
        inputType = RADIO,
        title = "Term",
        listOf(
            "24-34 Weeks",
            "34-36 Weeks",
            "36-38 Weeks",
        ),
        required = true
    )
    val babyCriedImmediatelyAfterBirth = FormInput(
        inputType = DROPDOWN,
        title = "Baby cried immediately at birth",
        listOf(
            "Yes",
            "No",
            "Don't Know"
        ),
        required = true
    )
    val anyDefectAtBirth = FormInput(
        inputType = DROPDOWN,
        title = "Any Defect Seen at Birth",
        listOf(
            "Yes",
            "Cleft Lip-Cleft Palate",
            "Neural Tube defect(Spina Bifida)",
            "Club Foot",
            "Hydrocephalus",
            "Imperforate Anus",
            "Downs Syndrome",
            "None"
        ),
        required = true
    )
    val babyHeight = FormInput(
        inputType = EDIT_TEXT,
        title = "Height at birth ( cm )",
        required = false
    )
    val babyWeight = FormInput(
        inputType = EDIT_TEXT,
        title = "Weight at birth (gram )",
        required = false
    )
    val secondPage: List<FormInput> by lazy {
        listOf(
            placeOfBirth,
            whoConductedDelivery,
            typeOfDelivery,
            complicationsDuringDelivery,
            breastFeedWithin1Hr,
            birthDose,
            term,
            babyCriedImmediatelyAfterBirth,
            anyDefectAtBirth,
            babyHeight,
            babyWeight,


            )
    }

    private fun getTypeFromAge(age: Int, ageUnit: AgeUnit?): TypeOfList? {
        return if (ageUnit == AgeUnit.DAYS || ageUnit == AgeUnit.MONTHS)
            TypeOfList.INFANT
        else if (ageUnit == AgeUnit.YEARS && age > 1 && age < 5)
            TypeOfList.CHILD
        else if (ageUnit == AgeUnit.YEARS && age > 5 && age < 15)
            TypeOfList.ADOLESCENT
        else null

    }

    fun getBenForFirstPage(userId: Int, hhId: Long): BenRegCache {

        if (ben == null) {
            ben = BenRegCache(
                householdId = hhId,
                ashaId = userId,
                syncState = SyncState.UNSYNCED,
                isKid = true,
                isAdult = false,
                kidDetails = BenRegKid(),
                isDraft = true
            )
        }
        ben?.apply {
            regDate = getLongFromDate(this@BenKidRegFormDataset.dateOfReg.value.value!!)
            firstName = this@BenKidRegFormDataset.firstName.value.value
            lastName = this@BenKidRegFormDataset.lastName.value.value
            dob = getLongFromDate(this@BenKidRegFormDataset.dob.value.value!!)
            age = this@BenKidRegFormDataset.age.value.value?.toInt() ?: 0
            ageUnit = when (this@BenKidRegFormDataset.ageUnit.value.value) {
                "Year" -> AgeUnit.YEARS
                "Month" -> AgeUnit.MONTHS
                "Day" -> AgeUnit.DAYS
                else -> null
            }
            registrationType = getTypeFromAge(age, ageUnit)
            gender = when (this@BenKidRegFormDataset.gender.value.value) {
                "Male" -> Gender.MALE
                "Female" -> Gender.FEMALE
                "Transgender" -> Gender.TRANSGENDER
                else -> null
            }
            fatherName = this@BenKidRegFormDataset.fatherName.value.value
            motherName = this@BenKidRegFormDataset.motherName.value.value
            familyHeadRelation = this@BenKidRegFormDataset.relationToHead.value.value
            familyHeadRelationOther = this@BenKidRegFormDataset.otherRelationToHead.value.value
            mobileNoOfRelation = this@BenKidRegFormDataset.mobileNoOfRelation.value.value
            contactNumber = stringToLong(this@BenKidRegFormDataset.contactNumber.value.value!!)
            community = this@BenKidRegFormDataset.community.value.value
            religion = this@BenKidRegFormDataset.religion.value.value
            religionOthers = this@BenKidRegFormDataset.otherReligion.value.value
            kidDetails?.childRegisteredAWC =
                this@BenKidRegFormDataset.childRegisteredAtAwc.value.value
            kidDetails?.childRegisteredSchool =
                this@BenKidRegFormDataset.childRegisteredAtSchool.value.value
            kidDetails?.typeOfSchool = this@BenKidRegFormDataset.typeOfSchool.value.value
            rchId = this@BenKidRegFormDataset.rchId.value.value
        }
        return ben!!
    }

    fun getBenForSecondPage(): BenRegCache {

        ben?.apply {
            kidDetails?.birthPlace = this@BenKidRegFormDataset.placeOfBirth.value.value
            kidDetails?.conductedDelivery =
                this@BenKidRegFormDataset.whoConductedDelivery.value.value
            kidDetails?.conductedDeliveryOther =
                this@BenKidRegFormDataset.otherWhoConductedDelivery.value.value
            kidDetails?.deliveryType = this@BenKidRegFormDataset.typeOfDelivery.value.value
            kidDetails?.complications =
                this@BenKidRegFormDataset.complicationsDuringDelivery.value.value
            kidDetails?.feedingStarted = this@BenKidRegFormDataset.breastFeedWithin1Hr.value.value
            kidDetails?.birthDosage = this@BenKidRegFormDataset.birthDose.value.value
            kidDetails?.birthBCG =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("BCG") ?: false
            kidDetails?.birthHepB =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("Hepatitis") ?: false
            kidDetails?.birthOPV =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("BCG") ?: false
            kidDetails?.term = this@BenKidRegFormDataset.term.value.value
            kidDetails?.gestationalAge = this@BenKidRegFormDataset.termGestationalAge.value.value
            kidDetails?.criedImmediately =
                this@BenKidRegFormDataset.babyCriedImmediatelyAfterBirth.value.value
            kidDetails?.birthDefects = this@BenKidRegFormDataset.anyDefectAtBirth.value.value
            kidDetails?.heightAtBirth = this@BenKidRegFormDataset.babyHeight.value.value
            kidDetails?.weightAtBirth = this@BenKidRegFormDataset.babyWeight.value.value
        }

        return ben!!
    }
}