package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CDR",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "cdrInd", value = ["benId", "hhId"])]
)

data class CDRCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    var childName: String? = null,
    var dateOfBirth: Long = 0,
    var age: Int = 0,
    var visitDate: Long? = System.currentTimeMillis(),
    var gender: String? = null,
    var motherName: String? = null,
    var fatherName: String? = null,
    var address: String? = null,
    var houseNumber: String? = null,
    var mohalla: String? = null,
    var landmarks: String? = null,
    var pincode: Int = 0,
    var landline: Long = 0,
    var mobileNumber: Long = 0,
    var dateOfDeath: Long = System.currentTimeMillis(),
    var timeOfDeath: Long = 0,
    var placeOfDeath: String? = null,
    var firstInformant: String? = null,
    var ashaSign: String? = null,
    var dateOfNotification: Long = System.currentTimeMillis(),
    var createdBy: String? = null,
    var createdDate: Long? = System.currentTimeMillis(),
    var processed: String? = null,
) {
    fun asPostModel(user: UserCache, household: HouseholdCache, ben: BenRegCache, cdrCount: Int): CDRPost {
        return CDRPost(
            beneficiaryid = benId,
            houseoldId = hhId.toString(),
            villageid = household.villageId,
            cdrBabyName = ben.firstName,
            cdrDob = getDateTimeStringFromLong(ben.dob),
            cdrAge = ben.age.toString(),
            cdrGender = when (ben.gender) {
                Gender.FEMALE -> "Female"
                Gender.MALE -> "Male"
                Gender.TRANSGENDER -> "Transgender"
                else -> "Other"
            },
            cdrYears = getYear(),
            cdrMonths = getMonth(),
            cdrDays = getDays(),
            motherName = motherName,
            fatherName = fatherName,
            vistDate = getDateTimeStringFromLong(visitDate),
            cdrAddress = address,
            cdrHouseNo = houseNumber,
            cdrColony = mohalla,
            cdrBlock = household.block,
            cdrDistrict = household.district,
            cdrState = household.state,
            cdrPincode = pincode.toString(),
            cdrLandmarks = landmarks,
            cdrLandline = landline.toString(),
            cdrMobile = mobileNumber.toString(),
            cdrDateDeath = getDateTimeStringFromLong(dateOfDeath),
            placeOfDeath = placeOfDeath,
            nameOfInformant = firstInformant,
            cdrTime = "",
            cdrSignature = ashaSign,
            cdrNotificationDate = getDateTimeStringFromLong(dateOfNotification),
            createdBy = createdBy,
            createdDate = createdDate,
            updatedBy = user.userName,
            updatedDate = System.currentTimeMillis()/ 1000L,
            editFlag = false,
            loginId = (cdrCount+101).toString(),
            id = cdrCount+101
        )
    }
}

data class CDRPost(
    val id: Int = 0,
    val beneficiaryid: Long = 0,
    val houseoldId: String? = null,
    val villageid: Int = 0,
    val syncstatus: Boolean? = null,
    val cdrBabyName: String? = null,
    val cdrDob: String? = null,
    val cdrAge: String? = null,
    val cdrGender: String? = null,
    val cdrYears: String? = null,
    val cdrMonths: String? = null,
    val cdrDays: String? = null,
    val cdrHours: String? = null,
    val motherName: String? = null,
    val fatherName: String? = null,
    val vistDate: String? = null,
    val cdrAddress: String? = null,
    val cdrHouseNo: String? = null,
    val cdrColony: String? = null,
    val cdrBlock: String? = null,
    val cdrDistrict: String? = null,
    val cdrState: String? = null,
    val cdrPincode: String? = null,
    val cdrLandmarks: String? = null,
    val cdrLandline: String? = null,
    val cdrMobile: String? = null,
    val cdrDateDeath: String? = null,
    val placeOfDeath: String? = null,
    val nameOfInformant: String? = null,
    val cdrTime: String? = null,
    val cdrSignature: String? = null,
    val cdrNotificationDate: String? = null,
    val createdBy: String? = null,
    val createdDate: Long? = 0L,
    val updatedBy: String? = null,
    val updatedDate: Long? = 0L,
    val editFlag: Boolean? = null,
    val loginId: String? = null
)