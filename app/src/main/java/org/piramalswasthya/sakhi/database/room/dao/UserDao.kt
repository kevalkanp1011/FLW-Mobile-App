package org.piramalswasthya.sakhi.database.room.dao

import androidx.room.*
import org.piramalswasthya.sakhi.model.UserCache

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user : UserCache)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user : UserCache)

    @Query("UPDATE USER SET logged_in = 0")
    suspend fun resetAllUsersLoggedInState()

    @Query("SELECT * FROM USER WHERE logged_in = 1 LIMIT 1")
    suspend fun getLoggedInUser() : UserCache?
}