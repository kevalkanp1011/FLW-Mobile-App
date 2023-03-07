package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*

@Dao
interface BenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg ben: BenRegCache)

    @Update
    suspend fun updateBen(ben: BenRegCache)

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 1 and householdId =:hhId LIMIT 1")
    suspend fun getDraftBenKidForHousehold(hhId: Long): BenRegCache?

    @Query("SELECT * FROM BEN_BASIC_CACHE")
    fun getAllBen(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE beneficiaryId =:benId AND householdId = :hhId LIMIT 1")
    suspend fun getBen(hhId: Long, benId: Long): BenRegCache?

    @Query("UPDATE BENEFICIARY SET syncState = :syncState WHERE beneficiaryId =:benId AND householdId = :hhId")
    suspend fun setSyncState(hhId: Long, benId: Long, syncState: SyncState)

    @Query("DELETE FROM BENEFICIARY WHERE householdId = :hhId and isKid = :kid")
    suspend fun deleteBen(hhId: Long, kid: Boolean)

    @Query("UPDATE BENEFICIARY SET beneficiaryId = :newId, benRegId = :benRegId WHERE householdId = :hhId AND beneficiaryId =:oldId")
    suspend fun substituteBenId(hhId: Long, oldId: Long, newId: Long, benRegId: Long)

    @Query("UPDATE BENEFICIARY SET serverUpdatedStatus = 1 , beneficiaryId = :newId , processed = 'U'  WHERE householdId = :hhId AND beneficiaryId =:oldId")
    suspend fun updateToFinalBenId(hhId: Long, oldId: Long, newId: Long)

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0 AND processed = 'N' AND syncState =:unsynced ")
    suspend fun getAllUnprocessedBen(unsynced: SyncState = SyncState.UNSYNCED): List<BenRegCache>

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0 AND processed = 'U' AND syncState =:unsynced ")
    suspend fun getAllBenForSyncWithServer(unsynced: SyncState = SyncState.UNSYNCED): List<BenRegCache>

    @Query("UPDATE BENEFICIARY SET processed = 'P' , syncState = :synced WHERE beneficiaryId =:benId")
    suspend fun benSyncedWithServer(benId: Long, synced: SyncState = SyncState.SYNCED)

    @Query("UPDATE BENEFICIARY SET processed = 'U' , syncState = 0 WHERE beneficiaryId =:benId")
    suspend fun benSyncWithServerFailed(vararg benId: Long)

    @Query("SELECT beneficiaryId FROM BENEFICIARY WHERE beneficiaryId IN (:list)")
    fun getAllBeneficiaryFromList(list: List<Long>): LiveData<List<Long>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age BETWEEN 15 AND 49 and reproductiveStatusId = 1")
    fun getAllEligibleCoupleList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 2")
    fun getAllPregnancyWomenList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 3")
    fun getAllDeliveryStageWomenList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE where age>30")
    fun getAllNCDList(): LiveData<List<BenBasicCache>>


    @Query("SELECT b.* FROM BEN_BASIC_CACHE b LEFT OUTER JOIN CBAC c ON b.benId=c.benId where b.age>30 and c.benId IS NULL")
    fun getAllNCDEligibleList(): LiveData<List<BenBasicCache>>

    @Query("SELECT b.* FROM BEN_BASIC_CACHE b INNER JOIN CBAC c on b.benId==c.benId WHERE c.total_score >= 4")
    fun getAllNCDPriorityList(): LiveData<List<BenBasicCache>>

    @Query("SELECT b.* FROM BEN_BASIC_CACHE b INNER JOIN CBAC c on b.benId==c.benId WHERE c.total_score <= 4")
    fun getAllNCDNonEligibleList(): LiveData<List<BenBasicCache>>

    // have to add those as well who we are adding to menopause list manually from app
    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 5")
    fun getAllMenopauseStageList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE gender = :female and age BETWEEN 15 AND 49")
    fun getAllReproductiveAgeList(female: Gender = Gender.FEMALE): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 4")
    fun getAllPNCMotherList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE ageUnit != :ageUnit or (ageUnit = :ageUnit and age < 2)")
    fun getAllInfantList(ageUnit: AgeUnit = AgeUnit.YEARS): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE ageUnit = :ageUnit and age >= 2 and age < 6")
    fun getAllChildList(ageUnit: AgeUnit = AgeUnit.YEARS): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE ageUnit = :ageUnit and age between 6 and 14")
    fun getAllAdolescentList(ageUnit: AgeUnit = AgeUnit.YEARS): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE isKid = 1 or reproductiveStatusId in (2, 3) ")
    fun getAllImmunizationDueList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE hrpStatus = 1")
    fun getAllHrpCasesList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 4 and hhId = :hhId")
    fun getAllPNCMotherListFromHousehold(hhId : Long): List<BenBasicCache>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE typeOfList = :infant or ageUnit = :ageUnit and age < 15")
    fun getAllCDRList(infant: TypeOfList = TypeOfList.INFANT, ageUnit: AgeUnit = AgeUnit.YEARS): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId in (2, 3, 4)")
    fun getAllMDSRList(): LiveData<List<BenBasicCache>>
}