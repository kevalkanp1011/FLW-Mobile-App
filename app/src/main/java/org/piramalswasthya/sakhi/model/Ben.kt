package org.piramalswasthya.sakhi.model

import androidx.room.*
import org.piramalswasthya.sakhi.database.room.SyncState
import java.text.SimpleDateFormat
import java.util.*

enum class TypeOfList{
    INFANT,
    CHILD,
    ADOLESCENT,
    GENERAL,
    ELIGIBLE_COUPLE,
    ANTENATAL_MOTHER,
    DELIVERY_STAGE,
    POSTNATAL_MOTHER,
    MENOPAUSE,
    TEENAGER,
    OTHER,

}
enum class AgeUnit{
    DAYS,
    MONTHS,
    YEARS
}
enum class Gender{
    MALE,
    FEMALE,
    TRANSGENDER
}
data class BenBasicDomain(
    val benId: Long,
    val hhId: Long,
    val regDate: String,
    val benName: String,
    val benSurname: String? = null,
    val gender: String,
    val age: String,
    val mobileNo: String,
    val fatherName: String? = null,
    val familyHeadName: String,
    val typeOfList: String,
    val rchId: String,
    val hrpStatus: String? = null,
    val syncState: SyncState
)

data class BenRegKid(
    var childName: String? = null,
    var childRegisteredAWC: String? = null,
    var childRegisteredAWCId: Int = 0,
    var childRegisteredSchool: String? = null,
    var childRegisteredSchoolId: Int = 0,
    var typeOfSchool: String? = null,
    var typeOfSchoolId: Int = 0,
    var birthPlace : String? = null,
    var birthPlaceId : String? = null,
    var facilityName : String? = null,
    var facilityid : String? = null,
    var facilityOther : String? = null,
    var placeName : String? = null,
    var conductedDelivery : String? = null,
    var conductedDeliveryId : String? = null,
    var conductedDeliveryOther : String? = null,
    var deliveryType : String? = null,
    var deliveryTypeId : String? = null,
    var complications : String? = null,
    var complicationsId : String? = null,
    var complicationsOther : String? = null,
    var term : String? = null,
    var termid : String? = null,
    var gestationalAge : String? = null,
    var gestationalAgeId : String? = null,
    var corticosteroidGivenMother : String? = null,
    var corticosteroidGivenMotherId : String? = null,
    var criedImmediately : String? = null,
    var criedImmediatelyId : String? = null,
    var birthDefects : String? = null,
    var birthDefectsId : String? = null,
    var birthDefectsOthers : String? = null,
    var heightAtBirth : String? = null,
    var weightAtBirth : String? = null,
    var feedingStarted : String? = null,
    var feedingStartedId : String? = null,
    var birthDosage : String? = null,
    var birthDosageId : String? = null,
    var opvBatchNo : String? = null,
    var opvGivenDueDate : String? = null,
    var opvDate : String? = null,
    var bcdBatchNo : String? = null,
    var bcgGivenDueDate : String? = null,
    var bcgDate : String? = null,
    var hptBatchNo : String? = null,
    var hptGivenDueDate : String? = null,
    var hptDate : String? = null,
    var vitaminKBatchNo : String? = null,
    var vitaminKGivenDueDate : String? = null,
    var vitaminKDate: String? = null,
    var deliveryTypeOther: String? = null,

    var motherBenId: String? = null,
    var childMotherName: String? = null,
    var motherPosition: String? = null,
    var birthBCG: Boolean = false,
    var birthHepB: Boolean = false,
    var birthOPV: Boolean = false,
)

data class BenRegGen(

    var maritalStatus: String? = null,
    var maritalStatusId: Int = 0,
    var spouseName: String? = null,
    var ageAtMarriage: Int = 0,
    var dateOfMarriage: Long = 0,
    var marriageDate: String? = null,
    //Menstrual details
    var menstrualStatus: String? = null,
    var menstrualStatusId: Int? = 0,
    var regularityOfMenstrualCycle: String? = null,
    var regularityOfMenstrualCycleId: Int = 0,
    var lengthOfMenstrualCycle: String? = null,
    var lengthOfMenstrualCycleId: Int = 0,
    var menstrualBFD: String? = null,
    var menstrualBFDId: Int = 0,
    var menstrualProblem: String? = null,
    var menstrualProblemId: Int = 0,
    var lastMenstrualPeriod: String? = null,
    var reproductiveStatus: String? = null,
    var reproductiveStatusId: Int = 0,
    var lastDeliveryConducted: String? = null,
    var lastDeliveryConductedId: String? = null,
    var otherLastDeliveryConducted: String? = null,
    var facilityName: String? = null,
    var whoConductedDelivery: String? = null,
    var whoConductedDeliveryId: String? = null,
    var otherWhoConductedDelivery: String? = null,
    var deliveryDate: String? = null,
    var expectedDateOfDelivery: String? = null,
    var numPreviousLiveBirth: String? = null,
//    var formStatus: String? = null,
//    var formType: String? = null,
    var ancCount: Int = 0,
    var hrpCount: Int = 0,
    var hrpSuspected: Boolean = false,
    var isDeathStatus: Boolean = false,
)

@Entity(
    tableName = "BENEFICIARY",
    primaryKeys = ["householdId", "beneficiaryId"],
    foreignKeys = [
        ForeignKey(
            entity = HouseholdCache::class,
            parentColumns = arrayOf("householdId"),
            childColumns = arrayOf("householdId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserCache::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("ashaId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class BenRegCache(

    @ColumnInfo(index = true)
    var householdId: Long,

    var beneficiaryId: Long,

    var benRegId: Int = 0,

    @ColumnInfo(index = true)
    var ashaId: Int,

    var isKid: Boolean,

    var isAdult: Boolean,

    var userImage: String? = null,

    @Suppress("ArrayInDataClass")
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var userImageBlob: ByteArray? = null,

    var regDate: Long? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var gender: Gender? = null,

    var genderId: Int = 0,

    var dob: Long = 0,

    var age: Int = 0,

    var ageUnit: AgeUnit? = null,

    var age_unitId: Int = 0,

    var fatherName: String? = null,

    var motherName: String? = null,

    var familyHeadRelation: String? = null,

    var familyHeadRelationPosition: Int = 0,

    var familyHeadRelationOther: String? = null,

    var mobileNoOfRelation: String? = null,

    var mobileNoOfRelationId: Int = 0,

    var mobileOthers: String? = null,

    var contactNumber: Long? = null,

    var literacy: String? = null,

    var literacyId: Int = 0,

    var community: String? = null,

    var communityId: Int = 0,

    var religion: String? = null,

    var religionId: Int = 0,

    var religionOthers: String? = null,

    var rchId: String? = null,

    var registrationType: TypeOfList? = null,

    var latitude: Double = 0.0,

    var longitude: Double = 0.0,

    ///////////////////////////Bank details Start///////////////////////////
    var hasAadhar: Boolean? = false,

    var hasAadharId: Int = 0,

    var aadharNum: String? = null,

    var aadharNumId: Int = 0,

    var bankAccountId: Int = 0,

    var bankAccount: String? = null,

    var nameOfBank: String? = null,

    var nameOfBranch: String? = null,

    var ifscCode: String? = null,

    ///////////////////////////Bank details End///////////////////////////
    var needOpCare: String? = null,

    var needOpCareId: Int = 0,

    var ncdPriority: Int = 0,

    var cbacAvailable: Boolean = false,

    var guidelineId: String? = null,

    var isHrpStatus: Boolean = false,

    var hrpIdentificationDate: String? = null,

    var hrpLastVisitDate: String? = null,

    var nishchayPregnancyStatus: String? = null,

    var nishchayPregnancyStatusPosition: Int = 0,

    var nishchayDeliveryStatus: String? = null,

    var nishchayDeliveryStatusPosition: Int = 0,

    var nayiPahalDeliveryStatus: String? = null,

    var nayiPahalDeliveryStatusPosition: Int = 0,

    var suspectedNcd: String? = null,

    var suspectedNcdDiseases: String? = null,

    var suspectedTb: String? = null,

    var confirmed_Ncd: String? = null,

    var confirmedHrp: String? = null,

    var confirmedTb: String? = null,

    var confirmedNcdDiseases: String? = null,

    var diagnosisStatus: String? = null,

    var noOfDaysForDelivery: Int? = null,


    /*
    5 Skipped:
        Aadhar, lastHrpVisitDate, marriageDate ( 2 copies)
        vanId and serviceMap ID, ( Can get from Foreign key)
     */


    @Embedded(prefix = "kid_")
    var kidDetails: BenRegKid? = null,

    @Embedded(prefix = "gen_")
    var genDetails: BenRegGen? = null,

    var countyId: Int = 0,

    var stateId: Int = 0,

    var districtId: Int = 0,

    var districtName: String? = null,

    var currSubDistrictId: Int = 0,

    var villageId: Int = 0,

    var villageName: String? = null,

    var processed: String? = null,

    var serverUpdatedStatus: Int = 0,

    var createdBy: String? = null,

    var createdDate: Long? = null,

    var updatedBy: String? = null,

    var updatedDate: Long? = null,

    var syncState: SyncState,

    var isDraft: Boolean,

    ){

    companion object{
         val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    }
    fun asBasicDomainModel() : BenBasicDomain{
        return BenBasicDomain(
            benId = beneficiaryId,
            hhId = householdId,
            regDate = dateFormat.format(Date(regDate!!)),
            benName = firstName?:"Not Available",
            benSurname = lastName?:"Not Available",
            gender = gender?.name?:"Not Available",
            age = if(age==0) "Not Available" else "$age $ageUnit",
            mobileNo = contactNumber?.toString()?:"Not Available",
            fatherName = fatherName,
            familyHeadName = "Not implemented at the moment!",
            typeOfList = registrationType?.name?:"Not Available",
            rchId = rchId?:"Not Available",
            hrpStatus = confirmedHrp?:"Not Available",
            syncState = syncState
        )
    }
}

data class BenRegNetwork(
    var id: Int = 0,

    @ColumnInfo(name = "houseoldId")
    var houseoldId: String,

    @PrimaryKey
    @ColumnInfo(name = "benficieryid")
    var benficieryid: Long = 0,

    //Benficiery Registration fields
    @ColumnInfo(name = "user_image")
    var user_image: String? = null,

    @ColumnInfo(name = "ashaid")
    var ashaid: Int = 0,

    @Suppress("ArrayInDataClass")
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var user_image1: ByteArray? = null,

    @ColumnInfo(name = "registrationDate")
    var registrationDate: String,

    @ColumnInfo(name = "firstName")
    var firstName: String? = null,

    @ColumnInfo(name = "lastName")
    var lastName: String? = null,

    @ColumnInfo(name = "gender")
    var gender: String? = null,

    @ColumnInfo(name = "genderId")
    var genderId: Int = 0,

    @ColumnInfo(name = "dob")
    var dob: String,

    @ColumnInfo(name = "age")
    var age: Int = 0,

    @ColumnInfo(name = "age_unit")
    var age_unit: String? = null,

    @ColumnInfo(name = "age_unitId")
    var age_unitId: Int = 0,

    @ColumnInfo(name = "maritalstatus")
    var maritalstatus: String? = null,

    @ColumnInfo(name = "maritalstatusId")
    var maritalstatusId: Int = 0,

    @ColumnInfo(name = "spousename")
    var spousename: String? = null,

    @ColumnInfo(name = "ageAtMarriage")
    var ageAtMarriage: Int = 0,

    @ColumnInfo(name = "dateMarriage")
    var dateMarriage: String,

    @ColumnInfo(name = "marriageDate")
    var marriageDate: String? = null,

    @ColumnInfo(name = "fatherName")
    var fatherName: String? = null,

    @ColumnInfo(name = "motherName")
    var motherName: String? = null,

    @ColumnInfo(name = "contact_number")
    var contact_number: String? = null,

    @ColumnInfo(name = "mobilenoofRelation")
    var mobilenoofRelation: String? = null,

    @ColumnInfo(name = "mobilenoofRelationId")
    var mobilenoofRelationId: Int = 0,

    @ColumnInfo(name = "mobileOthers")
    var mobileOthers: String? = null,

    @ColumnInfo(name = "literacy")
    var literacy: String? = null,

    @ColumnInfo(name = "literacyId")
    var literacyId: Int = 0,

    @ColumnInfo(name = "community")
    var community: String? = null,

    @ColumnInfo(name = "communityId")
    var communityId: Int = 0,

    @ColumnInfo(name = "religion")
    var religion: String? = null,

    @ColumnInfo(name = "religionID")
    var religionID: Int = 0,

    @ColumnInfo(name = "religionOthers")
    var religionOthers: String? = null,

    @ColumnInfo(name = "rchid")
    var rchid: String? = null,

    @ColumnInfo(name = "registrationType")
    var registrationType: String? = null,

    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,

    //Bank details
    @ColumnInfo(name = "aadha_no")
    var aadha_no: String? = null,

    @ColumnInfo(name = "aadha_noId")
    var aadha_noId: Int = 0,

    @ColumnInfo(name = "aadhaNo")
    var aadhaNo: String? = null,

    @ColumnInfo(name = "bank_account")
    var bank_account: String? = null,

    @ColumnInfo(name = "bank_accountId")
    var bank_accountId: Int = 0,

    @ColumnInfo(name = "bankAccount")
    var bankAccount: String? = null,

    @ColumnInfo(name = "nameOfBank")
    var nameOfBank: String? = null,

    @ColumnInfo(name = "nameOfBranch")
    var nameOfBranch: String? = null,

    @ColumnInfo(name = "ifscCode")
    var ifscCode: String? = null,

    @ColumnInfo(name = "need_opcare")
    var need_opcare: String? = null,

    @ColumnInfo(name = "need_opcareId")
    var need_opcareId: Int = 0,

    //Menstral details
    @ColumnInfo(name = "menstrualStatus")
    var menstrualStatus: String? = null,

    @ColumnInfo(name = "menstrualStatusId")
    var menstrualStatusId: Int = 0,

    @ColumnInfo(name = "regularityofMenstrualCycle")
    var regularityofMenstrualCycle: String? = null,

    @ColumnInfo(name = "regularityofMenstrualCycleId")
    var regularityofMenstrualCycleId: Int = 0,

    @ColumnInfo(name = "lengthofMenstrualCycle")
    var lengthofMenstrualCycle: String? = null,

    @ColumnInfo(name = "lengthofMenstrualCycleId")
    var lengthofMenstrualCycleId: Int = 0,

    @ColumnInfo(name = "menstrualBFD")
    var menstrualBFD: String? = null,

    @ColumnInfo(name = "menstrualBFDId")
    var menstrualBFDId: Int = 0,

    @ColumnInfo(name = "menstrualProblem")
    var menstrualProblem: String? = null,

    @ColumnInfo(name = "menstrualProblemId")
    var menstrualProblemId: Int = 0,

    @ColumnInfo(name = "lastMenstrualPeriod")
    var lastMenstrualPeriod: String? = null,

    @ColumnInfo(name = "reproductiveStatus")
    var reproductiveStatus: String? = null,

    @ColumnInfo(name = "reproductiveStatusId")
    var reproductiveStatusId: Int = 0,

    @ColumnInfo(name = "formStatus")
    var formStatus: String? = null,

    @ColumnInfo(name = "formType")
    var formType: String? = null,

    @ColumnInfo(name = "ANCCount")
    var aNCCount: Int = 0,

    @ColumnInfo(name = "HRPCount")
    var hRPCount: Int = 0,

    @ColumnInfo(name = "hrp_suspected")
    var hrp_suspected: Boolean? = null,

    @ColumnInfo(name = "death_status")
    var isDeath_status: Boolean = false,

    @ColumnInfo(name = "childRegisteredAWC")
    var childRegisteredAWC: String? = null,

    @ColumnInfo(name = "childRegisteredAWCID")
    var childRegisteredAWCID: Int = 0,

    @ColumnInfo(name = "childRegisteredSchool")
    var childRegisteredSchool: String? = null,

    @ColumnInfo(name = "childRegisteredSchoolID")
    var childRegisteredSchoolID: Int = 0,

    @ColumnInfo(name = "TypeofSchool")
    var typeofSchool: String? = null,

    @ColumnInfo(name = "TypeofSchoolID")
    var typeofSchoolID: Int = 0,

    @ColumnInfo(name = "expectedDateOfDelivery")
    var expectedDateOfDelivery: String? = null,

    @ColumnInfo(name = "PreviousLiveBirth")
    var previousLiveBirth: String? = null,

    //these are new fields of new borr registartion
    @ColumnInfo(name = "LastDeliveryConducted")
    var lastDeliveryConducted: String? = null,

    @ColumnInfo(name = "LastDeliveryConductedID")
    var lastDeliveryConductedID: Int = 0,

    @ColumnInfo(name = "facilitySelection")
    var facilitySelection: String? = null,

    //    @ColumnInfo(name = "FacilitySectionID")
    //    private int facilitySectionID;

    @ColumnInfo(name = "WhoConductedDelivery")
    var whoConductedDelivery: String? = null,

    @ColumnInfo(name = "WhoConductedDeliveryID")
    var whoConductedDeliveryID: Int = 0,

    //these are new fields of registration for asha login
    @ColumnInfo(name = "FamilyHeadRelation")
    var familyHeadRelation: String? = null,

    @ColumnInfo(name = "FamilyHeadRelationPosition")
    var familyHeadRelationPosition: Int = 0,

    /*@ColumnInfo(name = "PreviousLiveBirthID")
      private int PreviousLiveBirthID;*/
    @ColumnInfo(name = "ServerUpdatedStatus")
    var serverUpdatedStatus: Int = 0,

    @ColumnInfo(name = "createdBy")
    var createdBy: String? = null,

    @ColumnInfo(name = "createdDate")
    var createdDate: String? = null,

    @ColumnInfo(name = "updatedBy")
    var updatedBy: String? = null,

    @ColumnInfo(name = "updatedDate")
    var updatedDate: String,

    @ColumnInfo(name = "ncd_priority")
    var ncd_priority: Int = 0,

    @ColumnInfo(name = "cbac_available")
    var cbac_available: Boolean = false,

    @ColumnInfo(name = "guidelineId")
    var guidelineId: String? = null,

    @ColumnInfo(name = "villagename")
    var villagename: String? = null,

    @ColumnInfo(name = "deliveryDate")
    var deliveryDate: String? = null,

    @ColumnInfo(name = "BenRegId")
    var benRegId: Int = 0,

    @ColumnInfo(name = "ProviderServiceMapID")
    var providerServiceMapID: Int = 0,

    @ColumnInfo(name = "VanID")
    var vanID: Int = 0,

    @ColumnInfo(name = "Processed")
    var processed: String? = null,

    @ColumnInfo(name = "Countyid")
    var countyid: Int = 0,

    @ColumnInfo(name = "stateid")
    var stateid: Int = 0,

    @ColumnInfo(name = "districtid")
    var districtid: Int = 0,

    @ColumnInfo(name = "districtname")
    var districtname: String? = null,

    @ColumnInfo(name = "currSubDistrictId")
    var currSubDistrictId: Int = 0,

    @ColumnInfo(name = "villageid")
    var villageid: Int = 0,

    @ColumnInfo(name = "childname")
    var childname: String? = null,

    @ColumnInfo(name = "childBenId")
    var childBenId: Int = 0,

    @ColumnInfo(name = "childpos")
    var childpos: Int = 0,

    @ColumnInfo(name = "motherBenId")
    var motherBenId: Int = 0,

    @ColumnInfo(name = "hrpStatus")
    var isHrpStatus: Boolean = false,

    //    @ColumnInfo(name = "relatedBeneficiaryIds")
    //    List<RelatedBenIds> relatedBeneficiaryIds;

    @ColumnInfo(name = "hrp_identification_date")
    var hrp_identification_date: String? = null,

    @ColumnInfo(name = "hrp_last_vist_date")
    var hrp_last_vist_date: String? = null,

    @ColumnInfo(name = "lastHrpVisitDate")
    var lastHrpVisitDate: String? = null,

    @ColumnInfo(name = "nishchayPregnancyStatus")
    var nishchayPregnancyStatus: String? = null,

    @ColumnInfo(name = "nishchayPregnancyStatusPosition")
    var nishchayPregnancyStatusPosition: Int = 0,

    @ColumnInfo(name = "nishchayDeliveryStatus")
    var nishchayDeliveryStatus: String? = null,

    @ColumnInfo(name = "nishchayDeliveryStatusPosition")
    var nishchayDeliveryStatusPosition: Int = 0,

    @ColumnInfo(name = "nayiPahalDeliveryStatus")
    var nayiPahalDeliveryStatus: String? = null,

    @ColumnInfo(name = "nayiPahalDeliveryStatusPosition")
    var nayiPahalDeliveryStatusPosition: Int = 0,

    @ColumnInfo(name = "suspected_hrp")
    var suspected_hrp: String? = null,

    @ColumnInfo(name = "suspected_ncd")
    var suspected_ncd: String? = null,

    @ColumnInfo(name = "suspected_tb")
    var suspected_tb: String? = null,

    @ColumnInfo(name = "suspected_ncd_diseases")
    var suspected_ncd_diseases: String? = null,

    @ColumnInfo(name = "confirmed_ncd")
    var confirmed_ncd: String? = null,

    @ColumnInfo(name = "confirmed_hrp")
    var confirmed_hrp: String? = null,

    @ColumnInfo(name = "confirmed_tb")
    var confirmed_tb: String? = null,

    @ColumnInfo(name = "confirmed_ncd_diseases")
    var confirmed_ncd_diseases: String? = null,

    @ColumnInfo(name = "diagnosis_status")
    var diagnosis_status: String? = null,

    @ColumnInfo(name = "facilityOther")
    var facilityOther: String? = null,

    @ColumnInfo(name = "noOfDaysForDelivery")
    var noOfDaysForDelivery: Int? = null,

    //    public String getFacility_other() {
    @ColumnInfo(name = "FamilyHeadRelationOther")
    var familyHeadRelationOther: String? = null,

    @ColumnInfo(name = "immunizationStatus")
    var isImmunizationStatus: Boolean = false
)

private fun getLongFromDate(dateString: String): Long {
    val f = SimpleDateFormat("yyyy-MM-DDThh:mm:ss.sssTZD", Locale.ENGLISH)
    val date = f.parse(dateString)
    return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
}

fun asCacheModel(benRegNetwork: BenRegNetwork, newBornRegNetwork: NewBornRegNetwork?): BenRegCache {

    benRegNetwork.apply {
       val ben = BenRegCache(
           householdId = houseoldId.toLong(),
           beneficiaryId = benficieryid,
           ashaId = ashaid,
           age = age,
           ageUnit = when (age_unit) {
               "Year(s)" -> AgeUnit.YEARS
               "Month(s)" -> AgeUnit.MONTHS
               "Day(s)" -> AgeUnit.DAYS
               else -> AgeUnit.YEARS
           },
           isKid = !(age_unit == "Year(s)" && age > 14),
           isAdult = (age_unit == "Year(s)" && age > 14),
           userImage = user_image,
           userImageBlob = user_image1,
           regDate = getLongFromDate(registrationDate),
           firstName = firstName,
           lastName = lastName,
           gender = when(gender) {
               "Male" -> Gender.MALE
               "Female" -> Gender.FEMALE
               "Transgender" -> Gender.TRANSGENDER
               else -> Gender.MALE
           },
           genderId = genderId,
           dob = getLongFromDate(dob),
           age_unitId = age_unitId,
           fatherName = fatherName,
           motherName = motherName,
           familyHeadRelation = familyHeadRelation,
           familyHeadRelationPosition = familyHeadRelationPosition,
           familyHeadRelationOther = familyHeadRelationOther,
           mobileNoOfRelation = mobilenoofRelation,
           mobileNoOfRelationId = mobilenoofRelationId,
           mobileOthers = mobileOthers,
           contactNumber = contact_number?.toLong() ?:0L,
           literacy = literacy,
           literacyId = literacyId,
           community = community,
           communityId = communityId,
           religion = religion,
           religionId = religionID,
           religionOthers = religionOthers,
           rchId = rchid,
           registrationType = when(registrationType) {
               "Infant" -> TypeOfList.INFANT
               "Child" -> TypeOfList.CHILD
               "Adolescent" -> TypeOfList.ADOLESCENT
               "General" -> TypeOfList.GENERAL
               "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
               "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
               "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
               "Postnatal Mother" -> TypeOfList.POSTNATAL_MOTHER
               "Menopause" -> TypeOfList.MENOPAUSE
               "Teenager" -> TypeOfList.TEENAGER
               else -> TypeOfList.OTHER
           },
           latitude = latitude,
           longitude = longitude,
           aadharNum = aadhaNo,
           aadharNumId = aadha_noId,
           hasAadhar = (aadhaNo != null),
           hasAadharId = aadha_noId,
           bankAccountId = bank_accountId,
           bankAccount = bankAccount,
           nameOfBank = nameOfBank,
           nameOfBranch = nameOfBranch,
           ifscCode = ifscCode,
           needOpCare = need_opcare,
           needOpCareId = need_opcareId,
           ncdPriority = ncd_priority,
           cbacAvailable = cbac_available,
           guidelineId = guidelineId,
           isHrpStatus = isHrpStatus,
           hrpIdentificationDate = hrp_identification_date,
           hrpLastVisitDate = hrp_last_vist_date,
           nishchayPregnancyStatus = nishchayPregnancyStatus,
           nishchayPregnancyStatusPosition = nishchayPregnancyStatusPosition,
           nishchayDeliveryStatus = nishchayDeliveryStatus,
           nishchayDeliveryStatusPosition = nishchayDeliveryStatusPosition,
           nayiPahalDeliveryStatus = nayiPahalDeliveryStatus,
           nayiPahalDeliveryStatusPosition = nayiPahalDeliveryStatusPosition,
           suspectedNcd = suspected_ncd,
           suspectedNcdDiseases = suspected_ncd_diseases,
           suspectedTb = suspected_tb,
           confirmed_Ncd = confirmed_ncd,
           confirmedHrp = confirmed_hrp,
           confirmedTb = confirmed_tb,
           confirmedNcdDiseases = confirmed_ncd_diseases,
           diagnosisStatus = diagnosis_status,
           noOfDaysForDelivery = noOfDaysForDelivery,
           countyId = countyid,
           stateId = stateid,
           districtId = districtid,
           districtName = districtname,
           currSubDistrictId = currSubDistrictId,
           villageId = villageid,
           villageName = villagename,
           processed = processed,
           serverUpdatedStatus = serverUpdatedStatus,
           createdBy = createdBy,
           createdDate = getLongFromDate(updatedDate),
           kidDetails = BenRegKid(
               childRegisteredAWC = childRegisteredAWC,
               childRegisteredAWCId = childRegisteredAWCID,
               childRegisteredSchool = childRegisteredSchool,
               childRegisteredSchoolId = childRegisteredSchoolID,
               typeOfSchool = typeofSchool,
               typeOfSchoolId = typeofSchoolID
               ),
           genDetails = BenRegGen(
               maritalStatus = maritalstatus,
               maritalStatusId = maritalstatusId,
               spouseName = spousename,
               ageAtMarriage = ageAtMarriage,
               dateOfMarriage = getLongFromDate(dateMarriage),
               marriageDate = marriageDate,
               menstrualStatus = menstrualStatus,
               menstrualStatusId = menstrualStatusId,
               regularityOfMenstrualCycle = regularityofMenstrualCycle,
               regularityOfMenstrualCycleId = regularityofMenstrualCycleId,
               lengthOfMenstrualCycle = lengthofMenstrualCycle,
               lengthOfMenstrualCycleId = lengthofMenstrualCycleId,
               menstrualBFD = menstrualBFD,
               menstrualBFDId = menstrualBFDId,
               menstrualProblem = menstrualProblem,
               menstrualProblemId = menstrualProblemId,
               lastMenstrualPeriod = lastMenstrualPeriod,
               reproductiveStatus = reproductiveStatus,
               reproductiveStatusId = reproductiveStatusId,
               lastDeliveryConducted = lastDeliveryConducted,
               lastDeliveryConductedId = lastDeliveryConductedID.toString(),
               facilityName = facilitySelection,
               whoConductedDelivery = whoConductedDelivery,
               whoConductedDeliveryId = whoConductedDeliveryID.toString(),
               deliveryDate = deliveryDate,
               expectedDateOfDelivery = expectedDateOfDelivery,

               ),
           syncState = SyncState.UNSYNCED,
           isDraft = false
       )
        newBornRegNetwork?.let {
            ben.kidDetails?.childName = it.childName
            ben.kidDetails?.birthPlace = it.birthPlace
            ben.kidDetails?.birthPlaceId = it.birthPlaceid.toString()
            ben.kidDetails?.facilityName = it.facilityName
            ben.kidDetails?.facilityid = it.facilityid.toString()
            ben.kidDetails?.facilityOther = it.facilityOther
            ben.kidDetails?.placeName = it.placeName
            ben.kidDetails?.conductedDelivery = it.conductedDelivery
            ben.kidDetails?.conductedDeliveryId = it.conductedDeliveryid.toString()
            ben.kidDetails?.conductedDeliveryOther = it.conductedDeliveryOther
            ben.kidDetails?.deliveryType = it.deliveryType
            ben.kidDetails?.deliveryTypeId = it.deliveryTypeid.toString()
            ben.kidDetails?.complications = it.complecations
            ben.kidDetails?.complicationsId = it.complecationsid.toString()
            ben.kidDetails?.complicationsOther = it.complicationsOther
            ben.kidDetails?.term = it.term
            ben.kidDetails?.termid = it.termid.toString()
            ben.kidDetails?.gestationalAge = it.gestationalAge
            ben.kidDetails?.gestationalAgeId = it.gestationalAgeid.toString()
            ben.kidDetails?.corticosteroidGivenMother = it.corticosteroidGivenMother
            ben.kidDetails?.corticosteroidGivenMotherId = it.corticosteroidGivenMotherid.toString()
            ben.kidDetails?.criedImmediately = it.criedImmediately
            ben.kidDetails?.criedImmediatelyId = it.criedImmediatelyid.toString()
            ben.kidDetails?.birthDefects = it.birthDefects
            ben.kidDetails?.birthDefectsId = it.birthDefectsid.toString()
            ben.kidDetails?.heightAtBirth = it.heightAtBirth.toString()
            ben.kidDetails?.weightAtBirth = it.weightAtBirth.toString()
            ben.kidDetails?.feedingStarted = it.feedingStarted
            ben.kidDetails?.feedingStartedId = it.feedingStartedid.toString()
            ben.kidDetails?.birthDosage = it.birthDosage
            ben.kidDetails?.birthDosageId = it.birthDosageid.toString()
            ben.kidDetails?.opvBatchNo = it.opvBatchNo
            ben.kidDetails?.opvGivenDueDate = it.opvGivenDueDate
            ben.kidDetails?.opvDate = it.opvDate
            ben.kidDetails?.bcdBatchNo = it.bcdBatchNo
            ben.kidDetails?.bcgGivenDueDate = it.bcgGivenDueDate
            ben.kidDetails?.bcgDate = it.bcgDate
            ben.kidDetails?.hptBatchNo = it.hptdBatchNo
            ben.kidDetails?.hptGivenDueDate = it.hptGivenDueDate
            ben.kidDetails?.hptDate = it.hptDate
            ben.kidDetails?.vitaminKBatchNo = it.vitaminkBatchNo
            ben.kidDetails?.vitaminKGivenDueDate = it.vitaminkGivenDueDate
            ben.kidDetails?.vitaminKDate = it.vitaminkDate
            ben.kidDetails?.deliveryTypeOther = it.deliveryTypeOther
            ben.kidDetails?.motherBenId = it.motherBenId.toString()
            ben.kidDetails?.childMotherName = it.motherName
            ben.kidDetails?.motherPosition = it.motherposition.toString()
            ben.kidDetails?.birthBCG = it.birthBCG
            ben.kidDetails?.birthHepB = it.birthHepB
            ben.kidDetails?.birthOPV = it.birthOPV
        }
        return ben;
   }

}


