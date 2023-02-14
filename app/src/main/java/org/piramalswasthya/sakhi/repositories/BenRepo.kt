package org.piramalswasthya.sakhi.repositories

import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.piramalswasthya.sakhi.configuration.BenGenRegFormDataset
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.database.room.BeneficiaryIdsAvail
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.GetBenRequest
import org.piramalswasthya.sakhi.network.NcdNetworkApiService
import org.piramalswasthya.sakhi.network.TmcGenerateBenIdsRequest
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import timber.log.Timber
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class BenRepo @Inject constructor(
    private val database: InAppDb,
    private val tmcNetworkApiService: TmcNetworkApiService,
    private val ncdNetworkApiService: NcdNetworkApiService
) {

    val benList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllBen()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val eligibleCoupleList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllEligibleCoupleList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val pregnantList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllPregnancyWomenList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val deliveryList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllDeliveryStageWomenList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val ncdEligibleList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllNCDEligibleList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val ncdNonEligibleList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllNCDNonEligibleList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val menopauseList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllMenopauseStageList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val reproductiveAgeList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllReproductiveAgeList()) { list ->
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
                this.beneficiaryId = benIdObj.benId
                this.benRegId = benIdObj.benRegId
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
            this.locationRecord = locationRecord
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
                    this.beneficiaryId = benIdObj.benId
                    this.benRegId = benIdObj.benRegId
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
                this.locationRecord = it
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
                this.beneficiaryId = benIdObj.benId
                this.benRegId = benIdObj.benRegId
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
            this.locationRecord = locationRecord
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


    suspend fun getBenHousehold(hhId: Long): HouseholdCache? {
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

            val benNetworkPostList = mutableSetOf<BenPost>()
            val householdNetworkPostList = mutableSetOf<HouseholdNetwork>()
            val kidNetworkPostList = mutableSetOf<BenRegKidNetwork>()

            benList.forEach {
                benNetworkPostList.clear()
                householdNetworkPostList.clear()
                val isSuccess =
                    createBenIdAtServerByBeneficiarySending(it, user, it.locationRecord!!)
                if (isSuccess) {
                    benNetworkPostList.add(it.asNetworkPostModel(user))
                    householdNetworkPostList.add(
                        database.householdDao.getHousehold(it.householdId)!!.asNetworkModel(user)
                    )
                    if (it.isKid)
                        kidNetworkPostList.add(it.asKidNetworkModel())
                    val uploadDone = postDataToAmritServer(
                        benNetworkPostList,
                        householdNetworkPostList,
                        kidNetworkPostList
                    )
                    if (!uploadDone)
                        database.benDao.benSyncWithServerFailed(*benNetworkPostList.map { ben -> ben.benId }
                            .toTypedArray().toLongArray())
                }
            }
            return@withContext true
        }
    }

    private suspend fun postDataToAmritServer(
        benNetworkPostList: MutableSet<BenPost>,
        householdNetworkPostList: MutableSet<HouseholdNetwork>,
        kidNetworkPostList: MutableSet<BenRegKidNetwork>
    ): Boolean {
        if (benNetworkPostList.isEmpty() && householdNetworkPostList.isEmpty() && kidNetworkPostList.isEmpty())
            return false
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
                        val data = jsonObj.getJSONObject("data")
                        val hhCount = if (data.getJSONArray("houseHoldDetails").get(0)
                                .equals(null)
                        ) 0 else data.getJSONArray("houseHoldDetails").getInt(0)
                        val benCount = if (data.getJSONArray("beneficiaryDetails").get(0)
                                .equals(null)
                        ) 0 else data.getJSONArray("beneficiaryDetails").getInt(0)
                        val kidCount = if (data.getJSONArray("bornBirthDeatils").get(0)
                                .equals(null)
                        ) 0 else if (data.getJSONArray("bornBirthDeatils")
                                .length() == 0
                        ) 0 else data.getJSONArray("beneficiaryDetails").getInt(0)
                        if (hhCount != householdNetworkPostList.size || benCount != benNetworkPostList.size || kidCount != kidNetworkPostList.size) {
                            Timber.d("Bad Response from server, need to check $householdNetworkPostList\n$benNetworkPostList\n$kidNetworkPostList $data ")
                            return false
                        }
                        val benToUpdateList =
                            benNetworkPostList.map { it.benId }.toTypedArray()
                        val hhToUpdateList =
                            householdNetworkPostList.map { it.householdId.toLong() }.toTypedArray()
                        database.benDao.benSyncedWithServer(*benToUpdateList.toLongArray())
                        database.householdDao.householdSyncedWithServer(*hhToUpdateList.toLongArray())
                        householdNetworkPostList.map { it.householdId }
                        //TODO(Add sync up to household too)
                        return true
                    }
                }
            }
            Timber.d("Bad Response from server, need to check $householdNetworkPostList\n$benNetworkPostList\n$kidNetworkPostList $response ")
            return false
        } catch (e: SocketTimeoutException) {
            Timber.d("Caught exception $e here")
            return postDataToAmritServer(
                benNetworkPostList,
                householdNetworkPostList,
                kidNetworkPostList
            )
        }
    }

    private suspend fun createBenIdAtServerByBeneficiarySending(
        ben: BenRegCache,
        user: UserCache,
        locationRecord: LocationRecord
    ): Boolean {

        val sendingData = ben.asNetworkSendingModel(user, locationRecord)
        //val sendingDataString = Gson().toJson(sendingData)
        database.benDao.setSyncState(ben.householdId, ben.beneficiaryId, SyncState.SYNCING)
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

    suspend fun getBeneficiariesFromServerForWorker(pageNumber: Int): Int {
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
                        if (responseStatusCode != 200)
                            throw SocketTimeoutException("User logged out!")
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

                                val benId =
                                    if (jsonObject.has("benficieryid")) jsonObject.getLong("benficieryid") else -1L
                                val hhId =
                                    if (jsonObject.has("houseoldId")) jsonObject.getLong("houseoldId") else -1L
                                if (benId == -1L || hhId == -1L)
                                    continue
                                val benExists = database.benDao.getBen(hhId, benId) != null

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
                                        syncState = if (benExists) SyncState.SYNCED else SyncState.SYNCING
                                    )
                                )
                            }
                            try {
                                database.householdDao.upsert(
                                    *getHouseholdCacheFromServerResponse(
                                        responseString
                                    ).toTypedArray()
                                )
                            } catch (e: Exception) {
                                Timber.d("HouseHold list not synced $e")
                                return@withContext -1
                            }
                            database.benDao.upsert(*getBenCacheFromServerResponse(responseString).toTypedArray())

                            Timber.d("GeTBenDataList: $pageSize $benDataList")
                            return@withContext pageSize
                        }
                        throw IllegalStateException("Response code !-100")
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_ben error : $e")
                return@withContext getBeneficiariesFromServerForWorker(pageNumber)

            }
            Timber.d("get_ben data : $benDataList")
            -1
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

                                    val benId = jsonObject.getLong("benficieryid")
                                    val hhId = jsonObject.getLong("houseoldId")
                                    val benExists = database.benDao.getBen(hhId, benId) != null

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
                                            syncState = if (benExists) SyncState.SYNCED else SyncState.SYNCING
                                        )
                                    )
                                }
                                try {
                                    database.householdDao.upsert(
                                        *getHouseholdCacheFromServerResponse(
                                            responseString
                                        ).toTypedArray()
                                    )
                                } catch (e: Exception) {
                                    Timber.d("HouseHold list not synced $e")
                                    return@withContext Pair(0, benDataList)
                                }
                                val benCacheList = getBenCacheFromServerResponse(responseString)
                                database.benDao.upsert(*benCacheList.toTypedArray())

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

    private fun getLongFromDate(date: String): Long {
        //TODO ()
        return 0
    }

    private suspend fun getBenCacheFromServerResponse(response: String): MutableList<BenRegCache> {
        val jsonObj = JSONObject(response)
        val result = mutableListOf<BenRegCache>()

        val responseStatusCode = jsonObj.getInt("statusCode")
        if (responseStatusCode == 200) {
            val dataObj = jsonObj.getJSONObject("data")
            val jsonArray = dataObj.getJSONArray("data")

            if (jsonArray.length() != 0) {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val houseDataObj = jsonObject.getJSONObject("householdDetails")
                    val benDataObj = jsonObject.getJSONObject("beneficiaryDetails")
                    val cbacDataObj = jsonObject.getJSONObject("cbacDetails")
                    val childDataObj = jsonObject.getJSONObject("bornbirthDeatils")

                    val benId = jsonObject.getLong("benficieryid")
                    val hhId = jsonObject.getLong("houseoldId")
                    val benExists = database.benDao.getBen(hhId, benId) != null

                    if (benExists) {
                        continue
                    }
                    val hhExists = database.householdDao.getHousehold(hhId) != null

                    if (!hhExists) {
                        continue
                    }

                    result.add(
                        BenRegCache(
                            householdId = jsonObject.getLong("houseoldId"),
                            beneficiaryId = jsonObject.getLong("benficieryid"),
                            ashaId = jsonObject.getInt("ashaId"),
                            benRegId = jsonObject.getLong("BenRegId"),
                            age = benDataObj.getInt("age"),
                            ageUnit = when (benDataObj.getString("age_unit")) {
                                "Year(s)" -> AgeUnit.YEARS
                                "Month(s)" -> AgeUnit.MONTHS
                                "Day(s)" -> AgeUnit.DAYS
                                else -> AgeUnit.YEARS
                            },
                            isKid = !(benDataObj.getString("age_unit") == "Year(s)" && benDataObj.getInt(
                                "age"
                            ) > 14),
                            isAdult = (benDataObj.getString("age_unit") == "Year(s)" && benDataObj.getInt(
                                "age"
                            ) > 14),
                            userImageBlob = benDataObj.getString("user_image").toByteArray(),
                            regDate = getLongFromDate(benDataObj.getString("registrationDate")),
                            firstName = benDataObj.getString("firstName"),
                            lastName = benDataObj.getString("lastName"),
                            gender = when (benDataObj.getString("gender")) {
                                "Male" -> Gender.MALE
                                "Female" -> Gender.FEMALE
                                "Transgender" -> Gender.TRANSGENDER
                                else -> Gender.MALE
                            },
                            genderId = benDataObj.getInt("genderId"),
                            dob = getLongFromDate(benDataObj.getString("dob")),
                            age_unitId = benDataObj.getInt("age_unitId"),
                            fatherName = benDataObj.getString("fatherName"),
                            motherName = benDataObj.getString("motherName"),
                            familyHeadRelation = benDataObj.getString("familyHeadRelation"),
                            familyHeadRelationPosition = benDataObj.getInt("familyHeadRelationPosition"),
//                            familyHeadRelationOther = benDataObj.getString("familyHeadRelationOther"),
                            mobileNoOfRelation = benDataObj.getString("mobilenoofRelation"),
                            mobileNoOfRelationId = benDataObj.getInt("mobileNoOfRelationId"),
                            mobileOthers = benDataObj.getString("mobileOthers"),
                            contactNumber = benDataObj.getString("contact_number").toLong(),
//                            literacy = literacy,
                            literacyId = benDataObj.getInt("literacyId"),
                            community = benDataObj.getString("community"),
                            communityId = benDataObj.getInt("communityId"),
                            religion = benDataObj.getString("religion"),
                            religionId = benDataObj.getInt("religionID"),
                            religionOthers = benDataObj.getString("religionOthers"),
                            rchId = benDataObj.getString("rchid"),
                            registrationType = when (benDataObj.getString("registrationType")) {
                                "Infant" -> TypeOfList.INFANT
                                "Child" -> TypeOfList.CHILD
                                "Adolescent" -> TypeOfList.ADOLESCENT
                                "General" -> TypeOfList.GENERAL
                                "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
                                "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
                                "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
                                "Postnatal Mother" -> TypeOfList.POSTNATAL_MOTHER
                                "Menopause" -> TypeOfList.MENOPAUSE
                                "Teenager" -> TypeOfList.TEENAGER
                                else -> TypeOfList.OTHER
                            },
                            latitude = benDataObj.getDouble("latitude"),
                            longitude = benDataObj.getDouble("longitude"),
                            aadharNum = benDataObj.getString("aadhaNo"),
                            aadharNumId = benDataObj.getInt("aadha_noId"),
                            hasAadhar = benDataObj.getString("aadhaNo") != "",
                            hasAadharId = if (benDataObj.getInt("aadha_noId") == 1) 1 else 0,
//                            bankAccountId = bank_accountId,
                            bankAccount = benDataObj.getString("bankAccount"),
                            nameOfBank = benDataObj.getString("nameOfBank"),
//                            nameOfBranch = nameOfBranch,
                            ifscCode = benDataObj.getString("ifscCode"),
//                            needOpCare = need_opcare,
                            needOpCareId = benDataObj.getInt("need_opcareId"),
                            ncdPriority = benDataObj.getInt("ncd_priority"),
                            cbacAvailable = cbacDataObj.length() != 0,
                            guidelineId = benDataObj.getString("guidelineId"),
                            isHrpStatus = benDataObj.getBoolean("hrpStatus"),
//                            hrpIdentificationDate = hrp_identification_date,
//                            hrpLastVisitDate = hrp_last_vist_date,
//                            nishchayPregnancyStatus = nishchayPregnancyStatus,
//                            nishchayPregnancyStatusPosition = nishchayPregnancyStatusPosition,
//                            nishchayDeliveryStatus = nishchayDeliveryStatus,
//                            nishchayDeliveryStatusPosition = nishchayDeliveryStatusPosition,
//                            nayiPahalDeliveryStatus = nayiPahalDeliveryStatus,
//                            nayiPahalDeliveryStatusPosition = nayiPahalDeliveryStatusPosition,
                            suspectedNcd = cbacDataObj.getString("suspected_ncd"),
                            suspectedNcdDiseases = cbacDataObj.getString("suspected_ncd_diseases"),
                            suspectedTb = cbacDataObj.getString("suspected_tb"),
                            confirmed_Ncd = cbacDataObj.getString("confirmed_ncd"),
                            confirmedHrp = cbacDataObj.getString("confirmed_hrp"),
                            confirmedTb = cbacDataObj.getString("confirmed_tb"),
                            confirmedNcdDiseases = cbacDataObj.getString("confirmed_ncd_diseases"),
                            diagnosisStatus = cbacDataObj.getString("diagnosis_status"),
                            locationRecord = LocationRecord (
                                countryId = benDataObj.getInt("countyId"),
                                stateId = benDataObj.getInt("stateId"),
                                state = benDataObj.getString("stateName"),
                                districtId = benDataObj.getInt("districtid"),
                                district = benDataObj.getString("districtname"),
                                blockId = benDataObj.getInt("blockId"),
                                block = benDataObj.getString("blockName"),
                                villageId = benDataObj.getInt("villageId"),
                                village = benDataObj.getString("villageName"),
                            ),
                            processed = "P",
                            serverUpdatedStatus = 1,
                            createdBy = benDataObj.getString("createdBy"),
                            createdDate = getLongFromDate(benDataObj.getString("createdDate")),
                            kidDetails = BenRegKid(
//                                childRegisteredAWC = childRegisteredAWC,
                                childRegisteredAWCId = benDataObj.getInt("childRegisteredAWCID"),
//                                childRegisteredSchool = childRegisteredSchool,
                                childRegisteredSchoolId = benDataObj.getInt("childRegisteredSchoolID"),
//                                typeOfSchool = typeofSchool,
                                typeOfSchoolId = benDataObj.getInt("typeofSchoolID"),
                                birthPlace  = childDataObj.getString("birthPlace"),
                                birthPlaceId  = childDataObj.getInt("birthPlaceid").toString(),
                                facilityName  = childDataObj.getString("facilityName"),
                                facilityid  = childDataObj.getInt("facilityid").toString(),
                                facilityOther  = childDataObj.getString("facilityOther"),
                                placeName  = childDataObj.getString("placeName"),
                                conductedDelivery  = childDataObj.getString("conductedDelivery"),
                                conductedDeliveryId  = childDataObj.getInt("conductedDeliveryid").toString(),
                                conductedDeliveryOther  = childDataObj.getString("conductedDeliveryOther"),
                                deliveryType  = childDataObj.getString("deliveryType"),
                                deliveryTypeId  = childDataObj.getInt("deliveryTypeid").toString(),
                                complications  = childDataObj.getString("complecations"),
                                complicationsId  = childDataObj.getInt("complecationsid").toString(),
                                complicationsOther  = childDataObj.getString("complicationsOther"),
                                term  = childDataObj.getString("term"),
                                termid  = childDataObj.getInt("termid").toString(),
//                                gestationalAge  = childDataObj.getString("gestationalAge"),
                                gestationalAgeId  = childDataObj.getInt("gestationalAgeid").toString(),
//                                corticosteroidGivenMother  = childDataObj.getString("corticosteroidGivenMother"),
                                corticosteroidGivenMotherId  = childDataObj.getInt("corticosteroidGivenMotherid").toString(),
                                criedImmediately  = childDataObj.getString("criedImmediately"),
                                criedImmediatelyId = childDataObj.getInt("criedImmediatelyid").toString(),
                                birthDefects  = childDataObj.getString("birthDefects"),
                                birthDefectsId  = childDataObj.getInt("birthDefectsid").toString(),
                                birthDefectsOthers  = childDataObj.getString("birthDefectsOthers"),
                                heightAtBirth  = childDataObj.getInt("heightAtBirth").toString(),
                                weightAtBirth  = childDataObj.getInt("weightAtBirth").toString(),
                                feedingStarted  = childDataObj.getString("feedingStarted"),
                                feedingStartedId  = childDataObj.getInt("feedingStartedid").toString(),
                                birthDosage  = childDataObj.getString("birthDosage"),
                                birthDosageId  = childDataObj.getInt("birthDosageid").toString(),
                                opvBatchNo  = childDataObj.getString("opvBatchNo"),
//                                opvGivenDueDate  = childDataObj.getString("opvGivenDueDate"),
//                                opvDate  = childDataObj.getString("opvDate"),
                                bcdBatchNo  = childDataObj.getString("bcdBatchNo"),
//                                bcgGivenDueDate  = childDataObj.getString("bcgGivenDueDate"),
//                                bcgDate  = childDataObj.getString("bcgDate"),
                                hptBatchNo  = childDataObj.getString("hptdBatchNo"),
//                                hptGivenDueDate  = childDataObj.getString("hptGivenDueDate"),
//                                hptDate  = childDataObj.getString("hptDate"),
                                vitaminKBatchNo  = childDataObj.getString("vitaminkBatchNo"),
//                                vitaminKGivenDueDate  =  childDataObj.getString("vitaminKGivenDueDate"),
//                                vitaminKDate =  childDataObj.getString("vitaminKDate"),
                                deliveryTypeOther =  childDataObj.getString("deliveryTypeOther"),

//                                motherBenId =  childDataObj.getString("conductedDeliveryOther"),
//                                childMotherName =  childDataObj.getString("conductedDeliveryOther"),
//                                motherPosition =  childDataObj.getString("conductedDeliveryOther"),
                                birthBCG = childDataObj.getBoolean("birthBCG"),
                                birthHepB = childDataObj.getBoolean("birthHepB"),
                                birthOPV = childDataObj.getBoolean("birthOPV"),
                            ),
                            genDetails = BenRegGen(
                                maritalStatus = benDataObj.getString("maritalstatus"),
                                maritalStatusId = benDataObj.getInt("maritalstatusId"),
                                spouseName = benDataObj.getString("spousename"),
                                ageAtMarriage = benDataObj.getInt("ageAtMarriage"),
//                                dateOfMarriage = getLongFromDate(dateMarriage),
//                                marriageDate = benDataObj.getString("marriageDate"),
//                                menstrualStatus = menstrualStatus,
                                menstrualStatusId = benDataObj.getInt("menstrualStatusId"),
//                                regularityOfMenstrualCycle = regularityofMenstrualCycle,
                                regularityOfMenstrualCycleId = benDataObj.getInt("regularityofMenstrualCycleId"),
//                                lengthOfMenstrualCycle = lengthofMenstrualCycle,
                                lengthOfMenstrualCycleId = benDataObj.getInt("lengthofMenstrualCycleId"),
//                                menstrualBFD = menstrualBFD,
                                menstrualBFDId = benDataObj.getInt("menstrualBFDId"),
//                                menstrualProblem = menstrualProblem,
                                menstrualProblemId = benDataObj.getInt("menstrualProblemId"),
//                                lastMenstrualPeriod = lastMenstrualPeriod,
                                reproductiveStatus = benDataObj.getString("reproductiveStatus"),
                                reproductiveStatusId = benDataObj.getInt("reproductiveStatusId"),
//                                lastDeliveryConducted = lastDeliveryConducted,
                                lastDeliveryConductedId = benDataObj.getInt("lastDeliveryConductedID"),
//                                facilityName = facilitySelection,
//                                whoConductedDelivery = whoConductedDelivery,
                                whoConductedDeliveryId = benDataObj.getInt("whoConductedDeliveryID"),
//                                deliveryDate = deliveryDate,
//                                expectedDateOfDelivery = benDataObj.getString("expectedDateOfDelivery"),
//                                noOfDaysForDelivery = noOfDaysForDelivery,
                                ),
                            syncState = SyncState.SYNCED,
                            isDraft = false
                        )
                    )
                }
            }
        }
        return result
    }

    private suspend fun getHouseholdCacheFromServerResponse(response: String): MutableList<HouseholdCache> {
        val jsonObj = JSONObject(response)
        val result = mutableListOf<HouseholdCache>()

        val responseStatusCode = jsonObj.getInt("statusCode")
        if (responseStatusCode == 200) {
            val dataObj = jsonObj.getJSONObject("data")
            val jsonArray = dataObj.getJSONArray("data")

            if (jsonArray.length() != 0) {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val benId =
                        if (jsonObject.has("benficieryid")) jsonObject.getLong("benficieryid") else -1L
                    val hhId =
                        if (jsonObject.has("houseoldId")) jsonObject.getLong("houseoldId") else -1L
                    if (benId == -1L || hhId == -1L)
                        continue
                    val houseDataObj = jsonObject.getJSONObject("householdDetails")
                    val benDataObj = jsonObject.getJSONObject("beneficiaryDetails")

                    val hhExists = database.householdDao.getHousehold(hhId) != null
                            || result.map { it.householdId }.contains(hhId)

                    if (hhExists) {
                        continue
                    }
                    Timber.d("HouseHoldList $result")
                    try {
                        result.add(
                            HouseholdCache(
                                householdId = jsonObject.getLong("houseoldId"),
                                ashaId = jsonObject.getInt("ashaId"),
                                benId = jsonObject.getLong("benficieryid"),
                                familyHeadName = houseDataObj.getString("familyHeadName"),
                                familyName = if (jsonObject.has("familyName")) jsonObject.getString(
                                    "familyName"
                                ) else null,
                                familyHeadPhoneNo = houseDataObj.getString("familyHeadPhoneNo")
                                    .toLong(),
                                houseNo = houseDataObj.getString("houseno"),
                                rationCardDetails = houseDataObj.getString("rationCardDetails"),
                                povertyLine = houseDataObj.getString("type_bpl_apl"),
                                povertyLineId = houseDataObj.getInt("bpl_aplId"),
                                residentialArea = houseDataObj.getString("residentialArea"),
                                residentialAreaId = houseDataObj.getInt("residentialAreaId"),
                                otherResidentialArea = houseDataObj.getString("other_residentialArea"),
                                houseType = houseDataObj.getString("houseType"),
                                houseTypeId = houseDataObj.getInt("houseTypeId"),
                                otherHouseType = houseDataObj.getString("other_houseType"),
                                isHouseOwned = houseDataObj.getString("houseOwnerShip"),
                                isHouseOwnedId = houseDataObj.getInt("houseOwnerShipId"),
//                                isLandOwned = houseDataObj.getString("landOwned") == "Yes",
//                                isLandIrrigated = houseDataObj.has("landIrregated") && houseDataObj.getString("landIrregated") == "Yes",
//                                isLivestockOwned = houseDataObj.getString("liveStockOwnerShip") == "Yes",
                                street = houseDataObj.getString("street"),
                                colony = houseDataObj.getString("colony"),
                                pincode = houseDataObj.getInt("pincode"),
                                separateKitchen = houseDataObj.getString("seperateKitchen"),
                                fuelUsed = houseDataObj.getString("fuelUsed"),
                                fuelUsedId = houseDataObj.getInt("fuelUsedId"),
                                otherFuelUsed = houseDataObj.getString("other_fuelUsed"),
                                sourceOfDrinkingWater = houseDataObj.getString("sourceofDrinkingWater"),
                                sourceOfDrinkingWaterId = houseDataObj.getInt("sourceofDrinkingWaterId"),
                                otherSourceOfDrinkingWater = houseDataObj.getString("other_sourceofDrinkingWater"),
                                availabilityOfElectricity = houseDataObj.getString("avalabilityofElectricity"),
                                availabilityOfElectricityId = houseDataObj.getInt("avalabilityofElectricityId"),
                                otherAvailabilityOfElectricity = houseDataObj.getString("other_avalabilityofElectricity"),
                                availabilityOfToilet = houseDataObj.getString("availabilityofToilet"),
                                availabilityOfToiletId = houseDataObj.getInt("availabilityofToiletId"),
                                otherAvailabilityOfToilet = houseDataObj.getString("other_availabilityofToilet"),
//                                motorizedVehicle = houseDataObj.getString("motarizedVehicle"),
//                                otherMotorizedVehicle = houseDataObj.getString("other_motarizedVehicle"),
                                registrationType = if (houseDataObj.has("registrationType")) houseDataObj.getString(
                                    "registrationType"
                                ) else null,
                                state = houseDataObj.getString("state"),
                                stateId = houseDataObj.getInt("stateid"),
                                district = benDataObj.getString("districtname"),
                                districtId = houseDataObj.getInt("districtid"),
                                block = benDataObj.getString("blockName"),
                                blockId = houseDataObj.getInt("blockid"),
                                village = houseDataObj.getString("village"),
                                villageId = houseDataObj.getInt("villageid"),
                                countyId = houseDataObj.getInt("Countyid"),
                                serverUpdatedStatus = houseDataObj.getInt("serverUpdatedStatus"),
                                createdBy = houseDataObj.getString("createdBy"),
                                createdTimeStamp = getLongFromDate(houseDataObj.getString("createdDate")),
//                            updatedBy = houseDataObj.getString("other_houseType"),
//                            updatedTimeStamp = houseDataObj.getString("other_houseType"),
                                processed = "P",
                                isDraft = false,
                            )
                        )
                    } catch (e: JSONException) {
                        Timber.i("Household skipped: ${jsonObject.getLong("houseoldId")} with error $e")
                    }
                }
            }
        }
        return result
    }


}