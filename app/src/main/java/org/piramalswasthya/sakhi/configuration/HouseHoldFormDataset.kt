package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.text.InputType
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.FormInput

class HouseHoldFormDataset {

    private lateinit var firstPage: MutableList<FormInput>

    private lateinit var secondPage: MutableList<FormInput>

    private lateinit var thirdPage: MutableList<FormInput>

    fun getFirstPage(context: Context): List<FormInput> {
        firstPage = mutableListOf(
            FormInput(
                inputType = FormInput.InputType.EDIT_TEXT,
                title = context.getString(R.string.nhhr_first_name_hof),
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
        return firstPage
    }

    fun getSecondPage(context: Context): List<FormInput> {
        secondPage = mutableListOf(
            FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = context.getString(R.string.nhhr_type_residential_area),
                list = listOf(
                    "Rural",
                    "Urban",
                    "Tribal",
                    "Other"
                ),
                required = false
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
        return secondPage
    }

    fun getThirdPage(context: Context): List<FormInput> {
        thirdPage = mutableListOf(
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
                required = true
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
                required = true
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
                required = true
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
                required = true
            )
        )
        return thirdPage
    }
}