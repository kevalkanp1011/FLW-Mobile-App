package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel

@Entity(
    tableName = "INFANT_REG",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"),
        childColumns = arrayOf("benId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "infRegInd", value = ["benId"])])

data class InfantRegCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId : Long,
    var babyName: String? = null,
    var infantTerm: String? = null,
    var corticosteroidGiven: String? = null,
    var gender: String? = null,
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
    var vitkDose: Long? = System.currentTimeMillis()
):FormDataModel