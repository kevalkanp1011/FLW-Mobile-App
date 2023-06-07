package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.database.room.SyncState

@Entity(
    tableName = "EligibleCoupleReg",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ecrInd", value = ["benId", "hhId"])]
)
data class EligibleCoupleRegCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    var dateOfReg: Long = 0L,
    var rchId: Long? = 0L,
    var name: String? = null,
    var husbandName: String? = null,
    var age: Int? = 0,
    var ageAtMarriage: Int? = 0,
    var aadharNo: Long? = 0L,
    var bankAccount: Long? = null,
    var bankName: String? = null,
    var branchName: String? = null,
    var ifsc: String? = null,
    var noOfChildren: Int? = 0,
    var noOfLiveChildren: Int? = 0,
    var noOfMaleChildren: Int? = 0,
    var noOfFemaleChildren: Int? = 0,
    var dob1: Long? = 0L,
    var age1: Int? = 0,
    var gender1: Gender? = null,
    var marriageFirstChildGap: Int? = 0,
    var dob2: Long? = 0L,
    var age2: Int? = 0,
    var gender2: Gender? = null,
    var firstAndSecondChildGap: Int? = 0,
    var processed: String? = null,
    var syncState: SyncState
)