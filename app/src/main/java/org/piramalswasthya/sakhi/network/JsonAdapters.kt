package org.piramalswasthya.sakhi.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class D2DAuthUserRequest(
    val username : String,
    val password : String
)

@JsonClass(generateAdapter = true)
data class D2DAuthUserResponse(
    val jwt : String
)

@JsonClass(generateAdapter = true)
data class D2DSaveUserRequest(
    val id : Int,
    val username : String,
    val password : String
)

@JsonClass(generateAdapter = true)
data class D2DSaveUserResponse(
    val jwt : String
)




////////////////////---------TMC------------//////////////////////

@JsonClass(generateAdapter = true)
data class TmcAuthUserRequest(
    val userName : String,
    val password : String,
    val authKey : String = "",
    val doLogout : Boolean = true
)

@JsonClass(generateAdapter = true)
data class TmcUserDetailsRequest(
    val userID : Int
)

@JsonClass(generateAdapter = true)
data class TmcUserVanSpDetailsRequest(
    val userID : Int,
    val providerServiceMapID : Int
)


@JsonClass(generateAdapter = true)
data class TmcLocationDetailsRequest(
    val spID : Int,
    val spPSMID : Int
)

@JsonClass(generateAdapter = true)
data class TmcGenerateBenIdsRequest(
    val benIDRequired: Int,
    val vanID: Int
)

@JsonClass(generateAdapter = true)
data class GetBenRequest(
    val AshaId: String,
    val pageNo: Int,
    val fromDate: String,
    val toDate: String
)

///////////////-------------Abha id-------------/////////////////

@JsonClass(generateAdapter = true)
data class AbhaTokenRequest(
    val clientId : String = "SBX_001542",
    val clientSecret: String = "87b7eb89-b236-43b6-82b0-6eef154a9b90",
    val grantType :String = "client_credentials"
)

@JsonClass(generateAdapter = true)
data class AbhaTokenResponse(
    val accessToken : String,
    val expiresIn : Int,
    val refreshExpiresIn : Int,
    val refreshToken : String,
    val tokenType : String
)

@JsonClass(generateAdapter = true)
data class AbhaGenerateAadharOtpRequest(
    val aadhar : String
)
@JsonClass(generateAdapter = true)
data class AbhaGenerateAadharOtpResponse(
    val txnId : String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyAadharOtpRequest(
    val otp : String,
    val txnId : String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyAadharOtpResponse(
    val txnId : String
)




@JsonClass(generateAdapter = true)
data class AbhaGenerateMobileOtpRequest(
    val mobile : String,
    val txnId: String
)
@JsonClass(generateAdapter = true)
data class AbhaGenerateMobileOtpResponse(
    val txnId : String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyMobileOtpRequest(
    val otp : String,
    val txnId : String
)


@JsonClass(generateAdapter = true)
data class AbhaVerifyMobileOtpResponse(
    val txnId : String
)

@JsonClass(generateAdapter = true)
data class CreateAbhaIdRequest(

    // "email": "kalyan@beehyv.com",
    //  "firstName": "first",
    //  "healthId": "kaly.7089",
    //  "lastName": "last",
    //  "middleName": "middle",
    //  "password": "India@143",
    //  "profilePhoto": "",
    //  "txnId": "9a6684db-e090-42e9-9781-5a284c7fe8d1"

    val email : String,
    val firstName : String,
    val healthId : String,
    val lastName : String,
    val middleName : String,
    val password : String,
    val profilePhoto : String,
    val txnId : String
)


@JsonClass(generateAdapter = true)
data class CreateAbhaIdResponse(

    val token : String,
    val refreshToken : String,
    val healthIdNumber : String,
    val name : String,
    val gender : String,
    val yearOfBirth : String,
    val monthOfBirth : String,
    val dayOfBirth : String,
    val firstName : String,
    val healthId : String,
    val lastName : String,
    val middleName : String,
    val stateCode : String,
    val districtCode : String,
    val stateName : String,
    val districtName : String,
    val email : String,
    val kycPhoto : String?,
    val profilePhoto : String,
    val mobile : String,
    val authMethods : List<String>,
    val pincode : String?,
    val tags : Map<String, String>,
    val alreadyExists : String,
    val new : String,
)













