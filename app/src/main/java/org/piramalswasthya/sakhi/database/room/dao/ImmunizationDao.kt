package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory
import org.piramalswasthya.sakhi.model.ImmunizationCache
import org.piramalswasthya.sakhi.model.ImmunizationDetailsCache
import org.piramalswasthya.sakhi.model.Vaccine

@Dao
interface ImmunizationDao {

    @Query("SELECT COUNT(*)>0 FROM VACCINE")
    suspend fun vaccinesLoaded(): Boolean

    @Insert
    suspend fun addVaccine(vararg vaccine: Vaccine)

    @Query("SELECT * FROM IMMUNIZATION WHERE beneficiaryId=:benId AND vaccineId =:vaccineId limit 1")
    suspend fun getImmunizationRecord(benId: Long, vaccineId: Int): ImmunizationCache?

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM BEN_BASIC_CACHE ben LEFT OUTER JOIN IMMUNIZATION imm WHERE( ben.dob BETWEEN :minDob AND :maxDob) "
    )
    fun getBenWithImmunizationRecords(
        minDob: Long,
        maxDob: Long,
//        vaccineIdList: List<Int>
    ): Flow<List<ImmunizationDetailsCache>>

    @Query("SELECT * FROM VACCINE WHERE category = :category")
    fun getVaccinesForCategory(category: ChildImmunizationCategory): Flow<List<Vaccine>>

    @Query("SELECT * FROM VACCINE WHERE id = :vaccineId limit 1")
    suspend fun getVaccineById(vaccineId: Int): Vaccine?
}