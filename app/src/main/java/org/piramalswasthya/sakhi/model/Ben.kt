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
    val houseoldId: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "benficieryid")
    val benficieryid: Long = 0,
//Benficiery Registration fields

//Benficiery Registration fields
    @ColumnInfo(name = "ashaid")
    val ashaid: Int = 0,

    @ColumnInfo(name = "registrationDate")
    val registrationDate: String? = null,

    @ColumnInfo(name = "age")
    val age: Int = 0,

    @ColumnInfo(name = "age_unit")
    val age_unit: String? = null,

    @ColumnInfo(name = "age_unitId")
    val age_unitId: Int = 0,

    @ColumnInfo(name = "marriageDate")
    val marriageDate: String? = null,

    @ColumnInfo(name = "mobilenoofRelation")
    val mobilenoofRelation: String? = null,

    @ColumnInfo(name = "mobilenoofRelationId")
    val mobilenoofRelationId: Int = 0,

    @ColumnInfo(name = "mobileOthers")
    val mobileOthers: String? = null,

    @ColumnInfo(name = "literacy")
    val literacy: String? = null,

    @ColumnInfo(name = "literacyId")
    val literacyId: Int = 0,

    @ColumnInfo(name = "religionOthers")
    val religionOthers: String? = null,

    @ColumnInfo(name = "rchid")
    val rchid: String? = null,

    @ColumnInfo(name = "registrationType")
    val registrationType: String? = null,

    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,

//Bank details
    @ColumnInfo(name = "aadha_no")
    val aadha_no: String? = null,

    @ColumnInfo(name = "aadha_noId")
    val aadha_noId: Int = 0,

    @ColumnInfo(name = "aadhaNo")
    val aadhaNo: String? = null,

    @ColumnInfo(name = "need_opcare")
    val need_opcare: String? = null,

    @ColumnInfo(name = "need_opcareId")
    val need_opcareId: Int = 0,

//Menstrual details
    @ColumnInfo(name = "menstrualStatusId")
    val menstrualStatusId: Int = 0,

    @ColumnInfo(name = "regularityofMenstrualCycleId")
    val regularityofMenstrualCycleId: Int = 0,

    @ColumnInfo(name = "lengthofMenstrualCycleId")
    val lengthofMenstrualCycleId: Int = 0,

    @ColumnInfo(name = "menstrualBFDId")
    val menstrualBFDId: Int = 0,

    @ColumnInfo(name = "menstrualProblemId")
    val menstrualProblemId: Int = 0,

    @ColumnInfo(name = "lastMenstrualPeriod")
    val lastMenstrualPeriod: String? = null,

    @ColumnInfo(name = "reproductiveStatus")
    val reproductiveStatus: String? = null,

    @ColumnInfo(name = "reproductiveStatusId")
    val reproductiveStatusId: Int = 0,

    @ColumnInfo(name = "noOfDaysForDelivery")
    val noOfDaysForDelivery: Int? = null,

    @ColumnInfo(name = "formStatus")
    val formStatus: String? = null,

    @ColumnInfo(name = "formType")
    val formType: String? = null,

//these are new fields of new borr registartion
    @ColumnInfo(name = "childRegisteredAWCID")
    val childRegisteredAWCID: Int = 0,

    @ColumnInfo(name = "childRegisteredSchoolID")
    val childRegisteredSchoolID: Int = 0,

    @ColumnInfo(name = "TypeofSchoolID")
    val typeofSchoolID: Int = 0,

//strngs
    @ColumnInfo(name = "childRegisteredAWC")
    val childRegisteredAWC: String? = null,

    @ColumnInfo(name = "childRegisteredSchool")
    val childRegisteredSchool: String? = null,

    @ColumnInfo(name = "TypeofSchool")
    val typeofSchool: String? = null,

//these are new fields of registration for asha login
    @ColumnInfo(name = "PreviousLiveBirth")
    val previousLiveBirth: String? = null,

    @ColumnInfo(name = "LastDeliveryConductedID")
    val lastDeliveryConductedID: Int = 0,

//    @ColumnInfo(name = "FacilitySectionID")
//     int facilitySectionID;
    @ColumnInfo(name = "WhoConductedDeliveryID")
    val whoConductedDeliveryID: Int = 0,

    @ColumnInfo(name = "FamilyHeadRelation")
    val familyHeadRelation: String? = null,

    @ColumnInfo(name = "FamilyHeadRelationPosition")
    val familyHeadRelationPosition: Int = 0,


//strngs
    @ColumnInfo(name = "WhoConductedDelivery")
    val whoConductedDelivery: String? = null,

    @ColumnInfo(name = "LastDeliveryConducted")
    val lastDeliveryConducted: String? = null,

    @ColumnInfo(name = "facilitySelection")
    val facilitySelection: String? = null,


    @ColumnInfo(name = "ServerUpdatedStatus")
    val serverUpdatedStatus: Int = 0,

    @ColumnInfo(name = "createdBy")
    val createdBy: String? = null,

    @ColumnInfo(name = "createdDate")
    val createdDate: String? = null,

    @ColumnInfo(name = "ncd_priority")
    val ncd_priority: Int = 0,

    @ColumnInfo(name = "guidelineId")
    val guidelineId: String? = null,

    @ColumnInfo(name = "villagename")
    val villagename: String? = null,

    @ColumnInfo(name = "VanID")
    var VanID: Int = 0,

    @ColumnInfo(name = "Countyid")
    var Countyid: Int = 0,

    @ColumnInfo(name = "ProviderServiceMapID")
    var ProviderServiceMapID: Int = 0,

    @ColumnInfo(name = "Processed")
    var Processed: String? = null,

    @ColumnInfo(name = "currSubDistrictId")
    val currSubDistrictId: Int = 0,

    @ColumnInfo(name = "expectedDateOfDelivery")
    val expectedDateOfDelivery: String? = null,

    @ColumnInfo(name = "hrpStatus")
    val hrpStatus: Boolean = false,

    @ColumnInfo(name = "menstrualStatus")
    val menstrualStatus: String? = null,

    @ColumnInfo(name = "ageAtMarriage")
    val ageAtMarriage: Int = 0,

    @ColumnInfo(name = "dateMarriage")
    val dateMarriage: String? = null,

    @ColumnInfo(name = "deliveryDate")
    val deliveryDate: String? = null,


    @ColumnInfo(name = "suspected_hrp")
    val suspected_hrp: String? = null,

    @ColumnInfo(name = "suspected_ncd")
    val suspected_ncd: String? = null,

    @ColumnInfo(name = "suspected_tb")
    val suspected_tb: String? = null,

    @ColumnInfo(name = "suspected_ncd_diseases")
    val suspected_ncd_diseases: String? = null,

    @ColumnInfo(name = "confirmed_ncd")
    val confirmed_ncd: String? = null,

    @ColumnInfo(name = "confirmed_hrp")
    val confirmed_hrp: String? = null,

    @ColumnInfo(name = "confirmed_tb")
    val confirmed_tb: String? = null,


    @ColumnInfo(name = "confirmed_ncd_diseases")
    val confirmed_ncd_diseases: String? = null,

    @ColumnInfo(name = "diagnosis_status")
    val diagnosis_status: String? = null,

    @ColumnInfo(name = "nishchayPregnancyStatus")
    val nishchayPregnancyStatus: String? = null,

    @ColumnInfo(name = "nishchayPregnancyStatusPosition")
    val nishchayPregnancyStatusPosition: Int = 0,

    @ColumnInfo(name = "nishchayDeliveryStatus")
    val nishchayDeliveryStatus: String? = null,

    @ColumnInfo(name = "nishchayDeliveryStatusPosition")
    val nishchayDeliveryStatusPosition: Int = 0,

    @ColumnInfo(name = "nayiPahalDeliveryStatus")
    val nayiPahalDeliveryStatus: String? = null,

    @ColumnInfo(name = "nayiPahalDeliveryStatusPosition")
    val nayiPahalDeliveryStatusPosition: Int = 0,
){
//    fun asCacheModel(): BenRegCache {
//        return BenRegCache(
//
//        )
//    }
}