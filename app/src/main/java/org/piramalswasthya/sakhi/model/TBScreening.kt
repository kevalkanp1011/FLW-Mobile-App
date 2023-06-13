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
    var name: String?,
    var age: Int?,
    var sex: String?,
    var coughMoreThan2Weeks: Boolean?,
    var bloodInSputum: Boolean?,
    var feverMoreThan2Weeks: Boolean?,
    var lossOfWeight: Boolean?,
    var nightSweats: Boolean?,
    var historyOfTb: Boolean?,
    var takingAntiTBDrugs: Boolean?,
    var familySufferingFromTB: Boolean?
) : FormDataModel