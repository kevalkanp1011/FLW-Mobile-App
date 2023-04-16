package org.piramalswasthya.sakhi.configuration

import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.model.FormInput.InputType
import java.text.SimpleDateFormat
import java.util.*

class HBNCFormDataset(
//    context : Context,
    private val nthDay: Int, private val hbnc: HBNCCache? = null
) {

    companion object {
        private fun getLongFromDate(dateString: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = dateString?.let { f.parse(it) }
            return date?.time ?: 0L
        }
    }

    private fun FormInput.getPosition(): Int {
        return value.value?.let { entries?.indexOf(it)?.plus(1) } ?: 0
    }

    fun mapCardValues(hbnc: HBNCCache, user: UserCache) {
        hbnc.visitCard = HbncVisitCard(
            ashaName = ashaName.value.value,
            villageName = villageName.value.value,
            subCenterName = healthSubCenterName.value.value,
            blockName = blockName.value.value,
            motherName = motherName.value.value,
            fatherName = fatherName.value.value,
            dateOfDelivery = getLongFromDate(dateOfDelivery.value.value),
            placeOfDelivery = placeOfDelivery.getPosition(),
            babyGender = gender.getPosition(),
            typeOfDelivery = typeOfDelivery.getPosition(),
            stillBirth = stillBirth.getPosition(),
            startedBreastFeeding = startedBreastFeeding.getPosition(),
            dischargeDateMother = getLongFromDate(dateOfDischargeFromHospitalMother.value.value),
            dischargeDateBaby = getLongFromDate(dateOfDischargeFromHospitalBaby.value.value),
            weightInGrams = weightAtBirth.value.value?.toInt() ?: 0,
            registrationOfBirth = registrationOfBirth.getPosition(),
        )
    }

    fun mapPartIValues(hbnc: HBNCCache) {
        hbnc.part1 = HbncPartI(
            babyAlive = babyAlive.getPosition(),
            dateOfBabyDeath = getLongFromDate(dateOfBabyDeath.value.value),
            timeOfBabyDeath = timeOfBabyDeath.value.value,
            placeOfBabyDeath = placeOfBabyDeath.getPosition(),
            otherPlaceOfBabyDeath = otherPlaceOfBabyDeath.value.value,
            isBabyPreterm = babyPreterm.getPosition(),
            gestationalAge = gestationalAge.getPosition(),
            dateOfFirstExamination = getLongFromDate(dateOfBabyFirstExamination.value.value),
            timeOfFirstExamination = timeOfBabyFirstExamination.value.value,
            motherAlive = motherAlive.getPosition(),
            dateOfMotherDeath = getLongFromDate(dateOfMotherDeath.value.value),
            timeOfMotherDeath = timeOfMotherDeath.value.value,
            placeOfMotherDeath = placeOfBabyDeath.getPosition(),
            otherPlaceOfMotherDeath = otherPlaceOfMotherDeath.value.value,
            motherAnyProblem = motherAnyProblem.value.value,
            babyFirstFed = babyFedAfterBirth.getPosition(),
            otherBabyFirstFed = otherBabyFedAfterBirth.value.value,
            timeBabyFirstFed = whenBabyFirstFed.value.value,
            howBabyTookFirstFeed = howBabyTookFirstFeed.getPosition(),
            motherHasBreastFeedProblem = motherHasBreastFeedProblem.getPosition(),
            motherBreastFeedProblem = motherBreastFeedProblem.value.value,
        )
    }

    fun mapPartIIValues(hbnc: HBNCCache) {
        hbnc.part2 = HbncPartII(
            babyTemperature = babyTemperature.value.value,
            babyEyeCondition = babyEyeCondition.getPosition(),
            babyUmbilicalBleed = babyBleedUmbilicalCord.getPosition(),
            actionBabyUmbilicalBleed = actionUmbilicalBleed.getPosition(),
            babyWeight = babyWeight.value.value ?: "0",
            babyWeightMatchesColor = babyWeigntMatchesColor.getPosition(),
            babyWeightColorOnScale = babyWeightColor.getPosition(),
            allLimbsLimp = allLimbsLimp.getPosition(),
            feedLessStop = feedingLessStop.getPosition(),
            cryWeakStop = cryWeakStopped.getPosition(),
            dryBaby = babyDry.getPosition(),
            keepWarmWinter = babyKeptWarmWinter.getPosition(),
            exclusiveBreastFeeding = onlyBreastMilk.getPosition(),
            cordCleanDry = cordCleanDry.getPosition(),
            unusualInBaby = unusualWithBaby.getPosition(),
            otherUnusualInBaby = otherUnusualWithBaby.value.value,
        )
    }

    fun mapVisitValues(hbnc: HBNCCache) {
        hbnc.homeVisitForm = HbncHomeVisit(
            dateOfAshaVisit = getLongFromDate(dateOfMotherDeath.value.value),
            babyAlive = babyAlive.getPosition(),
            numTimesFullMeal24hr = timesMotherFed24hr.value.value?.toInt() ?: 0,
            numPadChanged24hr = timesPadChanged.value.value?.toInt() ?: 0,
            babyKeptWarmWinter = babyKeptWarmWinter.getPosition(),
            babyFedProperly = babyBreastFedProperly.getPosition(),
            babyCryContinuously = babyCryContinuously.getPosition(),
            motherTemperature = motherBodyTemperature.value.value,
            foulDischargeFever = motherWaterDischarge.getPosition(),
            motherSpeakAbnormallyFits = motherSpeakAbnormalFits.getPosition(),
            motherLessNoMilk = motherNoOrLessMilk.getPosition(),
            motherBreastProblem = motherBreastProblem.getPosition(),
            babyEyesSwollen = babyEyesSwollen.getPosition(),
            babyWeight = babyWeight.value.value,
            babyTemperature = babyTemperature.value.value,
            babyYellow = yellowJaundice.getPosition(),
            babyImmunizationStatus = childImmunizationStatus.value.value,
            babyReferred = babyReferred.getPosition(),
            dateOfBabyReferral = getLongFromDate(dateOfBabyReferral.value.value),
            placeOfBabyReferral = placeOfBabyReferral.getPosition(),
            otherPlaceOfBabyReferral = otherPlaceOfBabyReferral.value.value,
            motherReferred = motherReferred.getPosition(),
            dateOfMotherReferral = getLongFromDate(dateOfMotherReferral.value.value),
            placeOfMotherReferral = placeOfMotherReferral.getPosition(),
            otherPlaceOfMotherReferral = otherPlaceOfMotherReferral.value.value,
            allLimbsLimp = allLimbsLimp.getPosition(),
            feedingLessStopped = feedingLessStop.getPosition(),
            cryWeakStopped = cryWeakStopped.getPosition(),
            bloatedStomach = bloatedStomach.getPosition(),
            coldOnTouch = childColdOnTouch.getPosition(),
            chestDrawing = childChestDrawing.getPosition(),
            breathFast = breathFast.getPosition(),
            pusNavel = pusNavel.getPosition(),
            sup = sup.getPosition(),
            supName = supName.value.value,
            supComment = supRemark.value.value,
            supSignDate = getLongFromDate(dateOfSupSig.value.value),
        )
    }

    fun setVillageName(village: String) {
        villageName.value.value = village
    }

    fun setBlockName(block: String) {
        blockName.value.value = block
    }

    fun setAshaName(userName: String) {
        ashaName.value.value = userName
    }

    private val titleHomeVisit = FormInput(
        inputType = InputType.HEADLINE,
        title = "Home Visit Form for newborn and mother care",
        required = false
    )
    private val healthSubCenterName = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Health Subcenter Name ", required = false
    )
    private val phcName = FormInput(
        inputType = InputType.EDIT_TEXT, title = "P.H.C. Name ", required = false
    )
    private val motherName = FormInput(
        inputType = InputType.TEXT_VIEW, title = "Mother Name", required = false
    )
    private val fatherName = FormInput(
        inputType = InputType.TEXT_VIEW, title = "Father Name", required = false
    )

    private val dateOfDelivery = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of Delivery",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val placeOfDelivery = FormInput(
        inputType = InputType.DROPDOWN, title = "Place of Delivery", entries = arrayOf(
            "House",
            "Health center",
            "CHC",
            "PHC",
        ), required = false
    )
    private val gender = FormInput(
        inputType = InputType.RADIO, title = "Baby Gender", entries = arrayOf(
            "Male",
            "Female",
            "Transgender",
        ), required = false
    )

    private val typeOfDelivery = FormInput(
        inputType = InputType.RADIO, title = "Type of Delivery", entries = arrayOf(
            "Cesarean",
            "Normal",
        ), required = false
    )
    private val startedBreastFeeding = FormInput(
        inputType = InputType.DROPDOWN, title = "Started Breastfeeding", entries = arrayOf(
            "Within an hour", "1 - 4 hours", "4.1 - 24 hours", "After 24 hours"
        ), required = false
    )
    private val weightAtBirth = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Weight at birth ( grams )",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfDischargeFromHospitalMother = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Discharge Date of Mother",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val dateOfDischargeFromHospitalBaby = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Discharge Date of Baby",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val motherStatus = FormInput(
        inputType = InputType.RADIO, title = "Mother Status", entries = arrayOf(
            "Living",
            "Dead",
        ), required = false
    )
    private val registrationOfBirth = FormInput(
        inputType = InputType.RADIO, title = "Registration Of Birth", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val childStatus = FormInput(
        inputType = InputType.RADIO, title = "Child Status", entries = arrayOf(
            "Living",
            "Dead",
        ), required = false
    )
    private val homeVisitDate = FormInput(
        inputType = InputType.RADIO, title = "Home Visit Date", entries = arrayOf(
            "1st Day",
            "3rd Day",
        ), required = false
    )
    private val childImmunizationStatus = FormInput(
        inputType = InputType.CHECKBOXES, title = "Child Immunization Status", entries = arrayOf(
            "BCG", "Polio", "DPT 1", "Hepatitis-B"
        ), required = false
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
        inputType = InputType.HEADLINE, title = "New Born First Training Part 1", required = false
    )

    private val timeOfDelivery = FormInput(
        inputType = InputType.TIME_PICKER, title = "Delivery time", required = false
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
        inputType = InputType.DROPDOWN, title = "Does mother have any problem", entries = arrayOf(
            "Very Bleeding ",
            "Anesthesia/ Seizure outbreak",
        ), required = false
    )

    val babyFedAfterBirth = FormInput(
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

//    private val whenBabyFirstFed = FormInput(
//        inputType = InputType.EDIT_TEXT,
//        title = "When was the baby first breastfed ",
//        required = false
//    )

    private val howBabyTookFirstFeed = FormInput(
        inputType = InputType.DROPDOWN, title = "How did the baby breastfeed? ", entries = arrayOf(
            "Forcefully",
            "Weakly ",
            "Could not breastfeed but had to be fed with spoon",
            "Could neither breast-feed nor could take milk given by spoon",
        ), required = false
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
        inputType = InputType.RADIO, title = "Baby eye condition", entries = arrayOf(
            "Normal ", "Swelling", "oozing pus"
        ), required = false
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
        inputType = InputType.RADIO, title = "Weighing machine scale color", entries = arrayOf(
            "Red", "Yellow", "Green"
        ), required = false
    )

    //////////////////////////// Part Baby Phy Con /////////////////////////////////

    private val titleBabyPhysicalCondition = FormInput(
        inputType = InputType.HEADLINE,
        title = "Enter the child physical condition",
        required = false
    )
    private val allLimbsLimp = FormInput(
        inputType = InputType.RADIO,
        title = "All limbs limp",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val feedingLessStop = FormInput(
        inputType = InputType.RADIO,
        title = "Feeding less/stop",
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


    ////////////////////// Newborn first training (A) ask mother

    private val dateOfAshaVisit = FormInput(
        inputType = InputType.TEXT_VIEW, title = "Date of ASHA's visit", required = false
    )

    private val titleAskMotherA = FormInput(
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

    private val titleHealthCheckUpMotherB = FormInput(
        inputType = InputType.HEADLINE, title = "(B) Health Checkup of mother", required = false
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
    private val motherSpeakAbnormalFits = FormInput(
        inputType = InputType.RADIO,
        title = "Is mother speaking abnormally or having fits?",
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

    private val titleHealthCheckUpBabyC = FormInput(
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
        title = "Weight on Day $nthDay",
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
        title = "Respiratory rate more than 60 per minute",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val referredByAsha = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "In the above symptoms whether the child is referred by ASHA, if yes, then where (HSC/PHC/RH/SDH/DH) ",
        required = false
    )

    ////////////////////////// Part D ////////////////////

    private val titleSepsisD = FormInput(
        inputType = InputType.HEADLINE,
        title = "(D) Sepsis",
        subtitle = "Examine the following symptoms of sepsis. If symptoms are present, then write, Yes, if symptoms are not present, then do not write. Enter the symptoms seen from the health check-up on the first day of the newborns birth.",
        required = false
    )
    private val organLethargic = FormInput(
        inputType = InputType.EDIT_TEXT, title = "All organs are lethargic", required = false
    )
    private val drinkLessNoMilk = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Is drinking less milk/has stopped drinking milk ",
        required = false
    )
    private val slowNoCry = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Cry weak/ stopped", required = false
    )
    private val bloatedStomach = FormInput(
        inputType = InputType.RADIO,
        title = "Distended abdomen or mother says baby vomits often",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val childColdOnTouch = FormInput(
        inputType = InputType.RADIO,
        title = "The mother tells that the child feels cold when touching or the temperature of the child is more than 89 degrees Fahrenheit (37.5 degrees C)",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )

    private val childChestDrawing = FormInput(
        inputType = InputType.RADIO,
        title = "and the chest is pulled inward while breathing.",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )


    private val pusNavel = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Pus in the navel", required = false
    )
    private val ashaName = FormInput(
        inputType = InputType.TEXT_VIEW, title = "ASHA NAME", required = false
    )
    private val villageName = FormInput(
        inputType = InputType.TEXT_VIEW, title = "Village Name", required = false
    )
    private val blockName = FormInput(
        inputType = InputType.TEXT_VIEW, title = "Block Name", required = false
    )
    private val stillBirth = FormInput(
        inputType = InputType.RADIO, title = "Still Birth", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val supRemark = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Supervisors Remark ", required = false
    )
    private val sup = FormInput(
        inputType = InputType.DROPDOWN, title = "Supervisor", entries = arrayOf(
            "ASHA Facilitator",
            "ANM",
            "MPW",
        ), required = false
    )
    private val supName = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Supervisor name", required = false
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

    private val titleVisitCard = FormInput(
        inputType = InputType.HEADLINE, title = "Mother-Newborn Home Visit Card", required = false
    )
    private val titleVisitCardDischarge = FormInput(
        inputType = InputType.HEADLINE,
        title = "Discharge of Institutional Delivery",
        required = false
    )

    private val titleDateOfHomeVisit = FormInput(
        inputType = InputType.HEADLINE, title = "Date of Home Visit", required = false
    )
    val babyAlive = FormInput(
        inputType = InputType.RADIO, title = "Is the baby alive?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    val dateOfBabyDeath = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of death of baby",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val timeOfBabyDeath = FormInput(
        inputType = InputType.TIME_PICKER, title = "Time of death of baby", required = false
    )
    val placeOfBabyDeath = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Place of Baby Death",
        entries = arrayOf(
            "Home",
            "Sub-center",
            "PHC",
            "CHC",
            "Other",
        ),
        required = false,
    )
    val otherPlaceOfBabyDeath = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Other place of Baby Death", required = false
    )
    val babyPreterm = FormInput(
        inputType = InputType.RADIO,
        title = "Is the baby preterm?",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false,
    )
    val gestationalAge = FormInput(
        inputType = InputType.RADIO,
        title = "How many weeks has it been since baby born (Gestational Age)",
//        orientation = LinearLayout.VERTICAL,
        entries = arrayOf(
            "24 – 34 Weeks",
            "34 – 36 Weeks",
            "36 – 38 Weeks",
        ),
        required = false,
    )
    private val dateOfBabyFirstExamination = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of First examination of baby",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val timeOfBabyFirstExamination = FormInput(
        inputType = InputType.TIME_PICKER,
        title = "Time of First examination of baby",
        required = false
    )


    val motherAlive = FormInput(
        inputType = InputType.RADIO, title = "Is the mother alive?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    val dateOfMotherDeath = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of death of mother",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val timeOfMotherDeath = FormInput(
        inputType = InputType.TIME_PICKER, title = "Time of death of mother", required = false
    )
    val placeOfMotherDeath = FormInput(
        inputType = InputType.DROPDOWN,
        title = "Place of mother Death",
        entries = arrayOf(
            "Home",
            "Sub-center",
            "PHC",
            "CHC",
            "Other",
        ),
        required = false,
    )
    val otherPlaceOfMotherDeath = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Other place of mother Death", required = false
    )
    private val motherProblems = FormInput(
        inputType = InputType.CHECKBOXES,
        title = "Does Mother have any problems",
        entries = arrayOf(
            "Excessive Bleeding",
            "Unconscious / Fits",
        ),
        required = false
    )

    val otherBabyFedAfterBirth = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Other - What was given as the first feed to baby after birth?",
        required = false
    )
    private val whenBabyFirstFed = FormInput(
        inputType = InputType.TIME_PICKER,
        title = "When was the baby first fed",
        required = false
    )
    val motherHasBreastFeedProblem = FormInput(
        inputType = InputType.RADIO,
        title = "Does the mother have breastfeeding problem?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val motherBreastFeedProblem = FormInput(
        inputType = InputType.EDIT_TEXT,
        title = "Write the problem, if there is any problem in breast feeding, help the mother to overcome it",
        required = false
    )


    ///////////////////////////Part II////////////////////////////
    private val titleBabyFirstHealthCheckup = FormInput(
        inputType = InputType.HEADLINE,
        title = "Part 2: Baby first health check-up",
        required = false
    )
    private val babyTemperature = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Temperature of the baby", required = false
    )

    private val actionUmbilicalBleed = FormInput(
        inputType = InputType.RADIO,
        title = "If yes, either ASHA, ANM/MPW or TBA can tie again with a clean thread. Action taken? ",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val babyWeigntMatchesColor = FormInput(
        inputType = InputType.RADIO, title = "Weighing matches with the colour?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val titleRoutineNewBornCare = FormInput(
        inputType = InputType.HEADLINE,
        title = "Routine Newborn Care: whether the task was performed",
        required = false
    )
    private val babyDry = FormInput(
        inputType = InputType.RADIO, title = "Dry the baby", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val cryWeakStopped = FormInput(
        inputType = InputType.RADIO, title = "Cry weak/ stopped", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val cordCleanDry = FormInput(
        inputType = InputType.RADIO, title = "Keep the cord clean and dry", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )

    val unusualWithBaby = FormInput(
        inputType = InputType.RADIO,
        title = "Was there anything unusual with the baby?",
        entries = arrayOf("Curved limbs", "cleft lip", "Other"),
        required = false,
    )
    val otherUnusualWithBaby = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Other - unusual with the baby", required = false
    )

    /////////////// Part Visit //////////

    private val titleWashHands = FormInput(
        inputType = InputType.HEADLINE,
        title = "ASHA should wash hands with soap and water before touching the baby during each visit",
        required = false
    )
    val babyReferred = FormInput(
        inputType = InputType.RADIO,
        title = "Baby referred for any reason?",
        entries = arrayOf("Yes", "No"),
        required = false
    )
    val dateOfBabyReferral = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of baby referral",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val placeOfBabyReferral = FormInput(
        inputType = InputType.DROPDOWN, title = "Place of baby referral", entries = arrayOf(
            "Sub-Centre",
            "PHC",
            "CHC",
            "Sub-District Hospital",
            "District Hospital",
            "Medical College Hospital",
            "In Transit",
            "Private Hospital",
            "Accredited Private Hospital",
            "Other",
        ), required = false
    )
    val otherPlaceOfBabyReferral = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Other -Place of baby referral", required = false
    )
    val motherReferred = FormInput(
        inputType = InputType.RADIO,
        title = "Mother referred for any reason?",
        entries = arrayOf("Yes", "No"),
        required = false
    )
    val dateOfMotherReferral = FormInput(
        inputType = InputType.DATE_PICKER,
        title = "Date of mother referral",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val placeOfMotherReferral = FormInput(
        inputType = InputType.DROPDOWN, title = "Place of mother referral", entries = arrayOf(
            "Sub-Centre",
            "PHC",
            "CHC",
            "Sub-District Hospital",
            "District Hospital",
            "Medical College Hospital",
            "In Transit",
            "Private Hospital",
            "Accredited Private Hospital",
            "Other",
        ), required = false
    )
    val otherPlaceOfMotherReferral = FormInput(
        inputType = InputType.EDIT_TEXT, title = "Other -Place of mother referral", required = false
    )


    private val cardPage by lazy {
        listOf(
            titleVisitCard,
            ashaName,
            villageName,
            healthSubCenterName,
            blockName,
            motherName,
            fatherName,
            dateOfDelivery,
            placeOfDelivery,
            gender,
            typeOfDelivery,
            stillBirth,
            startedBreastFeeding,
            titleVisitCardDischarge,
            dateOfDischargeFromHospitalMother,
            dateOfDischargeFromHospitalBaby,
            weightAtBirth,
            registrationOfBirth
        )
    }

    fun getCardPage(
        asha: UserCache, childBen: BenRegCache, motherBen: BenRegCache?
    ): List<FormInput> {
        ashaName.value.value = asha.userName
        villageName.value.value = asha.villageEnglish[0]
        blockName.value.value = asha.blockEnglish[0]
        motherName.value.value = childBen.motherName
        fatherName.value.value = childBen.fatherName
        placeOfDelivery.value.value = childBen.kidDetails?.birthPlace
        gender.value.value = gender.entries?.get(childBen.genderId)
        typeOfDelivery.value.value =
            childBen.kidDetails?.deliveryTypeId?.let { typeOfDelivery.entries?.get(it) }
        motherBen?.let {
            dateOfDelivery.value.value = it.genDetails?.deliveryDate
        }
        return cardPage


    }

    private val partIPage by lazy {
        listOf(
            titleDateOfHomeVisit,
            babyAlive,
            babyPreterm,
            dateOfBabyFirstExamination,
            timeOfBabyFirstExamination,
            motherAlive,
            motherProblems,
            babyFedAfterBirth,
            whenBabyFirstFed,
            howBabyTookFirstFeed,
            motherHasBreastFeedProblem,
        )
    }

    suspend fun getPartIPage(visitCard: HbncVisitCard?): List<FormInput> {
        babyAlive.value.value = visitCard?.stillBirth?.let { babyAlive.entries?.get(it) }
        return partIPage
    }


    private val partIIPage by lazy {
        listOf(
            titleBabyFirstHealthCheckup,
            babyTemperature,
            babyEyeCondition,
            babyBleedUmbilicalCord,
            actionUmbilicalBleed,
            babyWeight,
            babyWeigntMatchesColor,
            babyWeightColor,
            titleBabyPhysicalCondition,
            allLimbsLimp,
            feedingLessStop,
            cryWeakStopped,
            titleRoutineNewBornCare,
            babyDry,
            wrapClothKeptMother,
            onlyBreastMilk,
            cordCleanDry,
            unusualWithBaby
        )
    }

    suspend fun getPartIIPage(): List<FormInput> {
        return partIIPage
    }

    private val visitPage by lazy {
        listOf(
            dateOfAshaVisit,
            titleAskMotherA,
            babyAlive,
            timesMotherFed24hr,
            timesPadChanged,
            babyKeptWarmWinter,
            babyBreastFedProperly,
            babyCryContinuously,
            motherBodyTemperature,
            motherWaterDischarge,
            motherSpeakAbnormalFits,
            motherNoOrLessMilk,
            motherBreastProblem,

            titleWashHands,
            babyEyesSwollen,
            babyWeight,
            babyTemperature,
            yellowJaundice,
            childImmunizationStatus,
            babyReferred,
            motherReferred,
            titleSepsisD,
            allLimbsLimp,
            feedingLessStop,
            cryWeakStopped,
            bloatedStomach,
            childColdOnTouch,
            childChestDrawing,
            breathFast,
            pusNavel,
            sup,
            supName,
            supRemark,
            dateOfSupSig
        )
    }

    fun getVisitPage(firstDay: HbncHomeVisit?): List<FormInput> {
        firstDay?.let {
            childImmunizationStatus.value.value = it.babyImmunizationStatus
        }
        return visitPage
    }
}