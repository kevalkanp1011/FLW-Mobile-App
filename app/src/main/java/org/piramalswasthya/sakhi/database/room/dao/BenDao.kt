package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.BenBasicCache
import org.piramalswasthya.sakhi.model.BenRegCache

@Dao
interface BenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg ben: BenRegCache)

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

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age >= 30")
    fun getAllNCDEligibleList(): LiveData<List<BenBasicCache>>

//    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age >= 30 and cbacScore > 4")
//    fun getAllNCDPriorityList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age < 30 and isKid = 0")
    fun getAllNCDNonEligibleList(): LiveData<List<BenBasicCache>>

    // have to add those as well who we are adding to menopause list manually from app
    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 5")
    fun getAllMenopauseStageList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE gender = \"Female\" and age BETWEEN 15 AND 49")
    fun getAllReproductiveAgeList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE reproductiveStatusId = 4")
    fun getAllPNCMotherList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age < 2")
    fun getAllInfantList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age >= 2 and age < 6")
    fun getAllChildList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE age between 6 and 14")
    fun getAllAdolescentList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE immunizationStatus = 1")
    fun getAllImmunizationDueList(): LiveData<List<BenBasicCache>>

    @Query("SELECT * FROM BEN_BASIC_CACHE WHERE hrpStatus = 1")
    fun getAllHrpCasesList(): LiveData<List<BenBasicCache>>
}