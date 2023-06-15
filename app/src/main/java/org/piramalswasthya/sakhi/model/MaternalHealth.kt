package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.helpers.getWeeksOfPregnancy


data class PregnantWomenVisitCache(
    val benId: Long,
    val name: String,
    val dob: Long,
    val spouseName: String,
    val lmp: Long,
) {
    fun asDomainModel() =
        PregnantWomenVisitDomain(
            benId = benId,
            name = name,
            age = "${BenBasicCache.getAgeFromDob(dob)} Years",
            spouseName = spouseName,
            lmp = lmp,
            weeksOfPregnancy = getWeeksOfPregnancy(System.currentTimeMillis(), lmp)
        )

}

data class PregnantWomenVisitDomain(
    val benId: Long,
    val name: String,
    val age: String,
    val spouseName: String,
    val lmp: Long,
    val weeksOfPregnancy: Int,
)

data class AncStatus(
    val benId: Long,
    val visitNumber: Int,
    val formState: AncFormState
)

enum class AncFormState {
    ALLOW_FILL,
    ALREADY_FILLED,
    NO_FILL
}

@Entity(
    tableName = "PREGNANCY_REGISTER",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"/* "householdId"*/),
        childColumns = arrayOf("benId" /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_pwc", value = ["benId"/* "hhId"*/])]
)

data class PregnantWomanRegistrationCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    var dateOfRegistration: Long = System.currentTimeMillis(),
    var mcpCardNumber: Long = 0,
    var lmpDate: Long = 0,
//    var weeksOfPregnancy : String,
//    var weeksOfPregnancyId : Int,
//    var expectedDateOfDelivery : Long,
    var bloodGroup: String? = null,
    var bloodGroupId: Int = 0,
    var weight: Int? = null,
    var height: Int? = null,
    var vdrlRprTestResult: String? = null,
    var vdrlRprTestResultId: Int = 0,
    var dateOfVdrlRprTest: Long? = null,

    var hivTestResult: String? = null,
    var hivTestResultId: Int = 0,
    var dateOfHivTest: Long? = null,

    var hbsAgTestResult: String? = null,
    var hbsAgTestResultId: Int = 0,
    var dateOfHbsAgTest: Long? = null,

    var pastIllness: String? = null,
    var otherPastIllness: String? = null,
    var is1st: Boolean = true,
    var numPrevPregnancy: Int? = null,
    var complicationPrevPregnancy: String? = null,
    var complicationPrevPregnancyId: Int? = null,
    var otherComplication: String? = null,
    var isHrp: Boolean = false,
    var hrpIdBy: String? = null,
    var hrpIdById: Int = 0,
    var active: Boolean = true
) : FormDataModel


@Entity(
    tableName = "PREGNANCY_ANC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"/* "householdId"*/),
        childColumns = arrayOf("benId" /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "ind_mha", value = ["benId"/* "hhId"*/])],
)

data class PregnantWomanAncCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val visitNumber: Int,
    var ancDate: Long = System.currentTimeMillis(),
    var isAborted: Boolean = false,
    var abortionType: String? = null,
    var abortionTypeId: Int = 0,
    var abortionFacility: String? = null,
    var abortionFacilityId: Int = 0,
    var abortionDate: Long? = null,
    var weight: Int? = null,
    var bpSystolic: Int? = null,
    var bpDiastolic: Int? = null,
    var pulseRate: String? = null,
    var hb: Double? = null,
    var fundalHeight: Int? = null,
    var urineAlbumin: String? = null,
    var urineAlbuminId: Int = 0,
    var randomBloodSugarTest: String? = null,
    var randomBloodSugarTestId: Int = 0,
    var tt1: Long? = null,
    var tt2: Long? = null,
    var ttBooster: Long? = null,
    var numFolicAcidTabGiven: Int = 0,
    var numIfaAcidTabGiven: Int = 0,
    var anyHighRisk: Boolean = false,
    var highRisk: String? = null,
    var highRiskId: Int = 0,
    var otherHighRisk: String? = null,
    var referralFacility: String? = null,
    var referralFacilityId: Int = 0,
    var hrpConfirmed: Boolean? = null,
    var hrpConfirmedBy: String? = null,
    var hrpConfirmedById : Int = 0,
    var maternalDeath : Boolean = false,
    var maternalDeathProbableCause : String? = null,
    var maternalDeathProbableCauseId : Int = 0,
    var otherMaternalDeathProbableCause : String? = null,
    var deathDate : Long? = null,
    var pregnantWomanDelivered : Boolean? = null
) : FormDataModel