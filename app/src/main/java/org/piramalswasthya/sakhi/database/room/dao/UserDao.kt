package org.piramalswasthya.sakhi.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.piramalswasthya.sakhi.model.*

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
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and (ageUnit = :year and age < 2) or ageUnit != :year)  AS infantCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and ageUnit = :year and age >= 2 and age < 6 ) AS childCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and ageUnit = :year and age between 6 and 14) AS adolescentCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 2) AS pregnantCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 3) AS deliveryStageCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 4) AS pncMotherCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gender =:female and age BETWEEN 15 AND 49) AS reproductiveAgeCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and gen_reproductiveStatusId = 5) AS menopauseCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and isKid = 1 or gen_reproductiveStatusId in (2, 3)) AS immunizationDueCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and isHrpStatus = 1)  AS hrpCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0)  AS generalOpCareCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS deathReportCount, " +
                "(SELECT COUNT(*) FROM BEN_BASIC_CACHE where age>30) AS ncdCount, " +
                "(SELECT COUNT(*) FROM BEN_BASIC_CACHE b LEFT OUTER JOIN CBAC c ON b.benId=c.benId where b.age>30 and b.ageUnit=:year  and c.benId IS NULL) AS ncdEligibleCount, " +
                "(SELECT COUNT(*) FROM BEN_BASIC_CACHE b INNER JOIN CBAC c on b.benId==c.benId WHERE c.total_score >= 4) AS ncdPriorityCount, " +
                "(SELECT COUNT(*) FROM BEN_BASIC_CACHE b INNER JOIN CBAC c on b.benId==c.benId WHERE c.total_score <= 4) AS ncdNonEligibleCount, " +
                "(SELECT COUNT(*) from BEN_ID_LIST where userId=:userId) AS availBenIdsCount "
    )
    fun getRecordCounts(
        userId: Int,
        year: AgeUnit = AgeUnit.YEARS,
        female: Gender = Gender.FEMALE
    ): LiveData<List<IconCount>>

    @Delete
    suspend fun logout(loggedInUser: UserCache)
}