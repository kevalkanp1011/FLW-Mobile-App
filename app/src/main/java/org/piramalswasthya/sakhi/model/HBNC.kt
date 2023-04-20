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


    @Embedded(prefix = "card_")
    var visitCard: HbncVisitCard? = null,

    @Embedded(prefix = "part_I_")
    var part1: HbncPartI? = null,

    @Embedded(prefix = "part_II_")
    var part2: HbncPartII? = null,

    @Embedded(prefix = "visit_")
    var homeVisitForm: HbncHomeVisit? = null,

//    @Embedded(prefix = "part_D")
//    var partD: HbncPartD? = null,

//    var ashaName: String? = null,
//    var supervisorRemark: String? = null,
//    var supervisorName: String? = null,
//    var supervisorComments: String? = null,
//    var dateSupervisorVisit: Long = 0,
    var processed: String? = null,
    var syncState: SyncState

) {

    companion object {
        private val format1 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    }

    private fun longToDateString(long: Long): String {
        return format1.format(Calendar.getInstance().apply { timeInMillis = long }.time)
    }



//    fun asPostModel(
//        user: UserCache,
//        household: HouseholdCache,
////        ben: BenRegCache,
//        hbncCount: Int
//    ): HBNCPost {
//        return HBNCPost(
//            benId = benId,
//            householdId = household.toString(),
//            createdBy = user.userName,
//            createdDate = System.currentTimeMillis() / 1000L,
//            updatedBy = user.userName,
//            updatedDate = System.currentTimeMillis() / 1000L,
//            //Part 0
//            healthSubCenterName = visitCard?.healthSubCenterName ?: "",
//            phcName = visitCard?.phcName ?: "",
//            motherName = visitCard?.motherName ?: "",
//            fatherName = visitCard?.fatherName ?: "",
//            dateOfDelivery = visitCard?.dateOfDelivery?.let { longToDateString(it) } ?: "",
//            placeOfDelivery = visitCard?.placeOfDelivery ?: 0,
//            babyGender = visitCard?.babyGender ?: 0,
//            typeOfDelivery = visitCard?.typeOfDelivery ?: 0,
//            initiationBreastFeedingStart = visitCard?.startedBreastFeed.toString(),
//            weightAtBirth = visitCard?.weightAtBirth?.toString() ?: "0",
//            dischargeDateFromHospital = visitCard?.dischargeDateFromHospital?.let { longToDateString(it) }
//                ?: "",
//            motherStatus = visitCard?.motherStatus ?: 0,
//            registrationOfBirth = visitCard?.registeredAtBirth ?: 0,
//            childStatus = visitCard?.childStatus ?: 0,
//            homeVisitDate = homeVisitDate,
//            chileImmunizationStatus = visitCard?.let { "${if (it.childImmunizedBCG) "1" else "0"}${if (it.childImmunizedPolio) "1" else "0"}${if (it.childImmunizedDpt) "1" else "0"}${if (it.childImmunizedHepB) "1" else "0"}".toInt() }
//                ?: 0,
//            newbornWeightRecorded = visitCard?.birthWeightRecordedInMCP ?: 0,
//            deliveryTime = visitCard?.deliveryTime ?: "",
//            dateCompletionPregnancy =visitCard?.dateOfCompletionOfPregnancy?.let { longToDateString(it) }
//                ?: "",
//            howManyWeekHaveBeenBorn = visitCard?.numWeeksWhenBorn?.toString()?:"",
//            dateTimeOfFirstTraining = visitCard?.dateOfFirstTraining?.let { longToDateString(it) }
//                ?: "",
//            doesMotherHaveProblem = visitCard   ?.doesMotherHaveProblem.toString(),
//            babyFedAfterBirth = visitCard?.babyFedAfterBirth?:0,
//            babyFirstBreastfed = visitCard?.whenBabyFirstBreastFed?:"",
//            howBabyBreastfeed = visitCard?.howBabyFirstFed?:0,
//            problemInBreastfeeding = visitCard?.breastFeedProblem?:"",
//            isMotherHavingTroubleBreastfeeding = visitCard?.breastFeedProblem2?:"",
//            recordBabyTemp = visitCard?.measureRecordBabyTemperature?:"",
//            babyEyeCondition = visitCard?.babyEyeCondition?:0,
//            isThereBleedingFromUmbilicalCord = visitCard?.babyBleedUmbilical?:0,
//            weighingMachineScaleColor = visitCard?.babyWeighingScaleColor?:0,
//            bodyIsSluggish = visitCard?.babyAllOrganLethargic?:0,
//            lessMilkIsDrinking = visitCard?.babyLessMilkDrinking?:0,
//            notDrinkingMilk = visitCard?.babyNoDrinkMilk?:0,
//            cryingSlow = visitCard?.babyCrySlow?:0,
//            notCrying = visitCard?.babyNoCry?:0,
//            whetherTheNewbornWasBeingLooked = visitCard?.babyBornLookedAfter?:0,
//            childWasWipedWithCleanCloth = visitCard?.babyWipedCleanCloth?:0,
//            childKeptWarm = visitCard?.babyKeptWarm?:0,
//            childWasGivenABath = visitCard?.babyGivenBath?:0,
//            childIsWrappedInClothAndKeptToTheMother = visitCard?.babyWrappedInClothKeptWithMother?:0,
//            startedBreastFeedingOnly = visitCard?.startedBreastFeedOnlyGivenBreastMilk?:0,
//            wasThereAnythingUnusualWithTheBath = visitCard?.babyAnythingUnusual?:0,
//
//            //Part A
//            howManyTimesTheMotherFeedsHerStomachIn24hours = part1?.numTimesEats.toString(),
//            howManyPadsHaveBeenChangedInDayForBleeding = part1?.numPadsChanged.toString(),
//            duringTheWinterSeasonIsKeptWarm = part1?.winterBabyKeptWarm?:0,
//            isTheChildBreastFedProperly = part1?.breastFeedProper?:0,
//            doesTheChildCryContinuouslyOrUrinateLessThan6TimesDay = part1?.babyCryContinuouslyOrUrinateLess6?:0,
//
//            //Part B
//            measureAndCheckTheTemperature = part2?.temperature?:"",
//            wateryDischargeWithFoulSmell = part2?.waterDischargeFoulSmell?:0,
//            doesTheMotherGrumbleUnevenlyOrHaveSeizure = part2?.motherGrumbleSeizure?:0,
//            motherMilkIsNotBeingProducedAfterDelivery = part2?.motherNoOrLessMilk?:0,
//            doesTheMotherHaveCrackedNipplePainBreast = part2?.crackedNipplePainHardBreast?:0,
//
//            //Part C
//            areEyesSwollen = homeVisitForm?.eyesSwollenPusComing?:0,
//            Weight = homeVisitForm?.weightOnDayN.toString(),
//            weightInKg = homeVisitForm?.weightOnDayN.toString(),
//            measureAndEnterTemperature = homeVisitForm?.temperature?:"0",
//            pusFilledPimplesInSkin = homeVisitForm?.pusPimpleOnSkin?:0,
//            crackedRednessOfTwistedSkin = homeVisitForm?.crackedRednessOfTwistedSkin?:"",
////            eye = homeVisitForm?.pusPimpleOnSkin?:0,
//            seizures = homeVisitForm?.seizure.toString(),
//            isBreathGoingFast = homeVisitForm?.breathGoingFast.toString(),
//            isChildReferredPHCRHSDH = homeVisitForm?.referredWhere?:"",
//
//            //Part D
//            allOrgansAreLethargic = partD?.organsLethargic?:"",
//            hasStoppedDrinkingMilk = partD?.lessNoMilkDrinking?:"",
//            isDrinkingLessMilk = partD?.lessNoMilkDrinking?:"",
//            slowCrying = partD?.slowOrStoppedCrying?:"",
//            stopCrying =  partD?.slowOrStoppedCrying?:"",
//            bloatedStomachOrMotherTellsThatChildVomitsAgain = partD?.bloatedStomachOrVomit?:"",
//            pusInTheNavel = partD?.pusInNavel?:"",
//
//            //Extras
//            id="0",
//            loginId = 0,
//            ashaName = user.userName,
//            supervisorRemarks = supervisorRemark?:"",
//            supervisorName = supervisorName?:"",
//            supervisorTickMarks = supervisorComments?:"",
//            visitDay = longToDateString(dateSupervisorVisit),
//            signaturesWithDateOfSupervision = longToDateString(dateSupervisorVisit),
//            villageId = user.villageIds.first(),
//            villageName = user.villageEnglish.first()
//        )
//    }
}

data class HbncVisitCard(
    val ashaName: String?,
    val villageName: String?,
    val subCenterName: String?,
    val blockName: String?,
    val motherName: String?,
    val fatherName: String?,
    val dateOfDelivery: Long,
    val placeOfDelivery: Int,
    val babyGender: Int,
    val typeOfDelivery: Int,
    val stillBirth: Int,
    val startedBreastFeeding: Int,
    val dischargeDateMother: Long,
    val dischargeDateBaby: Long,
    val weightInGrams: Int,
    val registrationOfBirth: Int
)

data class HbncPartI(
    val dateOfVisit : Long,
    val babyAlive: Int,
    val dateOfBabyDeath: Long,
    val timeOfBabyDeath: String?,
    val placeOfBabyDeath: Int,
    val otherPlaceOfBabyDeath: String?,
    val isBabyPreterm: Int,
    val gestationalAge: Int,
    val dateOfFirstExamination: Long,
    val timeOfFirstExamination: String?,
    val motherAlive: Int,
    val dateOfMotherDeath: Long,
    val timeOfMotherDeath: String?,
    val placeOfMotherDeath: Int,
    val otherPlaceOfMotherDeath: String?,
    val motherAnyProblem: String?,
    val babyFirstFed: Int,
    val otherBabyFirstFed: String?,
    val timeBabyFirstFed: String?,
    val howBabyTookFirstFeed: Int,
    val motherHasBreastFeedProblem: Int,
    val motherBreastFeedProblem: String?,
)

data class HbncPartII(
    val dateOfVisit : Long,
    val babyTemperature: String?,
    val babyEyeCondition: Int,
    val babyUmbilicalBleed : Int,
    val actionBabyUmbilicalBleed : Int,
    val babyWeight : String,
    val babyWeightMatchesColor : Int,
    val babyWeightColorOnScale : Int,

    val allLimbsLimp : Int,
    val feedLessStop : Int,
    val cryWeakStop : Int,
    val dryBaby : Int,
    val wrapClothCloseToMother : Int,
    val exclusiveBreastFeeding : Int,
    val cordCleanDry : Int,
    val unusualInBaby : Int,
    val otherUnusualInBaby : String?,

    )

data class HbncHomeVisit(
    val dateOfVisit : Long,
    val babyAlive: Int,
    val numTimesFullMeal24hr : Int,
    val numPadChanged24hr : Int,
    val babyKeptWarmWinter : Int,
    val babyFedProperly : Int,
    val babyCryContinuously : Int,

    val motherTemperature : String?,
    val foulDischargeFever : Int,
    val motherSpeakAbnormallyFits : Int,
    val motherLessNoMilk : Int,
    val motherBreastProblem : Int,

    val babyEyesSwollen : Int,
    val babyWeight : String?,
    val babyTemperature : String?,
    val babyYellow : Int,
    val babyImmunizationStatus : String?,

    val babyReferred : Int,
    val dateOfBabyReferral : Long,
    val placeOfBabyReferral : Int,
    val otherPlaceOfBabyReferral : String?,
    val motherReferred : Int,
    val dateOfMotherReferral : Long,
    val placeOfMotherReferral : Int,
    val otherPlaceOfMotherReferral : String?,
    val allLimbsLimp : Int,
    val feedingLessStopped : Int,
    val cryWeakStopped : Int,
    val bloatedStomach : Int,
    val coldOnTouch : Int,
    val chestDrawing : Int,
    val breathFast : Int,
    val pusNavel : Int,
    val sup : Int,
    val supName : String?,
    val supComment : String?,
    val supSignDate : Long,
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