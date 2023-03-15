package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "HBNC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "hbncInd", value = ["benId", "hhId"])]
)

data class HBNCCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    val processed : String,

){
    fun asPostModel(user: UserCache, household: HouseholdCache, ben: BenRegCache, hbncCount: Int): HBNCPost {
        return HBNCPost(
            benId = benId,
            hhId = hhId

        )
    }
}

@JsonClass(generateAdapter = true)
data class HBNCPost (
    val benId: Long,
    val hhId: Long,
)