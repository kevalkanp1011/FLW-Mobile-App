package org.piramalswasthya.sakhi.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NcdNetworkApiService {

    @POST("identity-0.0.1/rmnch/getBeneficiaryDataForAsha")
    suspend fun getBeneficiaries(@Body userDetail: GetBenRequest) : Response<ResponseBody>
}