package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class ChildImmunizationCategory {
    BIRTH, WEEK_6, WEEK_10, WEEK_14, MONTH_9_12, MONTH_16_24, YEAR_5_6, YEAR_10, YEAR_16, CATCH_UP
}

@Entity(tableName = "VACCINE")
data class Vaccine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: Int,
    val category: ChildImmunizationCategory,
    val dueDuration: Long,
    val overdueDuration: Long,
    val dependantDose: Int? = null,
    val dependantCoolDuration: Long? = null,
)

@Entity(
    tableName = "IMMUNIZATION", primaryKeys = ["beneficiaryId", "vaccineId"], foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"),
        childColumns = arrayOf("beneficiaryId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Vaccine::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("vaccineId"),
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        name = "ind_imm", value = ["beneficiaryId"]
    ), Index(name = "ind_vaccine", value = ["vaccineId"])]
)
data class ImmunizationCache(
    val beneficiaryId: Long,
    val vaccineId: Int,
    var date: Long=0L,
    var placeId: Int=0,
    var place: String="",
    var byWhoId: Int=0,
    var byWho: String="",
)

data class ImmunizationDetailsCache(
    @ColumnInfo(name = "benId") val benId: Long,
    @ColumnInfo(name = "benName") val benName : String,
    @Relation(
        parentColumn = "benId", entityColumn = "beneficiaryId"
    ) val vaccineList: List<ImmunizationCache>
)

data class ImmunizationDetailsDomain(
    val benId: Long,
    val name: String,
    val vaccineStateList: List<VaccineDomain>,
//    val onClick: (Long, Int) -> Unit
)


data class VaccineDomain(
//    val benId: Long,
    val vaccineId: Int,
    val state: VaccineState,
)

class VaccineClickListener(private val clickListener: (benId: Long, vaccineId: Int) -> Unit) {
    fun onClick(benId: Long, vaccine: VaccineDomain) = clickListener(benId, vaccine.vaccineId)
}

data class ImmunizationDetailsHeader(
    val list: List<String>
)


enum class VaccineState {
    PENDING, OVERDUE, DONE, MISSED, UNAVAILABLE
}