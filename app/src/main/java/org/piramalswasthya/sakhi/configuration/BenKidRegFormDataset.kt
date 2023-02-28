package org.piramalswasthya.sakhi.configuration

import android.content.Context
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class BenKidRegFormDataset(private val context: Context) {

    private var ben: BenRegCache? = null

    constructor(context: Context, ben: BenRegCache? = null) : this(context) {
        this.ben = ben

        //TODO(SETUP THE VALUES
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

    private val pic = FormInput(
        inputType = IMAGE_VIEW,
        title = context.getString(R.string.nbr_image),
        required = true
    )

    private val dateOfReg = FormInput(
        inputType = TEXT_VIEW,
        title = context.getString(R.string.nbr_dor),
        value = MutableStateFlow(getCurrentDate()),
        required = true
    )
    private val firstName = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_nb_first_name),
        allCaps = true,
        required = true
    )
    private val lastName = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_nb_last_name),
        allCaps = true,
        required = false,
    )
    val ageUnit = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_nb_age_unit),
        list = context.resources.getStringArray(R.array.nbr_age_unit_array),
        required = true,
    )
    val age = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_age),
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true,
    )
    val dob = FormInput(
        inputType = DATE_PICKER,
        title = context.getString(R.string.nbr_dob),
        max = getMaxDobMillis(),
        min = getMinDobMillis(),
        required = true,
    )
    val gender = FormInput(
        inputType = RADIO,
        title = context.getString(R.string.nbr_gender),
        list = context.resources.getStringArray(R.array.nbr_gender_array),
        required = true,
    )
    private val fatherName = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_father_name),
        allCaps = true,
        required = true
    )
    private val motherName = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_mother_name),
        allCaps = true,
        required = true
    )

    val mobileNoOfRelation = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_mobile_number_of),
        list = arrayOf(
            "Mother",
            "Father",
            "Family Head",
            "Other"
        ),
        required = true,
    )
    val contactNumber = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nrb_contact_number),
        required = true,
        etMaxLength = 10,
        isMobileNumber = true,
        min = 6000000000,
        max = 9999999999,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
    )


    val relationToHeadListDefault = arrayOf(
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
    val relationToHeadListMale = arrayOf(
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
    val relationToHeadListFemale = arrayOf(
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
        title = context.getString(R.string.nbr_rel_to_head),
        list = relationToHeadListDefault,
        required = true,
    )
    val otherRelationToHead = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_rel_to_head_other),
        required = true
    )
    private val community = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_community),
        list = arrayOf(
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
        title = context.getString(R.string.nbr_religion),
        list = arrayOf(
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
        title = context.getString(R.string.nbr_religion_other),
        required = true
    )

    val childRegisteredAtAwc = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_awc),
        arrayOf(
            "Yes",
            "No"
        ),
        required = true
    )

    val childRegisteredAtSchool = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_reg_school),
        arrayOf(
            "Yes",
            "No"
        ),
        required = true
    )


    val typeOfSchool = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_type_school),
        arrayOf(
            "Aganwadi",
            "Primary",
            "Secondary",
            "Private"
        ),
        required = false
    )


    val rchId = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_rch_id),
        required = false,
        etMaxLength = 12,
        isMobileNumber = true,
        min = 100000000000,
        max = 999999999999,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL

    )
    val firstPage: List<FormInput> by lazy {
        listOf(
            pic,
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
        title = context.getString(R.string.nbr_child_pob),
        list = arrayOf(
            "Home",
            "Health Facility",
            "Any other Place"
        ),
        required = true
    )
    val facility = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_facility),
        list = arrayOf(
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
        title = context.getString(R.string.nbr_child_pob_other),
        required = true
    )
    val whoConductedDelivery = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_who_cond_del),
        list = arrayOf(
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
        title = context.getString(R.string.nbr_child_who_cond_del_other),
        required = true
    )
    private val typeOfDelivery = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_type_del),
        arrayOf(
            "Normal Delivery",
            "C - Section",
            "Assisted"
        ),
        required = true
    )
    private val complicationsDuringDelivery = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_comp_del),
        arrayOf(
            "PPH",
            "Retained Placenta",
            "Obstructed Delivery",
            "Prolapsed cord",
            "Death",
            "None",
        ),
        required = true
    )
    private val breastFeedWithin1Hr = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_feed_1_hr),
        arrayOf(
            "Yes",
            "No",
            "Don't Know"
        ),
        required = true
    )
    val birthDose = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_birth_dose),
        arrayOf(
            "Given",
            "Not Given",
            "Don't Know"
        ),
        required = true
    )
    val birthDoseGiven = FormInput(
        inputType = CHECKBOXES,
        title = context.getString(R.string.nbr_child_birth_dose_details),
        arrayOf(
            "BCG",
            "Hepatitis",
            "OPV"
        ),
        required = true
    )

    val term = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_term),
        arrayOf(
            "Full-Term",
            "Pre-Term",
            "Don't Know"
        ),
        required = true
    )

    val termGestationalAge = FormInput(
        inputType = RADIO,
        title = context.getString(R.string.nbr_child_gest_age),
        arrayOf(
            "24-34 Weeks",
            "34-36 Weeks",
            "36-38 Weeks",
        ),
        required = true
    )
    val corticosteroidGivenAtLabor = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_corticosteroid),
        arrayOf(
            "Yes",
            "No",
            "Don't know",
        ),
        required = true
    )
    private val babyCriedImmediatelyAfterBirth = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_cry_imm_birth),
        arrayOf(
            "Yes",
            "No",
            "Don't Know"
        ),
        required = true
    )
    private val anyDefectAtBirth = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_defect_at_birth),
        arrayOf(
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
    private val babyHeight = FormInput(
        inputType = EDIT_TEXT,
        title = "Height at birth ( cm )",
        required = false
    )
    private val babyWeight = FormInput(
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
        Timber.d("Values Here $age $ageUnit")
        return if (ageUnit == AgeUnit.DAYS || ageUnit == AgeUnit.MONTHS || (ageUnit == AgeUnit.YEARS && age < 2))
            TypeOfList.INFANT
        else if (ageUnit == AgeUnit.YEARS && age < 6)
            TypeOfList.CHILD
        else if (ageUnit == AgeUnit.YEARS && age < 15)
            TypeOfList.ADOLESCENT
        else null

    }

    suspend fun getBenForFirstPage(userId: Int, hhId: Long): BenRegCache {

        if (ben == null) {
            ben = BenRegCache(
                householdId = hhId,
                ashaId = userId,
                beneficiaryId = -2,
                syncState = SyncState.UNSYNCED,
                isKid = true,
                isAdult = false,
                kidDetails = BenRegKid(),
                isDraft = true
            )
        }
        ben?.apply {
            userImageBlob = getByteArrayFromImageUri(pic.value.value!!)
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
            Timber.d("$ageUnit ${this@BenKidRegFormDataset.ageUnit.value.value}")
            age_unitId = when (ageUnit) {
                AgeUnit.YEARS -> 3
                AgeUnit.MONTHS -> 2
                AgeUnit.DAYS -> 1
                else -> 0
            }
            registrationType = getTypeFromAge(age, ageUnit)
            gender = when (this@BenKidRegFormDataset.gender.value.value) {
                "Male" -> Gender.MALE
                "Female" -> Gender.FEMALE
                "Transgender" -> Gender.TRANSGENDER
                else -> null
            }
            genderId = when (this@BenKidRegFormDataset.gender.value.value) {
                "Male" -> 1
                "Female" -> 2
                "Transgender" -> 3
                else -> 0
            }
            fatherName = this@BenKidRegFormDataset.fatherName.value.value
            motherName = this@BenKidRegFormDataset.motherName.value.value
            familyHeadRelation = this@BenKidRegFormDataset.relationToHead.value.value
            familyHeadRelationPosition =
                this@BenKidRegFormDataset.relationToHeadListDefault.indexOf(familyHeadRelation) + 1
            familyHeadRelationOther = this@BenKidRegFormDataset.otherRelationToHead.value.value
            mobileNoOfRelation = this@BenKidRegFormDataset.mobileNoOfRelation.value.value
            mobileNoOfRelationId =
                (this@BenKidRegFormDataset.mobileNoOfRelation.list?.indexOf(mobileNoOfRelation!!))?.let { it + 1 }
                    ?: 0
            contactNumber = stringToLong(this@BenKidRegFormDataset.contactNumber.value.value!!)
            community = this@BenKidRegFormDataset.community.value.value
            communityId =
                (this@BenKidRegFormDataset.community.list?.indexOf(community!!))?.let { it + 1 }
                    ?: 0

            religion = this@BenKidRegFormDataset.religion.value.value
            religionId =
                (this@BenKidRegFormDataset.religion.list?.indexOf(religion!!))?.let { it + 1 } ?: 0

            religionOthers = this@BenKidRegFormDataset.otherReligion.value.value
            kidDetails?.childRegisteredAWC =
                this@BenKidRegFormDataset.childRegisteredAtAwc.value.value
            kidDetails?.childRegisteredAWCId =
                this@BenKidRegFormDataset.childRegisteredAtAwc.list?.indexOf(kidDetails?.childRegisteredAWC)
                    ?.let { it + 1 } ?: 0
            kidDetails?.childRegisteredSchool =
                this@BenKidRegFormDataset.childRegisteredAtSchool.value.value
            kidDetails?.childRegisteredSchoolId =
                this@BenKidRegFormDataset.childRegisteredAtSchool.list?.indexOf(kidDetails?.childRegisteredSchool)
                    ?.let { it + 1 } ?: 0
            kidDetails?.typeOfSchool = this@BenKidRegFormDataset.typeOfSchool.value.value
            kidDetails?.typeOfSchoolId =
                this@BenKidRegFormDataset.typeOfSchool.list?.indexOf(kidDetails?.typeOfSchool)
                    ?.let { it + 1 } ?: 0
            rchId = this@BenKidRegFormDataset.rchId.value.value
        }
        return ben!!
    }

    private suspend fun getByteArrayFromImageUri(uriString: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, uriString.substringAfterLast("/"))
            val compressedFile = Compressor.compress(context, file) {
                quality(50)
            }
            val iStream = compressedFile.inputStream()
            val byteArray = getBytes(iStream)
            iStream.close()
            byteArray
        }
    }

    private fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun getBenForSecondPage(): BenRegCache {

        ben?.apply {
            kidDetails?.birthPlace = this@BenKidRegFormDataset.placeOfBirth.value.value
            kidDetails?.birthPlaceId =
                this@BenKidRegFormDataset.placeOfBirth.list?.indexOf(kidDetails?.birthPlace)
                    ?.let { it + 1 } ?: 0
            kidDetails?.conductedDelivery =
                this@BenKidRegFormDataset.whoConductedDelivery.value.value
            kidDetails?.conductedDeliveryId =
                this@BenKidRegFormDataset.whoConductedDelivery.list?.indexOf(kidDetails?.conductedDelivery)
                    ?.let { it + 1 } ?: 0
            kidDetails?.conductedDeliveryOther =
                this@BenKidRegFormDataset.otherWhoConductedDelivery.value.value
            kidDetails?.deliveryType = this@BenKidRegFormDataset.typeOfDelivery.value.value
            kidDetails?.deliveryTypeId =
                this@BenKidRegFormDataset.typeOfDelivery.list?.indexOf(kidDetails?.deliveryType)
                    ?.let { it + 1 } ?: 0
            kidDetails?.complications =
                this@BenKidRegFormDataset.complicationsDuringDelivery.value.value
            kidDetails?.complicationsId =
                this@BenKidRegFormDataset.complicationsDuringDelivery.list?.indexOf(kidDetails?.complications)
                    ?.let { it + 1 } ?: 0
            kidDetails?.feedingStarted = this@BenKidRegFormDataset.breastFeedWithin1Hr.value.value
            kidDetails?.feedingStartedId =
                this@BenKidRegFormDataset.breastFeedWithin1Hr.list?.indexOf(kidDetails?.feedingStarted)
                    ?.let { it + 1 } ?: 0
            kidDetails?.birthDosage = this@BenKidRegFormDataset.birthDose.value.value
            kidDetails?.birthDosageId =
                this@BenKidRegFormDataset.birthDose.list?.indexOf(kidDetails?.birthDosage)
                    ?.let { it + 1 } ?: 0
            kidDetails?.birthBCG =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("BCG") ?: false
            kidDetails?.birthHepB =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("Hepatitis") ?: false
            kidDetails?.birthOPV =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("BCG") ?: false
            kidDetails?.term = this@BenKidRegFormDataset.term.value.value
            kidDetails?.termId = this@BenKidRegFormDataset.term.list?.indexOf(kidDetails?.term)
                ?.let { it + 1 } ?: 0
            kidDetails?.gestationalAge = this@BenKidRegFormDataset.termGestationalAge.value.value
            kidDetails?.gestationalAgeId =
                this@BenKidRegFormDataset.termGestationalAge.list?.indexOf(kidDetails?.gestationalAge)
                    ?.let { it + 1 } ?: 0
            kidDetails?.corticosteroidGivenMother =
                this@BenKidRegFormDataset.corticosteroidGivenAtLabor.value.value
            kidDetails?.corticosteroidGivenMotherId =
                this@BenKidRegFormDataset.corticosteroidGivenAtLabor.list?.indexOf(kidDetails?.corticosteroidGivenMother)
                    ?.let { it + 1 } ?: 0
            kidDetails?.criedImmediately =
                this@BenKidRegFormDataset.babyCriedImmediatelyAfterBirth.value.value
            kidDetails?.criedImmediatelyId =
                this@BenKidRegFormDataset.babyCriedImmediatelyAfterBirth.list?.indexOf(kidDetails?.criedImmediately)
                    ?.let { it + 1 } ?: 0
            kidDetails?.birthDefects = this@BenKidRegFormDataset.anyDefectAtBirth.value.value
            kidDetails?.birthDefectsId =
                this@BenKidRegFormDataset.anyDefectAtBirth.list?.indexOf(kidDetails?.birthDefects)
                    ?.let { it + 1 } ?: 0
            kidDetails?.heightAtBirth =
                this@BenKidRegFormDataset.babyHeight.value.value?.toInt() ?: 0
            kidDetails?.weightAtBirth =
                this@BenKidRegFormDataset.babyWeight.value.value?.toInt() ?: 0


        }

        return ben!!
    }
}