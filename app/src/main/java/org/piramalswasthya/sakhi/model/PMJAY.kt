package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "PMJAY",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "pmjayInd", value = ["benId", "hhId"])]
)

data class PMJAYCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    val registrationDate: Long? = System.currentTimeMillis(),
    val registeredHospital: String? = null,
    val contactNumber: Long? = 0,
    val communicationContactNumber: Long? = 0,
    val patientAddress: String? = null,
    val communicationAddress: String? = null,
    val hospitalAddress: String? = null,
    val familyId: Long? = 0,
    val hasAadhaar: Boolean? = null,
    val memberType: String? = null,
    val PatientType: String? = null,
    val scheme: String? = null,
    var createdBy: String? = null,
    var createdDate: Long? = System.currentTimeMillis(),
    var processed: String? = null,
)