package org.piramalswasthya.sakhi.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DUMMY")
data class DummyEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int =0 ,

    val data : String,
    val sync : SyncState
)