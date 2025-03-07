package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.configuration.FormDataModel
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.utils.HelperUtil

@Entity(
    tableName = "HBYC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId"/* "householdId"*/),
        childColumns = arrayOf("benId" /*"hhId"*/),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "hbycInd", value = ["benId"/* "hhId"*/])]
)

data class HBYCCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    var month: String? = null,
    var subcenterName: String? = null,
    var year: String? = null,
    var primaryHealthCenterName: String? = null,
    var villagePopulation: String? = null,
    var infantPopulation: String? = null,
    var visitdate: Long? = 0,
    var hbycAgeCategory: String? = null,
    var orsPacketDelivered: Int = 0,
    var ironFolicAcidGiven: Int = 0,
    var isVaccinatedByAge: Int = 0,
    var wasIll: Int = 0,
    var referred: Int = 0,
    var supplementsGiven: Int = 0,
    var byHeightLength: Int = 0,
    var childrenWeighingLessReferred: Int = 0,
    var weightAccordingToAge: Int = 0,
    var delayInDevelopment: Int = 0,
    var referredToHealthInstitite: Int = 0,
    var vitaminASupplementsGiven: Int = 0,
    var deathAge: String? = null,
    var deathCause: String? = null,
    var qmOrAnmInformed: Int = 0,
    var deathPlace: Int? = null,
    var superVisorOn: Int = 0,
    var orsShortage: Int = 0,
    var ifaDecreased: Int = 0,
    var processed: String,
    var syncState: SyncState,
    var createdBy: String? = null,
    var createdDate: Long? = System.currentTimeMillis(),
) : FormDataModel {
    fun asPostModel(
        user: User,
        household: HouseholdCache,
        ben: BenRegCache,
        hbycCount: Int
    ): HbycPost {
        return HbycPost(
            anmNameNumber = user.userId.toString(),
            ashaWorkerNameNumber = user.userName,
            beneficiaryid = benId,
            byHeightLenght = byHeightLength,
            childName = ben.firstName,
            childVaccinatedByAge = hbycAgeCategory?.toInt(),
            childWasIll = wasIll,
            createdBy = createdBy,
            createdDate = HelperUtil.getDateStrFromLong(createdDate),
            deadChildGender = ben.genderId,
            deadNameChild = ben.firstName,
            deathCause = deathCause,
            delayConstraint = delayInDevelopment,
            districtName = household.locationRecord.district.name,
            gender = ben.genderId,
            hbycByAge = hbycAgeCategory,
            houseoldId = hhId.toString(),
            id = hbycCount,
            ifYesReferHospital = referred,
            ifYesThenHealth = referredToHealthInstitite,
            ironicFolicAcidSyurp = ironFolicAcidGiven,
            loginId = hbycCount,
            markAgeInMonth = deathAge?.substringBefore(" ")?.toInt(),
            month = month?.toInt(),
            numberOfChildrenWeiingLess = childrenWeighingLessReferred,
            ors = orsPacketDelivered,
            orsInLastMonth = orsShortage,
            placeOfDeath = deathPlace,
            primaryHealthCenter = primaryHealthCenterName,
            qmAnmWasInformed = qmOrAnmInformed,
            subCenterName = subcenterName,
            supperVisionFromBlock = superVisorOn,
            supplementStarted = vitaminASupplementsGiven,
            supplimentGiven = supplementsGiven,
            totalNumberChildVillage = infantPopulation?.toInt(),
            updatedBy = user.userName,
            updatedDate = HelperUtil.getDateStrFromLong(System.currentTimeMillis()),
            villagePopulation = villagePopulation?.toInt(),
            villageid = household.locationRecord.village.id,
            vistDate = HelperUtil.getDateStrFromLong(visitdate),
            vitaminASupplements = vitaminASupplementsGiven,
            weightAccordingToChildAge = weightAccordingToAge,
            year = year,
        )
    }
}

@JsonClass(generateAdapter = true)
data class HbycPost(
    val anmNameNumber: String? = null,
    val ashaWorkerNameNumber: String? = null,
    val beneficiaryid: Long,
    val byHeightLenght: Int? = null,
    val childName: String? = null,
    val childVaccinatedByAge: Int? = null,
    val childWasIll: Int? = null,
    val createdBy: String? = null,
    val createdDate: String? = null,
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
    val month: Int? = null,
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
    val totalNumberChildVillage: Int? = null,
    val updatedBy: String? = null,
    val updatedDate: String? = null,
    val villagePopulation: Int? = null,
    val villageid: Int? = null,
    val vistDate: String? = null,
    val vitaminASupplements: Int? = null,
    val weightAccordingToChildAge: Int? = null,
    val year: String? = null,
) {
    fun toCache(houseoldId: Long): HBYCCache {
        return HBYCCache(
            benId = beneficiaryid,
            hhId = houseoldId,
            byHeightLength = byHeightLenght ?: 0,
            hbycAgeCategory = childVaccinatedByAge.toString(),
            wasIll = childWasIll ?: 0,
            createdBy = createdBy,
            createdDate = HelperUtil.getLongFromDateMDY(createdDate),
            deathCause = deathCause,
            delayInDevelopment = delayConstraint ?: 0,
            referred = ifYesReferHospital ?: 0,
            referredToHealthInstitite = ifYesThenHealth ?: 0,
            ironFolicAcidGiven = ironicFolicAcidSyurp ?: 0,
            deathAge = markAgeInMonth.toString() + " months",
            month = month.toString(),
            childrenWeighingLessReferred = numberOfChildrenWeiingLess ?: 0,
            orsPacketDelivered = ors ?: 0,
            orsShortage = orsInLastMonth ?: 0,
            deathPlace = placeOfDeath,
            primaryHealthCenterName = primaryHealthCenter,
            qmOrAnmInformed = qmAnmWasInformed ?: 0,
            subcenterName = subCenterName,
            superVisorOn = supperVisionFromBlock ?: 0,
            vitaminASupplementsGiven = supplementStarted ?: 0,
            supplementsGiven = supplimentGiven ?: 0,
            infantPopulation = totalNumberChildVillage.toString(),
            weightAccordingToAge = weightAccordingToChildAge ?: 0,
            year = year,
            processed = "P",
            syncState = SyncState.SYNCED
        )
    }
}