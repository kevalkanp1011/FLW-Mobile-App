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
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :eligibleCouple) AS eligibleCoupleCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :infant) AS infantCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :child) AS childCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :adolescent) AS adolescentCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :pregnant) AS pregnantCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :deliveryStage) AS deliveryStageCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :pncMother) AS pncMotherCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :reproductiveAge) AS reproductiveAgeCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0 and registrationType = :menopause) AS menopauseCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0)  AS immunizationDueCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0)  AS generalOpCareCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS deathReportCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS ncdCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS ncdEligibleCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS ncdPriorityCount, " +
                "(SELECT COUNT(*) from BENEFICIARY where ashaId=:userId and isDraft = 0) AS ncdNonEligibleCount, " +
                "(SELECT COUNT(*) from BEN_ID_LIST where userId=:userId) AS availBenIdsCount "
    )
    fun getRecordCounts(
        userId: Int,
        eligibleCouple: TypeOfList = TypeOfList.ELIGIBLE_COUPLE,
        infant: TypeOfList = TypeOfList.INFANT,
        child: TypeOfList = TypeOfList.CHILD,
        adolescent: TypeOfList = TypeOfList.ADOLESCENT,
        pregnant: TypeOfList = TypeOfList.ANTENATAL_MOTHER,
        deliveryStage: TypeOfList = TypeOfList.DELIVERY_STAGE,
        pncMother: TypeOfList = TypeOfList.POSTNATAL_MOTHER,
        reproductiveAge: TypeOfList = TypeOfList.ELIGIBLE_COUPLE,
        menopause: TypeOfList = TypeOfList.MENOPAUSE,


        ): LiveData<List<IconCount>>

    @Delete
    suspend fun logout(loggedInUser: UserCache)
}