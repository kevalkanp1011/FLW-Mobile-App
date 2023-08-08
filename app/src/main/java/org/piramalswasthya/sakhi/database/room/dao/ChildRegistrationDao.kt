package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.BenBasicCache

@Dao
interface ChildRegistrationDao {

//    @Query("SELECT b.*, c.* FROM BEN_BASIC_CACHE b join infant_reg i on b.benId = i.motherBenId left outer join CHILD_REG c on i.motherBenId= c.benId WHERE villageId=:selectedVillage")
    @Query("SELECT b.* FROM BEN_BASIC_CACHE b join infant_reg i on b.benId = i.motherBenId where b.villageId = :selectedVillage")
    fun getAllRegisteredInfants(selectedVillage: Int): Flow<List<BenBasicCache>>

}