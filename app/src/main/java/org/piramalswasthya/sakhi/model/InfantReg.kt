package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.database.room.SyncState

@Entity(
    tableName = "INFANT_REG",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"),
        childColumns = arrayOf("motherBenId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "infRegInd", value = ["motherBenId"])])

data class InfantRegCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val motherBenId : Long,
    var babyName: String? = null,
    var infantTerm: String? = null,
    var corticosteroidGiven: String? = null,
    var gender: Gender? = null,
    var babyCriedAtBirth: Boolean? = null,
    var resuscitation: Boolean? = null,
    var referred: String? = null,
    var hadBirthDefect: String? = null,
    var birthDefect: String? = null,
    var otherDefect: String? = null,
    var weight: Double? = 0.0,
    var breastFeedingStarted: Boolean? = null,
    var opv0Dose: Long? = System.currentTimeMillis(),
    var bcgDose: Long? = System.currentTimeMillis(),
    var hepBDose: Long? = System.currentTimeMillis(),
    var vitkDose: Long? = System.currentTimeMillis(),
    var processed: String? = "N",
    var syncState: SyncState
):FormDataModel