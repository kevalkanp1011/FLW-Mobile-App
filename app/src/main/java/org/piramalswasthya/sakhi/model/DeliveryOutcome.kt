package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.database.room.SyncState

@Entity(
    tableName = "DELIVERY_OUTCOME",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"),
        childColumns = arrayOf("benId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "delOutInd", value = ["benId"])])

data class DeliveryOutcomeCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId : Long,
    var dateOfDelivery: Long = System.currentTimeMillis(),
    var timeOfDelivery: String? = null,
    var placeOfDelivery: String? = null,
    var typeOfDelivery: String? = null,
    var hadComplications: Boolean? = null,
    var complication: String? = null,
    var causeOfDeath: String? = null,
    var otherCauseOfDeath: String? = null,
    var otherComplication: String? = null,
    var deliveryOutcome: Int? = 0,
    var liveBirth: Int? = 0,
    var stillBirth: Int? = 0,
    var dateOfDischarge: Long = System.currentTimeMillis(),
    var timeOfDischarge: String? = null,
    var isJSYBenificiary: Boolean? = null,
    var processed: String? = "N",
    var syncState: SyncState
) : FormDataModel