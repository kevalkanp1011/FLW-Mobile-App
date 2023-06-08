package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel


@Entity(
    tableName = "PREGNANCY_REGISTER",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId",/* "householdId"*/),
        childColumns = arrayOf("benId", /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_pwc", value = ["benId",/* "hhId"*/])]
)

data class PregnantWomanRegistrationCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId : Long,
    var dateOfRegistration : Long = System.currentTimeMillis(),
    var mcpCardNumber : Long = 0,
    var lmpDate : Long = 0,
//    var weeksOfPregnancy : String,
//    var weeksOfPregnancyId : Int,
//    var expectedDateOfDelivery : Long,
    var bloodGroup : String? = null,
    var bloodGroupId : Int = 0,
    var weight : Int? = null,
    var height : Int? = null,
    var vdrlRprTestResult : String? = null,
    var vdrlRprTestResultId: Int =0,
    var dateOfVdrlRprTest : Long? = null,

    var hivTestResult: String? = null,
    var hivTestResultId :Int =0,
    var dateOfHivTest : Long? = null,

    var hbsAgTestResult : String? = null,
    var hbsAgTestResultId : Int =0,
    var dateOfHbsAgTest : Long? = null,

    var pastIllness : String? = null,
    var otherPastIllness: String? = null,
    var is1st : Boolean = true,
    var numPrevPregnancy : Int? = null,
    var complicationPrevPregnancy : String? = null,
    var complicationPrevPregnancyId : Int? = null,
    var otherComplication : String? = null,
    var isHrp : Boolean = false,
    var hrpIdBy : String? = null,
    var hrpIdById : Int
) : FormDataModel