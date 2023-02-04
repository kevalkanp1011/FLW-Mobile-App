package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.piramalswasthya.sakhi.model.IconCount
import org.piramalswasthya.sakhi.model.TypeOfList
import org.piramalswasthya.sakhi.model.UserCache

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserCache)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserCache)

    @Query("UPDATE USER SET logged_in = 0")
    suspend fun resetAllUsersLoggedInState()

    @Query("SELECT * FROM USER WHERE logged_in = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserCache?

    @Query("SELECT * FROM USER WHERE logged_in = 1 LIMIT 1")
    fun getLoggedInUserLiveData(): LiveData<UserCache>

    @Query(
        "SELECT " +
                "(SELECT COUNT(*)from HOUSEHOLD where ashaId=:userId and isDraft = 0) AS householdCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS allBenCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :eligibleCouple) AS eligibleCoupleCount, " +
                "(SELECT COUNT(*) from BEN_ID_LIST where userId=:userId) AS availBenIdsCount "
    )
    fun getRecordCounts(userId: Int, eligibleCouple: TypeOfList): LiveData<List<IconCount>>
}