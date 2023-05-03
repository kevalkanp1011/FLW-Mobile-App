package org.piramalswasthya.sakhi.configuration


import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.model.InputType.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HouseholdFormDataset(context: Context, language: Languages) : Dataset(context, language) {
    companion object {


        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val mdFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }

        private fun getCurrentTime(): String {
            val dateFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val date = Date()
            return dateFormat.format(date)
        }

        private fun getHHidFromUserId(userId: Int): Long {
            val date = getCurrentDate().replace("-", "")
            val time = getCurrentTime().replace(":", "")
            return (date + time + userId).toLong()
        }
    }

    //////////////////////////////// First Page /////////////////////////////////////////

    private val firstNameHeadOfFamily = FormElement(
        id = 0,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_first_name_hof),
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
        allCaps = true,
        required = true
    )

    private val lastNameHeadOfFamily = FormElement(
        id = 1,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_last_name_hof),
        etInputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
        allCaps = true,
        required = false
    )
    private val mobileNoHeadOfFamily = FormElement(
        id = 2,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_mob_no_hof),
        required = true,
        etMaxLength = 10,
        isMobileNumber = true,
        min = 6000000000,
        max = 9999999999,
        etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    )
    private val houseNo = FormElement(
        id = 3,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_house_no),
        etMaxLength = 100,
        required = false
    )
    private val wardNo = FormElement(
        id = 4,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_ward_no),
        etMaxLength = 100,
        required = false
    )
    private val wardName = FormElement(
        id = 5,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_ward_name),
        etMaxLength = 100,
        required = false
    )
    private val mohallaName = FormElement(
        id = 6,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_mohalla_name),
        etMaxLength = 100,
        required = false
    )
    private val povertyLine = FormElement(
        id = 7,
        inputType = RADIO,
        title = resources.getString(R.string.nhhr_poverty_line),
        entries = resources.getStringArray(R.array.nhhr_poverty_line_array),
        required = true
    )
    private val firstPage by lazy {
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

    suspend fun setFirstPage(family: HouseholdFamily?) {
        setUpPage(firstPage)
        family?.let { saved ->
            firstNameHeadOfFamily.value = saved.familyHeadName
            lastNameHeadOfFamily.value = saved.familyName
            mobileNoHeadOfFamily.value = saved.familyHeadPhoneNo.toString()
            houseNo.value = saved.houseNo
            wardNo.value = saved.wardNo
            wardName.value = saved.wardName
            mohallaName.value = saved.mohallaName
            povertyLine.value = povertyLine.getStringFromPosition(saved.povertyLineId)
        }
    }

    //////////////////////////////// Second Page /////////////////////////////////////////

    private val residentialArea = FormElement(
        id = 8,
        inputType = DROPDOWN,
        title = resources.getString(R.string.nhhr_type_residential_area),
        entries = resources.getStringArray(R.array.nhhr_type_residential_area_array),
        hasDependants = true,
        required = false
    )


    private val otherResidentialArea = FormElement(
        id = 9,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_type_residential_area_other),
        etMaxLength = 100,
        required = true
    )

    private val typeOfHouse = FormElement(
        id = 10,
        inputType = DROPDOWN,
        title = resources.getString(R.string.nhhr_type_of_house),
        entries = resources.getStringArray(R.array.nhhr_type_of_house_array),
        required = true
    )
    private val houseOwnership = FormElement(
        id = 11,
        inputType = RADIO,
        title = resources.getString(R.string.nhhr_house_own),
        entries = resources.getStringArray(R.array.nhhr_house_own_array),
        required = true
    )
    private val secondPage by lazy {
        listOf(
            residentialArea, typeOfHouse, houseOwnership
        )
    }

    suspend fun setSecondPage(details: HouseholdDetails?) {
        val list = secondPage.toMutableList()
        details?.let { saved ->
            residentialArea.value = residentialArea.getStringFromPosition(saved.residentialAreaId)
            otherResidentialArea.value = saved.otherResidentialArea
            typeOfHouse.value = typeOfHouse.getStringFromPosition(saved.houseTypeId)
            houseOwnership.value = houseOwnership.getStringFromPosition(saved.isHouseOwnedId)
        }
        if (residentialArea.value == residentialArea.entries!!.last()) {
            list.add(list.indexOf(residentialArea) + 1, otherResidentialArea)
        }
        setUpPage(list)
    }


    //////////////////////////////// Third Page /////////////////////////////////////////

    private val separateKitchen = FormElement(
        id = 12,
        inputType = RADIO,
        title = resources.getString(R.string.nhhr_separate_kitchen),
        entries = resources.getStringArray(R.array.nhhr_separate_kitchen_array),
        required = true
    )

    private val fuelForCooking = FormElement(
        id = 13,
        inputType = DROPDOWN,
        title = resources.getString(R.string.nhhr_fuel_cooking),
        entries = resources.getStringArray(R.array.nhhr_fuel_cooking_array),
        hasDependants = true,
        required = true,
    )

    private val otherFuelForCooking = FormElement(
        id = 14,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_fuel_cooking_other),
        etMaxLength = 100,
        required = true
    )


    private val sourceOfWater = FormElement(
        id = 15,
        inputType = DROPDOWN,
        title = resources.getString(R.string.nhhr_primary_water),
        entries = resources.getStringArray(R.array.nhhr_primary_water_array),
        hasDependants = true,
        required = true,
    )

    private val otherSourceOfWater = FormElement(
        id = 16,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_primary_water_other),
        etMaxLength = 100,
        required = true
    )


    private val sourceOfElectricity = FormElement(
        id = 17,
        inputType = DROPDOWN,
        title = resources.getString(R.string.nhhr_avail_electricity),
        entries = resources.getStringArray(R.array.nhhr_avail_electricity_array),
        hasDependants = true,
        required = true
    )

    private val otherSourceOfElectricity = FormElement(
        id = 18,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_avail_electricity_other),
        etMaxLength = 100,
        required = true
    )


    private val availOfToilet = FormElement(
        id = 19,
        inputType = DROPDOWN,
        title = resources.getString(R.string.nhhr_avail_toilet),
        entries = resources.getStringArray(R.array.nhhr_avail_toilet_array),
        hasDependants = true,
        required = true,
    )

    private val otherAvailOfToilet = FormElement(
        id = 20,
        inputType = EDIT_TEXT,
        title = resources.getString(R.string.nhhr_avail_toilet_other),
        etMaxLength = 100,
        required = true
    )

    private val thirdPage by lazy {
        listOf(
            separateKitchen, fuelForCooking, sourceOfWater, sourceOfElectricity, availOfToilet
        )
    }

    suspend fun setThirdPage(amenities: HouseholdAmenities?) {
        val list = thirdPage.toMutableList()
        amenities?.let { saved ->
            separateKitchen.value = separateKitchen.getStringFromPosition(saved.separateKitchenId)
            fuelForCooking.value = fuelForCooking.getStringFromPosition(saved.fuelUsedId)
            otherFuelForCooking.value = saved.otherFuelUsed
            sourceOfWater.value = sourceOfWater.getStringFromPosition(saved.sourceOfDrinkingWaterId)
            otherSourceOfWater.value = saved.otherSourceOfDrinkingWater
            sourceOfElectricity.value =
                sourceOfElectricity.getStringFromPosition(saved.availabilityOfElectricityId)
            otherSourceOfElectricity.value = saved.otherAvailabilityOfElectricity
            availOfToilet.value = availOfToilet.getStringFromPosition(saved.availabilityOfToiletId)
            otherAvailOfToilet.value = saved.otherAvailabilityOfToilet
        }
        if (fuelForCooking.value == fuelForCooking.entries!!.last()) {
            list.add(list.indexOf(fuelForCooking) + 1, otherFuelForCooking)
        }
        if (sourceOfWater.value == sourceOfWater.entries!!.last()) {
            list.add(list.indexOf(sourceOfWater) + 1, otherSourceOfWater)
        }
        if (sourceOfElectricity.value == sourceOfElectricity.entries!!.last()) {
            list.add(list.indexOf(sourceOfElectricity) + 1, otherSourceOfElectricity)
        }
        if (availOfToilet.value == availOfToilet.entries!!.let { it[it.size - 2] }) {
            list.add(list.indexOf(availOfToilet) + 1, otherAvailOfToilet)
        }

        setUpPage(list)
    }

//    fun getHouseholdForFirstPage(userId: Int): HouseholdCache {
//
//        if (household == null) {
//            household = HouseholdCache(
//                householdId = 0,
//                ashaId = userId,
//                isDraft = true,
//                processed = "N"
//            )
//        }
//        household?.family?.apply {
//            familyHeadName = firstNameHeadOfFamily.value
//            familyName = lastNameHeadOfFamily.value
//            familyHeadPhoneNo = mobileNoHeadOfFamily.value?.toLong()
//            houseNo = this@HouseholdFormDataset.houseNo.value
//            wardNo = this@HouseholdFormDataset.wardNo.value
//            wardName = this@HouseholdFormDataset.wardName.value
//            mohallaName = this@HouseholdFormDataset.mohallaName.value
//            val povertyLineLocal = this@HouseholdFormDataset.povertyLine.value
//            povertyLineId = this@HouseholdFormDataset.povertyLine.entries?.indexOf(povertyLineLocal)
//                ?.let { it + 1 } ?: 0
//            povertyLine =
//                resources.getStringArray(R.array.nhhr_poverty_line_array)[povertyLineId - 1]
//        }
//        return household!!
//    }

//    fun getHouseholdForSecondPage(): HouseholdCache {
//
//        household?.apply {
//
//        }
//
//        return household!!
//    }

//    fun getHouseholdForThirdPage(): HouseholdCache {
//        household?.apply {
//            separateKitchen = this@HouseholdFormDataset.separateKitchen.value
//            separateKitchenId =
//                this@HouseholdFormDataset.separateKitchen.entries?.indexOf(separateKitchen)
//                    ?.let { it + 1 } ?: 0
//            fuelUsed = this@HouseholdFormDataset.fuelForCooking.value
//            fuelUsedId =
//                this@HouseholdFormDataset.fuelForCooking.entries?.indexOf(fuelUsed)?.let { it + 1 }
//                    ?: 0
//            otherFuelUsed = this@HouseholdFormDataset.otherFuelForCooking.value
//            sourceOfDrinkingWater = this@HouseholdFormDataset.sourceOfWater.value
//            sourceOfDrinkingWaterId =
//                this@HouseholdFormDataset.sourceOfWater.entries?.indexOf(sourceOfDrinkingWater)
//                    ?.let { it + 1 } ?: 0
//            otherSourceOfDrinkingWater = this@HouseholdFormDataset.otherSourceOfWater.value
//            availabilityOfElectricity = this@HouseholdFormDataset.sourceOfElectricity.value
//            availabilityOfElectricityId =
//                this@HouseholdFormDataset.sourceOfElectricity.entries?.indexOf(
//                    availabilityOfElectricity
//                )?.let { it + 1 } ?: 0
//            otherAvailabilityOfElectricity =
//                this@HouseholdFormDataset.otherSourceOfElectricity.value
//            availabilityOfToilet = this@HouseholdFormDataset.availOfToilet.value
//            availabilityOfToiletId =
//                this@HouseholdFormDataset.availOfToilet.entries?.indexOf(availabilityOfToilet)
//                    ?.let { it + 1 } ?: 0
//            otherAvailabilityOfToilet = this@HouseholdFormDataset.otherAvailOfToilet.value
//
//            isDraft = false
//        }
//        return household!!
//    }

    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        return when (formId) {
            firstNameHeadOfFamily.id -> {
                validateEmptyOnEditText(firstNameHeadOfFamily)
                validateAllCapsOrSpaceOnEditText(firstNameHeadOfFamily)
            }
            lastNameHeadOfFamily.id -> validateAllCapsOrSpaceOnEditText(lastNameHeadOfFamily)
            mobileNoHeadOfFamily.id -> {
                validateEmptyOnEditText(mobileNoHeadOfFamily)
                validateMobileNumberOnEditText(mobileNoHeadOfFamily)
            }
            residentialArea.id -> triggerDependants(
                source = residentialArea,
                passedIndex = index,
                triggerIndex = residentialArea.entries!!.size - 1,
                target = otherResidentialArea
            )
            fuelForCooking.id -> triggerDependants(
                source = fuelForCooking,
                passedIndex = index,
                triggerIndex = fuelForCooking.entries!!.size - 1,
                target = otherFuelForCooking
            )
            sourceOfWater.id -> triggerDependants(
                source = sourceOfWater,
                passedIndex = index,
                triggerIndex = sourceOfWater.entries!!.size - 1,
                target = otherSourceOfWater
            )
            sourceOfElectricity.id -> triggerDependants(
                source = sourceOfElectricity,
                passedIndex = index,
                triggerIndex = sourceOfElectricity.entries!!.size - 1,
                target = otherSourceOfElectricity
            )
            availOfToilet.id -> triggerDependants(
                source = availOfToilet,
                passedIndex = index,
                triggerIndex = availOfToilet.entries!!.size - 2,
                target = otherAvailOfToilet
            )
            otherResidentialArea.id -> {
                validateEmptyOnEditText(otherResidentialArea)
                validateAllAlphabetsSpaceOnEditText(otherResidentialArea)
            }
            otherFuelForCooking.id -> {
                validateEmptyOnEditText(otherFuelForCooking)
                validateAllAlphabetsSpaceOnEditText(otherFuelForCooking)
            }
            otherSourceOfWater.id -> {
                validateEmptyOnEditText(otherSourceOfWater)
                validateAllAlphabetsSpaceOnEditText(otherSourceOfWater)
            }
            otherSourceOfElectricity.id -> {
                validateEmptyOnEditText(otherSourceOfElectricity)
                validateAllAlphabetsSpaceOnEditText(otherSourceOfElectricity)
            }
            otherAvailOfToilet.id -> {
                validateEmptyOnEditText(otherAvailOfToilet)
                validateAllAlphabetsSpaceOnEditText(otherAvailOfToilet)
            }
            else -> -1
        }
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        when (pageNumber) {
            1 -> {
                mapValuesForPage1(cacheModel)
            }
            2 -> {
                mapValuesForPage2(cacheModel)
            }
            3 -> {
                mapValuesForPage3(cacheModel)
            }
        }

    }

    private fun mapValuesForPage1(cacheModel: FormDataModel) {
        val family = HouseholdFamily()
        family.apply {
            familyHeadName = firstNameHeadOfFamily.value
            familyName = lastNameHeadOfFamily.value
            familyHeadPhoneNo = mobileNoHeadOfFamily.value?.toLong()
            houseNo = this@HouseholdFormDataset.houseNo.value
            wardNo = this@HouseholdFormDataset.wardNo.value
            wardName = this@HouseholdFormDataset.wardName.value
            mohallaName = this@HouseholdFormDataset.mohallaName.value
            val povertyLineLocal = this@HouseholdFormDataset.povertyLine.value
            povertyLineId = this@HouseholdFormDataset.povertyLine.entries?.indexOf(povertyLineLocal)
                ?.let { it + 1 } ?: 0
            povertyLine =
                resources.getStringArray(R.array.nhhr_poverty_line_array)[povertyLineId - 1]
        }
        (cacheModel as HouseholdCache).family = family
    }

    private fun mapValuesForPage2(cacheModel: FormDataModel) {
        val details = HouseholdDetails()
        details.apply {
            residentialArea = this@HouseholdFormDataset.residentialArea.value
            residentialAreaId =
                this@HouseholdFormDataset.residentialArea.entries?.indexOf(residentialArea)
                    ?.let { it + 1 } ?: 0
            otherResidentialArea = this@HouseholdFormDataset.otherResidentialArea.value
            houseType = this@HouseholdFormDataset.typeOfHouse.value
            houseTypeId =
                this@HouseholdFormDataset.typeOfHouse.entries?.indexOf(houseType)?.let { it + 1 }
                    ?: 0
            isHouseOwned = this@HouseholdFormDataset.houseOwnership.value
            isHouseOwnedId = this@HouseholdFormDataset.houseOwnership.entries?.indexOf(isHouseOwned)
                ?.let { it + 1 } ?: 0
        }
        (cacheModel as HouseholdCache).details = details
    }

    private fun mapValuesForPage3(cacheModel: FormDataModel) {
        val amenities = HouseholdAmenities()
        amenities.apply {
            separateKitchen = this@HouseholdFormDataset.separateKitchen.value
            separateKitchenId =
                this@HouseholdFormDataset.separateKitchen.entries?.indexOf(separateKitchen)
                    ?.let { it + 1 } ?: 0
            fuelUsed = this@HouseholdFormDataset.fuelForCooking.value
            fuelUsedId =
                this@HouseholdFormDataset.fuelForCooking.entries?.indexOf(fuelUsed)?.let { it + 1 }
                    ?: 0
            otherFuelUsed = this@HouseholdFormDataset.otherFuelForCooking.value
            sourceOfDrinkingWater = this@HouseholdFormDataset.sourceOfWater.value
            sourceOfDrinkingWaterId =
                this@HouseholdFormDataset.sourceOfWater.entries?.indexOf(sourceOfDrinkingWater)
                    ?.let { it + 1 } ?: 0
            otherSourceOfDrinkingWater = this@HouseholdFormDataset.otherSourceOfWater.value
            availabilityOfElectricity = this@HouseholdFormDataset.sourceOfElectricity.value
            availabilityOfElectricityId =
                this@HouseholdFormDataset.sourceOfElectricity.entries?.indexOf(
                    availabilityOfElectricity
                )?.let { it + 1 } ?: 0
            otherAvailabilityOfElectricity =
                this@HouseholdFormDataset.otherSourceOfElectricity.value
            availabilityOfToilet = this@HouseholdFormDataset.availOfToilet.value
            availabilityOfToiletId =
                this@HouseholdFormDataset.availOfToilet.entries?.indexOf(availabilityOfToilet)
                    ?.let { it + 1 } ?: 0
            otherAvailabilityOfToilet = this@HouseholdFormDataset.otherAvailOfToilet.value
        }
        (cacheModel as HouseholdCache).apply {
            this.amenities = amenities
        }
    }

    fun freezeHouseholdId(household: HouseholdCache, userId: Int) {
        household.householdId = getHHidFromUserId(userId)
    }


}