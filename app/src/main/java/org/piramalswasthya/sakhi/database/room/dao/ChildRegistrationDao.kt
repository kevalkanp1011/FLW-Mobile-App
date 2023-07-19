package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.piramalswasthya.sakhi.model.BenWithChildRegCache

@Dao
interface ChildRegistrationDao {

    @Query("SELECT b.*, c.* FROM BEN_BASIC_CACHE b join infant_reg i on b.benId = i.benId left outer join CHILD_REG c on i.benId= c.benId WHERE villageId=:selectedVillage")
    fun getAllRegisteredInfants(selectedVillage: Int): Flow<List<BenWithChildRegCache>>

}