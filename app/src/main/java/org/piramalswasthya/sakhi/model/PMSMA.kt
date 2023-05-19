package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*


@Entity(
    tableName = "PMSMA",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
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

    var givenMCPCard : Boolean? = null,

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

    var tetanusToxoid: String? = null,

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
) {


    private fun getDateStringFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        dateLong?.let {
            return dateFormat.format(dateLong)
        } ?: run {
            return null
        }

    }

    fun asPostModel(user: UserCache , ben : BenRegCache): PmsmaPost {
        return PmsmaPost(
            abdominalCheckUp = abdominalCheckUp,
            address = address,
            beneficiaryHaveMcpCarc = if (haveMCPCard) "Yes" else "No",
            beneficiaryid = benId,
            bloodPressure = bloodPressure,
            bloodSugarTest = if (bloodSugarTest==true) "Yes" else "No",
            calciumSupplementation = if (calciumSupplementation==true) "Yes" else "No",
            createdBy = user.userName,
            createdDate = System.currentTimeMillis() ,
            edit_flag = false,
            fetalRatePerMinutes = fetalHRPM.toString(),
            fetalTwinsPregnancy = if (twinPregnancy) "Yes" else "No",
            hbsg = hbsc,
            himoglobinBloodGroup = haemoglobinAndBloodGroup,
            hiv = hiv,
            houseoldId = hhId.toString(),
            husbandName = husbandName,
            ironFolicAcid = if (ironFolicAcid==true) "Yes" else "No",
            latitude = 0.0,
            loginId = benId,
            longitude =0.0,
            systolic = systolicBloodPressure,
            diastolic = bloodPressure,
            malaria = malaria,
            mcpCardIsGiven = if (haveMCPCard) "Yes" else "No",
            mobileNumber = mobileNumber.toString(),
            name = ben.firstName,
            lmpDate = getDateStringFromLong(lastMenstrualPeriod) ,
            eddDate = getDateStringFromLong(expectedDateOfDelivery),
            pmsMaMctsRchNumber = mctsNumberOrRchNumber,
            pregnantHighRiskCategoryReffredEdd = "0",
            pregnantHighRiskCategoryReffredLmp = if(highRiskPregnancyReferred==true) "1" else "2",
            pregnantHighRiskCategoryTreatedEdd = "0",
            pregnantHighRiskCategoryTreatedLmp = if(highRiskPregnant==true) "1" else "2",
            pregnantOfHighRiskEdd = "0",
            pregnantOfHighRiskLmp = if(highriskSymbols==true) "1" else "2" ,
            preparationForBirthComplicationAdvice = if(birthPrepAndNutritionAndFamilyPlanning==true)  "1" else "2",
            selectTheReasonGivenBelowLmp = highRiskReason,
            signatureOfMedicalOfficer = medicalOfficerSign,
            swallonCondition = if(swollenCondtion==true) "1" else "2",
            teatnousToxoid = tetanusToxoid,
            ultrasound = if(ultraSound==true) "1" else "2",
            updatedBy = user.userName,
            updatedDate = System.currentTimeMillis(),
            urineAlubmin = urineAlbumin,
            vdrl = vdrl,
            villageid = ben.locationRecord.village.id,
            wasHivTest = if(hivTestDuringANC==true) "1" else "2",
            weight = weight.toString(),
            writeTheNumberOfAncs = numANC.toString(),
            )
    }
}

@JsonClass(generateAdapter = true)
data class PmsmaPost(

    val abdominalCheckUp: String? = null,
    val address: String? = null,
    val beneficiaryHaveMcpCarc: String? = null,
    val beneficiaryid: Long,
    val bloodPressure: String? = null,
    val bloodSugarTest: String? = null,
    val calciumSupplementation: String? = null,
    val createdBy: String? = null,
    val createdDate: Long? = System.currentTimeMillis() / 1000L,
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
    val updatedDate: Long? = System.currentTimeMillis() / 1000L,
    val urineAlubmin: String? = null,
    val vdrl: String? = null,
    val villageid: Int? = 0,
    val wasHivTest: String? = null,
    val weight: String? = null,
    val writeTheNumberOfAncs: String? = null
)