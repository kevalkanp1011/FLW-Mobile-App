package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.content.res.Configuration
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
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

    private val defResources by lazy {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(Languages.ENGLISH.symbol))
        context.createConfigurationContext(configuration).resources
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
        allCaps = true,
        required = true
    )

    private val lastNameHeadOfFamily = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_last_name_hof),
        allCaps = true,
        required = false
    )
    private val mobileNoHeadOfFamily = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_mob_no_hof),
        required = true,
        etMaxLength = 10,
        isMobileNumber = true,
        min=6000000000,
        max=9999999999,
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
        list = context.resources.getStringArray(R.array.nhhr_poverty_line_array),
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

    val residentialArea = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_type_residential_area),
        list = context.resources.getStringArray(R.array.nhhr_type_residential_area_array),
        required = false
    )


    val otherResidentialArea = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_type_residential_area_other),
        required = false
    )

    private val typeOfHouse = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_type_of_house),
        list = context.resources.getStringArray(R.array.nhhr_type_of_house_array),
        required = true
    )
    private val houseOwnership = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = context.getString(R.string.nhhr_house_own),
        list = context.resources.getStringArray(R.array.nhhr_house_own_array),
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
        list = context.resources.getStringArray(R.array.nhhr_separate_kitchen_array),
        required = true
    )

    private val fuelForCooking = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_fuel_cooking),
        list = context.resources.getStringArray(R.array.nhhr_fuel_cooking_array),
        required = true,
    )
    val fuelForCookingTrigger: FormInput
        get() = fuelForCooking


    val otherFuelForCooking = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_fuel_cooking_other),
        required = true
    )


    private val sourceOfWater = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_primary_water),
        list = context.resources.getStringArray(R.array.nhhr_primary_water_array),
        required = true,
    )

    val sourceOfWaterTrigger: FormInput
        get() = sourceOfWater


    val otherSourceOfWater = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_primary_water_other),
        required = true
    )


    private val sourceOfElectricity = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_avail_electricity),
        list = context.resources.getStringArray(R.array.nhhr_avail_electricity_array),
        required = true
    )
    val sourceOfElectricityTrigger: FormInput
        get() = sourceOfElectricity

    val otherSourceOfElectricity = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_avail_electricity_other),
        required = true
    )


    private val availOfToilet = FormInput(
        inputType = FormInput.InputType.DROPDOWN,
        title = context.getString(R.string.nhhr_avail_toilet),
        list = context.resources.getStringArray(R.array.nhhr_avail_toilet_array),
        required = true,
    )

    val availOfToiletTrigger: FormInput
        get() = availOfToilet

    val otherAvailOfToilet = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = context.getString(R.string.nhhr_avail_toilet_other),
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
            val povertyLineLocal = this@HouseholdFormDataset.povertyLine.value.value
            povertyLineId =
                this@HouseholdFormDataset.povertyLine.list?.indexOf(povertyLineLocal)
                    ?.let { it + 1 }
                    ?: 0
            povertyLine =
                defResources.getStringArray(R.array.nhhr_poverty_line_array)[povertyLineId - 1]
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

            isDraft = false
        }
        return household!!
    }


}