package org.piramalswasthya.sakhi.model

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.database.room.SyncState
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "HBNC",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "hbncInd", value = ["benId", "hhId"])]
)

data class HBNCCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    val homeVisitDate: Int,


    @Embedded(prefix = "day_1")
    var part0: HBNCPart0? = null,

    @Embedded(prefix = "part_A")
    var partA: HbncPartA? = null,

    @Embedded(prefix = "part_B")
    var partB: HbncPartB? = null,

    @Embedded(prefix = "part_C")
    var partC: HbncPartC? = null,

    @Embedded(prefix = "part_D")
    var partD: HbncPartD? = null,

    var ashaName: String? = null,
    var supervisorRemark: String? = null,
    var supervisorName: String? = null,
    var supervisorComments: String? = null,
    var dateSupervisorVisit: Long = 0,
    var processed: String? = null,
    var syncState: SyncState

) {

    companion object {
        private val format1 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    }

    private fun longToDateString(long: Long): String {
        return format1.format(Calendar.getInstance().apply { timeInMillis = long }.time)
    }

    fun asPostModel(
        user: UserCache,
        household: HouseholdCache,
//        ben: BenRegCache,
        hbncCount: Int
    ): HBNCPost {
        return HBNCPost(
            benId = benId,
            householdId = household.toString(),
            createdBy = user.userName,
            createdDate = System.currentTimeMillis() / 1000L,
            updatedBy = user.userName,
            updatedDate = System.currentTimeMillis() / 1000L,
            //Part 0
            healthSubCenterName = part0?.healthSubCenterName ?: "",
            phcName = part0?.phcName ?: "",
            motherName = part0?.motherName ?: "",
            fatherName = part0?.fatherName ?: "",
            dateOfDelivery = part0?.dateOfDelivery?.let { longToDateString(it) } ?: "",
            placeOfDelivery = part0?.placeOfDelivery ?: 0,
            babyGender = part0?.babyGender ?: 0,
            typeOfDelivery = part0?.typeOfDelivery ?: 0,
            initiationBreastFeedingStart = part0?.startedBreastFeed.toString(),
            weightAtBirth = part0?.weightAtBirth?.toString() ?: "0",
            dischargeDateFromHospital = part0?.dischargeDateFromHospital?.let { longToDateString(it) }
                ?: "",
            motherStatus = part0?.motherStatus ?: 0,
            registrationOfBirth = part0?.registeredAtBirth ?: 0,
            childStatus = part0?.childStatus ?: 0,
            homeVisitDate = homeVisitDate,
            chileImmunizationStatus = part0?.let { "${if (it.childImmunizedBCG) "1" else "0"}${if (it.childImmunizedPolio) "1" else "0"}${if (it.childImmunizedDpt) "1" else "0"}${if (it.childImmunizedHepB) "1" else "0"}".toInt() }
                ?: 0,
            newbornWeightRecorded = part0?.birthWeightRecordedInMCP ?: 0,
            deliveryTime = part0?.deliveryTime ?: "",
            dateCompletionPregnancy =part0?.dateOfCompletionOfPregnancy?.let { longToDateString(it) }
                ?: "",
            howManyWeekHaveBeenBorn = part0?.numWeeksWhenBorn?.toString()?:"",
            dateTimeOfFirstTraining = part0?.dateOfFirstTraining?.let { longToDateString(it) }
                ?: "",
            doesMotherHaveProblem = part0   ?.doesMotherHaveProblem.toString(),
            babyFedAfterBirth = part0?.babyFedAfterBirth?:0,
            babyFirstBreastfed = part0?.whenBabyFirstBreastFed?:"",
            howBabyBreastfeed = part0?.howBabyFirstFed?:0,
            problemInBreastfeeding = part0?.breastFeedProblem?:"",
            isMotherHavingTroubleBreastfeeding = part0?.breastFeedProblem2?:"",
            recordBabyTemp = part0?.measureRecordBabyTemperature?:"",
            babyEyeCondition = part0?.babyEyeCondition?:0,
            isThereBleedingFromUmbilicalCord = part0?.babyBleedUmbilical?:0,
            weighingMachineScaleColor = part0?.babyWeighingScaleColor?:0,
            bodyIsSluggish = part0?.babyAllOrganLethargic?:0,
            lessMilkIsDrinking = part0?.babyLessMilkDrinking?:0,
            notDrinkingMilk = part0?.babyNoDrinkMilk?:0,
            cryingSlow = part0?.babyCrySlow?:0,
            notCrying = part0?.babyNoCry?:0,
            whetherTheNewbornWasBeingLooked = part0?.babyBornLookedAfter?:0,
            childWasWipedWithCleanCloth = part0?.babyWipedCleanCloth?:0,
            childKeptWarm = part0?.babyKeptWarm?:0,
            childWasGivenABath = part0?.babyGivenBath?:0,
            childIsWrappedInClothAndKeptToTheMother = part0?.babyWrappedInClothKeptWithMother?:0,
            startedBreastFeedingOnly = part0?.startedBreastFeedOnlyGivenBreastMilk?:0,
            wasThereAnythingUnusualWithTheBath = part0?.babyAnythingUnusual?:0,

            //Part A
            howManyTimesTheMotherFeedsHerStomachIn24hours = partA?.numTimesEats.toString(),
            howManyPadsHaveBeenChangedInDayForBleeding = partA?.numPadsChanged.toString(),
            duringTheWinterSeasonIsKeptWarm = partA?.winterBabyKeptWarm?:0,
            isTheChildBreastFedProperly = partA?.breastFeedProper?:0,
            doesTheChildCryContinuouslyOrUrinateLessThan6TimesDay = partA?.babyCryContinuouslyOrUrinateLess6?:0,

            //Part B
            measureAndCheckTheTemperature = partB?.temperature?:"",
            wateryDischargeWithFoulSmell = partB?.waterDischargeFoulSmell?:0,
            doesTheMotherGrumbleUnevenlyOrHaveSeizure = partB?.motherGrumbleSeizure?:0,
            motherMilkIsNotBeingProducedAfterDelivery = partB?.motherNoOrLessMilk?:0,
            doesTheMotherHaveCrackedNipplePainBreast = partB?.crackedNipplePainHardBreast?:0,

            //Part C
            areEyesSwollen = partC?.eyesSwollenPusComing?:0,
            Weight = partC?.weightOnDayN.toString(),
            weightInKg = partC?.weightOnDayN.toString(),
            measureAndEnterTemperature = partC?.temperature?:"0",
            pusFilledPimplesInSkin = partC?.pusPimpleOnSkin?:0,
            crackedRednessOfTwistedSkin = partC?.crackedRednessOfTwistedSkin?:"",
//            eye = partC?.pusPimpleOnSkin?:0,
            seizures = partC?.seizure.toString(),
            isBreathGoingFast = partC?.breathGoingFast.toString(),
            isChildReferredPHCRHSDH = partC?.referredWhere?:"",

            //Part D
            allOrgansAreLethargic = partD?.organsLethargic?:"",
            hasStoppedDrinkingMilk = partD?.lessNoMilkDrinking?:"",
            isDrinkingLessMilk = partD?.lessNoMilkDrinking?:"",
            slowCrying = partD?.slowOrStoppedCrying?:"",
            stopCrying =  partD?.slowOrStoppedCrying?:"",
            bloatedStomachOrMotherTellsThatChildVomitsAgain = partD?.bloatedStomachOrVomit?:"",
            pusInTheNavel = partD?.pusInNavel?:"",

            //Extras
            id="0",
            loginId = 0,
            ashaName = user.userName,
            supervisorRemarks = supervisorRemark?:"",
            supervisorName = supervisorName?:"",
            supervisorTickMarks = supervisorComments?:"",
            visitDay = longToDateString(dateSupervisorVisit),
            signaturesWithDateOfSupervision = longToDateString(dateSupervisorVisit),
            villageId = user.villageIds.first(),
            villageName = user.villageEnglish.first()
        )
    }
}

data class HBNCPart0(
    val healthSubCenterName: String?,
    val phcName: String?,
    val motherName: String?,
    val fatherName: String?,
    val dateOfDelivery: Long,
    val placeOfDelivery: Int,
    val babyGender: Int,
    val typeOfDelivery: Int,
    val startedBreastFeed: Int,
    val weightAtBirth: Int, /// grams
    val dischargeDateFromHospital: Long,
    val motherStatus: Int,
    val registeredAtBirth: Int,
    val childStatus: Int,
    val childImmunizedBCG: Boolean,
    val childImmunizedPolio: Boolean,
    val childImmunizedDpt: Boolean,
    val childImmunizedHepB: Boolean,
    val birthWeightRecordedInMCP: Int,

    val deliveryTime: String?,
    val dateOfCompletionOfPregnancy: Long,
    val numWeeksWhenBorn: Int,
    val dateOfFirstTraining: Long,
    val doesMotherHaveProblem: Int,
    val babyFedAfterBirth: Int,
    val whenBabyFirstBreastFed: String?,
    val howBabyFirstFed: Int,
    val breastFeedProblem: String?,
    val breastFeedProblem2: String?,
    val measureRecordBabyTemperature: String?,
    val babyEyeCondition: Int,
    val babyBleedUmbilical: Int,
    val babyWeighingScaleColor: Int,

    val babyAllOrganLethargic: Int,
    val babyLessMilkDrinking: Int,
    val babyNoDrinkMilk: Int,
    val babyCrySlow: Int,
    val babyNoCry: Int,
    val babyBornLookedAfter: Int,
    val babyWipedCleanCloth: Int,
    val babyKeptWarm: Int,
    val babyGivenBath: Int,
    val babyWrappedInClothKeptWithMother: Int,
    val startedBreastFeedOnlyGivenBreastMilk: Int,
    val babyAnythingUnusual: Int,


    )

data class HbncPartA(
    val numTimesEats: Int,
    val numPadsChanged: Int,
    val winterBabyKeptWarm: Int,
    val breastFeedProper: Int,
    val babyCryContinuouslyOrUrinateLess6: Int
)

data class HbncPartB(
    val temperature: String?,
    val waterDischargeFoulSmell: Int,
    val motherGrumbleSeizure: Int,
    val motherNoOrLessMilk: Int,
    val crackedNipplePainHardBreast: Int

)

data class HbncPartC(
    val eyesSwollenPusComing: Int,
    val weightOnDayN: Int,
    val temperature: String?,
    val pusPimpleOnSkin: Int,
    val crackedRednessOfTwistedSkin: String?,
    val yellowEyePalmSoleSkin: Int,
    val seizure: Int,
    val breathGoingFast: Int,
    val referredWhere: String?
)

data class HbncPartD(
    val organsLethargic: String?,
    val lessNoMilkDrinking: String?,
    val slowOrStoppedCrying: String?,
    val bloatedStomachOrVomit: String?,
    val coldOrHotOnTouch: String?,
    val pusInNavel: String?,
)


@JsonClass(generateAdapter = true)
data class HBNCPost(
    val ashaName: String,
    val babyEyeCondition: Int,
    val babyFedAfterBirth: Int,
    val babyFirstBreastfed: String,
    val babyGender: Int,

    @Json(name = "beneficiaryid")
    val benId: Long,
    val bodyIsSluggish: Int,
    val childStatus: Int,
    val chileImmunizationStatus: Int,
    val createdBy: String,
    val createdDate: Long,//in seconds (millis/1000L) at source code
    val cryingSlow: Int,
    val dateCompletionPregnancy: String,
    val dateOfDelivery: String,
    val dateTimeOfFirstTraining: String,
    val deliveryTime: String,
    val dischargeDateFromHospital: String,
    val fatherName: String,
    val motherName: String,

    @Json(name = "healthSubcenterName")
    val healthSubCenterName: String,
    val homeVisitDate: Int,

    @Json(name = "houseoldId")
    val householdId: String,
    val howBabyBreastfeed: Int,
    val howManyWeekHaveBeenBorn: String,
    val isMotherHavingTroubleBreastfeeding: String,
    val isThereBleedingFromUmbilicalCord: Int,
    val lessMilkIsDrinking: Int,
    val loginId: Int,
    val motherStatus: Int,
    val newbornWeightRecorded: Int,
    val notCrying: Int,
    val notDrinkingMilk: Int,
    val phcName: String,
    val placeOfDelivery: Int,
    val problemInBreastfeeding: String,
    val recordBabyTemp: String,
    val registrationOfBirth: Int,
    val typeOfDelivery: Int,
    val updatedBy: String,
    val updatedDate: Long,//in seconds (millis/1000L) at source code

    @Json(name = "village_name")
    val villageName: String,

    @Json(name = "villageid")
    val villageId: Int,

    @Json(name = "vistDay")
    val visitDay: String,
    val weighingMachineScaleColor: Int,
    val weightAtBirth: String,
    val weightInKg: String,
    val whetherTheNewbornWasBeingLooked: Int,
    val childWasWipedWithCleanCloth: Int,
    val childKeptWarm: Int,

    @Json(name = "childWasGivenAbath")
    val childWasGivenABath: Int,

    @Json(name = "childIsWrappedInClothAndKeptTotheMother")
    val childIsWrappedInClothAndKeptToTheMother: Int,
    val startedBreastFeedingOnly: Int,

    @Json(name = "wasThereAnythinUnsualWithTheBath")
    val wasThereAnythingUnusualWithTheBath: Int,
    val howManyTimesTheMotherFeedsHerStomachIn24hours: String,

    @Json(name = "howManyPadsHaveBeenChangedInDayForbleeding")
    val howManyPadsHaveBeenChangedInDayForBleeding: String,
    val duringTheWinterSeasonIsKeptWarm: Int,
    val isTheChildBreastFedProperly: Int,

    @Json(name = "doesTheChildCryContinouslyOrUrniateLessThan6TimesDay")
    val doesTheChildCryContinuouslyOrUrinateLessThan6TimesDay: Int,
    val measureAndCheckTheTemperature: String,
    val motherMilkIsNotBeingProducedAfterDelivery: Int,

    @Json(name = "doesTheMotherGrumbleUnevnlyOrHaveSeizure")
    val doesTheMotherGrumbleUnevenlyOrHaveSeizure: Int,
    val doesTheMotherHaveCrackedNipplePainBreast: Int,
    val wateryDischargeWithFoulSmell: Int,
    val areEyesSwollen: Int,
    val Weight: String,
    val measureAndEnterTemperature: String,
    val pusFilledPimplesInSkin: Int,
    val crackedRednessOfTwistedSkin: String,
    val seizures: String,
    val isBreathGoingFast: String,

    @Json(name = "isChildReferedPHCRHSDH")
    val isChildReferredPHCRHSDH: String,

    @Json(name = "allOrgansAreLetharic")
    val allOrgansAreLethargic: String,
    val isDrinkingLessMilk: String,

    @Json(name = "hasStoppedDrinkginMilk")
    val hasStoppedDrinkingMilk: String,
    val slowCrying: String,

    @Json(name = "stopCyring")
    val stopCrying: String,

    @Json(name = "bloatedStomachOrMotherTellsThatChildsVomitsAgain")
    val bloatedStomachOrMotherTellsThatChildVomitsAgain: String,

    @Json(name = "pusIntheNavel")
    val pusInTheNavel: String,
    val doesMotherHaveProblem: String,
    val id: String,

    @Json(name = "initiationBreastFeedingstart")
    val initiationBreastFeedingStart: String,
    val supervisorTickMarks: String,
    val supervisorRemarks: String,
    val supervisorName: String,
    val signaturesWithDateOfSupervision: String,

    )