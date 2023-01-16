package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.FormInput

object HouseHoldFormDataset {

    private lateinit var firstPage: MutableList<FormInput>

    private lateinit var secondPage: MutableList<FormInput>

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
                required = true
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
}