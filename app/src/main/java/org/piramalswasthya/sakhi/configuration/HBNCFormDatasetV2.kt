package org.piramalswasthya.sakhi.configuration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class HBNCFormDatasetV2(
//    context : Context,
    nthDay: Int
) {

    companion object {
        private fun getLongFromDate(dateString: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = dateString?.let { f.parse(it) }
            return date?.time ?: 0L
        }

        private fun getDateFromLong(dateLong: Long): String? {
            if (dateLong == 0L) return null
            val cal = Calendar.getInstance()
            cal.timeInMillis = dateLong
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return f.format(cal.time)


        }
    }

    private val list = mutableListOf<FormInputV2>()

    private val _listFlow = MutableStateFlow<List<FormInputV2>>(emptyList())
    val listFlow = _listFlow.asStateFlow()

    private fun FormInputV2.getPosition(): Int {
        return value?.let { entries?.indexOf(it)?.plus(1) } ?: 0
    }

    private fun FormInputV2.getStringFromPosition(position: Int): String? {
        return if (position == 0) null else entries?.get(position - 1)
    }

    suspend fun handleListOnValueChanged(visitDay: Int, formId: Int, index: Int) {
        Timber.d("handle called for day : $visitDay id : $formId index chosen : $index")
        when (visitDay) {
            Konstants.hbncCardDay -> handleForCardDay(formId, index)
            Konstants.hbncPart1Day -> handleForPart1Day(formId, index)
            Konstants.hbncPart2Day -> handleForPart2Day(formId, index)
            else -> handleForVisitDay( formId, index)
        }
        _listFlow.emit(list.toMutableList())
        Timber.d("Current list : ${list.size}")

    }


    private fun handleForCardDay(formId: Int, index: Int) {
        Timber.d("Handle Card day called : formId : $formId index : $index")
    }

    private fun handleForPart1Day(formId: Int, index: Int) {
        when (formId) {
            babyAlive.id -> {
                val depList = listOf(
                    dateOfBabyDeath,
                    timeOfBabyDeath,
                    placeOfBabyDeath,
                )
                if (index == 1 && !list.containsAll(depList)) {
                    list.addAll(
                        list.indexOf(babyAlive) + 1,
                        depList
                    )
                } else {
                    list.removeAll(
                        depList
                    )
                    list.remove(otherPlaceOfBabyDeath)
                }
            }
            placeOfBabyDeath.id -> {
                if (index == placeOfBabyDeath.entries!!.size - 1 && !list.contains(
                        otherPlaceOfBabyDeath
                    )
                )
                    list.add(
                        list.indexOf(placeOfBabyDeath) + 1,
                        otherPlaceOfBabyDeath
                    )
                else
                    list.remove(
                        otherPlaceOfBabyDeath
                    )
            }
            motherAlive.id -> {
                val depList = listOf(
                    dateOfMotherDeath,
                    timeOfMotherDeath,
                    placeOfMotherDeath,
                )
                if (index == 1 && !list.containsAll(depList))
                    list.addAll(
                        list.indexOf(motherAlive) + 1,
                        depList
                    )
                else {
                    list.removeAll(
                        depList
                    )
                    list.remove(otherPlaceOfMotherDeath)
                }
            }
            placeOfMotherDeath.id -> {
                if (index == placeOfMotherDeath.entries!!.size - 1)
                    list.add(
                        list.indexOf(placeOfMotherDeath) + 1,
                        otherPlaceOfMotherDeath
                    )
                else
                    list.remove(
                        otherPlaceOfMotherDeath
                    )
            }
        }

    }

    private fun handleForPart2Day(formId: Int, index: Int) {
        when (formId) {
            unusualWithBaby.id -> {

                if (index == 1 && !list.contains(otherUnusualWithBaby)) {
                    list.add(
                        list.indexOf(unusualWithBaby) + 1,
                        otherUnusualWithBaby
                    )
                } else
                    list.remove(
                        otherUnusualWithBaby
                    )
            }
        }
    }

    private fun handleForVisitDay(formId: Int, index: Int) {
        when (formId) {
            babyReferred.id -> {
                val depList = listOf(
                    dateOfBabyReferral,
                    placeOfBabyReferral
                )
                if (index == 0 && !list.containsAll(depList))
                    list.addAll(
                        list.indexOf(babyReferred) + 1,
                        depList
                    )
                else {
                    list.removeAll(
                        depList
                    )
                    list.remove(otherPlaceOfBabyReferral)
                }
            }
            placeOfBabyReferral.id -> {
                if (index == placeOfBabyReferral.entries!!.size-1 && !list.contains(
                        otherPlaceOfBabyReferral
                    )
                ) {
                    list.add(
                        list.indexOf(placeOfBabyReferral) + 1,
                        otherPlaceOfBabyReferral
                    )
                } else
                    list.remove(
                        otherPlaceOfBabyReferral
                    )
            }
            motherReferred.id -> {
                val depList = listOf(
                    dateOfMotherReferral,
                    placeOfMotherReferral
                )
                if (index == 0 && !list.containsAll(depList))
                    list.addAll(
                        list.indexOf(motherReferred) + 1,
                        depList
                    )
                else {
                    list.removeAll(
                        depList
                    )
                    list.remove(otherPlaceOfMotherReferral)
                }
            }
            placeOfMotherReferral.id -> {
                if (index == placeOfMotherReferral.entries!!.size-1 && !list.contains(
                        otherPlaceOfMotherReferral
                    )
                ) {
                    list.add(
                        list.indexOf(placeOfMotherReferral) + 1,
                        otherPlaceOfMotherReferral
                    )
                } else
                    list.remove(
                        otherPlaceOfMotherReferral
                    )
            }
        }
    }

    suspend fun setCardPageToList(
        asha: UserCache,
        childBen: BenRegCache,
        motherBen: BenRegCache?,
        visitCard: HbncVisitCard?
    ) {

        visitCard?.let { setExistingValuesForCardPage(it) } ?: run {
            ashaName.value = asha.userName
            villageName.value = asha.villageEnglish[0]
            blockName.value = asha.blockEnglish[0]
            motherName.value = childBen.motherName
            fatherName.value = childBen.fatherName
            placeOfDelivery.value = childBen.kidDetails?.birthPlace
            gender.value = gender.entries?.get(childBen.genderId)
            typeOfDelivery.value =
                childBen.kidDetails?.deliveryTypeId?.let { typeOfDelivery.getStringFromPosition(it) }
            motherBen?.let {
                dateOfDelivery.value = it.genDetails?.deliveryDate
            }
        }
//        Timber.d("list before adding $list")
        list.clear()
        list.addAll(cardPage)
        _listFlow.emit(list.toMutableList())
//        Timber.d("list after adding $list")
    }


    suspend fun setPart1PageToList(visitCard: HbncVisitCard?, hbncPart1: HbncPartI?) {
        list.clear()
        list.addAll(partIPage)
        babyAlive.value = visitCard?.stillBirth?.let {
            when (it) {
                0 -> null
                1 -> babyAlive.entries?.get(1).also {
                    if (hbncPart1 == null)
                        list.addAll(
                            list.indexOf(babyAlive) + 1, listOf(
                                dateOfBabyDeath,
                                timeOfBabyDeath,
                                placeOfBabyDeath,
                            )
                        )
                }
                2 -> babyAlive.entries?.get(0)
                else -> null
            }

        }

        if (hbncPart1 == null) {
            dateOfHomeVisit.value = getDateFromLong(System.currentTimeMillis())
        } else {
            setExistingValuesForPartIPage(hbncPart1)
        }
        _listFlow.emit(list.toMutableList())
    }

    suspend fun setPart2PageToList(hbncPart2: HbncPartII?) {
        list.clear()
        list.addAll(partIIPage)
        if (hbncPart2 == null) {
            dateOfHomeVisit.value = getDateFromLong(System.currentTimeMillis())
        } else {
            setExistingValuesForPartIIPage(hbncPart2)
        }
        _listFlow.emit(list.toMutableList())
    }

    suspend fun setVisitToList(
        firstDay: HbncHomeVisit?, currentDay: HbncHomeVisit?
    ) {
        list.clear()
        list.addAll(visitPage)
        if (currentDay == null) {
            dateOfHomeVisit.value = getDateFromLong(System.currentTimeMillis())
            firstDay?.let {
                childImmunizationStatus.value = it.babyImmunizationStatus
            }
        } else {
            setExistingValuesForVisitPage(currentDay)
        }
        _listFlow.emit(list.toMutableList())

    }


    fun mapCardValues(hbnc: HBNCCache) {
        hbnc.visitCard = HbncVisitCard(
            ashaName = ashaName.value,
            villageName = villageName.value,
            subCenterName = healthSubCenterName.value,
            blockName = blockName.value,
            motherName = motherName.value,
            fatherName = fatherName.value,
            dateOfDelivery = getLongFromDate(dateOfDelivery.value),
            placeOfDelivery = placeOfDelivery.getPosition(),
            babyGender = gender.getPosition(),
            typeOfDelivery = typeOfDelivery.getPosition(),
            stillBirth = stillBirth.getPosition(),
            startedBreastFeeding = startedBreastFeeding.getPosition(),
            dischargeDateMother = getLongFromDate(dateOfDischargeFromHospitalMother.value),
            dischargeDateBaby = getLongFromDate(dateOfDischargeFromHospitalBaby.value),
            weightInGrams = weightAtBirth.value?.toInt() ?: 0,
            registrationOfBirth = registrationOfBirth.getPosition(),
        )
    }

    fun mapPartIValues(hbnc: HBNCCache) {
        hbnc.part1 = HbncPartI(
            dateOfVisit = getLongFromDate(dateOfHomeVisit.value),
            babyAlive = babyAlive.getPosition(),
            dateOfBabyDeath = getLongFromDate(dateOfBabyDeath.value),
            timeOfBabyDeath = timeOfBabyDeath.value,
            placeOfBabyDeath = placeOfBabyDeath.getPosition(),
            otherPlaceOfBabyDeath = otherPlaceOfBabyDeath.value,
            isBabyPreterm = babyPreterm.getPosition(),
            gestationalAge = gestationalAge.getPosition(),
            dateOfFirstExamination = getLongFromDate(dateOfBabyFirstExamination.value),
            timeOfFirstExamination = timeOfBabyFirstExamination.value,
            motherAlive = motherAlive.getPosition(),
            dateOfMotherDeath = getLongFromDate(dateOfMotherDeath.value),
            timeOfMotherDeath = timeOfMotherDeath.value,
            placeOfMotherDeath = placeOfBabyDeath.getPosition(),
            otherPlaceOfMotherDeath = otherPlaceOfMotherDeath.value,
            motherAnyProblem = motherProblems.value,
            babyFirstFed = babyFedAfterBirth.getPosition(),
            otherBabyFirstFed = otherBabyFedAfterBirth.value,
            timeBabyFirstFed = whenBabyFirstFed.value,
            howBabyTookFirstFeed = howBabyTookFirstFeed.getPosition(),
            motherHasBreastFeedProblem = motherHasBreastFeedProblem.getPosition(),
            motherBreastFeedProblem = motherBreastFeedProblem.value,
        )
    }

    fun mapPartIIValues(hbnc: HBNCCache) {
        hbnc.part2 = HbncPartII(
            dateOfVisit = getLongFromDate(dateOfHomeVisit.value),
            babyTemperature = babyTemperature.value,
            babyEyeCondition = babyEyeCondition.getPosition(),
            babyUmbilicalBleed = babyBleedUmbilicalCord.getPosition(),
            actionBabyUmbilicalBleed = actionUmbilicalBleed.getPosition(),
            babyWeight = babyWeight.value ?: "0",
            babyWeightMatchesColor = babyWeigntMatchesColor.getPosition(),
            babyWeightColorOnScale = babyWeightColor.getPosition(),
            allLimbsLimp = allLimbsLimp.getPosition(),
            feedLessStop = feedingLessStop.getPosition(),
            cryWeakStop = cryWeakStopped.getPosition(),
            dryBaby = babyDry.getPosition(),
            wrapClothCloseToMother = wrapClothKeptMother.getPosition(),
            exclusiveBreastFeeding = onlyBreastMilk.getPosition(),
            cordCleanDry = cordCleanDry.getPosition(),
            unusualInBaby = unusualWithBaby.getPosition(),
            otherUnusualInBaby = otherUnusualWithBaby.value,
        )
    }

    fun mapVisitValues(hbnc: HBNCCache) {
        hbnc.homeVisitForm = HbncHomeVisit(
            dateOfVisit = getLongFromDate(dateOfMotherDeath.value),
            babyAlive = babyAlive.getPosition(),
            numTimesFullMeal24hr = timesMotherFed24hr.value?.toInt() ?: 0,
            numPadChanged24hr = timesPadChanged.value?.toInt() ?: 0,
            babyKeptWarmWinter = babyKeptWarmWinter.getPosition(),
            babyFedProperly = babyBreastFedProperly.getPosition(),
            babyCryContinuously = babyCryContinuously.getPosition(),
            motherTemperature = motherBodyTemperature.value,
            foulDischargeFever = motherWaterDischarge.getPosition(),
            motherSpeakAbnormallyFits = motherSpeakAbnormalFits.getPosition(),
            motherLessNoMilk = motherNoOrLessMilk.getPosition(),
            motherBreastProblem = motherBreastProblem.getPosition(),
            babyEyesSwollen = babyEyesSwollen.getPosition(),
            babyWeight = babyWeight.value,
            babyTemperature = babyTemperature.value,
            babyYellow = yellowJaundice.getPosition(),
            babyImmunizationStatus = childImmunizationStatus.value,
            babyReferred = babyReferred.getPosition(),
            dateOfBabyReferral = getLongFromDate(dateOfBabyReferral.value),
            placeOfBabyReferral = placeOfBabyReferral.getPosition(),
            otherPlaceOfBabyReferral = otherPlaceOfBabyReferral.value,
            motherReferred = motherReferred.getPosition(),
            dateOfMotherReferral = getLongFromDate(dateOfMotherReferral.value),
            placeOfMotherReferral = placeOfMotherReferral.getPosition(),
            otherPlaceOfMotherReferral = otherPlaceOfMotherReferral.value,
            allLimbsLimp = allLimbsLimp.getPosition(),
            feedingLessStopped = feedingLessStop.getPosition(),
            cryWeakStopped = cryWeakStopped.getPosition(),
            bloatedStomach = bloatedStomach.getPosition(),
            coldOnTouch = childColdOnTouch.getPosition(),
            chestDrawing = childChestDrawing.getPosition(),
            breathFast = breathFast.getPosition(),
            pusNavel = pusNavel.getPosition(),
            sup = sup.getPosition(),
            supName = supName.value,
            supComment = supRemark.value,
            supSignDate = getLongFromDate(dateOfSupSig.value),
        )
    }

    fun setVillageName(village: String) {
        villageName.value = village
    }

    fun setBlockName(block: String) {
        blockName.value = block
    }

    fun setAshaName(userName: String) {
        ashaName.value = userName
    }


    private val healthSubCenterName = FormInputV2(
        id = 2,
        inputType = InputType.EDIT_TEXT, title = "Health Subcenter Name ", required = false
    )
    private val motherName = FormInputV2(
        id = 4,
        inputType = InputType.TEXT_VIEW, title = "Mother Name", required = false
    )
    private val fatherName = FormInputV2(
        id = 5,
        inputType = InputType.TEXT_VIEW, title = "Father Name", required = false
    )

    private val dateOfDelivery = FormInputV2(
        id = 6,
        inputType = InputType.DATE_PICKER,
        title = "Date of Delivery",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val placeOfDelivery = FormInputV2(
        id = 7,
        inputType = InputType.DROPDOWN, title = "Place of Delivery", entries = arrayOf(
            "House",
            "Health center",
            "CHC",
            "PHC",
        ), required = false
    )
    private val gender = FormInputV2(
        id = 8,
        inputType = InputType.RADIO, title = "Baby Gender", entries = arrayOf(
            "Male",
            "Female",
            "Transgender",
        ), required = false
    )

    private val typeOfDelivery = FormInputV2(
        id = 9,
        inputType = InputType.RADIO, title = "Type of Delivery", entries = arrayOf(
            "Normal Delivery",
            "C - Section",
            "Assisted"
        ), required = false
    )
    private val startedBreastFeeding = FormInputV2(
        id = 10,
        inputType = InputType.DROPDOWN, title = "Started Breastfeeding", entries = arrayOf(
            "Within an hour", "1 - 4 hours", "4.1 - 24 hours", "After 24 hours"
        ), required = false
    )
    private val weightAtBirth = FormInputV2(
        id = 11,
        inputType = InputType.EDIT_TEXT,
        title = "Weight at birth ( grams )",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfDischargeFromHospitalMother = FormInputV2(
        id = 12,
        inputType = InputType.DATE_PICKER,
        title = "Discharge Date of Mother",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val dateOfDischargeFromHospitalBaby = FormInputV2(
        id = 13,
        inputType = InputType.DATE_PICKER,
        title = "Discharge Date of Baby",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val registrationOfBirth = FormInputV2(
        id = 15,
        inputType = InputType.RADIO, title = "Registration Of Birth", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )

    private val childImmunizationStatus = FormInputV2(
        id = 18,
        inputType = InputType.CHECKBOXES, title = "Child Immunization Status", entries = arrayOf(
            "BCG", "Polio", "DPT 1", "Hepatitis-B"
        ), required = false
    )

    private val babyFedAfterBirth = FormInputV2(
        id = 26,
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

    private val howBabyTookFirstFeed = FormInputV2(
        id = 27,
        inputType = InputType.DROPDOWN, title = "How did the baby breastfeed? ", entries = arrayOf(
            "Forcefully",
            "Weakly ",
            "Could not breastfeed but had to be fed with spoon",
            "Could neither breast-feed nor could take milk given by spoon",
        ), required = false
    )
    private val babyEyeCondition = FormInputV2(
        id = 32,
        inputType = InputType.RADIO, title = "Baby eye condition", entries = arrayOf(
            "Normal ", "Swelling", "oozing pus"
        ), required = false
    )
    private val babyBleedUmbilicalCord = FormInputV2(
        id = 33,
        inputType = InputType.RADIO,
        title = "Is there bleeding from the baby umbilical cord ",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val babyWeightColor = FormInputV2(
        id = 34,
        inputType = InputType.RADIO, title = "Weighing machine scale color", entries = arrayOf(
            "Red", "Yellow", "Green"
        ), required = false
    )

    private val titleBabyPhysicalCondition = FormInputV2(
        id = 35,
        inputType = InputType.HEADLINE,
        title = "Enter the child physical condition",
        required = false
    )
    private val allLimbsLimp = FormInputV2(
        id = 36,
        inputType = InputType.RADIO,
        title = "All limbs limp",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val feedingLessStop = FormInputV2(
        id = 37,
        inputType = InputType.RADIO,
        title = "Feeding less/stop",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val wrapClothKeptMother = FormInputV2(
        id = 45,
        inputType = InputType.RADIO,
        title = "The child is wrapped in cloth and kept to the mother",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val onlyBreastMilk = FormInputV2(
        id = 46,
        inputType = InputType.RADIO,
        title = "Started breastfeeding only/ only given breast milk",
        entries = arrayOf("Yes", "No"),
        required = false,
    )


    ////////////////////// Newborn first training (A) ask mother

    private val titleAskMotherA = FormInputV2(
        id = 48,
        inputType = InputType.HEADLINE,
        title = "Newborn first training (A) Ask mother",
        required = false
    )

    private val timesMotherFed24hr = FormInputV2(
        id = 49,
        inputType = InputType.EDIT_TEXT,
        title = "How many times the mother feeds her stomach in 24 hours. Action – If the mother does not eat full stomach or eat less than 4 times, advise mother to do so",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
        required = false
    )


    private val timesPadChanged = FormInputV2(
        id = 50,
        inputType = InputType.EDIT_TEXT,
        title = "How many pads have been changed in a day for bleeding? Action – If more than 2 pad, refer the mother to the hospital.",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )

    private val babyKeptWarmWinter = FormInputV2(
        id = 51,
        inputType = InputType.RADIO,
        title = "During the winter season, is the baby kept warm? (Closer to the mother, dressed well and wrapped). - If it is not being done, ask the mother to do it.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    private val babyBreastFedProperly = FormInputV2(
        id = 52,
        inputType = InputType.RADIO,
        title = "Is the child breastfed properly? (Whenever feeling hungry or breastfeeding at least 7 – 8 times in 24 hours). Action – if it is not being done then ask the mother to do it. ",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val babyCryContinuously = FormInputV2(
        id = 53,
        inputType = InputType.RADIO,
        title = "Does the child cry continuously or urinate less than 6 times a day? Action – Advice the mother for breast-feeding",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    
    private val motherBodyTemperature = FormInputV2(
        id = 55,
        inputType = InputType.EDIT_TEXT,
        title = "Measure and check the temperature. Action – Give the patient paracetamol tablet if the temperature is 102°F (38.9°C) and refer to the hospital if the temperature is higher than this.",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val motherWaterDischarge = FormInputV2(
        id = 56,
        inputType = InputType.RADIO,
        title = "Water discharge with foul smell and fever 102 degree Fahrenheit (38.9 degree C). ",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherSpeakAbnormalFits = FormInputV2(
        id = 57,
        inputType = InputType.RADIO,
        title = "Is mother speaking abnormally or having fits?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherNoOrLessMilk = FormInputV2(
        id = 58,
        inputType = InputType.RADIO,
        title = "Mothers milk is not being produced after delivery or she thinks less milk is being produced.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherBreastProblem = FormInputV2(
        id = 59,
        inputType = InputType.RADIO,
        title = "Does the mother have cracked nipple / pain and / or hard breasts",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    private val babyEyesSwollen = FormInputV2(
        id = 61,
        inputType = InputType.RADIO,
        title = "Are the eyes swollen / Are there pus from the eyes?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val babyWeight = FormInputV2(
        id = 62,
        inputType = InputType.EDIT_TEXT,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL,
        minDecimal = 0.5,
        maxDecimal = 7.0,
        etMaxLength = 3,
        title = "Weight on Day $nthDay",
        required = false,
    )
    private val yellowJaundice = FormInputV2(
        id = 66,
        inputType = InputType.RADIO,
        title = "Yellowing of the eye/palm/sole/skin (jaundice)",
        entries = arrayOf("Yes", "No"),
        required = false
    )

   
    private val breathFast = FormInputV2(
        id = 68,
        inputType = InputType.RADIO,
        title = "Respiratory rate more than 60 per minute",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
   
    private val titleSepsisD = FormInputV2(
        id = 70,
        inputType = InputType.HEADLINE,
        title = "(D) Sepsis",
        subtitle = "Examine the following symptoms of sepsis. If symptoms are present, then write, Yes, if symptoms are not present, then do not write. Enter the symptoms seen from the health check-up on the first day of the newborns birth.",
        required = false
    )
    
    private val bloatedStomach = FormInputV2(
        id = 74,
        inputType = InputType.RADIO,
        title = "Distended abdomen or mother says baby vomits often",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val childColdOnTouch = FormInputV2(
        id = 75,
        inputType = InputType.RADIO,
        title = "The mother tells that the child feels cold when touching or the temperature of the child is more than 89 degrees Fahrenheit (37.5 degrees C)",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )

    private val childChestDrawing = FormInputV2(
        id = 76,
        inputType = InputType.RADIO,
        title = "and the chest is pulled inward while breathing.",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )


    private val pusNavel = FormInputV2(
        id = 77,
        inputType = InputType.EDIT_TEXT, title = "Pus in the navel", required = false
    )
    private val ashaName = FormInputV2(
        id = 78,
        inputType = InputType.TEXT_VIEW, title = "ASHA NAME", required = false
    )
    private val villageName = FormInputV2(
        id = 79,
        inputType = InputType.TEXT_VIEW, title = "Village Name", required = false
    )
    private val blockName = FormInputV2(
        id = 80,
        inputType = InputType.TEXT_VIEW, title = "Block Name", required = false
    )
    private val stillBirth = FormInputV2(
        id = 81,
        inputType = InputType.RADIO, title = "Still Birth", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val supRemark = FormInputV2(
        id = 82,
        inputType = InputType.EDIT_TEXT, title = "Supervisors Remark ", required = false
    )
    private val sup = FormInputV2(
        id = 83,
        inputType = InputType.DROPDOWN, title = "Supervisor", entries = arrayOf(
            "ASHA Facilitator",
            "ANM",
            "MPW",
        ), required = false
    )
    private val supName = FormInputV2(
        id = 84,
        inputType = InputType.EDIT_TEXT, title = "Supervisor name", required = false
    )
    private val dateOfSupSig = FormInputV2(
        id = 86,
        inputType = InputType.DATE_PICKER,
        title = "Signature with Date of Supervisor",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )

    private val titleVisitCard = FormInputV2(
        id = 87,
        inputType = InputType.HEADLINE, title = "Mother-Newborn Home Visit Card", required = false
    )
    private val titleVisitCardDischarge = FormInputV2(
        id = 88,
        inputType = InputType.HEADLINE,
        title = "Discharge of Institutional Delivery",
        required = false
    )

    private val dateOfHomeVisit = FormInputV2(
        id = 89,
        inputType = InputType.TEXT_VIEW,
        title = "Date of Home Visit",
        required = false
    )
    private val babyAlive = FormInputV2(
        id = 90,
        inputType = InputType.RADIO, title = "Is the baby alive?", entries = arrayOf(
            "Yes",
            "No",
        ),
        hasDependants = true,
        required = false
    )
    private val dateOfBabyDeath = FormInputV2(
        id = 91,
        inputType = InputType.DATE_PICKER,
        title = "Date of death of baby",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val timeOfBabyDeath = FormInputV2(
        id = 92,
        inputType = InputType.TIME_PICKER, title = "Time of death of baby", required = false
    )
    private val placeOfBabyDeath = FormInputV2(
        id = 93,
        inputType = InputType.DROPDOWN,
        title = "Place of Baby Death",
        entries = arrayOf(
            "Home",
            "Sub-center",
            "PHC",
            "CHC",
            "Other",
        ),
        hasDependants = true,
        required = false,
    )
    private val otherPlaceOfBabyDeath = FormInputV2(
        id = 94,
        inputType = InputType.EDIT_TEXT, title = "Other place of Baby Death", required = false
    )
    private val babyPreterm = FormInputV2(
        id = 95,
        inputType = InputType.RADIO,
        title = "Is the baby preterm?",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        hasDependants = true,
        required = false,
    )
    private val gestationalAge = FormInputV2(
        id = 96,
        inputType = InputType.RADIO,
        title = "How many weeks has it been since baby born (Gestational Age)",
//        orientation = LinearLayout.VERTICAL,
        entries = arrayOf(
            "24 – 34 Weeks",
            "34 – 36 Weeks",
            "36 – 38 Weeks",
        ),
        required = true,
    )
    private val dateOfBabyFirstExamination = FormInputV2(
        id = 97,
        inputType = InputType.DATE_PICKER,
        title = "Date of First examination of baby",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val timeOfBabyFirstExamination = FormInputV2(
        id = 98,
        inputType = InputType.TIME_PICKER,
        title = "Time of First examination of baby",
        required = false
    )


    private val motherAlive = FormInputV2(
        id = 99,
        inputType = InputType.RADIO, title = "Is the mother alive?", entries = arrayOf(
            "Yes",
            "No",
        ),
        hasDependants = true,
        required = false
    )
    private val dateOfMotherDeath = FormInputV2(
        id = 100,
        inputType = InputType.DATE_PICKER,
        title = "Date of death of mother",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val timeOfMotherDeath = FormInputV2(
        id = 101,
        inputType = InputType.TIME_PICKER, title = "Time of death of mother", required = false
    )
    private val placeOfMotherDeath = FormInputV2(
        id = 102,
        inputType = InputType.DROPDOWN,
        title = "Place of mother Death",
        entries = arrayOf(
            "Home",
            "Sub-center",
            "PHC",
            "CHC",
            "Other",
        ),
        hasDependants = true,
        required = false,
    )
    private val otherPlaceOfMotherDeath = FormInputV2(
        id = 103,
        inputType = InputType.EDIT_TEXT,
        title = "Other place of mother Death",
        hasDependants = true,
        required = false
    )
    private val motherProblems = FormInputV2(
        id = 104,
        inputType = InputType.CHECKBOXES,
        title = "Does Mother have any problems",
        entries = arrayOf(
            "Excessive Bleeding",
            "Unconscious / Fits",
        ),
        required = false
    )

    private val otherBabyFedAfterBirth = FormInputV2(
        id = 105,
        inputType = InputType.EDIT_TEXT,
        title = "Other - What was given as the first feed to baby after birth?",
        required = false
    )
    private val whenBabyFirstFed = FormInputV2(
        id = 106,
        inputType = InputType.TIME_PICKER,
        title = "When was the baby first fed",
        required = false
    )
    private val motherHasBreastFeedProblem = FormInputV2(
        id = 107,
        inputType = InputType.RADIO,
        title = "Does the mother have breastfeeding problem?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val motherBreastFeedProblem = FormInputV2(
        id = 108,
        inputType = InputType.EDIT_TEXT,
        title = "Write the problem, if there is any problem in breast feeding, help the mother to overcome it",
        required = false
    )


    ///////////////////////////Part II////////////////////////////
    private val titleBabyFirstHealthCheckup = FormInputV2(
        id = 109,
        inputType = InputType.HEADLINE,
        title = "Part 2: Baby first health check-up",
        required = false
    )
    private val babyTemperature = FormInputV2(
        id = 110,
        inputType = InputType.EDIT_TEXT, title = "Temperature of the baby", required = false
    )

    private val actionUmbilicalBleed = FormInputV2(
        id = 111,
        inputType = InputType.RADIO,
        title = "If yes, either ASHA, ANM/MPW or TBA can tie again with a clean thread. Action taken? ",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val babyWeigntMatchesColor = FormInputV2(
        id = 112,
        inputType = InputType.RADIO, title = "Weighing matches with the colour?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val titleRoutineNewBornCare = FormInputV2(
        id = 113,
        inputType = InputType.HEADLINE,
        title = "Routine Newborn Care: whether the task was performed",
        required = false
    )
    private val babyDry = FormInputV2(
        id = 114,
        inputType = InputType.RADIO, title = "Dry the baby", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val cryWeakStopped = FormInputV2(
        id = 115,
        inputType = InputType.RADIO, title = "Cry weak/ stopped", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val cordCleanDry = FormInputV2(
        id = 116,
        inputType = InputType.RADIO, title = "Keep the cord clean and dry", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )

    private val unusualWithBaby = FormInputV2(
        id = 117,
        inputType = InputType.RADIO,
        title = "Was there anything unusual with the baby?",
        entries = arrayOf("Curved limbs", "cleft lip", "Other"),
        hasDependants = true,
        required = false,
    )
    private val otherUnusualWithBaby = FormInputV2(
        id = 118,
        inputType = InputType.EDIT_TEXT, title = "Other - unusual with the baby", required = false
    )

    /////////////// Part Visit //////////

    private val titleWashHands = FormInputV2(
        id = 119,
        inputType = InputType.HEADLINE,
        title = "ASHA should wash hands with soap and water before touching the baby during each visit",
        required = false
    )
    private val babyReferred = FormInputV2(
        id = 120,
        inputType = InputType.RADIO,
        title = "Baby referred for any reason?",
        entries = arrayOf("Yes", "No"),
        hasDependants = true,
        required = false
    )
    private val dateOfBabyReferral = FormInputV2(
        id = 121,
        inputType = InputType.DATE_PICKER,
        title = "Date of baby referral",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val placeOfBabyReferral = FormInputV2(
        id = 122,
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
        ),
        hasDependants = true,
        required = false
    )
    private val otherPlaceOfBabyReferral = FormInputV2(
        id = 123,
        inputType = InputType.EDIT_TEXT, title = "Other -Place of baby referral", required = false
    )
    private val motherReferred = FormInputV2(
        id = 124,
        inputType = InputType.RADIO,
        title = "Mother referred for any reason?",
        entries = arrayOf("Yes", "No"),
        hasDependants = true,
        required = false
    )
    private val dateOfMotherReferral = FormInputV2(
        id = 125,
        inputType = InputType.DATE_PICKER,
        title = "Date of mother referral",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val placeOfMotherReferral = FormInputV2(
        id = 126,
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
        ),
        hasDependants = true,
        required = false
    )
    private val otherPlaceOfMotherReferral = FormInputV2(
        id = 127,
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


    private fun setExistingValuesForCardPage(visitCard: HbncVisitCard?) {
        visitCard?.let {
            ashaName.value = it.ashaName
            villageName.value = it.villageName
            blockName.value = it.blockName
            motherName.value = it.motherName
            fatherName.value = it.fatherName
            placeOfDelivery.value = placeOfDelivery.getStringFromPosition(it.placeOfDelivery)
            gender.value = gender.getStringFromPosition(it.babyGender)
            typeOfDelivery.value = typeOfDelivery.getStringFromPosition(it.typeOfDelivery)
            dateOfDelivery.value = getDateFromLong(it.dateOfDelivery)
            healthSubCenterName.value = it.subCenterName
            dateOfDelivery.value = getDateFromLong(it.dateOfDelivery)
            gender.value = gender.entries?.get(it.babyGender)
            stillBirth.value = stillBirth.getStringFromPosition(it.stillBirth)
            startedBreastFeeding.value =
                startedBreastFeeding.getStringFromPosition(it.startedBreastFeeding)
            dateOfDischargeFromHospitalMother.value = getDateFromLong(it.dischargeDateMother)
            dateOfDischargeFromHospitalBaby.value = getDateFromLong(it.dischargeDateMother)
            weightAtBirth.value = it.weightInGrams.toString()
            registrationOfBirth.value =
                registrationOfBirth.getStringFromPosition(it.registrationOfBirth)

        }

    }


    private val partIPage by lazy {
        listOf(
            dateOfHomeVisit,
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


    private fun setExistingValuesForPartIPage(hbncPartI: HbncPartI?) {
        hbncPartI?.let {
            dateOfHomeVisit.value = getDateFromLong(it.dateOfVisit)
            babyAlive.value = babyAlive.getStringFromPosition(it.babyAlive)
            dateOfBabyDeath.value = getDateFromLong(it.dateOfBabyDeath)
            timeOfBabyDeath.value = it.timeOfBabyDeath
            placeOfBabyDeath.value = placeOfBabyDeath.getStringFromPosition(it.placeOfBabyDeath)
            otherPlaceOfBabyDeath.value = it.otherPlaceOfBabyDeath
            dateOfMotherDeath.value = getDateFromLong(it.dateOfMotherDeath)
            timeOfMotherDeath.value = it.timeOfMotherDeath
            placeOfMotherDeath.value =
                placeOfMotherDeath.getStringFromPosition(it.placeOfMotherDeath)
            otherPlaceOfMotherDeath.value = it.otherPlaceOfMotherDeath
            babyPreterm.value = babyPreterm.getStringFromPosition(it.isBabyPreterm)
            gestationalAge.value = gestationalAge.getStringFromPosition(it.gestationalAge)
            dateOfBabyFirstExamination.value = getDateFromLong(it.dateOfFirstExamination)
            timeOfBabyFirstExamination.value = it.timeOfFirstExamination
            motherAlive.value = motherAlive.getStringFromPosition(it.motherAlive)
            motherProblems.value = it.motherAnyProblem
            babyFedAfterBirth.value = babyFedAfterBirth.getStringFromPosition(it.babyFirstFed)
            otherBabyFedAfterBirth.value = it.otherBabyFirstFed
            whenBabyFirstFed.value = it.timeBabyFirstFed
            howBabyTookFirstFeed.value =
                howBabyTookFirstFeed.getStringFromPosition(it.howBabyTookFirstFeed)
            motherHasBreastFeedProblem.value =
                motherHasBreastFeedProblem.getStringFromPosition(it.motherHasBreastFeedProblem)
            motherBreastProblem.value = it.motherBreastFeedProblem
            addNecessaryDependantFieldsToListForPart1(it)
        }

    }

    private fun addNecessaryDependantFieldsToListForPart1(
        hbncPartI: HbncPartI
    ) {
        hbncPartI.let {
            if (it.babyAlive == 2) {
                list.addAll(
                    list.indexOf(babyAlive) + 1,
                    listOf(
                        dateOfBabyDeath,
                        timeOfBabyDeath,
                        placeOfBabyDeath,
                    )
                )
                if (it.placeOfBabyDeath == (placeOfBabyDeath.entries!!.size))
                    list.add(list.indexOf(placeOfBabyDeath) + 1, otherPlaceOfBabyDeath)

            }
            if (it.motherAlive == 2) {
                list.addAll(
                    list.indexOf(motherAlive) + 1,
                    listOf(
                        dateOfMotherDeath,
                        timeOfMotherDeath,
                        placeOfMotherDeath
                    )
                )
                if (it.placeOfMotherDeath == (placeOfMotherDeath.entries!!.size))
                    list.add(list.indexOf(placeOfMotherDeath) + 1, otherPlaceOfMotherDeath)
            }
            if (it.isBabyPreterm == 1) {
                list.add(list.indexOf(babyPreterm) + 1, gestationalAge)
            }
            if (it.babyFirstFed == babyFedAfterBirth.entries!!.size)
                list.add(list.indexOf(babyFedAfterBirth) + 1, otherBabyFedAfterBirth)
            if (it.motherHasBreastFeedProblem == 1)
                list.add(list.indexOf(motherHasBreastFeedProblem) + 1, motherBreastFeedProblem)

        }
    }


    private val partIIPage by lazy {
        listOf(
            dateOfHomeVisit,
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

    private fun setExistingValuesForPartIIPage(part2: HbncPartII?) {
        part2?.let {
            dateOfHomeVisit.value = getDateFromLong(it.dateOfVisit)
            babyTemperature.value = it.babyTemperature
            babyEyeCondition.value = babyEyeCondition.getStringFromPosition(it.babyEyeCondition)
            babyBleedUmbilicalCord.value =
                babyBleedUmbilicalCord.getStringFromPosition(it.babyUmbilicalBleed)
            actionUmbilicalBleed.value =
                actionUmbilicalBleed.getStringFromPosition(it.actionBabyUmbilicalBleed)
            babyWeight.value = it.babyWeight
            babyWeigntMatchesColor.value =
                babyWeigntMatchesColor.getStringFromPosition(it.babyWeightMatchesColor)
            babyWeightColor.value = babyWeightColor.getStringFromPosition(it.babyWeightColorOnScale)
            allLimbsLimp.value = allLimbsLimp.getStringFromPosition(it.allLimbsLimp)
            feedingLessStop.value = feedingLessStop.getStringFromPosition(it.feedLessStop)
            cryWeakStopped.value = cryWeakStopped.getStringFromPosition(it.cryWeakStop)
            babyDry.value = babyDry.getStringFromPosition(it.dryBaby)
            wrapClothKeptMother.value =
                wrapClothKeptMother.getStringFromPosition(it.wrapClothCloseToMother)
            onlyBreastMilk.value = onlyBreastMilk.getStringFromPosition(it.exclusiveBreastFeeding)
            cordCleanDry.value = cordCleanDry.getStringFromPosition(it.cordCleanDry)
            unusualWithBaby.value = unusualWithBaby.getStringFromPosition(it.unusualInBaby)
            otherUnusualWithBaby.value = it.otherUnusualInBaby
            addNecessaryDependantFieldsToListForPart2(it)
        }

    }

    private fun addNecessaryDependantFieldsToListForPart2(part2: HbncPartII) {
        part2.let {
            if (it.unusualInBaby == unusualWithBaby.entries!!.size)
                list.add(list.indexOf(unusualWithBaby) + 1, otherUnusualWithBaby)
        }

    }


    private val visitPage by lazy {
        listOf(
            dateOfHomeVisit,
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
            dateOfSupSig,
        )
    }

    private fun setExistingValuesForVisitPage(visit: HbncHomeVisit?) {
        visit?.let {
            dateOfHomeVisit.value = getDateFromLong(it.dateOfVisit)
            babyAlive.value = babyAlive.getStringFromPosition(it.babyAlive)
            timesMotherFed24hr.value = it.numTimesFullMeal24hr.toString()
            timesPadChanged.value = it.numPadChanged24hr.toString()
            babyKeptWarmWinter.value =
                babyKeptWarmWinter.getStringFromPosition(it.babyKeptWarmWinter)
            babyBreastFedProperly.value =
                babyBreastFedProperly.getStringFromPosition(it.babyFedProperly)
            babyCryContinuously.value =
                babyCryContinuously.getStringFromPosition(it.babyCryContinuously)
            motherBodyTemperature.value = it.motherTemperature
            motherWaterDischarge.value =
                motherWaterDischarge.getStringFromPosition(it.foulDischargeFever)
            motherSpeakAbnormalFits.value =
                motherSpeakAbnormalFits.getStringFromPosition(it.motherSpeakAbnormallyFits)
            motherNoOrLessMilk.value = motherNoOrLessMilk.getStringFromPosition(it.motherLessNoMilk)
            motherBreastProblem.value =
                motherBreastProblem.getStringFromPosition(it.motherBreastProblem)
            babyEyesSwollen.value = babyEyesSwollen.getStringFromPosition(it.babyEyesSwollen)
            babyWeight.value = it.babyWeight
            babyTemperature.value = it.babyTemperature
            yellowJaundice.value = yellowJaundice.getStringFromPosition(it.babyYellow)
            childImmunizationStatus.value = it.babyImmunizationStatus
            babyReferred.value = babyReferred.getStringFromPosition(it.babyReferred)
            motherReferred.value = motherReferred.getStringFromPosition(it.motherReferred)
            allLimbsLimp.value = allLimbsLimp.getStringFromPosition(it.allLimbsLimp)
            feedingLessStop.value = feedingLessStop.getStringFromPosition(it.feedingLessStopped)
            cryWeakStopped.value = cryWeakStopped.getStringFromPosition(it.cryWeakStopped)
            bloatedStomach.value = bloatedStomach.getStringFromPosition(it.bloatedStomach)
            childColdOnTouch.value = childColdOnTouch.getStringFromPosition(it.coldOnTouch)
            childChestDrawing.value = childChestDrawing.getStringFromPosition(it.chestDrawing)
            breathFast.value = breathFast.getStringFromPosition(it.breathFast)
            pusNavel.value = pusNavel.getStringFromPosition(it.pusNavel)
            sup.value = sup.getStringFromPosition(it.sup)
            supName.value = it.supName
            supRemark.value = it.supComment
            dateOfSupSig.value = getDateFromLong(it.supSignDate)
            addNecessaryDependantFieldsToListForVisit(it)
        }

    }

    private fun addNecessaryDependantFieldsToListForVisit(visit: HbncHomeVisit) {
        if(visit.babyReferred == 1) {
            list.addAll(
                listOf(
                    dateOfBabyReferral,
                    placeOfBabyReferral
                )
            )
            if(visit.placeOfBabyReferral == placeOfBabyReferral.entries!!.size)
                list.add(list.indexOf(placeOfBabyReferral)+1, otherPlaceOfBabyReferral)
        }
        if(visit.motherReferred == 1) {
            list.addAll(
                listOf(
                    dateOfMotherReferral,
                    placeOfMotherReferral
                )
            )
            if(visit.placeOfMotherReferral== placeOfMotherReferral.entries!!.size)
                list.add(list.indexOf(placeOfMotherReferral)+1, otherPlaceOfMotherReferral)
        }
    }
}