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



