package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.piramalswasthya.sakhi.model.PregnantWomanRegistrationCache

@Dao
interface MaternalHealthDao {

    @Query("select * from pregnancy_register where benId = :benId limit 1")
    fun getSavedRecord(benId : Long) : PregnantWomanRegistrationCache?

    @Insert
    suspend fun saveRecord(pregnancyRegistrationForm: PregnantWomanRegistrationCache)


}