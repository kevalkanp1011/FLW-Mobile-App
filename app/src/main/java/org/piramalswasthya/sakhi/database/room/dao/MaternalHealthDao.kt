package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.*

@Dao
interface MaternalHealthDao {

    @Query("select * from pregnancy_register where benId = :benId limit 1")
    fun getSavedRecord(benId: Long): PregnantWomanRegistrationCache?

    @Query("select * from pregnancy_anc where benId = :benId and visitNumber = :visitNumber limit 1")
    fun getSavedRecord(benId: Long, visitNumber: Int): PregnantWomanAncCache?

    @Query("select * from eligible_couple_tracking where benId = :benId order by visitDate limit 1")
    fun getEct(benId : Long) : EligibleCoupleTracking?

    @Insert
    suspend fun saveRecord(pregnancyRegistrationForm: PregnantWomanRegistrationCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecord(ancCache: PregnantWomanAncCache)

    @Query("select benId, visitNumber, :filled as formState from pregnancy_anc where benId = :benId order by visitNumber")
    suspend fun getAllAncRecordsFor(
        benId: Long,
        filled: AncFormState = AncFormState.ALREADY_FILLED
    ): List<AncStatus>

    @Query("select * from pregnancy_register reg left outer join pregnancy_anc anc on reg.benId=anc.benId ")
    fun getAllPregnancyRecords(): Flow<Map<PregnantWomanRegistrationCache, List<PregnantWomanAncCache>>>

    @Insert
    suspend fun saveRecord(eligibleCoupleTracking: EligibleCoupleTracking)

}