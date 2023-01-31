package org.piramalswasthya.sakhi.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "BEN_ID_LIST")
data class BeneficiaryIdsAvail (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var benId: Long,

    var benRegId: Int = 0
)