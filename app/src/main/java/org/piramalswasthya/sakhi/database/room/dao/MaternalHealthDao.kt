package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.piramalswasthya.sakhi.model.EligibleCoupleTracking
import org.piramalswasthya.sakhi.model.PregnantWomanRegistrationCache

@Dao
interface MaternalHealthDao {

    @Query("select * from pregnancy_register where benId = :benId limit 1")
    fun getSavedRecord(benId : Long) : PregnantWomanRegistrationCache?

    @Query("select * from eligible_couple_tracking where benId = :benId order by visitDate limit 1")
    fun getEct(benId : Long) : EligibleCoupleTracking?

    @Insert
    suspend fun saveRecord(pregnancyRegistrationForm: PregnantWomanRegistrationCache)

    @Insert
    suspend fun saveRecord(eligibleCoupleTracking: EligibleCoupleTracking)

}