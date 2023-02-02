package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.MDSRCache

class MDSRFormDataset(context: Context, private val mdsr: MDSRCache? = null) {

    private val dateOfDeath = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Date of death ",
        required = false
    )
    private val address = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Address",
        required = false
    )
    private val husbandName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Husband’s Name",
        required = false
    )
    private val causeOfDeath = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Cause of death",
        required = false
    )
    private val investigationDate = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Date of field investigation",
        required = false
    )
    private val actionTaken = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Action Take",
        required = false
    )
    private val blockMOSign = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Signature of MO I/C of the block",
        required = false
    )
    private val date = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Date",
        required = false
    )
}