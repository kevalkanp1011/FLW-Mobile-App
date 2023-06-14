package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel

@Entity(
    tableName = "TB_SUSPECTED",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId",/* "householdId"*/),
        childColumns = arrayOf("benId", /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_tbs", value = ["benId",/* "hhId"*/])]
)

data class TBSuspectedCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId : Long,
    var visitDate: Long = System.currentTimeMillis(),
    var isSputumCollected: Boolean? = null,
    var sputumSubmittedAt: String? = null,
    var nikshayId: String? = null,
    var sputumTestResult: String? = null,
    var referred: Boolean? = null,
    var followUps: String? = null
) : FormDataModel