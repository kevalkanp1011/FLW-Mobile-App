package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass


@Entity(
    tableName = "PMSMA",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "pmsmaInd", value = ["benId", "hhId"])]
)

data class PMSMACache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,

    var mctsNumberOrRchNumber: String? = null,

    var haveMCPCard: Boolean = false,

    var husbandName: String? = null,

    var address: String? = null,

    var mobileNumber: Long = 0,

    var numANC: Int = 0,

    var weight: Int = 0,

    var systolicBloodPressure: String? = null,

    var bloodPressure: String? = null,

    var abdominalCheckUp: String? = null,

    var fetalHRPM: Int = 0,

    var twinPregnancy: Boolean = false,

    var urineAlbumin: String? = null,

    var haemoglobinAndBloodGroup: String? = null,

    var hiv: String? = null,

    var vdrl: String? = null,

    var hbsc: String? = null,

    var malaria: String? = null,

    var hivTestDuringANC: Boolean? = null,

    var swollenCondtion: Boolean? = null,

    var bloodSugarTest: Boolean? = null,

    var ultraSound: Boolean? = null,

    var ironFolicAcid: Boolean? = null,

    var calciumSupplementation: Boolean? = null,

    var tetanusToxoid: Boolean? = null,

    var lastMenstrualPeriod: Long = 0,

    var expectedDateOfDelivery: Long = 0,

    var highriskSymbols: Boolean? = null,

    var highRiskReason: String? = null,

    var highRiskPregnant: Boolean? = null,

    var highRiskPregnancyReferred: Boolean? = null,

    var birthPrepAndNutritionAndFamilyPlanning: Boolean? = null,

    var medicalOfficerSign: String? = null,

    var createdBy: String? = null,

    var createdDate: Long? = System.currentTimeMillis(),

    var processed: String? = null,
)

@JsonClass(generateAdapter = true)
data class PsmsaPost(

    val abdominalCheckUp: String? = null,
    val address: String? = null,
    val beneficiaryHaveMcpCarc: String? = null,
    val beneficiaryid: Long,
    val bloodPressure: String? = null,
    val bloodSugarTest: String? = null,
    val calciumSupplementation: String? = null,
    val createdBy: String? = null,
    val createdDate: Long? = System.currentTimeMillis()/1000L,
    val edit_flag: Boolean? = false,
    val fetalRatePerMinutes: String? = null,
    val fetalTwinsPregnancy: String? = null,
    val hbsg: String? = null,
    val himoglobinBloodGroup: String? = null,
    val hiv: String? = null,
    val houseoldId: String? = null,
    val husbandName: String? = null,
    val ironFolicAcid: String? = null,
    val latitude: Double? = 0.0,
    val loginId: Long,
    val longitude: Double? = 0.0,
    val systolic: String? = null,
    val diastolic: String? = null,
    val malaria: String? = null,
    val mcpCardIsGiven: String? = null,
    val mobileNumber: String? = null,
    val name: String? = null,
    val lmpDate: String? = null,
    val eddDate: String? = null,
    val pmsMaMctsRchNumber: String? = null,
    val pregnantHighRiskCategoryReffredEdd: String? = null,
    val pregnantHighRiskCategoryReffredLmp: String? = null,
    val pregnantHighRiskCategoryTreatedEdd: String? = null,
    val pregnantHighRiskCategoryTreatedLmp: String? = null,
    val pregnantOfHighRiskEdd: String? = null,
    val pregnantOfHighRiskLmp: String? = null,
    val preparationForBirthComplicationAdvice: String? = null,
    val selectTheReasonGivenBelowLmp: String? = null,
    val signatureOfMedicalOfficer: String? = null,
    val swallonCondition: String? = null,
    val teatnousToxoid: String? = null,
    val ultrasound: String? = null,
    val updatedBy: String? = null,
    val updatedDate: Long? = System.currentTimeMillis()/1000L,
    val urineAlubmin: String? = null,
    val vdrl: String? = null,
    val villageid: Int? = 0,
    val wasHivTest: String? = null,
    val weight: String? = null,
    val writeTheNumberOfAncs: String? = null
)