package org.piramalswasthya.sakhi.network

import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.TBScreeningCache
import org.piramalswasthya.sakhi.model.TBSuspectedCache
import java.text.SimpleDateFormat
import java.util.Locale

@JsonClass(generateAdapter = true)
data class D2DAuthUserRequest(
    val username: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class D2DAuthUserResponse(
    val jwt: String
)

@JsonClass(generateAdapter = true)
data class D2DSaveUserRequest(
    val id: Int,
    val username: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class D2DSaveUserResponse(
    val jwt: String
)


////////////////////---------TMC------------//////////////////////

@JsonClass(generateAdapter = true)
data class TmcAuthUserRequest(
    val userName: String,
    val password: String,
    val authKey: String = "",
    val doLogout: Boolean = true
)

@JsonClass(generateAdapter = true)
data class TmcUserDetailsRequest(
    val userID: Int
)

@JsonClass(generateAdapter = true)
data class TmcUserVanSpDetailsRequest(
    val userID: Int,
    val providerServiceMapID: Int
)


@JsonClass(generateAdapter = true)
data class TmcLocationDetailsRequest(
    val spID: Int,
    val spPSMID: Int
)

@JsonClass(generateAdapter = true)
data class TmcGenerateBenIdsRequest(
    val benIDRequired: Int,
    val vanID: Int
)

@JsonClass(generateAdapter = true)
data class GetBenRequest(
    val ashaId: Int,
    val pageNo: Int,
    val fromDate: String,
    val toDate: String
)

@JsonClass(generateAdapter = true)
data class BenResponse(
    val benId: String,
    val benRegId: Long,
    val abhaDetails: List<BenAbhaResponse>?,
    val toDate: String
)

@JsonClass(generateAdapter = true)
data class BenHealthDetails(
    val benHealthID: Int,
    val healthIdNumber: String,
    val beneficiaryRegID: Long,
    val healthId: String
)

@JsonClass(generateAdapter = true)
data class BenAbhaResponse(
    val BeneficiaryRegID: Long,
    val HealthID: String,
    val HealthIDNumber: String,
    val AuthenticationMode: String?,
    val CreatedDate: String?
)
///////////////-------------Abha id-------------/////////////////

@JsonClass(generateAdapter = true)
data class AbhaTokenRequest(
    val clientId: String = "SBX_001542",
    val clientSecret: String = "87b7eb89-b236-43b6-82b0-6eef154a9b90",
    val grantType: String = "client_credentials"
//    val clientId: String = "healthid-api",
//    val clientSecret: String = "9042c774-f57b-46ba-bb11-796a4345ada1"
)

@JsonClass(generateAdapter = true)
data class AbhaTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshExpiresIn: Int,
    val refreshToken: String,
    val tokenType: String
)

@JsonClass(generateAdapter = true)
data class AbhaGenerateAadhaarOtpRequest(
    var aadhaar: String
)

@JsonClass(generateAdapter = true)
data class AadhaarVerifyBioRequest(
    var aadhaar: String,
    var bioType: String,
    var pid: String
)

@JsonClass(generateAdapter = true)
data class AbhaGenerateAadhaarOtpResponse(
    val txnId: String
)

@JsonClass(generateAdapter = true)
data class AbhaGenerateAadhaarOtpResponseV2(
    val txnId: String,
    val mobileNumber: String
)

@JsonClass(generateAdapter = true)
data class AbhaResendAadhaarOtpRequest(
    val txnId: String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyAadhaarOtpRequest(
    val otp: String,
    val txnId: String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyAadhaarOtpResponse(
    val txnId: String
)


@JsonClass(generateAdapter = true)
data class AbhaGenerateMobileOtpRequest(
    val mobile: String,
    val txnId: String
)

@JsonClass(generateAdapter = true)
data class AbhaGenerateMobileOtpResponse(
    val txnId: String
)

data class AbhaCheckAndGenerateMobileOtpResponse(
    val mobileLinked: Boolean,
    val txnId: String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyMobileOtpRequest(
    val otp: String,
    val txnId: String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyMobileOtpResponse(
    val txnId: String
)

@JsonClass(generateAdapter = true)
data class StateCodeResponse(
    val code: String,
    val name: String,
    val districts: List<DistrictCodeResponse>?
)

@JsonClass(generateAdapter = true)
data class DistrictCodeResponse(
    val code: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class CreateAbhaIdGovRequest(

    val aadharNumber: Long,
    val benefitName: String,
    val consentHealthId: Boolean,
    val dateOfBirth: String,
    val gender: String,
    val name: String,
    val stateCode: Int,
    val districtCode: Int
)

@JsonClass(generateAdapter = true)
data class CreateAbhaIdRequest(

    val email: String?,
    val firstName: String?,
    val healthId: String?,
    val lastName: String?,
    val middleName: String?,
    val password: String?,
    val profilePhoto: String?,
    val txnId: String
)

@JsonClass(generateAdapter = true)
data class CreateHIDResponse(
    val hID: Long,
    val healthIdNumber: String?,
    val name: String?,
    val gender: String?,
    val yearOfBirth: String?,
    val monthOfBirth: String?,
    val dayOfBirth: String?,
    val firstName: String?,
    val healthId: String?,
    val lastName: String?,
    val middleName: String?,
    val stateCode: String?,
    val districtCode: String?,
    val stateName: String?,
    val districtName: String?,
    val email: String?,
    val kycPhoto: String?,
    val mobile: String?,
    val authMethod: String?,
    val authMethods: Array<String>?,
    val deleted: Boolean,
    val processed: String?,
    val createdBy: String?,
    val txnId: String?,
)

@JsonClass(generateAdapter = true)
data class CreateAbhaIdResponse(

    val token: String,
    val refreshToken: String,
    val healthIdNumber: String,
    val name: String,
    val gender: String,
    val yearOfBirth: String,
    val monthOfBirth: String,
    val dayOfBirth: String,
    val firstName: String,
    val healthId: String?,
    val lastName: String,
    val middleName: String,
    val stateCode: String,
    val districtCode: String,
    val stateName: String,
    val districtName: String,
    val email: String?,
    val kycPhoto: String?,
    val profilePhoto: String,
    val mobile: String,
    val authMethods: Array<String>,
    val pincode: String?,
    val tags: Map<String, String>?,
    val alreadyExists: String,
    val new: Boolean,
    var txnId: String
)

@JsonClass(generateAdapter = true)
data class GenerateOtpHid(
    val authMethod: String?,
    val healthId: String?,
    val healthIdNumber: String?
)

@JsonClass(generateAdapter = true)
data class ValidateOtpHid(
    val otp: String?,
    val txnId: String?,
    val authMethod: String?
)
@JsonClass(generateAdapter = true)
data class GetBenHealthIdRequest(
    val beneficiaryRegID: Long?,
    val beneficiaryID: Long?,
)
@JsonClass(generateAdapter = true)
data class CreateHealthIdRequest(
    val otp: String?,
    val txnId: String?,
    val address: String?,
    val dayOfBirth: String?,
    val email: String?,
    val profilePhoto: String?,
    val password: String?,
    val healthId: String?,
    val healthIdNumber: String?,
    val firstName: String?,
    val gender: String?,
    val lastName: String?,
    val middleName: String?,
    val monthOfBirth: String?,
    val name: String?,
    val pincode: Int?,
    val yearOfBirth: String?,
    val providerServiceMapID: Int?,
    val createdBy: String?
)

data class MapHIDtoBeneficiary(
    val beneficiaryRegID: Long?,
    val beneficiaryID: Long?,
    val healthId: String?,
    val healthIdNumber: String?,
    val providerServiceMapId: Int?,
    val createdBy: String?
)

data class TBScreeningRequestDTO(
    val userId: Int,
    val tbScreeningList: List<TBScreeningDTO>
)

data class TBScreeningDTO(
    val id: Long,
    val benId: Long,
    val visitDate: String?,
    var coughMoreThan2Weeks: Boolean?,
    var bloodInSputum: Boolean?,
    var feverMoreThan2Weeks: Boolean?,
    var lossOfWeight: Boolean?,
    var nightSweats: Boolean?,
    var historyOfTb: Boolean?,
    var takingAntiTBDrugs: Boolean?,
    var familySufferingFromTB: Boolean?
) {
    fun toCache(): TBScreeningCache {
        return TBScreeningCache(
            benId = benId,
            visitDate = getLongFromDate(visitDate),
            coughMoreThan2Weeks = coughMoreThan2Weeks,
            bloodInSputum = bloodInSputum,
            feverMoreThan2Weeks = feverMoreThan2Weeks,
            lossOfWeight = lossOfWeight,
            nightSweats = nightSweats,
            historyOfTb = historyOfTb,
            takingAntiTBDrugs = takingAntiTBDrugs,
            familySufferingFromTB = familySufferingFromTB,
            syncState = SyncState.SYNCED
        )
    }
}

data class TBSuspectedDTO(
    val id: Long,
    val benId: Long,
    val visitDate: String?,
    val isSputumCollected: Boolean?,
    val sputumSubmittedAt: String?,
    val nikshayId: String?,
    val sputumTestResult: String?,
    val referred: Boolean?,
    val followUps: String?
) {
    fun toCache() : TBSuspectedCache {
        return TBSuspectedCache(
            benId = benId,
            visitDate = getLongFromDate(visitDate),
            isSputumCollected = isSputumCollected,
            sputumSubmittedAt = sputumSubmittedAt,
            nikshayId = nikshayId,
            sputumTestResult = sputumTestResult,
            referred = referred,
            followUps = followUps,
            syncState = SyncState.SYNCED
        )
    }
}
data class TBSuspectedRequestDTO(
    val userId: Int,
    val tbSuspectedList: List<TBSuspectedDTO>
)
fun getLongFromDate(dateString: String?): Long {
    val f = SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH)
    val date = dateString?.let { f.parse(it) }
    return date?.time ?: 0L
}
