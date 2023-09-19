package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.database.room.SyncState

@Entity(
    tableName = "PNC_VISIT",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"/* "householdId"*/),
        childColumns = arrayOf("benId" /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_pnc", value = ["benId"/* "hhId"*/])],
)
data class PNCVisitCache(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val benId: Long,
    val pncPeriod: String,
    var pncDate: Long = System.currentTimeMillis(),
    var ifaTabsGiven: Int? = 0,
    var anyContraceptionMethod: Boolean? = null,
    var contraceptionMethod: String? = null,
    var otherPpcMethod: String? = null,
    var motherDangerSign: String? = null,
    var otherDangerSign: String? = null,
    var referralFacility: String? = null,
    var motherDeath: String? = null,
    var deathDate: Long? = System.currentTimeMillis(),
    var causeOfDeath: String? = null,
    var otherDeathCause: String? = null,
    var placeOfDeath: String? = null,
    var remarks: String? = null,
    var processed: String? = "N",
    var createdBy: String,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedBy: String,
    val updatedDate: Long = System.currentTimeMillis(),
    var syncState: SyncState
    )