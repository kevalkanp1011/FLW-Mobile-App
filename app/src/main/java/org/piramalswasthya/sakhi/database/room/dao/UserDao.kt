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

    //TODO(Map count from immunizationDueCount)
    @Query(
        "SELECT (SELECT COUNT(*)from HOUSEHOLD where ashaId=:userId and isDraft = 0) AS householdCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS allBenCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and age BETWEEN 15 AND 49 and gen_reproductiveStatusId = 1) AS eligibleCoupleCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and age < 2) AS infantCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and age between 2 and 6) AS childCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and age between 6 and 14) AS adolescentCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 2) AS pregnantCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 3) AS deliveryStageCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 4) AS pncMotherCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gender = \"Female\" and age BETWEEN 15 AND 49) AS reproductiveAgeCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 5) AS menopauseCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and immunizationStatus = 1)  AS immunizationDueCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and isHrpStatus = 1)  AS hrpCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0)  AS generalOpCareCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS deathReportCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS ncdCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and age >= 30) AS ncdEligibleCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS ncdPriorityCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and age < 30) AS ncdNonEligibleCount, " +
                "(SELECT COUNT(*) from BEN_ID_LIST where userId=:userId) AS availBenIdsCount "
    )
    fun getRecordCounts(
        userId: Int,
        ): LiveData<List<IconCount>>

    @Delete
    suspend fun logout(loggedInUser: UserCache)
}