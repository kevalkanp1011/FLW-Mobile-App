package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.database.room.SyncState

@Entity(
    tableName = "ELIGIBLE_COUPLE_REG",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId",/* "householdId"*/),
        childColumns = arrayOf("benId", /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ecrInd", value = ["benId",/* "hhId"*/])])

data class EligibleCoupleRegCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
//    val hhId: Long,
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
    var dob3: Long? = 0L,
    var age3: Int? = 0,
    var gender3: Gender? = null,
    var secondAndThirdChildGap: Int? = 0,
    var dob4: Long? = 0L,
    var age4: Int? = 0,
    var gender4: Gender? = null,
    var thirdAndFourthChildGap: Int? = 0,
    var dob5: Long? = 0L,
    var age5: Int? = 0,
    var gender5: Gender? = null,
    var fourthAndFifthChildGap: Int? = 0,
    var dob6: Long? = 0L,
    var age6: Int? = 0,
    var gender6: Gender? = null,
    var fifthANdSixthChildGap: Int? = 0,
    var dob7: Long? = 0L,
    var age7: Int? = 0,
    var gender7: Gender? = null,
    var sixthAndSeventhChildGap: Int? = 0,
    var dob8: Long? = 0L,
    var age8: Int? = 0,
    var gender8: Gender? = null,
    var seventhAndEighthChildGap: Int? = 0,
    var dob9: Long? = 0L,
    var age9: Int? = 0,
    var gender9: Gender? = null,
    var eighthAndNinthChildGap: Int? = 0,
    var processed: String? = null,
    var syncState: SyncState
): FormDataModel