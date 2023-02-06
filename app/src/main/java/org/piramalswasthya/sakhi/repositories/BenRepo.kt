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
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.asNetworkSendingModel
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
            val calendar = Calendar.getInstance()
            val mdFormat =
                SimpleDateFormat("yyyy-MM-DDThh:mm:ss", Locale.ENGLISH)
            return mdFormat.format(calendar.time) + ".000Z"
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
            if (this.createdDate == null) {
                this.createdDate = System.currentTimeMillis()
                this.createdBy = user.userName
            } else {
                this.updatedDate = System.currentTimeMillis()
                this.updatedBy = user.userName
            }
            this.villageName = locationRecord.village
            this.countyId = locationRecord.countryId
            this.stateId = locationRecord.stateId
            this.districtId = locationRecord.districtId
            this.villageId = locationRecord.villageId
            this.isDraft = false
        }

        database.benDao.upsert(ben)
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
                if (this.createdDate == null) {
                    this.createdDate = System.currentTimeMillis()
                    this.createdBy = user.userName
                } else {
                    this.updatedDate = System.currentTimeMillis()
                    this.updatedBy = user.userName
                }
                this.villageName = it.village
                this.countyId = it.countryId
                this.stateId = it.stateId
                this.districtId = it.districtId
                this.villageId = it.villageId
                if (beneficiaryId < 0) {
                    val benIdObj = extractBenId()
                    this.beneficiaryId = benIdObj.benId
                    this.benRegId = benIdObj.benRegId

                }
                this.isDraft = false

            }

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
        }

        database.benDao.upsert(ben)
        try {
            if (locationRecord != null)
                createBenIdAtServerByBeneficiarySending(ben, locationRecord)
        } catch (e: java.lang.Exception) {
            Timber.d("Exception raised $e")
        }
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
            if (this.createdDate == null) {
                this.createdDate = System.currentTimeMillis()
                this.createdBy = user.userName
            } else {
                this.updatedDate = System.currentTimeMillis()
                this.updatedBy = user.userName
            }
            this.villageName = locationRecord.village
            this.countyId = locationRecord.countryId
            this.stateId = locationRecord.stateId
            this.districtId = locationRecord.districtId
            this.villageId = locationRecord.villageId
            this.isDraft = false
        }

        database.benDao.upsert(ben)

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

        try {
            createBenIdAtServerByBeneficiarySending(ben, locationRecord)
        } catch (e: java.lang.Exception) {
            Timber.d("Exception raised $e")
        }
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

    suspend fun createBenIdAtServerByBeneficiarySending(
        ben: BenRegCache,
        locationRecord: LocationRecord
    ) {
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val sendingData = ben.asNetworkSendingModel(user, locationRecord)
        val response = tmcNetworkApiService.getBenIdFromBeneficiarySending(sendingData)
        Timber.d(response.body()?.string() ?: "No Body inside the morgue!")


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
        try {
            val response = tmcNetworkApiService.getBenIdFromBeneficiarySending(sendingData)
            Timber.d(response.body()?.string() ?: "No Body inside the morgue!")
        } catch (e: java.lang.Exception) {
            Timber.d("Caugnt error $e")
        } finally {
            database.benDao.setSyncState(hhId, benId, SyncState.UNSYNCED)
        }


    }

    suspend fun getBeneficiariesFromServer() {
        val user =
            database.userDao.getLoggedInUser() ?: throw IllegalStateException("No user logged in!!")
        val response =
            ncdNetworkApiService.getBeneficiaries(
                GetBenRequest(
                    user.userId.toString(), 0,
                    "2020-10-20T15:50:45.000Z", getCurrentDate()
                )
            )
        val statusCode = response.code()
        if (statusCode == 200) {
            val responseString = response.body()?.string()
                ?: throw IllegalStateException("response body empty here!")
            Timber.d("GetBeneficiaries : $responseString" )
        }
    }

}