package org.piramalswasthya.sakhi.configuration
/*

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HouseholdCache
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class BenKidFormDataset (context: Context, private val ben: BenRegCache?= null) {
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

        val firstPage by lazy {
            listOf(
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_first_name_hof),
                    value = household?.familyHeadName,
                    required = true
                ),
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_last_name_hof),
                    required = false
                ),
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_mob_no_hof),
                    required = true,
                    etLength = 10,
                    etInputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                ),
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_house_no),
                    required = false
                ),
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_ward_no),
                    required = false
                ),
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_ward_name),
                    required = false
                ),
                FormInput(
                    inputType = FormInput.InputType.EDIT_TEXT,
                    title = context.getString(R.string.nhhr_mohalla_name),
                    required = false
                ),
                FormInput(
                    inputType = FormInput.InputType.DROPDOWN,
                    title = context.getString(R.string.nhhr_poverty_line),
                    list = listOf(
                        "APL",
                        "BPL",
                        "Don't Know"
                    ),
                    required = true
                )
            )
        }

        val secondPage by lazy {
            listOf(
                FormInput(
                    inputType = FormInput.InputType.DROPDOWN,
                    title = context.getString(R.string.nhhr_type_residential_area),
                    list = listOf(
                        "Rural",
                        "Urban",
                        "Tribal",
                        "Other"
                    ),
                    required = false,
                    hiddenFieldTrigger = "Other",
                    hiddenField = FormInput(
                        inputType = FormInput.InputType.EDIT_TEXT,
                        title = "Other type of residential area",
                        required = false
                    )
                ),
                FormInput(
                    inputType = FormInput.InputType.DROPDOWN,
                    title = context.getString(R.string.nhhr_type_of_house),
                    list = listOf(
                        "None",
                        "Kuchcha",
                        "Pucca",
                        "Other"
                    ),
                    required = true
                ),
                FormInput(
                    inputType = FormInput.InputType.DROPDOWN,
                    title = context.getString(R.string.nhhr_house_own),
                    listOf(
                        "Yes",
                        "No"
                    ),
                    required = true
                )
            )
        }

        val thirdPage by lazy {
            listOf(
                FormInput(
                    inputType = FormInput.InputType.DROPDOWN,
                    title = context.getString(R.string.nhhr_separate_kitchen),
                    list = listOf(
                        "Yes",
                        "No"
                    ),
                    required = true
                ),
                FormInput(
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
                    hiddenFieldTrigger = "Other",
                    hiddenField = FormInput(
                        inputType = FormInput.InputType.EDIT_TEXT,
                        title = "Other Type of fuel used for Cooking",
                        required = true
                    )
                ),
                FormInput(
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
                    hiddenFieldTrigger = "Other",
                    hiddenField = FormInput(
                        inputType = FormInput.InputType.EDIT_TEXT,
                        title = "Other Primary Source of Water",
                        required = true
                    )

                ),
                FormInput(
                    inputType = FormInput.InputType.DROPDOWN,
                    title = context.getString(R.string.nhhr_avail_electricity),
                    list = listOf(
                        "Electricity Supply",
                        "Generator",
                        "Solar Power",
                        "Kerosene Lamp",
                        "Other",
                    ),
                    required = true,
                    hiddenFieldTrigger = "Other",
                    hiddenField = FormInput(
                        inputType = FormInput.InputType.EDIT_TEXT,
                        title = "Other availability of Electricity",
                        required = true
                    )

                ),
                FormInput(
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
                    hiddenFieldTrigger = "Other",
                    hiddenField = FormInput(
                        inputType = FormInput.InputType.EDIT_TEXT,
                        title = "Other Availability of Toilet",
                        required = true
                    )

                )
            )
        }

        fun getHouseholdForFirstPage(userId: Int, hhId : Long): HouseholdCache {

            val household = HouseholdCache(
                householdId = hhId,
                ashaId = userId,
                isDraft = true
            )
            household.apply {
                familyHeadName = firstPage[0].value
                familyName = firstPage[1].value
                familyHeadPhoneNo = firstPage[2].value?.toLong()
                houseNo = firstPage[3].value
                wardNo = firstPage[4].value
                wardName = firstPage[5].value
                mohallaName = firstPage[6].value
                povertyLine = firstPage[7].value
            }
            return household
        }

        fun getBenForSecondPage(userId: Int, hhId: Long): HouseholdCache {

            val household = getHouseholdForFirstPage(userId,hhId)
            household.apply {
                residentialArea = secondPage[0].value
                otherResidentialArea = secondPage[0].hiddenField?.value
                houseType = secondPage[1].value
                isHouseOwned = secondPage[2].value
            }

            return household
        }

        fun getHouseholdForThirdPage(userId: Int,hhId: Long) : HouseholdCache {
            val household = getBenForSecondPage(userId,hhId)
            household.apply {
                separateKitchen = thirdPage[0].value
                fuelUsed = thirdPage[1].value
                otherFuelUsed = thirdPage[1].hiddenField?.value
                sourceOfDrinkingWater = thirdPage[2].value
                otherSourceOfDrinkingWater = thirdPage[2].hiddenField?.value
                availabilityOfElectricity = thirdPage[3].value
                otherAvailabilityOfElectricity = thirdPage[3].hiddenField?.value
                availabilityOfToilet = thirdPage[4].value
                otherAvailabilityOfToilet = thirdPage[4].hiddenField?.value
                isDraft = false
            }
            return household
        }
}*/
