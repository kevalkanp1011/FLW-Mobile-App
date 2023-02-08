package org.piramalswasthya.sakhi.repositories

import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.piramalswasthya.sakhi.configuration.BenGenRegFormDataset
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.database.room.BeneficiaryIdsAvail
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.GetBenRequest
import org.piramalswasthya.sakhi.network.NcdNetworkApiService
import org.piramalswasthya.sakhi.network.TmcGenerateBenIdsRequest
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class BenRepo @Inject constructor(
    private val database: InAppDb,
    private val pref: PreferenceDao,
    private val tmcNetworkApiService: TmcNetworkApiService,
    private val ncdNetworkApiService: NcdNetworkApiService
) {

    val benList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllBen()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    companion object {
        private fun getCurrentDate(): String {
            val dateLong = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val dateString = dateFormat.format(dateLong)
            val timeString = timeFormat.format(dateLong)

            return "${dateString}T${timeString}.000Z"
        }
    }

    suspend fun getDraftForm(hhId: Long, isKid: Boolean): BenRegCache? {
        return withContext(Dispatchers.IO) {
            if (isKid)
                database.benDao.getDraftBenKidForHousehold(hhId)
            else
                null
        }
    }


    suspend fun persistKidFirstPage(form: BenKidRegFormDataset, hhId: Long) {
        Timber.d("Persisting first page!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben = form.getBenForFirstPage(user.userId, hhId)
        database.benDao.upsert(ben)
    }

    suspend fun persistGenFirstPage(form: BenGenRegFormDataset, hhId: Long) {
        Timber.d("Persisting first page!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben = form.getBenForFirstPage(user.userId, hhId)
        database.benDao.upsert(ben)
    }

    suspend fun persistKidSecondPage(form: BenKidRegFormDataset, locationRecord: LocationRecord) {
//        val draftBen = database.benDao.getDraftBenKidForHousehold(hhId)
//            ?: throw IllegalStateException("no draft saved!!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben =
            form.getBenForSecondPage()

        ben.apply {
            if (ben.beneficiaryId == -2L) {
                Timber.d("saving...")
                val benIdObj = extractBenId()
                database.benDao.substituteBenId(
                    ben.householdId,
                    ben.beneficiaryId,
                    benIdObj.benId,
                    benIdObj.benRegId
                )
            }
            if (this.createdDate == null) {
                this.processed = "N"
                this.serverUpdatedStatus = 0
                this.createdDate = System.currentTimeMillis()
                this.createdBy = user.userName
            } else {
                this.updatedDate = System.currentTimeMillis()
                this.updatedBy = user.userName
            }
            this.villageName = locationRecord.village
            this.countryId = locationRecord.countryId
            this.stateId = locationRecord.stateId
            this.districtId = locationRecord.districtId
            this.villageId = locationRecord.villageId
            this.isDraft = false
        }

        database.benDao.upsert(ben)

        return
    }

    suspend fun persistGenSecondPage(form: BenGenRegFormDataset, locationRecord: LocationRecord?) {
//        val draftBen = database.benDao.getDraftBenKidForHousehold(hhId)
//            ?: throw IllegalStateException("no draft saved!!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben =
            form.getBenForSecondPage()

        locationRecord?.let {

            ben.apply {
                if (ben.beneficiaryId == -1L) {
                    Timber.d("saving...")
                    val benIdObj = extractBenId()
                    database.benDao.substituteBenId(
                        ben.householdId,
                        ben.beneficiaryId,
                        benIdObj.benId,
                        benIdObj.benRegId
                    )
                }
                if (this.createdDate == null) {
                    this.processed = "N"
                    this.createdDate = System.currentTimeMillis()
                    this.createdBy = user.userName
                } else {
                    this.updatedDate = System.currentTimeMillis()
                    this.updatedBy = user.userName
                }
                this.serverUpdatedStatus = 0
                this.villageName = it.village
                this.countryId = it.countryId
                this.stateId = it.stateId
                this.districtId = it.districtId
                this.villageId = it.villageId
                this.isDraft = false

            }
        }
        database.benDao.upsert(ben)
//        try {
//            if (locationRecord != null)
//                createBenIdAtServerByBeneficiarySending(ben, locationRecord)
//        } catch (e: java.lang.Exception) {
//            Timber.d("Exception raised $e")
//        }
        return
    }

    suspend fun persistGenThirdPage(form: BenGenRegFormDataset, locationRecord: LocationRecord) {
//        val draftBen = database.benDao.getDraftBenKidForHousehold(hhId)
//            ?: throw IllegalStateException("no draft saved!!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben =
            form.getBenForThirdPage()
        ben.apply {
            if (ben.beneficiaryId == -1L) {
                Timber.d("saving...")
                val benIdObj = extractBenId()
                database.benDao.substituteBenId(
                    ben.householdId,
                    ben.beneficiaryId,
                    benIdObj.benId,
                    benIdObj.benRegId
                )
            }
            if (this.createdDate == null) {
                this.processed = "N"
                this.serverUpdatedStatus = 0
                this.createdDate = System.currentTimeMillis()
                this.createdBy = user.userName
            } else {
                this.updatedDate = System.currentTimeMillis()
                this.updatedBy = user.userName
            }
            this.villageName = locationRecord.village
            this.countryId = locationRecord.countryId
            this.stateId = locationRecord.stateId
            this.districtId = locationRecord.districtId
            this.villageId = locationRecord.villageId
            this.isDraft = false
        }

        database.benDao.upsert(ben)


//        try {
//            createBenIdAtServerByBeneficiarySending(ben, locationRecord)
//        } catch (e: java.lang.Exception) {
//            Timber.d("Exception raised $e")
//        }
        return
    }


    suspend fun getBenHousehold(hhId: Long): HouseholdCache {
        return database.householdDao.getHousehold(hhId)

    }

    private suspend fun extractBenId(): BeneficiaryIdsAvail {
        return withContext(Dispatchers.IO) {
            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val benIdObj = database.benIdGenDao.getEntry(user.userId)
            database.benIdGenDao.delete(benIdObj)
            benIdObj
        }

    }

    suspend fun getBenIdsGeneratedFromServer(maxCount: Int = 100) {
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val benIdCount = database.benIdGenDao.count()
        if (benIdCount > 90)
            return
        val count = maxCount - benIdCount
        val response =
            tmcNetworkApiService.generateBeneficiaryIDs(TmcGenerateBenIdsRequest(count, user.vanId))
        val statusCode = response.code()
        if (statusCode == 200) {
            val responseString = response.body()?.string()
                ?: throw IllegalStateException("response body empty here!")
            val jsonObj = JSONObject(responseString)
            val jsonObject2 = jsonObj.getJSONObject("response")
            val responseStatusCode = jsonObject2.getInt("statusCode")
            val errorMessage = jsonObject2.getString("statusMessage")
            if (responseStatusCode == 200) {
                val data = jsonObject2.getString("data")
                val jsonArray = JSONArray(data)
                val benIdList = mutableListOf<BeneficiaryIdsAvail>()
                for (i in 0 until jsonArray.length()) {
                    val jObj = jsonArray.getJSONObject(i)
                    val beneficiaryId = jObj.getLong("beneficiaryId")
                    val benRegId = jObj.getLong("benRegId")
                    benIdList.add(
                        BeneficiaryIdsAvail(
                            userId = user.userId,
                            benId = beneficiaryId,
                            benRegId = benRegId
                        )
                    )
                }
                database.benIdGenDao.insert(*benIdList.toTypedArray())
            } else {
                Timber.d("getBenIdsGeneratedFromServer() returned error message : $errorMessage")
            }

        }


    }

    suspend fun deleteBenDraft(hhId: Long, isKid: Boolean) {
        withContext(Dispatchers.IO) {
            database.benDao.deleteBen(hhId, isKid)
        }
    }

    suspend fun processNewBen(): Boolean {
        return withContext(Dispatchers.IO) {
            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val benList = database.benDao.getAllUnprocessedBen()
            val locationRecord =
                pref.getLocationRecord() ?: return@withContext false

            val benNetworkPostList = mutableSetOf<BenPost>()
            val householdNetworkPostList = mutableSetOf<HouseholdNetwork>()
            val kidNetworkPostList = mutableSetOf<BenRegKidNetwork>()

            benList.forEach {
                val isSuccess = createBenIdAtServerByBeneficiarySending(it, user, locationRecord)
                if (isSuccess) {
                    benNetworkPostList.add(it.asNetworkPostModel(user))
                    householdNetworkPostList.add(
                        database.householdDao.getHousehold(it.householdId).asNetworkModel(user)
                    )
                    if (it.isKid)
                        kidNetworkPostList.add(it.asKidNetworkModel())
                }
            }
            if (benNetworkPostList.isEmpty() && householdNetworkPostList.isEmpty() && kidNetworkPostList.isEmpty())
                return@withContext false
            val rmnchData = SendingRMNCHData(
                householdNetworkPostList.toList(),
                benNetworkPostList.toList(),
                null,
                kidNetworkPostList.toList()
            )
            try {
                val response = tmcNetworkApiService.submitRmnchDataAmrit(rmnchData)
                val statusCode = response.code()

                if (statusCode == 200) {
                    var responseString: String? = null

                    responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        val errorMessage = jsonObj.getString("errorMessage")
                        if (responseStatusCode == 200) {
                            val benToUpdateList =
                                benNetworkPostList.map { it.benficieryid }.toTypedArray()
                            database.benDao.benSyncedWithServer(*benToUpdateList.toLongArray())
                            householdNetworkPostList.map { it.householdId }
                            //TODO(Add sync up to household too)
                            return@withContext true
                        }
                    }
                }
                return@withContext false
            } catch (e: java.lang.Exception) {

                Timber.d("Caught exception $e here")
                return@withContext false
            }
        }
    }

    private suspend fun createBenIdAtServerByBeneficiarySending(
        ben: BenRegCache,
        user: UserCache,
        locationRecord: LocationRecord
    ): Boolean {
        val sendingData = ben.asNetworkSendingModel(user, locationRecord)
        //val sendingDataString = Gson().toJson(sendingData)

        try {
            val response = tmcNetworkApiService.getBenIdFromBeneficiarySending(sendingData)
            val responseString = response.body()?.string()
            if (responseString != null) {
                val jsonObj = JSONObject(responseString)
                val errorMessage = jsonObj.getString("errorMessage")
                val responseStatusCode: Int = jsonObj.getInt("statusCode")
                if (responseStatusCode == 200) {
                    val jsonObjectData: JSONObject = jsonObj.getJSONObject("data")
                    val resBenId = jsonObjectData.getString("response")
                    val benNumber = resBenId.substring(resBenId.length - 12)
                    val newBenId = java.lang.Long.valueOf(benNumber)
                    database.benDao.updateToFinalBenId(ben.householdId, ben.beneficiaryId, newBenId)
                    return true
                }
            }
            throw IllegalStateException("Response undesired!")
        } catch (e: java.lang.Exception) {
            database.benDao.setSyncState(ben.householdId, ben.beneficiaryId, SyncState.UNSYNCED)
            Timber.d("Caugnt error $e")
            return false
        }

    }

    suspend fun createBenIdAtServerByBeneficiarySending(
        hhId: Long,
        benId: Long,
        locationRecord: LocationRecord
    ) {
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben = database.benDao.getBen(hhId, benId)
        database.benDao.setSyncState(hhId, benId, SyncState.SYNCING)
        val sendingData = ben.asNetworkSendingModel(user, locationRecord)
        //val sendingDataString = Gson().toJson(sendingData)

        try {
            val response = tmcNetworkApiService.getBenIdFromBeneficiarySending(sendingData)
            Timber.d(response.body()?.string() ?: "No Body inside the morgue!")
        } catch (e: java.lang.Exception) {
            Timber.d("Caugnt error $e")
        } finally {
            database.benDao.setSyncState(hhId, benId, SyncState.UNSYNCED)
        }


    }


    suspend fun getBeneficiariesFromServer(pageNumber: Int): Pair<Int, MutableList<BenBasicDomain>> {
        return withContext(Dispatchers.IO) {
            val benDataList = mutableListOf<BenBasicDomain>()
            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")
            try {
                val response =
                    ncdNetworkApiService.getBeneficiaries(
                        GetBenRequest(
                            user.userId.toString(), pageNumber,
                            "2020-10-20T15:50:45.000Z", getCurrentDate()
                        )
                    )
                val statusCode = response.code()
                if (statusCode == 200) {
                    val responseString = response.body()?.string()
                    if (responseString != null) {
                        val jsonObj = JSONObject(responseString)

                        val errorMessage = jsonObj.getString("errorMessage")
                        val responseStatusCode = jsonObj.getInt("statusCode")
                        if (responseStatusCode == 200) {
                            val dataObj = jsonObj.getJSONObject("data")
                            val jsonArray = dataObj.getJSONArray("data")
                            val pageSize = dataObj.getInt("totalPage")

                            if (jsonArray.length() != 0) {
//                                lay_recy.setVisibility(View.VISIBLE)
//                                lay_no_ben.setVisibility(View.GONE)
//                                draftLists.clear()
//                                benServerDataList.clear()
//                                houseHoldServerData.clear()

                                for (i in 0 until jsonArray.length()) {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val houseDataObj = jsonObject.getJSONObject("householdDetails")
                                    val benDataObj = jsonObject.getJSONObject("beneficiaryDetails")

                                    benDataList.add(
                                        BenBasicDomain(
                                            benId = jsonObject.getLong("benficieryid"),
                                            hhId = jsonObject.getLong("houseoldId"),
                                            regDate = benDataObj.getString("registrationDate"),
                                            benName = benDataObj.getString("firstName"),
                                            benSurname = benDataObj.getString("lastName"),
                                            gender = benDataObj.getString("gender"),
                                            age = benDataObj.getInt("age").toString(),
                                            mobileNo = benDataObj.getString("contact_number"),
                                            fatherName = benDataObj.getString("fatherName"),
                                            familyHeadName = houseDataObj.getString("familyHeadName"),
                                            rchId = benDataObj.getString("rchid"),
                                            hrpStatus = benDataObj.getBoolean("hrpStatus")
                                                .toString(),
                                            typeOfList = benDataObj.getString("registrationType"),
                                            syncState = SyncState.UNSYNCED
                                        )
                                    )
                                }
                                Timber.d("GeTBenDataList: $pageSize $benDataList")
                                return@withContext Pair(pageSize, benDataList)
                            }
                            throw IllegalStateException("Response code !-100")
                        } else {
                            Timber.d("getBenData() returned error message : $errorMessage")
                            throw IllegalStateException("Response code !-100")
                        }
                    }
                }

            } catch (e: Exception) {
                Timber.d("get_ben error : $e")
            }
            Timber.d("get_ben data : $benDataList")
            Pair(0, benDataList)
        }
    }
}