package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CBAC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = UserCache::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("ashaId"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class CbacCache(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(index = true)
    val benId: Long,
    @ColumnInfo(index = true)
    val hhId: Long,
    @ColumnInfo(index = true)
    val ashaId: Int,


    )