package org.piramalswasthya.sakhi.database.room

import androidx.room.*

@Dao
interface BeneficiaryIdsAvailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg idEntry: BeneficiaryIdsAvail)

    @Query("SELECT COUNT(*) FROM BEN_ID_LIST")
    suspend fun count(): Int

    @Query("SELECT * FROM BEN_ID_LIST WHERE userId=:userId  LIMIT 1")
    suspend fun getEntry(userId: Int): BeneficiaryIdsAvail

    @Delete
    suspend fun delete(idEntry: BeneficiaryIdsAvail)


}