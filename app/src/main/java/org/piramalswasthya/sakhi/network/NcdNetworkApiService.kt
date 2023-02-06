package org.piramalswasthya.sakhi.network

import okhttp3.ResponseBody
import org.piramalswasthya.sakhi.model.BeneficiaryDataSending
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NcdNetworkApiService {

    @POST("identity-0.0.1/rmnch/getBeneficiaryDataForAsha")
    suspend fun getBeneficiaries(@Body userDetail: GetBenRequest): Response<ResponseBody>


    @POST("tmapi-v1.0/registrar/registrarBeneficaryRegistrationNew")
    suspend fun getBenIdFromBeneficiarySending(@Body beneficiaryDataSending: BeneficiaryDataSending): Response<ResponseBody>

}