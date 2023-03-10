package org.piramalswasthya.sakhi.repositories

import android.app.Application
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
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.ImageSizeConverter
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.network.GetBenRequest
import org.piramalswasthya.sakhi.network.TmcGenerateBenIdsRequest
import org.piramalswasthya.sakhi.network.AmritApiService
import timber.log.Timber
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class BenRepo @Inject constructor(
    private val context: Application,
    private val database: InAppDb,
    private val preferenceDao: PreferenceDao,
    private val userRepo: UserRepo,
    private val tmcNetworkApiService: AmritApiService
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
            list.map { it.asBenBasicDomainModelForPmsmaForm() }
        }
    }
    val deliveryList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllDeliveryStageWomenList()) { list ->
            list.map { it.asBenBasicDomainModelForPmsmaForm() }
        }
    }
    val ncdList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllNCDList()) { list ->
            list.map { it.asBasicDomainModel() }
        }
    }
    val ncdEligibleList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllNCDEligibleList()) { list ->
            list.map { it.asBenBasicDomainModelForCbacForm() }
        }
    }
    val ncdPriorityList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllNCDPriorityList()) { list ->
            list.map { it.asBenBasicDomainModelForCbacForm() }
        }
    }
    val ncdNonEligibleList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllNCDNonEligibleList()) { list ->
            list.map { it.asBenBasicDomainModelForCbacForm() }
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
            list.map { it.asBasicDomainModelForPmjayForm() }
        }
    }

    val cdrList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllCDRList()) { list ->
            list.map { it.asBenBasicDomainModelForCdrForm() }
        }
    }

    val mdsrList by lazy {
        //TODO(implement BenDao)
        Transformations.map(database.benDao.getAllMDSRList()) { list ->
            list.map { it.asBenBasicDomainModelForMdsrForm() }
        }
    }

    companion object {
        private fun getCurrentDate(millis: Long = System.currentTimeMillis()): String {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val dateString = dateFormat.format(millis)
            val timeString = timeFormat.format(millis)

            return "${dateString}T${timeString}.000Z"
        }
    }

    suspend fun getBenBasicDomainListFromHousehold(hhId: Long): List<BenBasicDomain> {
        return database.benDao.getAllBenForHousehold(hhId).map { it.asBasicDomainModel() }

    }

    suspend fun persistBenKid(
        form: BenKidRegFormDataset,
        hhId: Long,
        locationRecord: LocationRecord
    ) {
//        val draftBen = database.benDao.getDraftBenKidForHousehold(hhId)
//            ?: throw IllegalStateException("no draft saved!!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben =
            form.getBenForSecondPage(user.userId, hhId)

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
            }
            this.updatedDate = System.currentTimeMillis()
            this.updatedBy = user.userName
            this.locationRecord = locationRecord
            this.isDraft = false
        }

        database.benDao.upsert(ben)

        return
    }

    suspend fun persistBenGen1(
        hhId: Long,
        form: BenGenRegFormDataset,
        locationRecord: LocationRecord?
    ) {
//        val draftBen = database.benDao.getDraftBenKidForHousehold(hhId)
//            ?: throw IllegalStateException("no draft saved!!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben =
            form.getBenForSecondPage(user.userId, hhId)

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
//                    if (ben.familyHeadRelationPosition == 19) {
//                        val household = database.householdDao.getHousehold(ben.householdId)
//                        household?.benId = ben.beneficiaryId
//                        household?.familyHeadName = ben.firstName
//                        household?.processed = "N"
//                        household?.let { it1 -> database.householdDao.upsert(it1) }
//                    }
                }
                if (this.createdDate == null) {
                    this.processed = "N"
                    this.createdDate = System.currentTimeMillis()
                    this.createdBy = user.userName
                } else {
                    this.processed = "U"
                }

                this.updatedDate = System.currentTimeMillis()
                this.updatedBy = user.userName
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

    suspend fun persistBenGen2(
        hhId: Long,
        form: BenGenRegFormDataset,
        locationRecord: LocationRecord
    ) {
//        val draftBen = database.benDao.getDraftBenKidForHousehold(hhId)
//            ?: throw IllegalStateException("no draft saved!!")
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val ben =
            form.getBenForThirdPage(user.userId, hhId)
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
//                if (ben.familyHeadRelationPosition == 19) {
//                    val household = database.householdDao.getHousehold(ben.householdId)
//                    household?.benId = ben.beneficiaryId
//                    household?.familyHeadName = ben.firstName
//                    household?.processed = "N"
//                    household?.let { it1 -> database.householdDao.upsert(it1) }
//                }
            }
            if (this.createdDate == null) {
                this.processed = "N"

                this.createdDate = System.currentTimeMillis()
                this.createdBy = user.userName
            } else {
                this.processed = "U"
            }
            this.serverUpdatedStatus = 0
            this.updatedDate = System.currentTimeMillis()
            this.updatedBy = user.userName
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


    suspend fun getHousehold(hhId: Long): HouseholdCache? {
        return database.householdDao.getHousehold(hhId)

    }

    suspend fun getBeneficiary(benId: Long, hhId: Long): BenRegCache? {
        return database.benDao.getBen(hhId, benId)

    }

    suspend fun getBenKidForm(benId: Long, hhId: Long): BenKidRegFormDataset {
        return withContext(Dispatchers.IO) {
            BenKidRegFormDataset(context,getBeneficiary(benId, hhId)!!)
        }
    }



    suspend fun getBenGenForm(benId: Long, hhId: Long): BenGenRegFormDataset {
        return withContext(Dispatchers.IO) {
            BenGenRegFormDataset(context,getBeneficiary(benId, hhId)!!)
        }
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

    suspend fun getBenIdsGeneratedFromServer(maxCount: Int = Konstants.benIdCapacity) {
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val benIdCount = database.benIdGenDao.count()
        if (benIdCount > Konstants.benIdWorkerTriggerLimit)
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

    suspend fun syncUnprocessedRecords(): Boolean {
        return withContext(Dispatchers.IO) {
            val user =
                database.userDao.getLoggedInUser()
                    ?: throw IllegalStateException("No user logged in!!")

            val benUnprocessedList = database.benDao.getAllUnprocessedBen()
            val hhUnprocessedList = database.householdDao.getAllUnprocessedHousehold()
            val cbacUnprocessedList = database.cbacDao.getAllUnprocessedCbac()

            val benNetworkPostList = mutableSetOf<BenPost>()
            val householdNetworkPostList = mutableSetOf<HouseholdNetwork>()
            val kidNetworkPostList = mutableSetOf<BenRegKidNetwork>()
            val cbacPostList = mutableSetOf<CbacPost>()

            benUnprocessedList.forEach {
                benNetworkPostList.add(it.asNetworkPostModel(user))
                database.benDao.setSyncState(it.householdId, it.beneficiaryId, SyncState.SYNCING)
                if (it.isKid)
                    kidNetworkPostList.add(it.asKidNetworkModel(user))
            }

            hhUnprocessedList.forEach {
                householdNetworkPostList.add(
                    database.householdDao.getHousehold(it.householdId)!!.asNetworkModel(user)
                )
            }
            cbacUnprocessedList.forEach {
                cbacPostList.add(it.asPostModel(context.resources))
            }

            val uploadDone = postDataToAmritServer(
                benNetworkPostList,
                householdNetworkPostList,
                kidNetworkPostList,
                cbacPostList
            )
            if (!uploadDone) {
                benNetworkPostList
                    .map { ben -> ben.benId }
                    .takeIf { it.isNotEmpty() }
                    ?.toTypedArray()?.toLongArray()
                    ?.let {
                    database.benDao.benSyncWithServerFailed(*it)
                }
            }

//            val updateBenList = database.benDao.getAllBenForSyncWithServer()
//            updateBenList.forEach {
//                database.benDao.setSyncState(it.householdId, it.beneficiaryId, SyncState.SYNCING)
//                benNetworkPostList.add(it.asNetworkPostModel(user))
//                householdNetworkPostList.add(
//                    database.householdDao.getHousehold(it.householdId)!!.asNetworkModel(user)
//                )
//                if (it.isKid)
//                    kidNetworkPostList.add(it.asKidNetworkModel(user))
//                val cbac = database.cbacDao.getCbacFromBenId(it.beneficiaryId)
//                cbac?.let { form ->
//                    cbacPostList.add(
//                        form.asPostModel(
//                            it.gender!!,
//                            context.resources
//                        )
//                    )
//                }
//                val uploadDone = postDataToAmritServer(
//                    benNetworkPostList,
//                    householdNetworkPostList,
//                    kidNetworkPostList,
//                    cbacPostList
//                )
//                if (!uploadDone)
//                    database.benDao.benSyncWithServerFailed(*benNetworkPostList.map { ben -> ben.benId }
//                        .toTypedArray().toLongArray())
//
//            }
        return@withContext true
    }
}

private suspend fun postDataToAmritServer(
    benNetworkPostSet: MutableSet<BenPost>,
    householdNetworkPostSet: MutableSet<HouseholdNetwork>,
    kidNetworkPostSet: MutableSet<BenRegKidNetwork>,
    cbacPostList: MutableSet<CbacPost>
): Boolean {
    if (benNetworkPostSet.isEmpty() && householdNetworkPostSet.isEmpty() && kidNetworkPostSet.isEmpty() && cbacPostList.isEmpty())
        return true
    val rmnchData = SendingRMNCHData(
        householdNetworkPostSet.toList(),
        benNetworkPostSet.toList(),
        cbacPostList.toList(),
        kidNetworkPostSet.toList()
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
                    Timber.d("Currently checking... $hhCount $benCount $kidCount \n${householdNetworkPostSet.size}\n${benNetworkPostSet.size}\n${kidNetworkPostSet.size}\n $hhCount $benCount $kidCount")
                    if (hhCount != householdNetworkPostSet.size || benCount != benNetworkPostSet.size || kidCount != kidNetworkPostSet.size) {
                        Timber.d("Bad Response from server, need to check $householdNetworkPostSet\n$benNetworkPostSet\n$kidNetworkPostSet $data ")
                        return false
                    }
                    val benToUpdateList =
                        benNetworkPostSet.map { it.benId }.toTypedArray()
                    val hhToUpdateList =
                        householdNetworkPostSet.map { it.householdId.toLong() }.toTypedArray()
                    Timber.d("Yuuhooo  -- ---${benNetworkPostSet.first().benId}  ${householdNetworkPostSet.first().householdId}")
                    database.benDao.benSyncedWithServer(*benToUpdateList.toLongArray())
                    database.householdDao.householdSyncedWithServer(*hhToUpdateList.toLongArray())
                    database.cbacDao.cbacSyncedWithServer(*benToUpdateList.toLongArray())
                    householdNetworkPostSet.map { it.householdId }
                    return true
                } else if (responseStatusCode == 5002) {
                    val user = userRepo.getLoggedInUser()
                        ?: throw IllegalStateException("User not logged in according to db")
                    if( userRepo.refreshTokenTmc(user.userName, user.password))
                        throw SocketTimeoutException("Refreshed Token!")
                    else
                        throw IllegalStateException("User seems to be logged out and refresh token not working!!!!")
                }
            }
        }
        Timber.w("Bad Response from server, need to check $householdNetworkPostSet\n$benNetworkPostSet\n$kidNetworkPostSet $response ")
        return false
    } catch (e: SocketTimeoutException) {
        Timber.d("Caught exception $e here")
        return postDataToAmritServer(
            benNetworkPostSet,
            householdNetworkPostSet,
            kidNetworkPostSet,
            cbacPostList
        )
    } catch (e: JSONException) {
        Timber.d("Caught exception $e here")
        return false
    } catch (e : java.lang.Exception){
        Timber.d("Caught exception $e here")
        return false
    }
}

suspend fun getBeneficiariesFromServerForWorker(pageNumber: Int): Int {
    return withContext(Dispatchers.IO) {
        val user =
            database.userDao.getLoggedInUser()
                ?: throw IllegalStateException("No user logged in!!")
        val lastTimeStamp = preferenceDao.getLastSyncedTimeStamp()
        try {
            val response =
                tmcNetworkApiService.getBeneficiaries(
                    GetBenRequest(
                        user.userId.toString(), pageNumber,
                        getCurrentDate(lastTimeStamp), getCurrentDate()
                    )
                )
            val statusCode = response.code()
            if (statusCode == 200) {
                val responseString = response.body()?.string()
                if (responseString != null) {
                    val jsonObj = JSONObject(responseString)

                    val errorMessage = jsonObj.getString("errorMessage")
                    val responseStatusCode = jsonObj.getInt("statusCode")
                    Timber.d("Pull from amrit page $pageNumber response status : $responseStatusCode")
                    when (responseStatusCode) {
                        200 -> {

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
                            val benCacheList = getBenCacheFromServerResponse(responseString)
                            database.benDao.upsert(*benCacheList.toTypedArray())
                            val cbacCacheList = getCbacCacheFromServerResponse(responseString)
                            database.cbacDao.upsert(*cbacCacheList.toTypedArray())

                            Timber.d("GeTBenDataList: $pageSize")
                            return@withContext pageSize
                        }
                        5002 -> {
                            if (pageNumber == 0 && userRepo.refreshTokenTmc(
                                    user.userName,
                                    user.password
                                )
                            )
                                throw SocketTimeoutException("Refreshed Token!")
                            else throw IllegalStateException("User Logged out!!")
                        }
                        5000 -> {
                            if(errorMessage=="No record found")
                                return@withContext 0
                        }
                        else -> {
                            throw IllegalStateException("$responseStatusCode received, dont know what todo!?")
                        }
                    }
                }
            }

        } catch (e: SocketTimeoutException) {
            Timber.d("get_ben error : $e")
            return@withContext -2

        } catch (e: java.lang.IllegalStateException) {
            Timber.d("get_ben error : $e")
            return@withContext -1
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
                tmcNetworkApiService.getBeneficiaries(
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
                                        hrpStatus = benDataObj.getBoolean("hrpStatus"),
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
    val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH)
    val localDateTime = formatter.parse(date)
    return localDateTime?.time ?: 0
}

private suspend fun getCbacCacheFromServerResponse(response: String): MutableList<CbacCache> {
    val jsonObj = JSONObject(response)
    val result = mutableListOf<CbacCache>()

    val responseStatusCode = jsonObj.getInt("statusCode")
    if (responseStatusCode == 200) {
        val dataObj = jsonObj.getJSONObject("data")
        val jsonArray = dataObj.getJSONArray("data")

        if (jsonArray.length() != 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val cbacDataObj = jsonObject.getJSONObject("cbacDetails")
                val benId =
                    if (jsonObject.has("benficieryid")) jsonObject.getLong("benficieryid") else -1L
                val hhId =
                    if (jsonObject.has("houseoldId")) jsonObject.getLong("houseoldId") else -1L
                if (benId == -1L || hhId == -1L)
                    continue
                if (cbacDataObj.length() == 0) continue
                if (database.cbacDao.getCbacFromBenId(benId) != null) continue
                val ben = database.benDao.getBen(hhId, benId) ?: continue
                val user = database.userDao.getLoggedInUser()!!


                try {
                    result.add(
                        CbacCache(
                            benId = ben.beneficiaryId,
                            hhId = cbacDataObj.getLong("houseoldId"),
                            ashaId = ben.ashaId,
                            gender = ben.gender!!,
                            cbac_age_posi = cbacDataObj.getInt("cbac_age_posi"),
                            cbac_smoke_posi = cbacDataObj.getInt("cbac_smoke_posi"),
                            cbac_alcohol_posi = cbacDataObj.getInt("cbac_alcohol_posi"),
                            cbac_waist_posi = cbacDataObj.getInt("cbac_waist_posi"),
                            cbac_pa_posi = cbacDataObj.getInt("cbac_pa_posi"),
                            cbac_familyhistory_posi = cbacDataObj.getInt("cbac_familyhistory_posi"),
                            total_score = cbacDataObj.getInt("total_score"),
                            cbac_sufferingtb_pos = cbacDataObj.getInt("cbac_sufferingtb_pos"),
                            cbac_antitbdrugs_pos = cbacDataObj.getInt("cbac_antitbdrugs_pos"),
                            cbac_tbhistory_pos = cbacDataObj.getInt("cbac_tbhistory_pos"),
                            cbac_sortnesofbirth_pos = cbacDataObj.getInt("cbac_sortnesofbirth_pos"),
                            cbac_coughing_pos = cbacDataObj.getInt("cbac_coughing_pos"),
                            cbac_bloodsputum_pos = cbacDataObj.getInt("cbac_bloodsputum_pos"),
                            cbac_fivermore_pos = cbacDataObj.getInt("cbac_fivermore_pos"),
                            cbac_loseofweight_pos = cbacDataObj.getInt("cbac_loseofweight_pos"),
                            cbac_nightsweats_pos = cbacDataObj.getInt("cbac_nightsweats_pos"),
                            cbac_historyoffits_pos = cbacDataObj.getInt("cbac_historyoffits_pos"),
                            cbac_difficultyinmouth_pos = cbacDataObj.getInt("cbac_difficultyinmouth_pos"),
                            cbac_uicers_pos = cbacDataObj.getInt("cbac_uicers_pos"),
                            cbac_toneofvoice_pos = cbacDataObj.getInt("cbac_toneofvoice_pos"),
                            cbac_lumpinbreast_pos = cbacDataObj.getInt("cbac_lumpinbreast_pos"),
                            cbac_blooddischage_pos = cbacDataObj.getInt("cbac_blooddischage_pos"),
                            cbac_changeinbreast_pos = cbacDataObj.getInt("cbac_changeinbreast_pos"),
                            cbac_bleedingbtwnperiods_pos = cbacDataObj.getInt("cbac_bleedingbtwnperiods_pos"),
                            cbac_bleedingaftermenopause_pos = cbacDataObj.getInt("cbac_bleedingaftermenopause_pos"),
                            cbac_bleedingafterintercourse_pos = cbacDataObj.getInt("cbac_bleedingafterintercourse_pos"),
                            cbac_foulveginaldischarge_pos = cbacDataObj.getInt("cbac_foulveginaldischarge_pos"),
                            cbac_growth_in_mouth_posi = cbacDataObj.getInt("cbac_growth_in_mouth_posi"),
                            cbac_Pain_while_chewing_posi = cbacDataObj.getInt("cbac_Pain_while_chewing_posi"),
                            cbac_hyper_pigmented_patch_posi = cbacDataObj.getInt("cbac_hyper_pigmented_patch_posi"),
                            cbac_any_thickend_skin_posi = cbacDataObj.getInt("cbac_any_thickend_skin_posi"),
                            cbac_nodules_on_skin_posi = cbacDataObj.getInt("cbac_nodules_on_skin_posi"),
                            cbac_numbness_on_palm_posi = cbacDataObj.getInt("cbac_numbness_on_palm_posi"),
                            cbac_clawing_of_fingers_posi = cbacDataObj.getInt("cbac_clawing_of_fingers_posi"),
                            cbac_tingling_or_numbness_posi = cbacDataObj.getInt("cbac_tingling_or_numbness_posi"),
                            cbac_inability_close_eyelid_posi = cbacDataObj.getInt("cbac_inability_close_eyelid_posi"),
                            cbac_diff_holding_obj_posi = cbacDataObj.getInt("cbac_diff_holding_obj_posi"),
                            cbac_weekness_in_feet_posi = cbacDataObj.getInt("cbac_weekness_in_feet_posi"),
                            cbac_fuel_used_posi = cbacDataObj.getInt("cbac_fuel_used_posi"),
                            cbac_occupational_exposure_posi = cbacDataObj.getInt("cbac_occupational_exposure_posi"),
                            cbac_little_interest_posi = cbacDataObj.getInt("cbac_little_interest_posi"),
                            cbac_feeling_down_posi = cbacDataObj.getInt("cbac_feeling_down_posi"),
                            cbac_little_interest_score = cbacDataObj.getInt("cbac_little_interest_score"),
                            cbac_feeling_down_score = cbacDataObj.getInt("cbac_feeling_down_score"),
                            cbac_referpatient_mo = cbacDataObj.getInt("cbac_referpatient_mo")
                                .toString(),
                            cbac_tracing_all_fm = cbacDataObj.getInt("cbac_tracing_all_fm")
                                .toString(),
                            cbac_sputemcollection = cbacDataObj.getInt("cbac_sputemcollection")
                                .toString(),
                            serverUpdatedStatus = cbacDataObj.getInt("serverUpdatedStatus"),
                            createdBy = cbacDataObj.getString("createdBy"),
                            createdDate = getLongFromDate(cbacDataObj.getString("createdDate")),
                            ProviderServiceMapID = cbacDataObj.getInt("ProviderServiceMapID"),
                            VanID = if (cbacDataObj.has("vanID")) cbacDataObj.getInt("vanID") else user.vanId,
                            Processed = cbacDataObj.getString("Processed"),
                            Countyid = cbacDataObj.getInt("Countyid"),
                            stateid = cbacDataObj.getInt("stateid"),
                            districtid = cbacDataObj.getInt("districtid"),
                            villageid = cbacDataObj.getInt("villageid"),
                            cbac_reg_id = if (cbacDataObj.has("BenRegId")) cbacDataObj.getLong("BenRegId") else 1L,
                            suspected_hrp = cbacDataObj.getString("suspected_hrp"),
                            confirmed_hrp = cbacDataObj.getString("confirmed_hrp"),
                            suspected_ncd = cbacDataObj.getString("suspected_ncd"),
                            confirmed_ncd = cbacDataObj.getString("confirmed_ncd"),
                            suspected_tb = cbacDataObj.getString("suspected_tb"),
                            confirmed_tb = cbacDataObj.getString("confirmed_tb"),
                            suspected_ncd_diseases = cbacDataObj.getString("suspected_ncd_diseases"),
                            diagnosis_status = cbacDataObj.getString("confirmed_tb"),
                        )
                    )
                } catch (e: JSONException) {
                    Timber.i("Cbac skipped: ${jsonObject.getLong("benficieryid")} with error $e")
                }
            }
        }
    }
    return result
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
                                    "Years" -> AgeUnit.YEARS
                                    "Months" -> AgeUnit.MONTHS
                                    "Days" -> AgeUnit.DAYS
                                    else -> AgeUnit.YEARS
                                }
                            } else null,
                            isKid = !(benDataObj.getString("age_unit") == "Years" && benDataObj.getInt(
                                "age"
                            ) > 14),
                            isAdult = (benDataObj.getString("age_unit") == "Years" && benDataObj.getInt(
                                "age"
                            ) > 14),
                            userImageBlob = getCompressedByteArray(benId, benDataObj),
                            regDate = if (benDataObj.has("registrationDate")) getLongFromDate(
                                benDataObj.getString("registrationDate")
                            ) else 0,
                            firstName = if (benDataObj.has("firstName")) benDataObj.getString("firstName") else null,
                            lastName = if (benDataObj.has("lastName")) benDataObj.getString("lastName") else null,
                            genderId = benDataObj.getInt("genderId"),
                            gender = if (benDataObj.has("gender")) {
                                when (benDataObj.getInt("genderId")) {
                                    1-> Gender.MALE
                                    2 -> Gender.FEMALE
                                    3 -> Gender.TRANSGENDER
                                    else -> Gender.MALE
                                }
                            } else null,
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
                            literacyId = if (benDataObj.has("literacyId")) benDataObj.getInt("literacyId") else 0,
                            community = if (benDataObj.has("community")) benDataObj.getString("community") else null,
                            communityId = if (benDataObj.has("communityId")) benDataObj.getInt("communityId") else 0,
                            religion = if (benDataObj.has("religion")) benDataObj.getString("religion") else null,
                            religionId = if (benDataObj.has("religionID")) benDataObj.getInt("religionID") else 0,
                            religionOthers = if (benDataObj.has("religionOthers")) benDataObj.getString(
                                "religionOthers"
                            ) else null,
                            rchId = if (benDataObj.has("rchid")) benDataObj.getString("rchid") else null,
                            registrationType = if (benDataObj.has("registrationType")) {
                                when (benDataObj.getString("registrationType")) {
                                    "NewBorn" -> {
                                        if (benDataObj.getString("age_unit") != "Years" || benDataObj.getInt(
                                                "age"
                                            ) < 2
                                        ) TypeOfList.INFANT
                                        else if (benDataObj.getInt("age") < 6) TypeOfList.CHILD
                                        else TypeOfList.ADOLESCENT
                                    }
                                    "General Beneficiary", "सामान्य लाभार्थी" -> if (benDataObj.has(
                                            "reproductiveStatus"
                                        )
                                    ) {
                                        with(benDataObj.getString("reproductiveStatus")) {
                                            when {
                                                contains("Eligible Couple") ||
                                                        contains("पात्र युगल") -> TypeOfList.ELIGIBLE_COUPLE
                                                contains("Antenatal Mother") -> TypeOfList.ANTENATAL_MOTHER
                                                contains("Delivery Stage") -> TypeOfList.DELIVERY_STAGE
                                                contains("Postnatal Mother") -> TypeOfList.POSTNATAL_MOTHER
                                                contains("Menopause Stage") -> TypeOfList.MENOPAUSE
                                                contains("Teenager") ||
                                                        contains("किशोरी") -> TypeOfList.TEENAGER
                                                else -> TypeOfList.GENERAL
                                            }
                                        }
                                    } else TypeOfList.GENERAL
                                    else -> TypeOfList.GENERAL
                                }
                            } else TypeOfList.OTHER,
                            latitude = benDataObj.getDouble("latitude"),
                            longitude = benDataObj.getDouble("longitude"),
                            aadharNum = if (benDataObj.has("aadhaNo")) benDataObj.getString("aadhaNo") else null,
                            aadharNumId = benDataObj.getInt("aadha_noId"),
                            hasAadhar = if (benDataObj.has("aadhaNo")) benDataObj.getString("aadhaNo") != "" else false,
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
                                heightAtBirth = if (childDataObj.has("heightAtBirth")) childDataObj.getDouble(
                                    "heightAtBirth"
                                ) else 0.0,
                                weightAtBirth = if (childDataObj.has("weightAtBirth")) childDataObj.getDouble(
                                    "weightAtBirth"
                                ) else 0.0,
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
                    val registrationType = if (benDataObj.has("registrationType")) {
                        when (benDataObj.getString("registrationType")) {
                            "NewBorn" -> {
                                if (benDataObj.getString("age_unit") != "Years" || benDataObj.getInt(
                                        "age"
                                    ) < 2
                                ) TypeOfList.INFANT
                                else if (benDataObj.getInt("age") < 6) TypeOfList.CHILD
                                else TypeOfList.ADOLESCENT
                            }
                            "General Beneficiary", "सामान्य लाभार्थी" -> if (benDataObj.has(
                                    "reproductiveStatus"
                                )
                            ) {
                                when (benDataObj.getString("reproductiveStatus")) {
                                    "Eligible Couple" -> TypeOfList.ELIGIBLE_COUPLE
                                    "Antenatal Mother" -> TypeOfList.ANTENATAL_MOTHER
                                    "Delivery Stage" -> TypeOfList.DELIVERY_STAGE
                                    "Postnatal Mother" -> TypeOfList.POSTNATAL_MOTHER
                                    "Menopause" -> TypeOfList.MENOPAUSE
                                    "Teenager" -> TypeOfList.TEENAGER
                                    else -> TypeOfList.OTHER
                                }
                            } else TypeOfList.GENERAL
                            else -> TypeOfList.GENERAL
                        }
                    } else TypeOfList.OTHER
                    Timber.d(
                        "Custom Validation: $registrationType, ${benDataObj.getString("age_unit")}, " +
                                "${benDataObj.getInt("age")}, ${benDataObj.getString("reproductiveStatus")}"
                    )
                } catch (e: JSONException) {
                    Timber.i("Beneficiary skipped: ${jsonObject.getLong("benficieryid")} with error $e")
                } catch (e: NumberFormatException) {
                    Timber.i("Beneficiary skipped: ${jsonObject.getLong("benficieryid")} with error $e")
                }
            }
        }
    }
    return result
}

private suspend fun getCompressedByteArray(benId: Long, benDataObj: JSONObject) =
    if (benDataObj.has("user_image"))
        ImageSizeConverter.compressByteArray(
            context,
            benId,
            benDataObj.getString("user_image")
        ) else null

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
                            houseNo = houseDataObj.getString("houseno")
                                .let { if (it == "null") null else it },
//                                rationCardDetails = houseDataObj.getString("rationCardDetails"),
                            povertyLine = houseDataObj.getString("type_bpl_apl"),
                            povertyLineId = houseDataObj.getInt("bpl_aplId"),
                            residentialArea = houseDataObj.getString("residentialArea")
                                .let { if (it == "null") null else it },
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

suspend fun getPncMothersFromHhId(hhId: Long): List<BenBasicCache> {
    return database.benDao.getAllPNCMotherListFromHousehold(hhId)
}


}