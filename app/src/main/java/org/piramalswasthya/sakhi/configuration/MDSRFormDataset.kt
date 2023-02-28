package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.widget.LinearLayout
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.MDSRCache
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class MDSRFormDataset(context: Context) {

    companion object {
        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val mdFormat =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }

        private fun getLongFromDate(dateString: String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
        }
    }

    fun mapValues(mdsrCache: MDSRCache) {
        mdsrCache.dateOfDeath = getLongFromDate(dateOfDeath.value.value!!)
        mdsrCache.address = address.value.value
        mdsrCache.husbandName = husbandName.value.value
        mdsrCache.causeOfDeath = if(causeOfDeath.value.value == "Maternal") 1 else 2
        mdsrCache.reasonOfDeath = reasonOfDeath.value.value
        mdsrCache.actionTaken = if(actionTaken.value.value == "Yes") 1 else 2
        mdsrCache.investigationDate = getLongFromDate(investigationDate.value.value!!)
        mdsrCache.blockMOSign = blockMOSign.value.value
        mdsrCache.date = getLongFromDate(date.value.value!!)
        mdsrCache.createdDate = System.currentTimeMillis()
    }

    private var mdsr: MDSRCache? = null

    constructor(context: Context, mdsr: MDSRCache? = null) : this(context) {
        this.mdsr = mdsr
        //TODO(SETUP THE VALUES)
    }

    private val dateOfDeath = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Date of death ",
        min = 0L,
        max = System.currentTimeMillis(),
        required = true
    )
    val address = FormInput(
        inputType = FormInput.InputType.TEXT_VIEW,
        title = "Address",
        required = false
    )
    private val husbandName = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Husband’s Name",
        required = false
    )
    val causeOfDeath = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Cause of death",
        required = true,
        orientation = LinearLayout.VERTICAL,
        list = arrayOf("Maternal", "Non-maternal")
    )
    val reasonOfDeath = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "specify Reason",
        required = true
    )
    private val investigationDate = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        title = "Date of field investigation",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val actionTaken = FormInput(
        inputType = FormInput.InputType.RADIO,
        title = "Action Take",
        required = false,
        orientation = LinearLayout.VERTICAL,
        list = arrayOf("Yes", "No")
    )
    private val blockMOSign = FormInput(
        inputType = FormInput.InputType.EDIT_TEXT,
        title = "Signature of MO I/C of the block",
        required = false
    )
    private val date = FormInput(
        inputType = FormInput.InputType.DATE_PICKER,
        min = 0L,
        max = System.currentTimeMillis(),
        title = "Date",
        required = false
    )

    val firstPage by lazy {
        listOf(dateOfDeath, address, husbandName, causeOfDeath, investigationDate, actionTaken,
            blockMOSign, date)
    }
}