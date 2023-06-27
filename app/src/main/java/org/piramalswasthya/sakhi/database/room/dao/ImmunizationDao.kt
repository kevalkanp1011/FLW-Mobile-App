package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.ChildImmunizationDetailsCache
import org.piramalswasthya.sakhi.model.ImmunizationCache
import org.piramalswasthya.sakhi.model.ImmunizationCategory
import org.piramalswasthya.sakhi.model.MotherImmunizationDetailsCache
import org.piramalswasthya.sakhi.model.Vaccine

@Dao
interface ImmunizationDao {

    @Query("SELECT COUNT(*)>0 FROM VACCINE")
    suspend fun vaccinesLoaded(): Boolean

    @Insert
    suspend fun addVaccine(vararg vaccine: Vaccine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImmunizationRecord( imm: ImmunizationCache)

    @Query("SELECT * FROM IMMUNIZATION WHERE beneficiaryId=:benId AND vaccineId =:vaccineId limit 1")
    suspend fun getImmunizationRecord(benId: Long, vaccineId: Int): ImmunizationCache?

    @Query(
        "SELECT * FROM BEN_BASIC_CACHE ben LEFT OUTER JOIN IMMUNIZATION imm WHERE ben.dob BETWEEN :minDob AND :maxDob "
    )
    fun getBenWithImmunizationRecords(
        minDob: Long,
        maxDob: Long,
//        vaccineIdList: List<Int>
    ): Flow<List<ChildImmunizationDetailsCache>>
    @Query(
        "SELECT ben.*, reg.lmpDate as lmp, imm.* FROM BEN_BASIC_CACHE ben inner join pregnancy_register reg on ben.benId = reg.benId LEFT OUTER JOIN IMMUNIZATION imm WHERE ben.reproductiveStatusId = :reproductiveStatusId "
    )
    fun getBenWithImmunizationRecords(
        reproductiveStatusId : Int = 2
//        vaccineIdList: List<Int>
    ): Flow<List<MotherImmunizationDetailsCache>>

    @Query("SELECT * FROM VACCINE where category = :immCat order by id")
    suspend fun getVaccinesForCategory(immCat : ImmunizationCategory): List<Vaccine>

    @Query("SELECT * FROM VACCINE WHERE id = :vaccineId limit 1")
    suspend fun getVaccineById(vaccineId: Int): Vaccine?
}