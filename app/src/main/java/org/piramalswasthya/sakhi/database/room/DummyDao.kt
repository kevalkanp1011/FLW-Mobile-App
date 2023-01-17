package org.piramalswasthya.sakhi.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DummyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dummy : DummyEntity)

    @Query("SELECT COUNT(*)>0 FROM dummy WHERE sync =:syncState")
    suspend fun getNumEntriesBySyncState(syncState: SyncState) : Boolean


}