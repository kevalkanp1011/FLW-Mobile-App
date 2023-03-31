package org.piramalswasthya.sakhi.model

import androidx.room.*
import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.database.room.SyncState

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

    var ashaName : String? = null,
    var supervisorRemark : String? = null,
    var supervisorName :String? = null,
    var supervisorComments : String? = null,
    var dateSupervisorVisit : Long = 0,
    var processed: String? = null,
    val syncState: SyncState

) {
    fun asPostModel(
        user: UserCache,
        household: HouseholdCache,
        ben: BenRegCache,
        hbncCount: Int
    ): HBNCPost {
        return HBNCPost(
            benId = benId,
            hhId = hhId,
            day = 1,


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
    val homeVisitDate: Int,
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
    val howBabyFirstFed : Int,
    val breastFeedProblem: String?,
    val breastFeedProblem2: String?,
    val measureRecordBabyTemperature: String?,
    val babyEyeCondition: Int,
    val babyBleedUmbilical: Int,
    val babyWeighingScaleColor: Int,

    val babyAllOrganLethargic: Int,
    val babyLessMilkDrinking: Int,
    val babyNoDrinkMilk : Int,
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
    val numTimesEats : Int,
    val numPadsChanged : Int,
    val winterBabyKeptWarm : Int,
    val breastFeedProper : Int,
    val babyCryContinuouslyOrUrinateLess6 : Int
)

data class HbncPartB(
    val temperature : String?,
    val waterDischargeFoulSmell : Int,
    val motherGrumbleSeizure : Int,
    val motherNoOrLessMilk : Int,
    val crackedNipplePainHardBreast : Int

)

data class HbncPartC(
    val eyesSwollenPusComing : Int,
    val weightOnDay1 : Int,
    val temperature : String?,
    val pusPimpleOnSkin : Int,
    val crackedRednessOfTwistedSkin : String?,
    val yellowEyePalmSoleSkin : Int,
    val seizure : Int,
    val breathGoingFast : Int,
    val referredWhere : String?
)

data class HbncPartD(
    val organsLethargic : String?,
    val lessNoMilkDrinking : String?,
    val slowOrStoppedCrying : String?,
    val bloatedStomachOrVomit : String?,
    val coldOrHotOnTouch : String?,
    val pusInNavel : String?,
)


@JsonClass(generateAdapter = true)
data class HBNCPost(
    val benId: Long,
    val hhId: Long,
    val day: Int,

    )