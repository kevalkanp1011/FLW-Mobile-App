package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.BenRegCache

@Dao
interface BenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(vararg ben: BenRegCache)

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 1 and householdId =:hhId LIMIT 1")
    suspend fun getDraftBenKidForHousehold(hhId: Long): BenRegCache?

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0")
    fun getAllBen(): LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE beneficiaryId =:benId AND householdId = :hhId LIMIT 1")
    suspend fun getBen(hhId: Long, benId: Long): BenRegCache?

    @Query("UPDATE BENEFICIARY SET syncState = :syncState WHERE beneficiaryId =:benId AND householdId = :hhId")
    suspend fun setSyncState(hhId: Long, benId: Long, syncState: SyncState)

    @Query("DELETE FROM BENEFICIARY WHERE householdId = :hhId and isKid = :kid")
    suspend fun deleteBen(hhId: Long, kid: Boolean)

    @Query("UPDATE BENEFICIARY SET beneficiaryId = :newId, benRegId = :benRegId WHERE householdId = :hhId AND beneficiaryId =:oldId")
    suspend fun substituteBenId(hhId: Long, oldId: Long, newId: Long, benRegId: Long)

    @Query("UPDATE BENEFICIARY SET serverUpdatedStatus = 1, beneficiaryId = :newId , processed = \"U\"  WHERE householdId = :hhId AND beneficiaryId =:oldId")
    suspend fun updateToFinalBenId(hhId: Long, oldId: Long, newId: Long)

    @Query("SELECT * FROM BENEFICIARY WHERE isDraft = 0 AND processed = \"N\" AND syncState =:unsynced ")
    suspend fun getAllUnprocessedBen(unsynced: SyncState = SyncState.UNSYNCED): List<BenRegCache>

    @Query("UPDATE BENEFICIARY SET processed = \"P\", syncState = 2 WHERE beneficiaryId =:benId")
    suspend fun benSyncedWithServer(vararg benId: Long)

    @Query("UPDATE BENEFICIARY SET processed = \"U\", syncState = 0 WHERE beneficiaryId =:benId")
    suspend fun benSyncWithServerFailed(vararg benId: Long)

    @Query("SELECT beneficiaryId FROM BENEFICIARY WHERE beneficiaryId IN (:list)")
    fun getAllBeneficiaryFromList(list: List<Long>): LiveData<List<Long>>

    @Query("SELECT * FROM BENEFICIARY WHERE age BETWEEN 15 AND 49 and gender = \"Female\" and gen_marriageDate IS NOT NULL")
    fun getAllEligibleCoupleList(): LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE gender = \"Female\" and nishchayPregnancyStatus IS NOT NULL")
    fun getAllPregnancyWomenList(): LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE gender = \"Female\" and nishchayDeliveryStatus IS NOT NULL")
    fun getAllDeliveryStageWomenList(): LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE age >= 30")
    fun getAllNCDEligibleList(): LiveData<List<BenRegCache>>

//    @Query("SELECT * FROM BENEFICIARY WHERE age >= 30 and cbacScore > 4")
//    fun getAllNCDPriorityList(): LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE age < 30")
    fun getAllNCDNonEligibleList(): LiveData<List<BenRegCache>>

    // have to add those as well who we are adding to menopause list manually from app
    @Query("SELECT * FROM BENEFICIARY WHERE gender = \"Female\" and age > 50")
    fun getAllMenopauseStageList(): LiveData<List<BenRegCache>>

    @Query("SELECT * FROM BENEFICIARY WHERE gender = \"Female\" and gen_reproductiveStatus = \"Delivery Stage\"")
    fun getAllReproductiveAgeList(): LiveData<List<BenRegCache>>
}