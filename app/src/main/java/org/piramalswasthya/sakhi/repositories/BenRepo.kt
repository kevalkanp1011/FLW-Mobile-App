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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    val infantList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllInfantList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val childList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllChildList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val adolescentList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllAdolescentList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val immunizationList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllImmunizationDueList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val hrpList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllHrpCasesList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val pncMotherList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllPNCMotherList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val cdrList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllCDRList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }

    val mdsrList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllMDSRList()) { list ->
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
                        kidNetworkPostList.add(it.asKidNetworkModel(user))
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

                val responseString: String? = response.body()?.string()
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
                        val kidCount = if (data.getJSONArray("bornBirthDeatils").length() == 0)
                            0 else if (data.getJSONArray("bornBirthDeatils").get(0).equals(null))
                            0 else data.getJSONArray("beneficiaryDetails").getInt(0)
                        Timber.d("Currently checking... $hhCount $benCount $kidCount \n${householdNetworkPostList.size}\n${benNetworkPostList.size}\n${kidNetworkPostList.size}\n $hhCount $benCount $kidCount")
                        if (hhCount != householdNetworkPostList.size || benCount != benNetworkPostList.size || kidCount != kidNetworkPostList.size) {
                            Timber.d("Bad Response from server, need to check $householdNetworkPostList\n$benNetworkPostList\n$kidNetworkPostList $data ")
                            return false
                        }
                        val benToUpdateList =
                            benNetworkPostList.map { it.benId }.toTypedArray()
                        val hhToUpdateList =
                            householdNetworkPostList.map { it.householdId.toLong() }.toTypedArray()
                        Timber.d("Yuuhooo  -- ---${benNetworkPostList.first().benId}  ${householdNetworkPostList.first().householdId}")
                        database.benDao.benSyncedWithServer(benNetworkPostList.first().benId)
                        database.householdDao.householdSyncedWithServer(householdNetworkPostList.first().householdId.toLong())
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
        } catch (e: JSONException) {
            Timber.d("Caught exception $e here")
            return false
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
                    ben.beneficiaryId = newBenId
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
                            throw IllegalStateException("User logged out!")
                        val dataObj = jsonObj.getJSONObject("data")
                        val pageSize = dataObj.getInt("totalPage")

                        try {
                            database.householdDao.upsert(
                                *getHouseholdCacheFromServerResponse(
                                    responseString
                                ).toTypedArray()
                            )
                        } catch (e: Exception) {
                            Timber.d("HouseHold list not synced $e")
                            return@withContext 0
                        }
                        val benCacheList = getBenCacheFromServerResponse(responseString);
                        database.benDao.upsert(*benCacheList.toTypedArray())

                        Timber.d("GeTBenDataList: $pageSize")
                        return@withContext pageSize
                    }
                }

            } catch (e: SocketTimeoutException) {
                Timber.d("get_ben error : $e")
                return@withContext -2

            }
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

                                    val benId =
                                        if (jsonObject.has("benficieryid")) jsonObject.getLong("benficieryid") else -1L
                                    val hhId =
                                        if (jsonObject.has("houseoldId")) jsonObject.getLong("houseoldId") else -1L
                                    if (hhId == -1L || benId == -1L) {
                                        continue
                                    }
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
        val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm:ss a")
        val localDateTime = formatter.parse(date)
        return localDateTime.getTime();
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
                    val benDataObj = jsonObject.getJSONObject("beneficiaryDetails")
                    val houseDataObj = jsonObject.getJSONObject("householdDetails")
                    val cbacDataObj = jsonObject.getJSONObject("cbacDetails")
                    val childDataObj = jsonObject.getJSONObject("bornbirthDeatils")
                    val benId =
                        if (jsonObject.has("benficieryid")) jsonObject.getLong("benficieryid") else -1L
                    val hhId =
                        if (jsonObject.has("houseoldId")) jsonObject.getLong("houseoldId") else -1L
                    if (benId == -1L || hhId == -1L)
                        continue
                    val benExists = database.benDao.getBen(hhId, benId) != null

                    if (benExists) {
                        continue
                    }
                    val hhExists = database.householdDao.getHousehold(hhId) != null

                    if (!hhExists) {
                        continue
                    }

                    try {
                        result.add(
                            BenRegCache(
                                householdId = jsonObject.getLong("houseoldId"),
                                beneficiaryId = jsonObject.getLong("benficieryid"),
                                ashaId = jsonObject.getInt("ashaId"),
                                benRegId = jsonObject.getLong("BenRegId"),
                                age = benDataObj.getInt("age"),
                                ageUnit = if (benDataObj.has("gender")) {
                                    when (benDataObj.getString("age_unit")) {
                                        "Year(s)" -> AgeUnit.YEARS
                                        "Month(s)" -> AgeUnit.MONTHS
                                        "Day(s)" -> AgeUnit.DAYS
                                        else -> AgeUnit.YEARS
                                    }
                                } else null,
                                isKid = !(benDataObj.getString("age_unit") == "Years" && benDataObj.getInt(
                                    "age"
                                ) > 14),
                                isAdult = (benDataObj.getString("age_unit") == "Years" && benDataObj.getInt(
                                    "age"
                                ) > 14),
                                userImageBlob = if (benDataObj.has("user_image")) benDataObj.getString(
                                    "user_image"
                                ).toByteArray() else null,
                                regDate = if (benDataObj.has("registrationDate")) getLongFromDate(
                                    benDataObj.getString("registrationDate")
                                ) else 0,
                                firstName = if (benDataObj.has("firstName")) benDataObj.getString("firstName") else null,
                                lastName = if (benDataObj.has("lastName")) benDataObj.getString("lastName") else null,
                                gender = if (benDataObj.has("gender")) {
                                    when (benDataObj.getString("gender")) {
                                        "Male" -> Gender.MALE
                                        "Female" -> Gender.FEMALE
                                        "Transgender" -> Gender.TRANSGENDER
                                        else -> Gender.MALE
                                    }
                                } else null,
                                genderId = benDataObj.getInt("genderId"),
                                dob = getLongFromDate(benDataObj.getString("dob")),
                                age_unitId = benDataObj.getInt("age_unitId"),
                                fatherName = if (benDataObj.has("fatherName")) benDataObj.getString(
                                    "fatherName"
                                ) else null,
                                motherName = if (benDataObj.has("motherName")) benDataObj.getString(
                                    "motherName"
                                ) else null,
                                familyHeadRelation = if (benDataObj.has("familyHeadRelation")) benDataObj.getString(
                                    "familyHeadRelation"
                                ) else null,
                                familyHeadRelationPosition = benDataObj.getInt("familyHeadRelationPosition"),
//                            familyHeadRelationOther = benDataObj.getString("familyHeadRelationOther"),
                                mobileNoOfRelation = if (benDataObj.has("mobilenoofRelation")) benDataObj.getString(
                                    "mobilenoofRelation"
                                ) else null,
                                mobileNoOfRelationId = if (benDataObj.has("mobilenoofRelationId")) benDataObj.getInt(
                                    "mobilenoofRelationId"
                                ) else 0,
                                mobileOthers = if (benDataObj.has("mobileOthers")) benDataObj.getString(
                                    "mobileOthers"
                                ) else null,
                                contactNumber = if (benDataObj.has("contact_number")) benDataObj.getString(
                                    "contact_number"
                                ).toLong() else 0,
//                            literacy = literacy,
                            literacyId = if(benDataObj.has("literacyId")) benDataObj.getInt("literacyId") else 0,
                            community = if(benDataObj.has("community")) benDataObj.getString("community") else null,
                            communityId = if(benDataObj.has("communityId")) benDataObj.getInt("communityId") else 0,
                            religion = if(benDataObj.has("religion")) benDataObj.getString("religion") else null,
                            religionId = if(benDataObj.has("religionID")) benDataObj.getInt("religionID") else 0,
                            religionOthers = if(benDataObj.has("religionOthers")) benDataObj.getString("religionOthers") else null,
                            rchId = if(benDataObj.has("rchid")) benDataObj.getString("rchid") else null,
                            registrationType = if(benDataObj.has("registrationType")) {
                                when (benDataObj.getString("registrationType")) {
                                    "NewBorn" -> { if (benDataObj.getString("age_unit") != "Years" ||  benDataObj.getInt("age") < 2) TypeOfList.INFANT
                                                else if (benDataObj.getInt("age") < 6) TypeOfList.CHILD
                                                else TypeOfList.ADOLESCENT }
                                    "General Beneficiary", "सामान्य लाभार्थी" -> if(benDataObj.has("reproductiveStatus")) {
                                        when(benDataObj.getString("reproductiveStatus")) {
                                            "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
                                            "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
                                            "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
                                            "Postnatal Mother" -> TypeOfList.POSTNATAL_MOTHER
                                            "Menopause" -> TypeOfList.MENOPAUSE
                                            "Teenager" -> TypeOfList.TEENAGER
                                            else -> TypeOfList.OTHER
                                        }} else TypeOfList.OTHER
                                    else -> TypeOfList.GENERAL
                                }
                                } else TypeOfList.OTHER,
                            latitude = benDataObj.getDouble("latitude"),
                            longitude = benDataObj.getDouble("longitude"),
                            aadharNum = if(benDataObj.has("aadhaNo")) benDataObj.getString("aadhaNo") else null,
                            aadharNumId = benDataObj.getInt("aadha_noId"),
                            hasAadhar = if(benDataObj.has("aadhaNo")) benDataObj.getString("aadhaNo") != "" else false,
                            hasAadharId = if (benDataObj.getInt("aadha_noId") == 1) 1 else 0,
//                            bankAccountId = benDataObj.getString("bank_accountId"),
                                bankAccount = if (benDataObj.has("bankAccount")) benDataObj.getString(
                                    "bankAccount"
                                ) else null,
                                nameOfBank = if (benDataObj.has("nameOfBank")) benDataObj.getString(
                                    "nameOfBank"
                                ) else null,
//                            nameOfBranch = benDataObj.getString("nameOfBranch"),
                                ifscCode = if (benDataObj.has("ifscCode")) benDataObj.getString("ifscCode") else null,
//                            needOpCare = benDataObj.getString("need_opcare"),
                                needOpCareId = if (benDataObj.has("need_opcareId")) benDataObj.getInt(
                                    "need_opcareId"
                                ) else 0,
                                ncdPriority = if (benDataObj.has("ncd_priority")) benDataObj.getInt(
                                    "ncd_priority"
                                ) else 0,
                                cbacAvailable = cbacDataObj.length() != 0,
                                guidelineId = if (benDataObj.has("guidelineId")) benDataObj.getString(
                                    "guidelineId"
                                ) else null,
                                isHrpStatus = if (benDataObj.has("hrpStatus")) benDataObj.getBoolean(
                                    "hrpStatus"
                                ) else false,
//                            hrpIdentificationDate = hrp_identification_date,
//                            hrpLastVisitDate = hrp_last_vist_date,
//                            nishchayPregnancyStatus = nishchayPregnancyStatus,
//                            nishchayPregnancyStatusPosition = nishchayPregnancyStatusPosition,
//                            nishchayDeliveryStatus = nishchayDeliveryStatus,
//                            nishchayDeliveryStatusPosition = nishchayDeliveryStatusPosition,
//                            nayiPahalDeliveryStatus = nayiPahalDeliveryStatus,
//                            nayiPahalDeliveryStatusPosition = nayiPahalDeliveryStatusPosition,
                                suspectedNcd = if (cbacDataObj.has("suspected_ncd")) cbacDataObj.getString(
                                    "suspected_ncd"
                                ) else null,
                                suspectedNcdDiseases = if (cbacDataObj.has("suspected_ncd_diseases")) cbacDataObj.getString(
                                    "suspected_ncd_diseases"
                                ) else null,
                                suspectedTb = if (cbacDataObj.has("suspected_tb")) cbacDataObj.getString(
                                    "suspected_tb"
                                ) else null,
                                confirmed_Ncd = if (cbacDataObj.has("confirmed_ncd")) cbacDataObj.getString(
                                    "confirmed_ncd"
                                ) else null,
                                confirmedHrp = if (cbacDataObj.has("confirmed_hrp")) cbacDataObj.getString(
                                    "confirmed_hrp"
                                ) else null,
                                confirmedTb = if (cbacDataObj.has("confirmed_tb")) cbacDataObj.getString(
                                    "confirmed_tb"
                                ) else null,
                                confirmedNcdDiseases = if (cbacDataObj.has("confirmed_ncd_diseases")) cbacDataObj.getString(
                                    "confirmed_ncd_diseases"
                                ) else null,
                                diagnosisStatus = if (cbacDataObj.has("diagnosis_status")) cbacDataObj.getString(
                                    "diagnosis_status"
                                ) else null,
                                locationRecord = LocationRecord(
                                    countryId = houseDataObj.getInt("Countyid"),
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
                                kidDetails = if (childDataObj.length() == 0) null else BenRegKid(
                                    childRegisteredAWCId = if (benDataObj.has("childRegisteredAWCID")) benDataObj.getInt(
                                        "childRegisteredAWCID"
                                    ) else 0,
                                    childRegisteredSchoolId = if (benDataObj.has("childRegisteredSchoolID")) benDataObj.getInt(
                                        "childRegisteredSchoolID"
                                    ) else 0,
                                    typeOfSchoolId = if (benDataObj.has("typeofSchoolID")) benDataObj.getInt(
                                        "typeofSchoolID"
                                    ) else 0,
                                    birthPlace = if (childDataObj.has("birthPlace")) childDataObj.getString(
                                        "birthPlace"
                                    ) else null,
                                    birthPlaceId = if (childDataObj.has("birthPlaceid")) childDataObj.getInt(
                                        "birthPlaceid"
                                    ) else 0,
                                    facilityName = if (childDataObj.has("facilityName")) childDataObj.getString(
                                        "facilityName"
                                    ) else null,
                                    facilityId = if (childDataObj.has("facilityid")) childDataObj.getInt(
                                        "facilityid"
                                    ) else 0,
                                    facilityOther = if (childDataObj.has("facilityOther")) childDataObj.getString(
                                        "facilityOther"
                                    ) else null,
                                    placeName = if (childDataObj.has("placeName")) childDataObj.getString(
                                        "placeName"
                                    ) else null,
                                    conductedDelivery = if (childDataObj.has("conductedDelivery")) childDataObj.getString(
                                        "conductedDelivery"
                                    ) else null,
                                    conductedDeliveryId = if (childDataObj.has("conductedDeliveryid")) childDataObj.getInt(
                                        "conductedDeliveryid"
                                    ) else 0,
                                    conductedDeliveryOther = if (childDataObj.has("conductedDeliveryOther")) childDataObj.getString(
                                        "conductedDeliveryOther"
                                    ) else null,
                                    deliveryType = if (childDataObj.has("deliveryType")) childDataObj.getString(
                                        "deliveryType"
                                    ) else null,
                                    deliveryTypeId = if (childDataObj.has("deliveryTypeid")) childDataObj.getInt(
                                        "deliveryTypeid"
                                    ) else 0,
                                    complications = if (childDataObj.has("complecations")) childDataObj.getString(
                                        "complecations"
                                    ) else null,
                                    complicationsId = if (childDataObj.has("complecationsid")) childDataObj.getInt(
                                        "complecationsid"
                                    ) else 0,
                                    complicationsOther = if (childDataObj.has("complicationsOther")) childDataObj.getString(
                                        "complicationsOther"
                                    ) else null,
                                    term = if (childDataObj.has("term")) childDataObj.getString("term") else null,
                                    termId = if (childDataObj.has("termid")) childDataObj.getInt("termid") else 0,
//                                    gestationalAge  = if(childDataObj.has("gestationalAge")) childDataObj.getString("gestationalAge") else null,
                                    gestationalAgeId = if (childDataObj.has("gestationalAgeid")) childDataObj.getInt(
                                        "gestationalAgeid"
                                    ) else 0,
//                                    corticosteroidGivenMother  = if(childDataObj.has("corticosteroidGivenMother")) childDataObj.getString("corticosteroidGivenMother") else null,
                                    corticosteroidGivenMotherId = if (childDataObj.has("corticosteroidGivenMotherid")) childDataObj.getInt(
                                        "corticosteroidGivenMotherid"
                                    ) else 0,
                                    criedImmediately = if (childDataObj.has("criedImmediately")) childDataObj.getString(
                                        "criedImmediately"
                                    ) else null,
                                    criedImmediatelyId = if (childDataObj.has("criedImmediatelyid")) childDataObj.getInt(
                                        "criedImmediatelyid"
                                    ) else 0,
                                    birthDefects = if (childDataObj.has("birthDefects")) childDataObj.getString(
                                        "birthDefects"
                                    ) else null,
                                    birthDefectsId = if (childDataObj.has("birthDefectsid")) childDataObj.getInt(
                                        "birthDefectsid"
                                    ) else 0,
                                    birthDefectsOthers = if (childDataObj.has("birthDefectsOthers")) childDataObj.getString(
                                        "birthDefectsOthers"
                                    ) else null,
                                    heightAtBirth = if (childDataObj.has("heightAtBirth")) childDataObj.getInt(
                                        "heightAtBirth"
                                    ) else 0,
                                    weightAtBirth = if (childDataObj.has("weightAtBirth")) childDataObj.getInt(
                                        "weightAtBirth"
                                    ) else 0,
                                    feedingStarted = if (childDataObj.has("feedingStarted")) childDataObj.getString(
                                        "feedingStarted"
                                    ) else null,
                                    feedingStartedId = if (childDataObj.has("feedingStartedid")) childDataObj.getInt(
                                        "feedingStartedid"
                                    ) else 0,
                                    birthDosage = if (childDataObj.has("birthDosage")) childDataObj.getString(
                                        "birthDosage"
                                    ) else null,
                                    birthDosageId = if (childDataObj.has("birthDosageid")) childDataObj.getInt(
                                        "birthDosageid"
                                    ) else 0,
                                    opvBatchNo = if (childDataObj.has("opvBatchNo")) childDataObj.getString(
                                        "opvBatchNo"
                                    ) else null,
//                                opvGivenDueDate  = childDataObj.getString("opvGivenDueDate"),
//                                opvDate  = childDataObj.getString("opvDate"),
                                    bcdBatchNo = if (childDataObj.has("bcdBatchNo")) childDataObj.getString(
                                        "bcdBatchNo"
                                    ) else null,
//                                bcgGivenDueDate  = childDataObj.getString("bcgGivenDueDate"),
//                                bcgDate  = childDataObj.getString("bcgDate"),
                                    hptBatchNo = if (childDataObj.has("hptdBatchNo")) childDataObj.getString(
                                        "hptdBatchNo"
                                    ) else null,
//                                hptGivenDueDate  = childDataObj.getString("hptGivenDueDate"),
//                                hptDate  = childDataObj.getString("hptDate"),
                                    vitaminKBatchNo = if (childDataObj.has("vitaminkBatchNo")) childDataObj.getString(
                                        "vitaminkBatchNo"
                                    ) else null,
//                                vitaminKGivenDueDate  =  childDataObj.getString("vitaminKGivenDueDate"),
//                                vitaminKDate =  childDataObj.getString("vitaminKDate"),
                                    deliveryTypeOther = if (childDataObj.has("deliveryTypeOther")) childDataObj.getString(
                                        "deliveryTypeOther"
                                    ) else null,

//                                motherBenId =  childDataObj.getString("conductedDeliveryOther"),
//                                childMotherName =  childDataObj.getString("conductedDeliveryOther"),
//                                motherPosition =  childDataObj.getString("conductedDeliveryOther"),
                                    birthBCG = if (childDataObj.has("birthBCG")) childDataObj.getBoolean(
                                        "birthBCG"
                                    ) else false,
                                    birthHepB = if (childDataObj.has("birthHepB")) childDataObj.getBoolean(
                                        "birthHepB"
                                    ) else false,
                                    birthOPV = if (childDataObj.has("birthOPV")) childDataObj.getBoolean(
                                        "birthOPV"
                                    ) else false,
                                ),
                                genDetails = BenRegGen(
                                    maritalStatus = if (benDataObj.has("maritalstatus")) benDataObj.getString(
                                        "maritalstatus"
                                    ) else null,
                                    maritalStatusId = if (benDataObj.has("maritalstatusId")) benDataObj.getInt(
                                        "maritalstatusId"
                                    ) else 0,
                                    spouseName = if (benDataObj.has("spousename")) benDataObj.getString(
                                        "spousename"
                                    ) else null,
                                    ageAtMarriage = if (benDataObj.has("ageAtMarriage")) benDataObj.getInt(
                                        "ageAtMarriage"
                                    ) else 0,
//                                dateOfMarriage = getLongFromDate(dateMarriage),
                                    marriageDate = if (benDataObj.has("marriageDate")) getLongFromDate(
                                        benDataObj.getString("marriageDate")
                                    ) else null,
//                                menstrualStatus = menstrualStatus,
                                    menstrualStatusId = if (benDataObj.has("menstrualStatusId")) benDataObj.getInt(
                                        "menstrualStatusId"
                                    ) else null,
//                                regularityOfMenstrualCycle = regularityofMenstrualCycle,
                                    regularityOfMenstrualCycleId = if (benDataObj.has("regularityofMenstrualCycleId")) benDataObj.getInt(
                                        "regularityofMenstrualCycleId"
                                    ) else 0,
//                                lengthOfMenstrualCycle = lengthofMenstrualCycle,
                                    lengthOfMenstrualCycleId = if (benDataObj.has("lengthofMenstrualCycleId")) benDataObj.getInt(
                                        "lengthofMenstrualCycleId"
                                    ) else 0,
//                                menstrualBFD = menstrualBFD,
                                    menstrualBFDId = if (benDataObj.has("menstrualBFDId")) benDataObj.getInt(
                                        "menstrualBFDId"
                                    ) else 0,
//                                menstrualProblem = menstrualProblem,
                                    menstrualProblemId = if (benDataObj.has("menstrualProblemId")) benDataObj.getInt(
                                        "menstrualProblemId"
                                    ) else 0,
//                                lastMenstrualPeriod = lastMenstrualPeriod,
                                    reproductiveStatus = if (benDataObj.has("reproductiveStatus")) benDataObj.getString(
                                        "reproductiveStatus"
                                    ) else null,
                                    reproductiveStatusId = if (benDataObj.has("reproductiveStatusId")) benDataObj.getInt(
                                        "reproductiveStatusId"
                                    ) else 0,
//                                lastDeliveryConducted = lastDeliveryConducted,
                                    lastDeliveryConductedId = if (benDataObj.has("lastDeliveryConductedID")) benDataObj.getInt(
                                        "lastDeliveryConductedID"
                                    ) else 0,
//                                facilityName = facilitySelection,
//                                whoConductedDelivery = whoConductedDelivery,
                                    whoConductedDeliveryId = if (benDataObj.has("whoConductedDeliveryID")) benDataObj.getInt(
                                        "whoConductedDeliveryID"
                                    ) else 0,
//                                deliveryDate = deliveryDate,
                                    expectedDateOfDelivery = if (benDataObj.has("expectedDateOfDelivery")) getLongFromDate(
                                        benDataObj.getString("expectedDateOfDelivery")
                                    ) else null,
//                                noOfDaysForDelivery = noOfDaysForDelivery,
                                ),
                                syncState = SyncState.SYNCED,
                                isDraft = false
                            )
                        )
                    } catch (e: JSONException) {
                        Timber.i("Beneficiary skipped: ${jsonObject.getLong("benficieryid")} with error $e")
                    }
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
//                                rationCardDetails = houseDataObj.getString("rationCardDetails"),
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
//                                street = houseDataObj.getString("street"),
//                                colony = houseDataObj.getString("colony"),
//                                pincode = houseDataObj.getInt("pincode"),
                                separateKitchen = houseDataObj.getString("seperateKitchen"),
                                separateKitchenId = houseDataObj.getInt("seperateKitchenId"),
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