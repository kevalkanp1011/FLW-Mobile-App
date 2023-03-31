package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import android.widget.LinearLayout
import kotlinx.coroutines.flow.MutableStateFlow
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.helpers.ImageUtils
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class BenKidRegFormDataset(private val context: Context, pncMotherList : List<String>?) {

    private var ben: BenRegCache? = null

    constructor(context: Context, ben: BenRegCache/*, pncMotherList: List<String>*/) : this(context, null) {
        this.ben = ben
       

    }

    companion object {
        private fun getDateFromLong(long : Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = long
            val mdFormat =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }
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
        entries = context.resources.getStringArray(R.array.nbr_age_unit_array),
        required = true,
    )
    val age = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_age),
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
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
        entries = context.resources.getStringArray(R.array.nbr_gender_array),
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
        entries = arrayOf(
            "Mother",
            "Father",
            "Family Head",
            "Other"
        ),
        required = true,
    )
    val otherMobileNoOfRelation = FormInput(
        inputType = EDIT_TEXT,
        title = "Other - Mobile Number of",
        required = true
    )
    val contactNumber = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nrb_contact_number),
        required = true,
        etMaxLength = 10,
        isMobileNumber = true,
        min = 6000000000,
        max = 9999999999,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
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
        entries = relationToHeadListDefault,
        required = true,
    )
    val otherRelationToHead = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_rel_to_head_other),
        allCaps = true,
        required = true
    )
    private val community = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_community),
        entries = arrayOf(
            "General",
            "SC",
            "ST",
            "BC",
            "OBC",
            "EBC",
            "Not given"
        ),
        required = true
    )
    val religion = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_religion),
        entries = arrayOf(
            "Hindu",
            "Muslim",
            "Christian",
            "Sikh",
            "Buddhism",
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
        allCaps = true,
        required = true
    )

    val childRegisteredAtAwc = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_awc),
        entries = arrayOf(
            "Yes",
            "No"
        ),
        required = true
    )

    val childRegisteredAtSchool = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_reg_school),
        entries = arrayOf(
            "Yes",
            "No"
        ),
        required = true
    )


    val typeOfSchool = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_type_school),
        entries = arrayOf(
            "Anganwadi",
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
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL

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
    
    fun loadFirstPageOnViewMode(): List<FormInput> {
        val viewList = mutableListOf(
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
            contactNumber,
            community,
            religion,
            rchId,
        )

        ben?.let{ benCache ->
            dateOfReg.value.value = getDateFromLong(benCache.regDate)
            firstName.value.value = benCache.firstName
            lastName.value.value = benCache.lastName
            ageUnit.value.value =
                context.resources.getStringArray(R.array.nbr_age_unit_array)[benCache.age_unitId - 1]
            age.value.value = benCache.age.toString()
            dob.value.value = getDateFromLong(benCache.dob)
            gender.value.value =
                context.resources.getStringArray(R.array.nbr_gender_array)[benCache.genderId - 1]
            fatherName.value.value = benCache.fatherName
            motherName.value.value = benCache.motherName
            mobileNoOfRelation.value.value = mobileNoOfRelation.entries?.get(benCache.mobileNoOfRelationId-1)
            otherMobileNoOfRelation.value.value = benCache.mobileOthers
            contactNumber.value.value = benCache.contactNumber.toString()
            relationToHead.value.value = relationToHeadListDefault[benCache.familyHeadRelationPosition-1]
            community.value.value = community.entries?.get(benCache.communityId -1)
            religion.value.value = religion.entries?.get(benCache.religionId -1)
            otherReligion.value.value = benCache.religionOthers
            childRegisteredAtAwc.value.value =
                benCache.kidDetails?.childRegisteredAWCId?.takeIf{it>0}?.let{ childRegisteredAtAwc.entries?.get(it-1) }
            childRegisteredAtSchool.value.value =
                benCache.kidDetails?.childRegisteredSchoolId?.takeIf{it>0}?.let { childRegisteredAtSchool.entries?.get(it-1) }
            typeOfSchool.value.value =
                benCache.kidDetails?.typeOfSchoolId?.takeIf{it>0}?.let { typeOfSchool.entries?.get(it-1) }
            rchId.value.value = benCache.rchId
        }
        otherRelationToHead.value.value?.let {  viewList.add(viewList.indexOf(relationToHead)+1,otherRelationToHead) }
        otherMobileNoOfRelation.value.value?.let {  viewList.add(viewList.indexOf(mobileNoOfRelation)+1,otherMobileNoOfRelation) }
        otherReligion.value.value?.let {  viewList.add(viewList.indexOf(religion)+1,otherReligion) }

        childRegisteredAtAwc.value.value?.let {  viewList.add(viewList.indexOf(rchId),childRegisteredAtAwc) }
        childRegisteredAtSchool.value.value?.let {  viewList.add(viewList.indexOf(rchId),childRegisteredAtSchool) }
        typeOfSchool.value.value?.let {  viewList.add(viewList.indexOf(rchId),typeOfSchool) }



        return viewList

    }
    
    

    //////////////////////////////////////////Second Page///////////////////////////////////////////

    val placeOfBirth = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_pob),
        entries = arrayOf(
            "Home",
            "Health Facility",
            "Any other Place"
        ),
        required = true
    )
    val facility = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_facility),
        entries = arrayOf(
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
    val otherFacility = FormInput(
        inputType = EDIT_TEXT,
        title = context.getString(R.string.nbr_child_pob_other_facility),
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
        entries = arrayOf(
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
        entries = arrayOf(
            "Normal Delivery",
            "C - Section",
            "Assisted"
        ),
        required = true
    )
     val complicationsDuringDelivery = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_comp_del),
        entries = arrayOf(
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
        entries = arrayOf(
            "Yes",
            "No",
            "Don't Know"
        ),
        required = true
    )
    val birthDose = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_birth_dose),
        entries = arrayOf(
            "Given",
            "Not Given",
            "Don't Know"
        ),
        required = true
    )
    val birthDoseGiven = FormInput(
        inputType = CHECKBOXES,
        title = context.getString(R.string.nbr_child_birth_dose_details),
        entries = arrayOf(
            "BCG",
            "Hepatitis",
            "OPV"
        ),
        required = true
    )

    val term = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_term),
        entries = arrayOf(
            "Full-Term",
            "Pre-Term",
            "Don't Know"
        ),
        required = true
    )

    val termGestationalAge = FormInput(
        inputType = RADIO,
        title = context.getString(R.string.nbr_child_gest_age),
        entries = arrayOf(
            "24-34 Weeks",
            "34-36 Weeks",
            "36-38 Weeks",
        ),
        required = true
    )
    val corticosteroidGivenAtLabor = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_corticosteroid),
        entries = arrayOf(
            "Yes",
            "No",
            "Don't know",
        ),
        required = true
    )
    private val babyCriedImmediatelyAfterBirth = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_cry_imm_birth),
        entries = arrayOf(
            "Yes",
            "No",
            "Don't Know"
        ),
        required = true
    )
    private val anyDefectAtBirth = FormInput(
        inputType = DROPDOWN,
        title = context.getString(R.string.nbr_child_defect_at_birth),
        entries = arrayOf(
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
    val motherUnselected  = FormInput(
        inputType = CHECKBOXES,
        title = "Mother Unselected",
        entries = arrayOf("Yes"),
        orientation = LinearLayout.HORIZONTAL,
        required = false
    )
    val motherOfChild = FormInput(
        inputType = DROPDOWN,
        title = "Mother of the child",
        entries = pncMotherList?.toTypedArray(),
        required = true
    )


    private val babyHeight = FormInput(
        inputType = EDIT_TEXT,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
        minDecimal = 40.0,
        maxDecimal = 50.0,
        etMaxLength = 4,
        title = "Height at birth ( cm )",
        required = false
    )
    private val babyWeight = FormInput(
        inputType = EDIT_TEXT,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
        minDecimal = 0.5,
        maxDecimal = 7.0,
        etMaxLength = 3,
        title = "Weight at birth (Kgs )",
        required = false
    )

    val deathRemoveList by lazy{
        listOf(
            breastFeedWithin1Hr,
            birthDose,
            term,
            babyCriedImmediatelyAfterBirth,
            anyDefectAtBirth,
            babyHeight,
            babyWeight
        )
    }

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
            motherUnselected,
            babyHeight,
            babyWeight,


            )
    }
    fun loadSecondPageOnViewMode(): List<FormInput> {
        val viewList = mutableListOf(
            placeOfBirth,
            whoConductedDelivery,
            typeOfDelivery,
            complicationsDuringDelivery,
            breastFeedWithin1Hr,
            birthDose,
            term,
            babyCriedImmediatelyAfterBirth,
            anyDefectAtBirth,
            motherOfChild,
            babyHeight,
            babyWeight,
        )

        ben?.let{ benCache->
            placeOfBirth.value.value =
                benCache.kidDetails?.birthPlaceId?.takeIf{it>0}?.let { placeOfBirth.entries?.get(it-1) }
            facility.value.value = benCache.kidDetails?.facilityId?.takeIf{it>0}?.let { facility.entries?.get(it-1) }
            otherFacility.value.value = benCache.kidDetails?.facilityOther
            otherPlaceOfBirth.value.value = benCache.kidDetails?.placeName
            whoConductedDelivery.value.value = benCache.kidDetails?.conductedDeliveryId?.takeIf { it>0 }?.let { whoConductedDelivery.entries?.get(it-1) }
            otherWhoConductedDelivery.value.value = benCache.kidDetails?.conductedDeliveryOther
            typeOfDelivery.value.value = benCache.kidDetails?.deliveryTypeId?.takeIf { it>0 }?.let { typeOfDelivery.entries?.get(it-1) }
            complicationsDuringDelivery.value.value = benCache.kidDetails?.complicationsId?.takeIf { it>0 }?.let { complicationsDuringDelivery.entries?.get(it-1) }
            breastFeedWithin1Hr.value.value = benCache.kidDetails?.feedingStartedId?.takeIf { it>0 }?.let { breastFeedWithin1Hr.entries?.get(it-1) }
            birthDose.value.value = benCache.kidDetails?.birthDosageId?.takeIf { it>0 }?.let { birthDose.entries?.get(it-1) }
            birthDoseGiven.value.value = "${if(benCache.kidDetails?.birthBCG==true) "BCG" else ""}${if(benCache.kidDetails?.birthHepB==true) "Hepatitis" else ""}${if(benCache.kidDetails?.birthOPV==true) "OPV" else ""}"
            term.value.value = benCache.kidDetails?.termId?.takeIf { it>0 }?.let { term.entries?.get(it-1) }
            termGestationalAge.value.value = benCache.kidDetails?.gestationalAgeId?.takeIf { it>0 }?.let { termGestationalAge.entries?.get(it-1) }
            corticosteroidGivenAtLabor.value.value = benCache.kidDetails?.corticosteroidGivenMotherId?.takeIf { it>0 }?.let { corticosteroidGivenAtLabor.entries?.get(it-1) }
            babyCriedImmediatelyAfterBirth.value.value = benCache.kidDetails?.criedImmediatelyId?.takeIf { it>0 }?.let { babyCriedImmediatelyAfterBirth.entries?.get(it-1) }
            anyDefectAtBirth.value.value = benCache.kidDetails?.birthDefectsId?.takeIf { it>0 }?.let { anyDefectAtBirth.entries?.get(it-1) }
            motherOfChild.value.value = benCache.kidDetails?.childMotherName
            babyHeight.value.value = benCache.kidDetails?.heightAtBirth?.toString()
            babyWeight.value.value = benCache.kidDetails?.weightAtBirth?.toString()

        }
        otherPlaceOfBirth.value.value?.let {  viewList.add(viewList.indexOf(placeOfBirth)+1,otherPlaceOfBirth) }
        otherFacility.value.value?.let {  viewList.add(viewList.indexOf(facility)+1,otherFacility) }
        otherWhoConductedDelivery.value.value?.let {  viewList.add(viewList.indexOf(whoConductedDelivery)+1,otherWhoConductedDelivery) }
        birthDoseGiven.value.value?.let { viewList.add(viewList.indexOf(birthDose)+1,birthDoseGiven) }
        termGestationalAge.value.value?.let { viewList.add(viewList.indexOf(term)+1,termGestationalAge) }
        corticosteroidGivenAtLabor.value.value?.let { viewList.add(viewList.indexOf(termGestationalAge)+1,corticosteroidGivenAtLabor) }


        return viewList

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
            userImageBlob = ImageUtils.getByteArrayFromImageUri(context,pic.value.value!!)
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
                (this@BenKidRegFormDataset.mobileNoOfRelation.entries?.indexOf(mobileNoOfRelation!!))?.let { it + 1 }
                    ?: 0
            contactNumber = stringToLong(this@BenKidRegFormDataset.contactNumber.value.value!!)
            community = this@BenKidRegFormDataset.community.value.value
            communityId =
                (this@BenKidRegFormDataset.community.entries?.indexOf(community!!))?.let { it + 1 }
                    ?: 0

            religion = this@BenKidRegFormDataset.religion.value.value
            religionId =
                (this@BenKidRegFormDataset.religion.entries?.indexOf(religion!!))?.let { it + 1 } ?: 0

            religionOthers = this@BenKidRegFormDataset.otherReligion.value.value
            kidDetails?.childRegisteredAWC =
                this@BenKidRegFormDataset.childRegisteredAtAwc.value.value
            kidDetails?.childRegisteredAWCId =
                this@BenKidRegFormDataset.childRegisteredAtAwc.entries?.indexOf(kidDetails?.childRegisteredAWC)
                    ?.let { it + 1 } ?: 0
            kidDetails?.childRegisteredSchool =
                this@BenKidRegFormDataset.childRegisteredAtSchool.value.value
            kidDetails?.childRegisteredSchoolId =
                this@BenKidRegFormDataset.childRegisteredAtSchool.entries?.indexOf(kidDetails?.childRegisteredSchool)
                    ?.let { it + 1 } ?: 0
            kidDetails?.typeOfSchool = this@BenKidRegFormDataset.typeOfSchool.value.value
            kidDetails?.typeOfSchoolId =
                this@BenKidRegFormDataset.typeOfSchool.entries?.indexOf(kidDetails?.typeOfSchool)
                    ?.let { it + 1 } ?: 0
            rchId = this@BenKidRegFormDataset.rchId.value.value
        }
        return ben!!
    }

    fun getBenRegType(): TypeOfList? {
        return ben?.registrationType
    }

    suspend fun getBenForSecondPage(userId: Int, hhId: Long): BenRegCache {
        getBenForFirstPage(userId, hhId = hhId)

        ben?.apply {
            kidDetails?.birthPlace = this@BenKidRegFormDataset.placeOfBirth.value.value
            kidDetails?.birthPlaceId =
                this@BenKidRegFormDataset.placeOfBirth.entries?.indexOf(kidDetails?.birthPlace)
                    ?.let { it + 1 } ?: 0
            kidDetails?.conductedDelivery =
                this@BenKidRegFormDataset.whoConductedDelivery.value.value
            kidDetails?.conductedDeliveryId =
                this@BenKidRegFormDataset.whoConductedDelivery.entries?.indexOf(kidDetails?.conductedDelivery)
                    ?.let { it + 1 } ?: 0
            kidDetails?.conductedDeliveryOther =
                this@BenKidRegFormDataset.otherWhoConductedDelivery.value.value
            kidDetails?.deliveryType = this@BenKidRegFormDataset.typeOfDelivery.value.value
            kidDetails?.deliveryTypeId =
                this@BenKidRegFormDataset.typeOfDelivery.entries?.indexOf(kidDetails?.deliveryType)
                    ?.let { it + 1 } ?: 0
            kidDetails?.complications =
                this@BenKidRegFormDataset.complicationsDuringDelivery.value.value
            kidDetails?.complicationsId =
                this@BenKidRegFormDataset.complicationsDuringDelivery.entries?.indexOf(kidDetails?.complications)
                    ?.let { it + 1 } ?: 0
            kidDetails?.feedingStarted = this@BenKidRegFormDataset.breastFeedWithin1Hr.value.value
            kidDetails?.feedingStartedId =
                this@BenKidRegFormDataset.breastFeedWithin1Hr.entries?.indexOf(kidDetails?.feedingStarted)
                    ?.let { it + 1 } ?: 0
            kidDetails?.birthDosage = this@BenKidRegFormDataset.birthDose.value.value
            kidDetails?.birthDosageId =
                this@BenKidRegFormDataset.birthDose.entries?.indexOf(kidDetails?.birthDosage)
                    ?.let { it + 1 } ?: 0
            kidDetails?.birthBCG =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("BCG") ?: false
            kidDetails?.birthHepB =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("Hepatitis") ?: false
            kidDetails?.birthOPV =
                this@BenKidRegFormDataset.birthDoseGiven.value.value?.contains("OPV") ?: false
            kidDetails?.term = this@BenKidRegFormDataset.term.value.value
            kidDetails?.termId = this@BenKidRegFormDataset.term.entries?.indexOf(kidDetails?.term)
                ?.let { it + 1 } ?: 0
            kidDetails?.gestationalAge = this@BenKidRegFormDataset.termGestationalAge.value.value
            kidDetails?.gestationalAgeId =
                this@BenKidRegFormDataset.termGestationalAge.entries?.indexOf(kidDetails?.gestationalAge)
                    ?.let { it + 1 } ?: 0
            kidDetails?.corticosteroidGivenMother =
                this@BenKidRegFormDataset.corticosteroidGivenAtLabor.value.value
            kidDetails?.corticosteroidGivenMotherId =
                this@BenKidRegFormDataset.corticosteroidGivenAtLabor.entries?.indexOf(kidDetails?.corticosteroidGivenMother)
                    ?.let { it + 1 } ?: 0
            kidDetails?.criedImmediately =
                this@BenKidRegFormDataset.babyCriedImmediatelyAfterBirth.value.value
            kidDetails?.criedImmediatelyId =
                this@BenKidRegFormDataset.babyCriedImmediatelyAfterBirth.entries?.indexOf(kidDetails?.criedImmediately)
                    ?.let { it + 1 } ?: 0
            kidDetails?.birthDefects = this@BenKidRegFormDataset.anyDefectAtBirth.value.value
            kidDetails?.birthDefectsId =
                this@BenKidRegFormDataset.anyDefectAtBirth.entries?.indexOf(kidDetails?.birthDefects)
                    ?.let { it + 1 } ?: 0
            kidDetails?.heightAtBirth =
                this@BenKidRegFormDataset.babyHeight.value.value?.toDouble() ?: 0.0
            kidDetails?.weightAtBirth =
                this@BenKidRegFormDataset.babyWeight.value.value?.toDouble() ?: 0.0


        }

        return ben!!
    }

    suspend fun setPic() {
        pic.value.value = ben?.userImageBlob?.let {
            ImageUtils.getUriFromByteArray(
                context,
                ben!!.beneficiaryId,
                it
            ).toString()
        }
    }
}