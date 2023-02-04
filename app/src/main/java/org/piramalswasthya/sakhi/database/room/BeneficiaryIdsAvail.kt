package org.piramalswasthya.sakhi.database.room


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.model.UserCache


@Entity(
    tableName = "BEN_ID_LIST",
    foreignKeys = [
        ForeignKey(
            entity = UserCache::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BeneficiaryIdsAvail(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(index = true)
    val userId: Int,

    var benId: Long,

    var benRegId: Int = 0
)