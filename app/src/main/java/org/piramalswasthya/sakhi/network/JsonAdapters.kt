package org.piramalswasthya.sakhi.network

import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.BuildConfig
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.HRPNonPregnantAssessCache
import org.piramalswasthya.sakhi.model.HRPNonPregnantTrackCache
import org.piramalswasthya.sakhi.model.HRPPregnantAssessCache
import org.piramalswasthya.sakhi.model.HRPPregnantTrackCache
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
data class GetDataPaginatedRequest(
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
    val clientId: String = BuildConfig.ABHA_CLIENT_ID,
    val clientSecret: String = BuildConfig.ABHA_CLIENT_SECRET,
    val grantType: String = "client_credentials"
)

@JsonClass(generateAdapter = true)
data class AbhaTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshExpiresIn: Int,
    val refreshToken: String,
    val tokenType: String
)

// ABHA v1/v2 request
//@JsonClass(generateAdapter = true)
//data class AbhaGenerateAadhaarOtpRequest(
//    var aadhaar: String
//)

// ABHA v3 request
@JsonClass(generateAdapter = true)
data class AbhaGenerateAadhaarOtpRequest(
//    var aadhaar: String
    val txnId: String,
    val scope: List<String>,
    val loginHint: String,
    var loginId: String,
    var otpSystem: String
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

// ABHA v1/v2 request
//@JsonClass(generateAdapter = true)
//data class AbhaVerifyAadhaarOtpRequest(
//    val otp: String,
//    val txnId: String
//)

// ABHA v3 request
@JsonClass(generateAdapter = true)
data class AbhaVerifyAadhaarOtpRequest(
    val authData: AuthData,
    val consent: Consent
)

@JsonClass(generateAdapter = true)
data class AuthData(
    val authMethods: List<String>,
    val otp: Otp
)

@JsonClass(generateAdapter = true)
data class Consent(
    val code: String,
    val version: String
)

@JsonClass(generateAdapter = true)
data class Otp(
    var timeStamp: String,
    val txnId: String,
    var otpValue: String,
    var mobile: String
)

// ABHA v1/v2 request
//@JsonClass(generateAdapter = true)
//data class AbhaVerifyAadhaarOtpResponse(
//    val txnId: String
//)

// ABHA v3 request
@JsonClass(generateAdapter = true)
data class AbhaVerifyAadhaarOtpResponse(
    val message: String,
    val txnId: String,
    val tokens: Tokens,
    val ABHAProfile: ABHAProfile,
    val isNew: Boolean
)

@JsonClass(generateAdapter = true)
data class Tokens(
    val token: String,
    val expiresIn: Int,
    val refreshToken: String,
    val refreshExpiresIn: Int
)

@JsonClass(generateAdapter = true)
data class ABHAProfile(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val dob: String,
    val gender: String,
    val photo: String,
    val mobile: String? = null,
    val email: String? = null,
    val phrAddress: List<String>,
    val address: String,
    val districtCode: String,
    val stateCode: String,
    val pinCode: String,
    val abhaType: String,
    val stateName: String,
    val districtName: String,
    val ABHANumber: String,
    val abhaStatus: String
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


// ABHA v1/v2 request
//@JsonClass(generateAdapter = true)
//data class AbhaVerifyMobileOtpRequest(
//    val otp: String,
//    val txnId: String
//)

// ABHA v3 request
@JsonClass(generateAdapter = true)
data class AbhaVerifyMobileOtpRequest(
    val scope: List<String>,
    val authData: AuthData2
)

@JsonClass(generateAdapter = true)
data class AuthData2(
    val authMethods: List<String>,
    val otp: Otp2
)

@JsonClass(generateAdapter = true)
data class Otp2(
    var timeStamp: String,
    val txnId: String,
    var otpValue: String
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
    var providerServiceMapId: Int?,
    var createdBy: String?
)

data class TBScreeningRequestDTO(
    val userId: Int,
    val tbScreeningList: List<TBScreeningDTO>
)

data class UserDataDTO<T>(
    val userId: Int,
    val entries: List<T>
)

data class HRPPregnantTrackDTO(
    var id: Int = 0,
    val benId: Long,
    var visitDate: String?,
    var rdPmsa: String? = null,
    var rdDengue: String? = null,
    var rdFilaria: String? = null,
    var severeAnemia: String? = null,
    var hemoglobinTest: String? = null,
    var ifaGiven: String? = null,
    var ifaQuantity: Int? = null,
    var pregInducedHypertension: String? = null,
    var systolic: Int? = null,
    var diastolic: Int? = null,
    var gestDiabetesMellitus: String? = null,
    var bloodGlucoseTest: String? = null,
    var fbg: Int? = null,
    var rbg: Int? = null,
    var ppbg: Int? = null,
    var fastingOgtt: Int? = null,
    var after2hrsOgtt: Int? = null,
    var hypothyrodism: String? = null,
    var polyhydromnios: String? = null,
    var oligohydromnios: String? = null,
    var antepartumHem: String? = null,
    var malPresentation: String? = null,
    var hivsyph: String? = null,
    var visit: String?
) {
    fun toCache(): HRPPregnantTrackCache {
        return HRPPregnantTrackCache(
            benId = benId,
            visitDate = getLongFromDate(visitDate),
            rdPmsa = rdPmsa,
            rdDengue = rdDengue,
            rdFilaria = rdFilaria,
            severeAnemia = severeAnemia,
            hemoglobinTest = hemoglobinTest,
            ifaGiven = ifaGiven,
            ifaQuantity = ifaQuantity,
            pregInducedHypertension = pregInducedHypertension,
            systolic = systolic,
            diastolic = diastolic,
            gestDiabetesMellitus = gestDiabetesMellitus,
            bloodGlucoseTest = bloodGlucoseTest,
            fbg = fbg,
            rbg = rbg,
            ppbg = ppbg,
            fastingOgtt = fastingOgtt,
            after2hrsOgtt = after2hrsOgtt,
            hypothyrodism = hypothyrodism,
            polyhydromnios = polyhydromnios,
            oligohydromnios = oligohydromnios,
            antepartumHem = antepartumHem,
            malPresentation = malPresentation,
            hivsyph = hivsyph,
            visit = visit,
            syncState = SyncState.SYNCED
        )
    }
}

data class HRPPregnantAssessDTO(
    var id: Int = 0,
    val benId: Long,
    var noOfDeliveries: String? = null,
    var timeLessThan18m: String? = null,
    var heightShort: String? = null,
    var age: String? = null,
    var rhNegative: String? = null,
    var homeDelivery: String? = null,
    var badObstetric: String? = null,
    var multiplePregnancy: String? = null,
    var lmpDate: String?,
    var edd: String?,
    var isHighRisk: Boolean = false,
    var visitDate: String?
) {
    fun toCache(): HRPPregnantAssessCache {
        return HRPPregnantAssessCache(
            benId = benId,
            noOfDeliveries = noOfDeliveries,
            timeLessThan18m = timeLessThan18m,
            heightShort = heightShort,
            age = age,
            rhNegative = rhNegative,
            homeDelivery = homeDelivery,
            badObstetric = badObstetric,
            multiplePregnancy = multiplePregnancy,
            lmpDate = getLongFromDate(lmpDate),
            edd = getLongFromDate(edd),
            isHighRisk = isHighRisk,
            visitDate = getLongFromDate(visitDate),
            syncState = SyncState.SYNCED
        )
    }
}

data class HRPNonPregnantTrackDTO(
    var id: Int = 0,
    val benId: Long,
    var visitDate: String?,
    var anemia: String? = null,
    var hypertension: String? = null,
    var systolic: Int? = null,
    var diastolic: Int? = null,
    var diabetes: String? = null,
    var bloodGlucoseTest: String? = null,
    var fbg: Int? = null,
    var rbg: Int? = null,
    var ppbg: Int? = null,
    var severeAnemia: String? = null,
    val hemoglobinTest: String? = null,
    val ifaGiven: String? = null,
    val ifaQuantity: Int? = null,
    var fp: String? = null,
    var lmp: String?,
    var missedPeriod: String? = null,
    var isPregnant: String? = null,
) {
    fun toCache(): HRPNonPregnantTrackCache {
        return HRPNonPregnantTrackCache(
            benId = benId,
            visitDate = getLongFromDate(visitDate),
            anemia = anemia,
            hypertension = hypertension,
            systolic = systolic,
            diastolic = diastolic,
            diabetes = diabetes,
            bloodGlucoseTest = bloodGlucoseTest,
            rbg = rbg,
            fbg = fbg,
            ppbg = ppbg,
            severeAnemia = severeAnemia,
            hemoglobinTest = hemoglobinTest,
            ifaGiven = ifaGiven,
            ifaQuantity = ifaQuantity,
            fp = fp,
            lmp = getLongFromDate(lmp),
            missedPeriod = missedPeriod,
            isPregnant = isPregnant,
            syncState = SyncState.SYNCED
        )
    }
}

data class HRPNonPregnantAssessDTO(
    val id: Int = 0,
    val benId: Long,
    var noOfDeliveries: String? = null,
    var timeLessThan18m: String? = null,
    var heightShort: String? = null,
    var age: String? = null,
    var misCarriage: String? = null,
    var homeDelivery: String? = null,
    var medicalIssues: String? = null,
    var pastCSection: String? = null,
    var isHighRisk: Boolean = false,
    var visitDate: String?
) {
    fun toCache(): HRPNonPregnantAssessCache {
        return HRPNonPregnantAssessCache(
            benId = benId,
            noOfDeliveries = noOfDeliveries,
            timeLessThan18m = timeLessThan18m,
            heightShort = heightShort,
            age = age,
            misCarriage = misCarriage,
            homeDelivery = homeDelivery,
            medicalIssues = medicalIssues,
            pastCSection = pastCSection,
            isHighRisk = isHighRisk,
            visitDate = getLongFromDate(visitDate),
            syncState = SyncState.SYNCED
        )
    }
}

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
    fun toCache(): TBSuspectedCache {
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
