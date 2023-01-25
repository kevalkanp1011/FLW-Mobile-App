package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.database.room.SyncState
import java.text.SimpleDateFormat
import java.util.*

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


@Entity(
    tableName = "BENEFICIARY",
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

    @PrimaryKey(autoGenerate = true)
    var beneficiaryId: Long = 0,

    @ColumnInfo(index = true)
    var ashaId: Int,

    var userImage: String? = null,

    @Suppress("ArrayInDataClass")
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var userImageBlob: ByteArray? = null,

    var regDate: Long? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var gender: String? = null,

    var genderId: Int = 0,

    var dob: Long = 0,

    var age: Int = 0,

    var age_unit: String? = null,

    var age_unitId: Int = 0,

    var maritalStatus: String? = null,

    var maritalStatusId: Int = 0,

    var spouseName: String? = null,

    var ageAtMarriage : Int = 0,

    var dateOfMarriage: Long = 0,

    var marriageDate: String? = null,

    var fatherName: String? = null,

    var motherName: String? = null,

    var contactNumber: String? = null,

    var mobileNoOfRelation: String? = null,

    var mobileNoOfRelationId : Int= 0,

    var mobileOthers: String? = null,

    var literacy: String? = null,

    var literacyId :Int = 0,

    var community: String? = null,

    var communityId : Int = 0,

    var religion: String? = null,

    var religionId : Int = 0,

    var religionOthers: String? = null,

    var rchId: String? = null,

    var registrationType: String? = null,

    var latitude: Double = 0.0,

    var longitude: Double = 0.0,

        //Bank details
    var aadharNum: String? = null,

    var aadharNumId : Int = 0,

    var bankAccountId: Int = 0,

    var bankAccount: String? = null,

    var nameOfBank: String? = null,

    var nameOfBranch: String? = null,

    var ifscCode: String? = null,

    var needOpCare: String? = null,

    var needOpCareId: Int = 0,

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

    var formStatus: String? = null,

    var formType: String? = null,

    var ancCount: Int = 0,

    var hrpCount: Int = 0,

    var hrpSuspected: Boolean = false,

    var isDeathStatus : Boolean = false,

    var childRegisteredAWC: String? = null,

    var childRegisteredAWCId: Int = 0,

    var childRegisteredSchool: String? = null,

    var childRegisteredSchoolId: Int = 0,

    var typeOfSchool: String? = null,

    var typeOfSchoolId: Int = 0,

    var expectedDateOfDelivery: String? = null,

    var previousLiveBirth: String? = null,

    //these are new fields of new born registration
    var lastDeliveryConducted: String? = null,

    var lastDeliveryConductedID: Int = 0,

    var facilitySelection: String? = null,

    var whoConductedDelivery: String? = null,

    var whoConductedDeliveryID: Int = 0,

    //these are new fields of registration for asha login
    var familyHeadRelation: String? = null,

    var familyHeadRelationPosition: Int = 0,

    var serverUpdatedStatus: Int = 0,

    var createdBy: String? = null,

    var createdDate: String? = null,

    var updatedBy: String? = null,

    var updatedDate: String? = null,

    var ncdPriority: Int = 0,

    var cbacAvailable: Boolean = false,

    var guidelineId: String? = null,

    var villageName: String? = null,

    var deliveryDate: String? = null,

    var benRegId: Int = 0,

/*    var providerServiceMapID: Int = 0,

    var vanID = 0,*/
    var processed: String? = null,

    var countyId: Int = 0,

    var stateId: Int = 0,

    var districtId: Int = 0,

    var districtName: String? = null,

    var currSubDistrictId: Int = 0,

    var villageId: Int = 0,

    var childName: String? = null,

    var childBenId: Long = 0,

    var childPos: Int = 0,

    var motherBenId: Long?=null,

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

    var facilityOther: String? = null,

    var noOfDaysForDelivery: Int? = null,

    var familyHeadRelationOther: String? = null,

    var isImmunizationStatus: Boolean = false,

    /*
    5 Skipped:
        Aadhar, lastHrpVisitDate, marriageDate ( 2 copies)
        vanId and serviceMap ID, ( Can get from Foreign key)
     */
    var syncState : SyncState,

    var isDraft : Boolean
){

    companion object{
        private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    }
    fun asBasicDomainModel() : BenBasicDomain{
        return BenBasicDomain(
            benId = beneficiaryId,
            hhId = householdId,
            regDate = dateFormat.format(Date(regDate!!)),
            benName = firstName?:"Not Available",
            benSurname = lastName?:"Not Available",
            gender = gender?:"Not Available",
            age = if(age==0) "Not Available" else "$age $age_unit",
            mobileNo = contactNumber?:mobileNoOfRelation?:"Not Available",
            fatherName = fatherName,
            familyHeadName = "Not implemented at the moment!",
            typeOfList = registrationType?:"Not Available",
            rchId = rchId?:"Not Available",
            hrpStatus = confirmedHrp?:"Not Available",
            syncState = syncState
        )
    }
}
