package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "HBYC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "hbycInd", value = ["benId", "hhId"])]
)

data class HBYCCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    val processed : String,
    var month: String? = null,
    var subcenterName: String? = null,
    var year: String? = null,
    var primaryHealthCenterName: String? = null,
    var villagePopulation: String? = null,
    var infantPopulation: String? = null,
    var visitdate: Long? = 0,
    var hbycAgeCategory: String? = null,
    var orsPacketDelivered: Boolean? = null,
    var ironFolicAcidGiven: Boolean? = null,
    var isVaccinatedByAge: Boolean? = null,
    var wasIll: Boolean? = null,
    var referred: Boolean? = null,
    var supplementsGiven: Boolean? = null,
    var byHeightLength: Boolean? = null,
    var childrenWeighingLessReferred: Boolean? = null,
    var weightAccordingToAge: Boolean? = null,
    var delayInDevelopment: Boolean? = null,
    var referredToHealthInstitite: Boolean? = null,
    var vitaminASupplementsGiven: Boolean? = null,
    var deathAge: String? = null,
    var deathCause: String? = null,
    var qmOrAnmInformed: Boolean? = null,
    var deathPlace: String? = null,
    var superVisorOn: Boolean? = null,
    var orsShortage: Boolean? = null,
    var ifaDecreased: Boolean? = null,
    var createdBy: String? = null,
    var createdDate: Long? = System.currentTimeMillis(),
) {
    fun asPostModel(user: UserCache, household: HouseholdCache, ben: BenRegCache, pmjayCount: Int): HbycPost {
        return HbycPost(
            beneficiaryid = benId,
        )
    }
}

@JsonClass(generateAdapter = true)
data class HbycPost (
    val anmNameNumber: String? = null,
    val ashaWorkerNameNumber: String? = null,
    val beneficiaryid: Long,
    val byHeightLenght: Int? = null,
    val childName: String? = null,
    val childVaccinatedByAge: Int? = null,
    val childWasIll: Int? = null,
    val createdBy: String? = null,
    val createdDate: Long? = System.currentTimeMillis(),
    val deadChildGender: Int? = null,
    val deadNameChild: String? = null,
    val deathCause: String? = null,
    val delayConstraint: Int? = null,
    val districtName: String? = null,
    val gender: Int? = null,
    val hbycByAge: String? = null,
    val houseoldId: String? = null,
    val id: Int? = 0,
    val ifYesReferHospital: Int? = null,
    val ifYesThenHealth: Int? = null,
    val ironicFolicAcidSyurp: Int? = null,
    val loginId: Int? = 0,
    val markAgeInMonth: Int? = null,
    val month: String? = null,
    val numberOfChildrenWeiingLess: Int? = null,
    val ors: Int? = null,
    val orsInLastMonth: Int? = null,
    val placeOfDeath: Int? = null,
    val primaryHealthCenter: String? = null,
    val qmAnmWasInformed: Int? = null,
    val subCenterName: String? = null,
    val supperVisionFromBlock: Int? = null,
    val supplementStarted: Int? = null,
    val supplimentGiven: Int? = null,
    val totalNumberChildVillage: String? = null,
    val updatedBy: String? = null,
    val updatedDate: Long? = System.currentTimeMillis(),
    val villagePopulation: String? = null,
    val villageid: Int? = null,
    val vistDate: String? = null,
    val vitaminASupplements: Int? = null,
    val weightAccordingToChildAge: Int? = null,
    val year: String? = null,
)