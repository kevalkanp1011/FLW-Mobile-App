package org.piramalswasthya.sakhi.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.database.room.SyncState
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(
    tableName = "ELIGIBLE_COUPLE_TRACKING",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"/* "householdId"*/),
        childColumns = arrayOf("benId" /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_ect", value = ["benId"/* "hhId"*/])]
)

data class EligibleCoupleTrackingCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    var visitDate: Long = System.currentTimeMillis(),
    var isPregnancyTestDone: String? = null,
    var pregnancyTestResult: String? = null,
    var isPregnant: String? = null,
    var usingFamilyPlanning: Boolean? = null,
    var methodOfContraception: String? = null,
    var processed: String? = "N",
    var syncState: SyncState
) : FormDataModel


data class BenWithEcTrackingCache(
//    @ColumnInfo(name = "benId")
//    val ecBenId: Long,

    @Embedded
    val ben: BenBasicCache,
    @Relation(
        parentColumn = "benId", entityColumn = "benId"
    )
    val ecr: EligibleCoupleRegCache,

    @Relation(
        parentColumn = "benId", entityColumn = "benId", entity = EligibleCoupleTrackingCache::class
    )
    val savedECTRecords: List<EligibleCoupleTrackingCache>
) {

    companion object {
        private val dateFormat = SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault())

        private fun getECTFilledDateFromLong(long: Long): String {
            return "Visited on ${dateFormat.format(long)}"
        }
    }

    fun asDomainModel(): BenWithEctListDomain {
        return BenWithEctListDomain(
//            ecBenId,
            ben.asBasicDomainModel(),
            ecr.noOfChildren?.toString() ?: "0",
            savedECTRecords.map {
                ECTDomain(
                    it.benId,
                    it.visitDate, getECTFilledDateFromLong(it.visitDate)
                )
            }
        )
    }
}

data class ECTDomain(
    val benId: Long,
    val filledOn: Long,
    val filledOnString: String
)

data class BenWithEctListDomain(
//    val benId: Long,
    val ben: BenBasicDomain,
    val numChildren: String,
    val savedECTRecords: List<ECTDomain>
)