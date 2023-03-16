package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType
import org.piramalswasthya.sakhi.model.HBNCCache
import java.text.SimpleDateFormat
import java.util.*

class HBNCFormDataset(context: Context, private val hbnc: HBNCCache? = null) {

    companion object {
        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    fun mapValues(hbnc: HBNCCache) {

    }

    private val titleHomeVisit = FormInput(
        inputType = InputType.HEADLINE,
        title = "Home Visit Form for newborn and mother care",
        required = false
    )
    private val healthSubCenterName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Health Subcenter Name ",
        required = false
    )
    private val phcName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "P.H.C. Name ",
        required = false
    )
    private val motherName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Mother Name",
        required = false
    )
    private val fatherName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Father Name",
        required = false
    )

    private val dateOfDelivery = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of Delivery",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val placeOfDelivery = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Place of Delivery",
        entries = arrayOf(
            "House",
            "Health center",
            "CHC",
            "PHC",
        ),
        required = false
    )
    private val gender = FormInput(
        inputType = InputType.RADIO,
        title = "Baby Gender",
        entries = arrayOf(
            "Male",
            "Female",
            "Transgender",
        ),
        required = false
    )

    private val typeOfDelivery = FormInput(
        inputType = InputType.RADIO,
        title = "Type of Delivery",
        entries = arrayOf(
            "Cesarean",
            "Normal",
        ),
        required = false
    )
    private val startedBreastFeeding = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Started Breastfeeding",
        entries = arrayOf(
            "Within an hour ",
            "An hour later ",
            "After 24 hours"
        ),
        required = false
    )
    private val weightAtBirth = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Weight at birth ( grams )",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfDischargeFromHospital = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Discharge date from hospital",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val motherStatus = FormInput(
        inputType = InputType.RADIO,
        title = "Mother Status",
        entries = arrayOf(
            "Living",
            "Dead",
        ),
        required = false
    )
    private val registrationOfBirth = FormInput(
        inputType = InputType.RADIO,
        title = "Registration Of Birth",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val childStatus = FormInput(
        inputType = InputType.RADIO,
        title = "Child Status",
        entries = arrayOf(
            "Living",
            "Dead",
        ),
        required = false
    )
    private val homeVisitDate = FormInput(
        inputType = InputType.RADIO,
        title = "Home Visit Date",
        entries = arrayOf(
            "1st Day",
            "3rd Day",
        ),
        required = false
    )
    private val childImmunizationStatus = FormInput(
        inputType = InputType.CHECKBOXES,
        title = "Child Immunization Status",
        entries = arrayOf(
            "BCG",
            "Polio",
            "DPT 1",
            "Hepatitis-B"
        ),
        required = false
    )
    private val birthWeightRecordedInCard = FormInput(
        inputType = InputType.RADIO,
        title = "Birth weight of the newborn recorded in Mother and Child Protection Card",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    //////////////////// Part 1 ////////////////////////

    private val titleTrainingPart1 = FormInput(
        inputType = InputType.HEADLINE,
        title = "New Born First Training Part 1",
        required = false
    )

    private val timeOfDelivery = FormInput(
        inputType = InputType.TIME_PICKER,
        title = "Delivery time",
        required = false
    )
    private val dateOfCompletionOfPregnancy = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of completion of pregnancy",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val weeksSinceBabyBorn = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "How many weeks have been born (if child is born in less that 35 weeks, pay attention)",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfFirstTraining = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date and time of first training",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val motherAnyProblem = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Does mother have any problem",
        entries = arrayOf(
            "Very Bleeding ",
            "Anesthesia/ Seizure outbreak",
        ),
        required = false
    )

    private val babyFedAfterBirth = FormInput(
        inputType = InputType.DROPDOWN,
        title = "What was the baby fed after birth ",
        entries = arrayOf(
            "Mother Milk",
            "Water",
            "Honey",
            "Mishri water",
            "Goat Milk",
            "Other",
        ),
        required = false
    )

    private val whenBabyFirstFed = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "When was the baby first breastfed ",
        required = false
    )

    private val howBabyFirstFed = FormInput(
        inputType = InputType.DROPDOWN,
        title = "How did the baby breastfeed? ",
        entries = arrayOf(
            "Forcefully With weakness ",
            "Was not able to breastfeed but milk was taken from spoon ",
            "Neither could have breastfeeding nor did he drink from spoon",
        ),
        required = false
    )
    private val actionBreastFeedProblem = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Any breastfeeding problem if yes write taken action",
        required = false
    )
    private val anyBreastFeedProblem = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "If there is any problem in breastfeeding",
        required = false
    )


    //////////////////////////// Part 2 /////////////////////////////////

    private val titleTrainingPart2 = FormInput(
        inputType = InputType.HEADLINE,
        title = "Baby first health check-up training Part 2",
        required = false
    )

    private val babyBodyTemperature = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Measure and record baby body temperature, write action",
        required = false
    )

    private val babyEyeCondition = FormInput(
        inputType = InputType.RADIO,
        title = "Baby eye condition",
        entries = arrayOf(
            "Normal ",
            "Swollen",
        ),
        required = false
    )
    private val babyBleedUmbilicalCord = FormInput(
        inputType = InputType.RADIO,
        title = "Is there bleeding from the baby umbilical cord ",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val babyWeightColor = FormInput(
        inputType = InputType.RADIO,
        title = "Weighing machine scale color",
        entries = arrayOf(
            "Red",
            "Yellow",
            "Green"
        ),
        required = false
    )

    //////////////////////////// Part Baby Phy Con /////////////////////////////////

    private val titleBabyPhysicalCondition = FormInput(
        inputType = InputType.HEADLINE,
        title = "Enter the child physical condition",
        required = false
    )
    private val allOrganLethargic = FormInput(
        inputType = InputType.RADIO,
        title = "All organs are lethargic",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val lessMilkDrink = FormInput(
        inputType = InputType.RADIO,
        title = "Less milk is drinking",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val notDrinkMilk = FormInput(
        inputType = InputType.RADIO,
        title = "Not drinking milk",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val crySlow = FormInput(
        inputType = InputType.RADIO,
        title = "Crying slow",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val notCry = FormInput(
        inputType = InputType.RADIO,
        title = "Not crying",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val lookedAfterRegularly = FormInput(
        inputType = InputType.RADIO,
        title = "Whether the newborn was being looked after regularly",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val wipedWithCleanCloth = FormInput(
        inputType = InputType.RADIO,
        title = "The child was wiped with a clean cloth",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val keptWarm = FormInput(
        inputType = InputType.RADIO,
        title = "The child is kept warm",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val givenBath = FormInput(
        inputType = InputType.RADIO,
        title = "The child was not given a bath",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val wrapClothKeptMother = FormInput(
        inputType = InputType.RADIO,
        title = "The child is wrapped in cloth and kept to the mother",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val onlyBreastMilk = FormInput(
        inputType = InputType.RADIO,
        title = "Started breastfeeding only/ only given breast milk",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val unusualWithBaby = FormInput(
        inputType = InputType.RADIO,
        title = "Was there anything unusual with the baby? (If there is any abnormal, report it to the medical officer in char",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    ////////////////////// Newborn first training (A) ask mother

    private val titleAskMother_A = FormInput(
        inputType = InputType.HEADLINE,
        title = "Newborn first training (A) Ask mother",
        required = false
    )

    private val timesMotherFed24hr = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "How many times the mother feeds her stomach in 24 hours. Action – If the mother does not eat full stomach or eat less than 4 times, advise mother to do so",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )


    private val timesPadChanged = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "How many pads have been changed in a day for bleeding? Action – If more than 2 pad, refer the mother to the hospital.",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )

    private val babyKeptWarmWinter = FormInput(
        inputType = InputType.RADIO,
        title = "During the winter season, is the baby kept warm? (Closer to the mother, dressed well and wrapped). - If it is not being done, ask the mother to do it.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    private val babyBreastFedProperly = FormInput(
        inputType = InputType.RADIO,
        title = "Is the child breastfed properly? (Whenever feeling hungry or breastfeeding at least 7 – 8 times in 24 hours). Action – if it is not being done then ask the mother to do it. ",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val babyCryContinuously = FormInput(
        inputType = InputType.RADIO,
        title = "Does the child cry continuously or urinate less than 6 times a day? Action – Advice the mother for breast-feeding",
        entries = arrayOf("Yes", "No"),
        required = false,
    )


    //////////////////// Part - B //////////////////

    private val titleHealthCheckUpMother_B = FormInput(
        inputType = InputType.HEADLINE,
        title = "(B) Health Checkup of mother",
        required = false
    )

    private val motherBodyTemperature = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Measure and check the temperature. Action – Give the patient paracetamol tablet if the temperature is 102°F (38.9°C) and refer to the hospital if the temperature is higher than this.",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val motherWaterDischarge = FormInput(
        inputType = InputType.RADIO,
        title = "Water discharge with foul smell and fever 102 degree Fahrenheit (38.9 degree C). ",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherGrumbleSeizure = FormInput(
        inputType = InputType.RADIO,
        title = "Does the mother grumble unevenly or have seizures? Action – If yes, refer you to the hospital.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherNoOrLessMilk = FormInput(
        inputType = InputType.RADIO,
        title = "Mothers milk is not being produced after delivery or she thinks less milk is being produced.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherBreastProblem = FormInput(
        inputType = InputType.RADIO,
        title = "Does the mother have cracked nipple / pain and / or hard breasts",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    //////////////////// Part - C //////////////////

    private val titleHealthCheckUpBaby_C = FormInput(
        inputType = InputType.HEADLINE,
        title = "(c) Health check-up of newborn baby ",
        required = false
    )
    private val babyEyesSwollen = FormInput(
        inputType = InputType.RADIO,
        title = "Are the eyes swollen / Are there pus from the eyes?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val babyWeight = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Weight on the first, third, 7th, 14th, 21st and 42nd days (Write weight)",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false,
    )

    private val babyBodyTemperature2 = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Measure and enter temperature",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )


    private val pusPimples = FormInput(
        inputType = InputType.RADIO,
        title = "Pus filled pimples in the skin",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val crackRedTwistSkin = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Cracked / redness of twisted skin (thigh / armpit / hip / buttock) - Write",
        required = false
    )
    private val yellowJaundice = FormInput(
        inputType = InputType.RADIO,
        title = "Yellowing of the eye/palm/sole/skin (jaundice)",
        entries = arrayOf("Yes", "No"),
        required = false
    )

    private val seizures = FormInput(
        inputType = InputType.RADIO,
        title = "Seizures",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val breathFast = FormInput(
        inputType = InputType.RADIO,
        title = "Is the breath going fast (yes or no)",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val referredByAsha = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "In the above symptoms whether the child is referred by ASHA, if yes, then where (HSC/PHC/RH/SDH/DH) ",
        required = false
    )

    ////////////////////////// Part D ////////////////////

    private val titleSepsis_D = FormInput(
        inputType = InputType.HEADLINE,
        title = "(D) Examine the following symptoms of sepsis. If symptoms are present, then write, Yes, if symptoms are not present, then do not write. Enter the symptoms seen from the health check-up on the first day of the newborns birth.",
        required = false
    )
    private val organLethargic = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "All organs are lethargic",
        required = false
    )
    private val drinkLessNoMilk = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Is drinking less milk/has stopped drinking milk ",
        required = false
    )
    private val slowNoCry = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Slow Crying/Stopped Crying",
        required = false
    )
    private val bloatedStomach = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Bloated stomach or mother tells that the child vomits again and again",
        required = false
    )
    private val childColdOnTouch = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "The mother tells that the child feels cold when touching or the temperature of the child is more than 89 degrees Fahrenheit (37.5 degrees C) and the chest is pulled inward while breathing.",
        required = false
    )
    private val pusNavel = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Pus in the navel",
        required = false
    )
    private val ashaName = FormInput(
        inputType = InputType.TEXT_VIEW,
        title = "ASHA NAME",
        required = false
    )
    private val supRemark = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Supervisors Remark ",
        required = false
    )
    private val supName = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Supervisor name",
        required = false
    )
    private val supervisorComment = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Supervisors Comments: Please Tick Mark",
        entries = arrayOf(
            "Fully filled format ",
            "Incomplete format ",
            "Wrongly filled format",
        ),
        required = false,
    )
    private val dateOfSupSig = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Signature with Date of Supervisor",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )















    val firstPage by lazy {
        listOf(
            titleHomeVisit,
            healthSubCenterName,
            phcName,
            motherName,
            fatherName,
            dateOfDelivery,
            placeOfDelivery,
            gender,
            typeOfDelivery,
            startedBreastFeeding,
            weightAtBirth,
            dateOfDischargeFromHospital,
            motherStatus,
            registrationOfBirth,
            childStatus,
            homeVisitDate,
            childImmunizationStatus,
            birthWeightRecordedInCard,

            titleTrainingPart1,
            timeOfDelivery,
            dateOfCompletionOfPregnancy,
            weeksSinceBabyBorn,
            dateOfFirstTraining,
            motherAnyProblem,
            babyFedAfterBirth,
            whenBabyFirstFed,
            howBabyFirstFed,
            actionBreastFeedProblem,
            anyBreastFeedProblem,

            titleTrainingPart2,
            babyBodyTemperature,
            babyEyeCondition,
            babyBleedUmbilicalCord,
            babyWeightColor,

            titleBabyPhysicalCondition,
            allOrganLethargic,
            lessMilkDrink,
            notDrinkMilk,
            crySlow,
            notCry,
            lookedAfterRegularly,
            wipedWithCleanCloth,
            keptWarm,
            givenBath,
            wrapClothKeptMother,
            onlyBreastMilk,
            unusualWithBaby,

            titleAskMother_A,
            timesMotherFed24hr,
            timesPadChanged,
            babyKeptWarmWinter,
            babyBreastFedProperly,
            babyCryContinuously,

            titleHealthCheckUpMother_B,
            motherBodyTemperature,
            motherWaterDischarge,
            motherGrumbleSeizure,
            motherNoOrLessMilk,
            motherBreastProblem,
            titleHealthCheckUpBaby_C,

            babyEyesSwollen,
            babyWeight,
            babyBodyTemperature2,
            pusPimples,
            crackRedTwistSkin,
            yellowJaundice,
            seizures,
            breathFast,
            referredByAsha,

            titleSepsis_D,
            organLethargic,
            drinkLessNoMilk,
            slowNoCry,
            bloatedStomach,
            childColdOnTouch,
            pusNavel,
            ashaName,
            supRemark,
            supName,
            supervisorComment,
            dateOfSupSig





            )
    }
}