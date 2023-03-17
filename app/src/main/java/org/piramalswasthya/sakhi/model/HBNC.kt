package org.piramalswasthya.sakhi.model

import androidx.room.*
import com.squareup.moshi.JsonClass

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

data class HBNCCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    val processed : String,

){
    fun asPostModel(user: UserCache, household: HouseholdCache, ben: BenRegCache, hbncCount: Int): HBNCPost {
        return HBNCPost(
            benId = benId,
            hhId = hhId,
            day = 1,


        )
    }
}

data class HBNCPart0(
    val healthSubCenterName : String,
    val phcName : String,
    val motherName : String,
    val fatherName : String,
    val dateOfDelivery : Long,
    val placeOfDelivery : Int,
    val babyGender : Int,
    val typeOfDelivery : Int,
    val startedBreastFeed : Int,
    val weightAtBirth : Int, /// grams
    val dischargeDateFromHospital : Long,
    val motherStatus : Int,
    val registeredAtBirth : Int,
    val childStatus : Int,
    val homeVisitDate : Int,
    val childImmunizedBCG : Boolean,
    val childImmunizedPolio : Boolean,
    val childImmunizedDpt : Boolean,
    val childImmunizedHepB : Boolean,
    val birthWeightRecordedInMCP : Int,

    val deliveryTime : Long,
    val dateOfCompletionOfPregnancy : Long,
    val numWeeksWhenBorn : Int,
    val dateOfFirstTraining : Long,
    val doesMotherHaveProblem : Int,
    val babyFedAfterBirth : Int,
    val whenBabyFirstBreastFed : Int,
    val breastFeedProblem : String?,
    val breastFeedProblem2 : String?,
    val measureRecordBabyTemperature  : String?,
    val babyEyeCondition : Int,
    val babyBleedUmbilical : Int,
    val babyWeighingScaleColor : Int,

//    val babyAllOrganLethargic : Int,
//    val babyAllOrganLethargic : Int,
//    val babyAllOrganLethargic : Int,
//    val babyAllOrganLethargic : Int,
//    val babyAllOrganLethargic : Int,





    )

data class HbncPartA(
     val dummy : Boolean
)
data class HbncPartB(
    val dummy : Boolean
)
data class HbncPartC(
    val dummy : Boolean
)
data class HbncPartD(
    val dummy : Boolean
)


@JsonClass(generateAdapter = true)
data class HBNCPost (
    val benId: Long,
    val hhId: Long,
    val day : Int,

    @Embedded(prefix = "day_1")
    var part0 : HBNCPart0? = null,

    @Embedded(prefix = "part_A")
    var partA : HbncPartA ? = null,

    @Embedded(prefix = "part_B")
    var partB : HbncPartB ? = null,

    @Embedded(prefix = "part_C")
    var partC : HbncPartC ? = null,

    @Embedded(prefix = "part_D")
    var partD : HbncPartD ? = null,
)