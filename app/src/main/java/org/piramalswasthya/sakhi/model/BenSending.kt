package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.model.Gender.*
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class BeneficiaryDataSending(
    @Json(name = "ageAtMarriage")
    val ageAtMarriage: String,

    @Json(name = "benImage")
    val benImage: String = "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\noQ4fRuea0nCk6D9KynBxIIwoak/UVlSKH//Z\n",

    @Json(name = "benPhoneMaps")
    val benPhoneMaps: Array<BenPhoneMaps>,

    @Json(name = "beneficiaryIdentities")
    val beneficiaryIdentities: Array<BeneficiaryIdentities>,

    @Json(name = "createdBy")
    val createdBy: String,

    @Json(name = "dOB")
    private val dOB: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "emergencyRegistration")
    val isEmergencyRegistration: Boolean = false,

    @Json(name = "fatherName")
    val fatherName: String,

    @Json(name = "firstName")
    val firstName: String,


    @Json(name = "genderID")
    val genderID: Int = 0,

    @Json(name = "genderName")
    val genderName: String,

    @Json(name = "govtIdentityNo")
    val govtIdentityNo: String,

    @Json(name = "govtIdentityTypeID")
    val govtIdentityTypeID: String,

    @Json(name = "i_bendemographics")
    val benDemographics: BenDemographics,

    @Json(name = "lastName")
    val lastName: String,


    @ColumnInfo(name = "marriageDate")
    val marriageDate: String,

    @Json(name = "spouseName")
    val spouseName: String,

    @Json(name = "titleId")
    val titleId: String,


    @Json(name = "parkingPlaceID")
    val parkingPlaceID: Int = 0,

    @Json(name = "bankName")
    val bankName: String? = null,

    @Json(name = "providerServiceMapID")
    val providerServiceMapID: String,

    @Json(name = "maritalStatusID")
    val maritalStatusID: String,

    @Json(name = "vanID")
    val vanID: Int = 0,


    @Json(name = "accountNo")
    val accountNo: String? = null,


    @Json(name = "ifscCode")
    val ifscCode: String? = null,


    @Json(name = "motherName")
    val motherName: String,

    @Json(name = "branchName")
    val branchName: String? = null,

    @Json(name = "providerServiceMapId")
    val providerServiceMapId: String,


    @Json(name = "maritalStatusName")
    val maritalStatusName: String,

    )

data class BenDemographics(
    @Json(name = "incomeStatusName")
    var incomeStatusName: String = "null",

    @Json(name = "blockName")
    var blockName: String = "null",

    @Json(name = "occupationName")
    var occupationName: String = "null",

    @Json(name = "stateID")
    var stateID: Int = 0,

    @Json(name = "districtBranchID")
    var districtBranchID: Int = 0,

    @Json(name = "parkingPlaceID")
    var parkingPlaceID: Int = 0,

    @Json(name = "incomeStatusID")
    var incomeStatusID: String = "null",

    @Json(name = "blockID")
    var blockID: Int = 0,

    @Json(name = "districtID")
    var districtID: Int = 0,

    @Json(name = "stateName")
    var stateName: String = "null",

    @Json(name = "educationName")
    var educationName: String = "null",

    @Json(name = "addressLine1")
    var addressLine1: String = "null",

    @Json(name = "communityName")
    var communityName: String = "null",

    @Json(name = "addressLine2")
    var addressLine2: String = "null",

    @Json(name = "religionID")
    var religionID: String = "null",

    @Json(name = "religionName")
    var religionName: String = "null",

    @Json(name = "zoneName")
    var zoneName: String = "null",

    @Json(name = "addressLine3")
    var addressLine3: String = "null",

    @Json(name = "districtBranchName")
    var districtBranchName: String = "null",

    @Json(name = "districtName")
    var districtName: String = "null",

    @Json(name = "habitation")
    var habitation: String = "null",

    @Json(name = "servicePointID")
    var servicePointID: String = "null",

    @Json(name = "countryID")
    var countryID: Int = 0,

    @Json(name = "educationID")
    var educationID: String = "null",

    @Json(name = "occupationID")
    var occupationID: String = "null",

    @Json(name = "parkingPlaceName")
    var parkingPlaceName: String = "null",

    @Json(name = "pinCode")
    var pinCode: String = "null",

    @Json(name = "zoneID")
    var zoneID: Int = 0,

    @Json(name = "countryName")
    var countryName: String = "null",

    @Json(name = "communityID")
    var communityID: String = "null",

    @Json(name = "servicePointName")
    var servicePointName: String = "null"
)


data class BeneficiaryIdentities(
    @Json(name = "govtIdentityNo")
    var govtIdentityNo: Int = 0,

    @Json(name = "govtIdentityTypeID")
    var govtIdentityTypeID: Int = 0,

    @Json(name = "govtIdentityTypeName")
    var govtIdentityTypeName: String = "null",

    @Json(name = "identityType")
    var identityType: String,

    @Json(name = "createdBy")
    var createdBy: String,
)


data class BenPhoneMaps(

    @Json(name = "createdBy")
    var createdBy: String,

    @Json(name = "phoneNo")
    var phoneNo: String

)

fun BenRegCache.asNetworkSendingModel(
    user: UserCache,
    locationRecord: LocationRecord
): BeneficiaryDataSending {

    return BeneficiaryDataSending(
        firstName = firstName!!,
        lastName = lastName ?: "NA",
        dOB = getDateTimeStringFromLong(dob),
        fatherName = fatherName!!,
        motherName = motherName!!,
        spouseName = genDetails?.spouseName ?: "NA",
        govtIdentityNo = "",
        govtIdentityTypeID = "",
        isEmergencyRegistration = false,
        titleId = "",
        //benImage = null,
        bankName = nameOfBank,
        branchName = nameOfBranch,
        ifscCode = ifscCode,
        accountNo = bankAccount,
        ageAtMarriage = genDetails?.ageAtMarriage?.toString() ?: "0",
        marriageDate = genDetails?.marriageDate ?: "0",
        genderID = genderId,
        genderName = when (gender) {
            MALE -> "Male"
            FEMALE -> "Female"
            TRANSGENDER -> "Transgender"
            null -> "NA"
        },
        maritalStatusID = genDetails?.maritalStatusId?.toString() ?: "0",
        maritalStatusName = genDetails?.maritalStatus.toString(),
        email = "",
        providerServiceMapID = user.serviceMapId.toString(),
        providerServiceMapId = user.serviceMapId.toString(),
        benDemographics = BenDemographics(
            communityID = communityId.toString(),
            communityName = community ?: "",
            religionID = religionId.toString(),
            religionName = religion ?: "",
            countryID = 1,
            countryName = "India",
            stateID = locationRecord.stateId,
            stateName = locationRecord.state,
            districtID = locationRecord.districtId,
            districtName = locationRecord.district,
            blockID = locationRecord.blockId,
            districtBranchID = locationRecord.villageId,
            districtBranchName = locationRecord.village,
            zoneID = user.zoneId,
            zoneName = user.zoneName,
            parkingPlaceName = user.parkingPlaceName,
            parkingPlaceID = user.parkingPlaceId,
            servicePointID = user.servicePointId.toString(),
            servicePointName = user.servicePointName,
            addressLine1 = "D.No 3-160E",
            addressLine2 = "ARS Road",
            addressLine3 = "Neggipudi",
        ),
        benPhoneMaps = arrayOf(
            BenPhoneMaps(
                phoneNo = contactNumber.toString(),
                createdBy = user.userName,
            )
        ),
        beneficiaryIdentities = arrayOf(
            BeneficiaryIdentities(
                govtIdentityNo = 0,
                govtIdentityTypeName = "null",
                govtIdentityTypeID = 0,
                identityType = "National ID",
                createdBy = user.userName

            )
        ),
        vanID = user.vanId,
        parkingPlaceID = user.parkingPlaceId,
        createdBy = user.userName,


        )
}
private fun getDateTimeStringFromLong(dateLong: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    val dateString = dateFormat.format(dateLong)
    val timeString = timeFormat.format(dateLong)
    System.currentTimeMillis()
    return "${dateString}T${timeString}.000Z"
}

    


