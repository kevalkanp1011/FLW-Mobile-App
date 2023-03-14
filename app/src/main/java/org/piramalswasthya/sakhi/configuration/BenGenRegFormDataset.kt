package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import android.widget.LinearLayout
import kotlinx.coroutines.flow.MutableStateFlow
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.helpers.ImageUtils
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class BenGenRegFormDataset(private val context: Context) {

    private var ben: BenRegCache? = null

    constructor(context: Context, ben: BenRegCache) : this(context) {
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
    private val pic = FormInput(
        inputType = IMAGE_VIEW,
        title = "Image",
        required = true
    )
    val dateOfReg = FormInput(
        inputType = TEXT_VIEW,
        title = "Date of Registration",
        value = MutableStateFlow(getCurrentDate()),
        required = true
    )
    private val firstName = FormInput(
        inputType = EDIT_TEXT,
        title = "First Name",
        allCaps = true,
        required = true
    )
    private val lastName = FormInput(
        inputType = EDIT_TEXT,
        title = "Last Name / Surname",
        allCaps = true,
        required = false,
    )
    val age = FormInput(
        inputType = EDIT_TEXT,
        title = "Age (in Years)",
        min = 15,
        max = 99,
        etMaxLength = 2,
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
        list = arrayOf(
            "Male",
            "Female",
            "Transgender"
        ),
        required = true,
    )

    val maritalStatusMale = arrayOf(
        "Unmarried",
        "Married",
        "Divorced",
        "Separated",
        "Widower",
    )

    val maritalStatusFemale = arrayOf(
        "Unmarried",
        "Married",
        "Divorced",
        "Separated",
        "Widow",
    )
    val maritalStatus = FormInput(
        inputType = DROPDOWN,
        title = "Marital Status",
        list = maritalStatusMale ,
        required = true,
    )
    val husbandName = FormInput(
        inputType = EDIT_TEXT,
        title = "Husband's Name",
        allCaps = true,
        required = true
    )
    val wifeName = FormInput(
        inputType = EDIT_TEXT,
        title = "Wife's Name",
        allCaps = true,
        required = true
    )
    val spouseName = FormInput(
        inputType = EDIT_TEXT,
        title = "Spouse's Name",
        allCaps = true,
        required = true
    )
    val ageAtMarriage = FormInput(
        inputType = EDIT_TEXT,
        title = "Age At Marriage",
        min = 12,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true,
    )
    val dateOfMarriage = FormInput(
        inputType = DATE_PICKER,
        title = "Date of Marriage",
        max = System.currentTimeMillis(),
        min = 0L,
        required = true,
    )
    val fatherName = FormInput(
        inputType = EDIT_TEXT,
        title = "Father's Name",
        allCaps = true,
        required = true
    )
    val motherName = FormInput(
        inputType = EDIT_TEXT,
        title = "Mother's Name",
        allCaps = true,
        required = true
    )

    val mobileNoOfRelation = FormInput(
        inputType = DROPDOWN,
        title = "Mobile Number Of",
        list = arrayOf(
            "Self",
            "Husband",
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
        title = "Contact Number",
        required = true,
        etMaxLength = 10,
        isMobileNumber = true,
        min=6000000000,
        max=9999999999,
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
        list = arrayOf(
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
        title = "Religion",
        list = arrayOf(
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
        title = "Other - Enter Religion",
        required = true
    )

    val firstPage: List<FormInput> by lazy {
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

    val hasAadharNo = FormInput(
        inputType = RADIO,
        title = "Has Aadhar Number",
        list = arrayOf("Yes", "No"),

        required = false
    )

    val aadharNo = FormInput(
        inputType = EDIT_TEXT,
        title = "Enter Aadhar Number",
        required = true,
        etMaxLength = 12,
        min = 100000000000L,
        max = 999999999999L,
        isMobileNumber = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    )

    val rchId = FormInput(
        inputType = EDIT_TEXT,
        title = "RCH ID",
        required = false,
        etMaxLength = 12,
        isMobileNumber = true,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL

    )

    val secondPage =
        listOf(
            hasAadharNo,
            rchId
        )


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
        list = arrayOf(
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

    val reproductiveStatusOther = FormInput(
        inputType = EDIT_TEXT,
        title = "Reproductive Status Other",
        etMaxLength = 100,
        required = true
    )

    val nishchayKitDeliveryStatus = FormInput(
        inputType = RADIO,
        title = "Nishchay Kit Delivery Status",
        list = arrayOf("Delivered", "Not Delivered"),
        orientation = LinearLayout.VERTICAL,
        required = true,

        )

    val pregnancyTestResult = FormInput(
        inputType = RADIO,
        title = "Pregnancy Test Result",
        list = arrayOf("Pregnant", "Not Pregnant", "Pending"),
        orientation = LinearLayout.VERTICAL,
        required = true,

        )

    val expectedDateOfDelivery = FormInput(
        inputType = TEXT_VIEW,
        title = "Expected Date Of Delivery",
        required = true
    )


    val numPrevLiveBirthOrPregnancy = FormInput(
        inputType = EDIT_TEXT,
        title = "No. of Previous Live Birth / Pregnancy",
        min = 0,
        max = 20,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = true,
    )

    val lastDeliveryConducted = FormInput(
        inputType = DROPDOWN,
        title = "Last Delivery Conducted",
        list = arrayOf(
            "Home",
            "PHC",
            "HWC",
            "CHC",
            "District Hospital",
            "Medical college Hospital",
            "Other",
        ),
        required = true
    )
    val facility = FormInput(
        inputType = EDIT_TEXT,
        title = "Facility Name",
        required = true
    )
    val otherPlaceOfDelivery = FormInput(
        inputType = EDIT_TEXT,
        title = " Enter the Place of last delivery conducted",
        required = true
    )
    val whoConductedDelivery = FormInput(
        inputType = DROPDOWN,
        title = "Who Conducted Delivery",
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
        title = "Other - Enter who Conducted Delivery",
        required = true
    )

    val dateOfDelivery = FormInput(
        inputType = DATE_PICKER,
        title = "Date Of Delivery",
        required = true,
        min = age.value.value?.let { getLongFromDate(it) } ?: 0L,
        max = System.currentTimeMillis()
    )


    val thirdPage =
        listOf(
            reproductiveStatus,
            lastMenstrualPeriod,

            )


    suspend fun getBenForFirstPage(userId: Int, hhId: Long): BenRegCache {
        if (ben == null) {
            ben = BenRegCache(
                ashaId = userId,
                beneficiaryId = -1L,
                isKid = false,
                isAdult = true,
                householdId = hhId,
                isDraft = true,
                genDetails = BenRegGen(),
                syncState = SyncState.UNSYNCED
            )
        }
        ben?.apply {
            userImageBlob = ImageUtils.getByteArrayFromImageUri(context,pic.value.value!!)
            Timber.d("BenGenReg: $userImageBlob, ${pic.value.value}")
            regDate = getLongFromDate(this@BenGenRegFormDataset.dateOfReg.value.value!!)
            firstName = this@BenGenRegFormDataset.firstName.value.value
            lastName = this@BenGenRegFormDataset.lastName.value.value
            dob = getLongFromDate(this@BenGenRegFormDataset.dob.value.value!!)
            age = this@BenGenRegFormDataset.age.value.value?.toInt() ?: 0
            ageUnit = AgeUnit.YEARS
            age_unitId = 3
            gender = when (this@BenGenRegFormDataset.gender.value.value) {
                "Male" -> Gender.MALE
                "Female" -> Gender.FEMALE
                "Transgender" -> Gender.TRANSGENDER
                else -> null
            }
            genderId = when (this@BenGenRegFormDataset.gender.value.value) {
                "Male" -> 1
                "Female" -> 2
                "Transgender" -> 3
                else -> 0
            }
            this.registrationType = TypeOfList.GENERAL
            genDetails?.maritalStatus = this@BenGenRegFormDataset.maritalStatus.value.value
            genDetails?.maritalStatusId =
                (this@BenGenRegFormDataset.maritalStatus.list?.indexOf(genDetails?.maritalStatus!!))?.let { it + 1 }
                    ?: 0
            genDetails?.spouseName = this@BenGenRegFormDataset.husbandName.value.value
                ?: this@BenGenRegFormDataset.wifeName.value.value
                        ?: this@BenGenRegFormDataset.spouseName.value.value
            genDetails?.ageAtMarriage =
                this@BenGenRegFormDataset.ageAtMarriage.value.value?.toInt() ?: 0
            genDetails?.marriageDate = this@BenGenRegFormDataset.dateOfMarriage.value.value?.let{getLongFromDate(it)}?:getDoMFromDoR(genDetails?.ageAtMarriage, regDate!!)
            fatherName = this@BenGenRegFormDataset.fatherName.value.value
            motherName = this@BenGenRegFormDataset.motherName.value.value
            familyHeadRelation = this@BenGenRegFormDataset.relationToHead.value.value
            familyHeadRelationPosition =
                this@BenGenRegFormDataset.relationToHeadListDefault.indexOf(familyHeadRelation) + 1
            familyHeadRelationOther = this@BenGenRegFormDataset.otherRelationToHead.value.value
            mobileNoOfRelation = this@BenGenRegFormDataset.mobileNoOfRelation.value.value
            mobileNoOfRelationId =
                (this@BenGenRegFormDataset.mobileNoOfRelation.list?.indexOf(mobileNoOfRelation!!))?.let { it + 1 }
                    ?: 0
            mobileOthers = this@BenGenRegFormDataset.otherMobileNoOfRelation.value.value
            contactNumber = stringToLong(this@BenGenRegFormDataset.contactNumber.value.value!!)
            community = this@BenGenRegFormDataset.community.value.value
            communityId =
                (this@BenGenRegFormDataset.community.list?.indexOf(community!!))?.let { it + 1 }
                    ?: 0
            religion = this@BenGenRegFormDataset.religion.value.value
            religionId =
                (this@BenGenRegFormDataset.religion.list?.indexOf(religion!!))?.let { it + 1 } ?: 0
            religionOthers = this@BenGenRegFormDataset.otherReligion.value.value

            rchId = this@BenGenRegFormDataset.rchId.value.value
        }
        return ben!!

    }


    private fun getDoMFromDoR(ageAtMarriage: Int?, regDate: Long): Long? {
        if (ageAtMarriage == null)
            return null
        val cal = Calendar.getInstance()
        cal.timeInMillis = regDate
        cal.add(Calendar.YEAR, -1 * ageAtMarriage)
        return cal.timeInMillis

    }


    suspend fun getBenForSecondPage(userId: Int, hhId: Long): BenRegCache {
        getBenForFirstPage(userId,hhId)

        ben?.apply {
            this.hasAadhar = when (this@BenGenRegFormDataset.hasAadharNo.value.value) {
                "Yes" -> true
                "No" -> false
                else -> null
            }
            this.hasAadharId = when (this.hasAadhar) {
                true -> 1
                false -> 2
                else -> 0
            }
            this.aadharNum = this@BenGenRegFormDataset.aadharNo.value.value
            this.rchId = this@BenGenRegFormDataset.rchId.value.value
        }
        return ben!!

    }

    suspend fun getBenForThirdPage(userId: Int, hhId: Long): BenRegCache {
        getBenForSecondPage(userId, hhId)
        ben?.apply {
            this.genDetails?.apply {
                reproductiveStatus = this@BenGenRegFormDataset.reproductiveStatus.value.value
                reproductiveStatusId =
                    (this@BenGenRegFormDataset.reproductiveStatus.list?.indexOf(reproductiveStatus))?.let { it + 1 }
                        ?: 0
                lastMenstrualPeriod =
                    this@BenGenRegFormDataset.lastMenstrualPeriod.value.value?.let {
                        getLongFromDate(
                            it
                        )
                    }
                nishchayDeliveryStatus =
                    this@BenGenRegFormDataset.nishchayKitDeliveryStatus.value.value
                nishchayDeliveryStatusPosition =
                    (this@BenGenRegFormDataset.nishchayKitDeliveryStatus.list?.indexOf(
                        nishchayDeliveryStatus
                    ))?.let { it + 1 } ?: 0
                nishchayPregnancyStatus = this@BenGenRegFormDataset.pregnancyTestResult.value.value
                nishchayPregnancyStatusPosition =
                    (this@BenGenRegFormDataset.pregnancyTestResult.list?.indexOf(
                        nishchayPregnancyStatus
                    ))?.let { it + 1 } ?: 0
                expectedDateOfDelivery =
                    this@BenGenRegFormDataset.expectedDateOfDelivery.value.value?.let {
                        getLongFromDate(
                            it
                        )
                    }
                numPreviousLiveBirth =
                    this@BenGenRegFormDataset.numPrevLiveBirthOrPregnancy.value.value?.toInt() ?: 0
                lastDeliveryConducted = this@BenGenRegFormDataset.lastDeliveryConducted.value.value
                lastDeliveryConductedId =
                    (this@BenGenRegFormDataset.lastDeliveryConducted.list?.indexOf(
                        lastDeliveryConducted
                    )) ?: 0
                otherLastDeliveryConducted =
                    this@BenGenRegFormDataset.otherPlaceOfDelivery.value.value
                facilityName = this@BenGenRegFormDataset.facility.value.value
                whoConductedDelivery = this@BenGenRegFormDataset.whoConductedDelivery.value.value
                whoConductedDeliveryId =
                    (this@BenGenRegFormDataset.whoConductedDelivery.list?.indexOf(
                        whoConductedDelivery
                    ))?.let { it + 1 } ?: 0
                otherWhoConductedDelivery =
                    this@BenGenRegFormDataset.otherWhoConductedDelivery.value.value
                registrationType = when (reproductiveStatus) {
                    "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
                    "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
                    "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
                    "Postnatal Mother-Lactating Mother" -> TypeOfList.POSTNATAL_MOTHER
                    "Menopause Stage" -> TypeOfList.MENOPAUSE
                    "Teenager" -> TypeOfList.TEENAGER
                    else -> TypeOfList.OTHER
                }


            }
        }
        return ben!!
    }


}