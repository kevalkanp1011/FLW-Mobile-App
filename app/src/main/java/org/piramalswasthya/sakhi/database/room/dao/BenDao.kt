package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
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
    fun getAllBen(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE hhId = :hhId")
    suspend fun getAllBenForHousehold(hhId: Long): List<BenBasicCache>

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

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0 AND (processed = 'N' OR processed = 'U') AND syncState =:unsynced ")
    suspend fun getAllUnsyncedBen(unsynced: SyncState = SyncState.UNSYNCED): List<BenRegCache>

    @Query("SELECT COUNT(*) FROM BENEFICIARY WHERE isDraft = 0 AND (processed = 'N' OR processed = 'U') AND syncState =:unsynced ")
    fun getUnProcessedRecordCount(unsynced: SyncState = SyncState.UNSYNCED): Flow<Int>

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0 AND processed = 'U' AND syncState =:unsynced ")
    suspend fun getAllBenForSyncWithServer(unsynced: SyncState = SyncState.UNSYNCED): List<BenRegCache>

    @Query("UPDATE BENEFICIARY SET processed = 'P' , syncState = 2 WHERE beneficiaryId =:benId")
    suspend fun benSyncedWithServer(vararg benId: Long)

    @Query("UPDATE BENEFICIARY SET processed = 'U' , syncState = 0 WHERE beneficiaryId =:benId")
    suspend fun benSyncWithServerFailed(vararg benId: Long)

    @Query("SELECT beneficiaryId FROM BENEFICIARY WHERE beneficiaryId IN (:list)")
    fun getAllBeneficiaryFromList(list: List<Long>): LiveData<List<Long>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE /*age BETWEEN 15 AND 49 and*/ reproductiveStatusId = 1")
    fun getAllEligibleCoupleList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 2")
    fun getAllPregnancyWomenList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 3")
    fun getAllDeliveryStageWomenList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE where dob<=:ncdTimestamp")
    fun getAllNCDList(ncdTimestamp: Long): Flow<List<BenBasicCache>>


    @Query("SELECT b.* FROM BEN_BASIC_CACHE b LEFT OUTER JOIN CBAC c ON b.benId=c.benId where b.dob<=:ncdTimestamp and c.benId IS NULL")
    fun getAllNCDEligibleList(ncdTimestamp: Long): Flow<List<BenBasicCache>>

    @Query("SELECT b.* FROM BEN_BASIC_CACHE b INNER JOIN CBAC c on b.benId==c.benId WHERE c.total_score >= 4")
    fun getAllNCDPriorityList(): Flow<List<BenBasicCache>>

    @Query("SELECT b.* FROM BEN_BASIC_CACHE b INNER JOIN CBAC c on b.benId==c.benId WHERE c.total_score < 4")
    fun getAllNCDNonEligibleList(): Flow<List<BenBasicCache>>

    // have to add those as well who we are adding to menopause entries manually from app
    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 5")
    fun getAllMenopauseStageList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE gender = :female and dob BETWEEN :minReproductiveAge AND :maxReproductiveAge")
    fun getAllReproductiveAgeList(
        minReproductiveAge: Long,
        maxReproductiveAge: Long,
        female: Gender = Gender.FEMALE
    ): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 4")
    fun getAllPNCMotherList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE dob>=:infantTimestamp")
    fun getAllInfantList(infantTimestamp : Long): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE dob between :minChildTimestamp AND :maxChildTimestamp")
    fun getAllChildList( minChildTimestamp: Long,
                         maxChildTimestamp: Long,): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE dob between :minAdolescentTimestamp AND :maxAdolescentTimestamp")
    fun getAllAdolescentList(minAdolescentTimestamp: Long,
                             maxAdolescentTimestamp: Long,): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE isKid = 1 or reproductiveStatusId in (2, 3) ")
    fun getAllImmunizationDueList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE hrpStatus = 1")
    fun getAllHrpCasesList(): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 4 and hhId = :hhId")
    suspend fun getAllPNCMotherListFromHousehold(hhId: Long): List<BenBasicCache>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE dob>= :cdrTimestamp")
    fun getAllCDRList(
       cdrTimestamp : Long
    ): Flow<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId in (2, 3, 4)")
    fun getAllMDSRList(): Flow<List<BenBasicCache>>


}