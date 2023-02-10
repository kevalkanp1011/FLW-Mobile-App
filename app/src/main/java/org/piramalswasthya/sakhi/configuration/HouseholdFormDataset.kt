package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HouseholdCache
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HouseholdFormDataset(context: Context) {

    private var household: HouseholdCache? = null

    constructor(context: Context, household: HouseholdCache) : this(context) {
        this.household = household
        firstNameHeadOfFamily.value.value = household.familyHeadName
        lastNameHeadOfFamily.value.value = household.familyName
        mobileNoHeadOfFamily.value.value = household.familyHeadPhoneNo.toString()
        houseNo.value.value = household.houseNo
        wardNo.value.value = household.wardNo
        wardName.value.value = household.wardName
        mohallaName.value.value = household.mohallaName
        povertyLine.value.value = household.povertyLine
        residentialArea.value.value = household.residentialArea
        otherResidentialArea.value.value = household.otherResidentialArea
        typeOfHouse.value.value = household.houseType
        houseOwnership.value.value = household.isHouseOwned
        separateKitchen.value.value = household.separateKitchen
        fuelForCooking.value.value = household.fuelUsed
        otherFuelForCooking.value.value = household.otherFuelUsed
        sourceOfWater.value.value = household.sourceOfDrinkingWater
        otherSourceOfWater.value.value = household.otherSourceOfDrinkingWater
        sourceOfElectricity.value.value = household.availabilityOfElectricity
        otherSourceOfElectricity.value.value = household.otherAvailabilityOfElectricity
        availOfToilet.value.value = household.availabilityOfToilet
        otherAvailOfToilet.value.value = household.otherAvailabilityOfToilet
    }


    companion object {


        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val mdFormat =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }

        private fun getCurrentTime(): String {
            val dateFormat: DateFormat =
                SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val date = Date()
            return dateFormat.format(date)
        }

        fun getHHidFromUserId(userId: Int): Long {
            val date = getCurrentDate().replace("-", "")
            val time = getCurrentTime().replace(":", "")
            return (date + time + userId).toLong()
        }
    }

    //////////////////////////////// First Page /////////////////////////////////////////

    private val firstNameHeadOfFamily = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_first_name_hof),
        required = true
    )

    private val lastNameHeadOfFamily = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_last_name_hof),
        required = false
    )
    private val mobileNoHeadOfFamily = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_mob_no_hof),
        required = true,
        etLength = 10,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    )
    private val houseNo = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_house_no),
        required = false
    )
    private val wardNo = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_ward_no),
        required = false
    )
    private val wardName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_ward_name),
        required = false
    )
    private val mohallaName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_mohalla_name),
        required = false
    )
    private val povertyLine = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = context.getString(R.string.nhhr_poverty_line),
        list = listOf(
            "APL",
            "BPL",
            "Don't Know"
        ),
        required = true
    )
    val firstPage by lazy {
        listOf(
            firstNameHeadOfFamily,
            lastNameHeadOfFamily,
            mobileNoHeadOfFamily,
            houseNo,
            wardNo,
            wardName,
            mohallaName,
            povertyLine
        )
    }

    //////////////////////////////// Second Page /////////////////////////////////////////

    private val residentialArea = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_type_residential_area),
        list = listOf(
            "Rural",
            "Urban",
            "Tribal",
            "Other"
        ),
        required = false
    )

    val residentialAreaTrigger: FormInput
        get() = residentialArea

    val otherResidentialArea = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Other type of residential area",
        required = false
    )

    private val typeOfHouse = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_type_of_house),
        list = listOf(
            "None",
            "Kuchcha",
            "Pucca",
            "Other"
        ),
        required = true
    )
    private val houseOwnership = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = context.getString(R.string.nhhr_house_own),
        listOf(
            "Yes",
            "No"
        ),
        required = true
    )
    val secondPage by lazy {
        listOf(
            residentialArea,
            typeOfHouse,
            houseOwnership
        )
    }

    //////////////////////////////// Third Page /////////////////////////////////////////

    private val separateKitchen = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = context.getString(R.string.nhhr_separate_kitchen),
        list = listOf(
            "Yes",
            "No"
        ),
        required = true
    )

    private val fuelForCooking = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_fuel_cooking),
        list = listOf(
            "Firewood",
            "Crop Residue",
            "Cow dung cake",
            "Coal",
            "Kerosene",
            "LPG",
            "Other"
        ),
        required = true,
    )
    val fuelForCookingTrigger: FormInput
        get() = fuelForCooking


    val otherFuelForCooking = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Other Type of fuel used for Cooking",
        required = true
    )


    private val sourceOfWater = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_primary_water),
        list = listOf(
            "Tap Water",
            "Hand pump within house",
            "Hand pump outside of house",
            "Well",
            "Tank",
            "River",
            "Pond",
            "Other"
        ),
        required = true,
    )

    val sourceOfWaterTrigger: FormInput
        get() = sourceOfWater


    val otherSourceOfWater = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Other Primary Source of Water",
        required = true
    )


    private val sourceOfElectricity = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_avail_electricity),
        list = listOf(
            "Electricity Supply",
            "Generator",
            "Solar Power",
            "Kerosene Lamp",
            "Other",
        ),
        required = true
    )
    val sourceOfElectricityTrigger: FormInput
        get() = sourceOfElectricity

    val otherSourceOfElectricity = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Other availability of Electricity",
        required = true
    )


    private val availOfToilet = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_avail_toilet),
        list = listOf(
            "Flush toilet with running water",
            "Flush toiler without water",
            "Pit toilet with running water supply",
            "Pit toilet without water supply",
            "Other",
            "None"
        ),
        required = true,
    )

    val availOfToiletTrigger: FormInput
        get() = availOfToilet

    val otherAvailOfToilet = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Other Availability of Toilet",
        required = true
    )

    val thirdPage by lazy {
        listOf(
            separateKitchen,
            fuelForCooking,
            sourceOfWater,
            sourceOfElectricity,
            availOfToilet
        )
    }

    fun getHouseholdForFirstPage(userId: Int): HouseholdCache {

        if (household == null) {
            household = HouseholdCache(
                householdId = 0,
                ashaId = userId,
                isDraft = true,
                processed = "N"
            )
        }
        household?.apply {
            familyHeadName = firstNameHeadOfFamily.value.value
            familyName = lastNameHeadOfFamily.value.value
            familyHeadPhoneNo = mobileNoHeadOfFamily.value.value?.toLong()
            houseNo = this@HouseholdFormDataset.houseNo.value.value
            wardNo = this@HouseholdFormDataset.wardNo.value.value
            wardName = this@HouseholdFormDataset.wardName.value.value
            mohallaName = this@HouseholdFormDataset.mohallaName.value.value
            povertyLine = this@HouseholdFormDataset.povertyLine.value.value
            povertyLineId =
                this@HouseholdFormDataset.povertyLine.list?.indexOf(povertyLine)?.let { it + 1 }
                    ?: 0
        }
        return household!!
    }

    fun getHouseholdForSecondPage(): HouseholdCache {

        household?.apply {
            residentialArea = this@HouseholdFormDataset.residentialArea.value.value
            residentialAreaId =
                this@HouseholdFormDataset.residentialArea.list?.indexOf(residentialArea)
                    ?.let { it + 1 } ?: 0
            otherResidentialArea = this@HouseholdFormDataset.otherResidentialArea.value.value
            houseType = this@HouseholdFormDataset.typeOfHouse.value.value
            houseTypeId =
                this@HouseholdFormDataset.typeOfHouse.list?.indexOf(houseType)?.let { it + 1 } ?: 0
            isHouseOwned = this@HouseholdFormDataset.houseOwnership.value.value
            isHouseOwnedId =
                this@HouseholdFormDataset.houseOwnership.list?.indexOf(isHouseOwned)?.let { it + 1 }
                    ?: 0
        }

        return household!!
    }

    fun getHouseholdForThirdPage(): HouseholdCache {
        household?.apply {
            separateKitchen = this@HouseholdFormDataset.separateKitchen.value.value
            separateKitchenId =
                this@HouseholdFormDataset.separateKitchen.list?.indexOf(separateKitchen)
                    ?.let { it + 1 } ?: 0
            fuelUsed = this@HouseholdFormDataset.fuelForCooking.value.value
            fuelUsedId =
                this@HouseholdFormDataset.fuelForCooking.list?.indexOf(fuelUsed)?.let { it + 1 }
                    ?: 0
            otherFuelUsed = this@HouseholdFormDataset.otherFuelForCooking.value.value
            sourceOfDrinkingWater = this@HouseholdFormDataset.sourceOfWater.value.value
            sourceOfDrinkingWaterId =
                this@HouseholdFormDataset.sourceOfWater.list?.indexOf(sourceOfDrinkingWater)
                    ?.let { it + 1 } ?: 0
            otherSourceOfDrinkingWater = this@HouseholdFormDataset.otherSourceOfWater.value.value
            availabilityOfElectricity = this@HouseholdFormDataset.sourceOfElectricity.value.value
            availabilityOfElectricityId =
                this@HouseholdFormDataset.sourceOfElectricity.list?.indexOf(
                    availabilityOfElectricity
                )?.let { it + 1 } ?: 0
            otherAvailabilityOfElectricity =
                this@HouseholdFormDataset.otherSourceOfElectricity.value.value
            availabilityOfToilet = this@HouseholdFormDataset.availOfToilet.value.value
            availabilityOfToiletId =
                this@HouseholdFormDataset.availOfToilet.list?.indexOf(availabilityOfToilet)
                    ?.let { it + 1 } ?: 0
            otherAvailabilityOfToilet = this@HouseholdFormDataset.otherAvailOfToilet.value.value
            if (householdId == 0L) {
                householdId = getHHidFromUserId(ashaId)
                serverUpdatedStatus = 2
                processed = "N"
            } else {
                serverUpdatedStatus = 1
            }
            isDraft = false
        }
        return household!!
    }


}