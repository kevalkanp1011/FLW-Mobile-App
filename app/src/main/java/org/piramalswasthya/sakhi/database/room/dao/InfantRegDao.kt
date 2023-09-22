package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.piramalswasthya.sakhi.model.DeliveryOutcomeCache
import org.piramalswasthya.sakhi.model.InfantRegCache

@Dao
interface InfantRegDao {

    @Query("SELECT * FROM INFANT_REG WHERE motherBenId =:benId limit 1")
    fun getInfantReg(benId: Long): InfantRegCache?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveInfantReg(infantRegCache: InfantRegCache)

    @Query("SELECT * FROM INFANT_REG WHERE processed = 'N'")
    suspend fun getAllUnprocessedInfantReg(): List<InfantRegCache>

    @Update
    suspend fun updateInfantReg(it: InfantRegCache)
}