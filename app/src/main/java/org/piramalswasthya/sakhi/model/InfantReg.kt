package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.network.getLongFromDate
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(
    tableName = "INFANT_REG",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"),
        childColumns = arrayOf("motherBenId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "infRegInd", value = ["motherBenId"])]
)

data class InfantRegCache(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val motherBenId: Long,
    val isActive : Boolean ,
    var babyName: String? = null,
    var babyIndex: Int,
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
    var createdBy: String,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedBy: String,
    val updatedDate: Long = System.currentTimeMillis(),
    var syncState: SyncState
) : FormDataModel {

    private fun getDateStringFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        dateLong?.let {
            return dateFormat.format(dateLong)
        } ?: run {
            return null
        }
    }

    fun asPostModel(): InfantRegPost {
        return InfantRegPost(
            id = id,
            benId = motherBenId,
            babyName = babyName,
            babyIndex = babyIndex,
            infantTerm = infantTerm,
            corticosteroidGiven = corticosteroidGiven,
            gender = gender?.name,
            babyCriedAtBirth = babyCriedAtBirth,
            resuscitation = resuscitation,
            referred = referred,
            hadBirthDefect = hadBirthDefect,
            birthDefect = birthDefect,
            otherDefect = otherDefect,
            weight = weight,
            breastFeedingStarted = breastFeedingStarted,
            opv0Dose = getDateStringFromLong(opv0Dose),
            bcgDose = getDateStringFromLong(bcgDose),
            hepBDose = getDateStringFromLong(hepBDose),
            vitkDose = getDateStringFromLong(vitkDose),
            createdDate = getDateStringFromLong(createdDate),
            createdBy = createdBy,
            updatedDate = getDateStringFromLong(updatedDate),
            updatedBy = updatedBy
        )
    }
}


data class InfantRegPost(
    val id: Long = 0,
    val benId: Long,
    val babyName: String? = null,
    val babyIndex: Int,
    val infantTerm: String? = null,
    val corticosteroidGiven: String? = null,
    val gender: String? = null,
    val babyCriedAtBirth: Boolean? = null,
    val resuscitation: Boolean? = null,
    val referred: String? = null,
    val hadBirthDefect: String? = null,
    val birthDefect: String? = null,
    val otherDefect: String? = null,
    val weight: Double? = null,
    val breastFeedingStarted: Boolean? = null,
    val opv0Dose: String? = null,
    val bcgDose: String? = null,
    val hepBDose: String? = null,
    val vitkDose: String? = null,
    val createdDate: String? = null,
    val createdBy: String,
    val updatedDate: String? = null,
    val updatedBy: String
) {
    fun toCacheModel(): InfantRegCache {
        return InfantRegCache(
            id = id,
            motherBenId = benId,
            isActive = true,
            babyName = babyName,
            babyIndex = babyIndex,
            infantTerm = infantTerm,
            corticosteroidGiven = corticosteroidGiven,
//            gender = gender,
            babyCriedAtBirth = babyCriedAtBirth,
            resuscitation = resuscitation,
            referred = referred,
            hadBirthDefect = hadBirthDefect,
            birthDefect = birthDefect,
            otherDefect = otherDefect,
            weight = weight,
            breastFeedingStarted = breastFeedingStarted,
            opv0Dose = getLongFromDate(opv0Dose),
            bcgDose = getLongFromDate(bcgDose),
            hepBDose = getLongFromDate(hepBDose),
            vitkDose = getLongFromDate(vitkDose),
            processed = "P",
            createdBy = createdBy,
            createdDate = getLongFromDate(createdDate),
            updatedBy = updatedBy,
            updatedDate = getLongFromDate(updatedDate),
            syncState = SyncState.SYNCED
        )
    }
}