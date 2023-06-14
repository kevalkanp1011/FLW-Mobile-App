package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel

@Entity(
    tableName = "TB_SCREENING",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId",/* "householdId"*/),
        childColumns = arrayOf("benId", /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_tbsn", value = ["benId",/* "hhId"*/])]
)

data class TBScreeningCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId : Long,
    var visitDate: Long = System.currentTimeMillis(),
    var coughMoreThan2Weeks: Boolean? = null,
    var bloodInSputum: Boolean? = null,
    var feverMoreThan2Weeks: Boolean? = null,
    var lossOfWeight: Boolean? = null,
    var nightSweats: Boolean? = null,
    var historyOfTb: Boolean? = null,
    var takingAntiTBDrugs: Boolean? = null,
    var familySufferingFromTB: Boolean? = null
) : FormDataModel