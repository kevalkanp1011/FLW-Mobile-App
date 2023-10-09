package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.piramalswasthya.sakhi.databinding.AlertAgePickerBinding
import org.piramalswasthya.sakhi.databinding.FragmentInputFormPageHhBinding

class AgePickerDialog () : DialogFragment() {

    private var _binding : AlertAgePickerBinding? = null

    private val binding: AlertAgePickerBinding
        get() = _binding!!

    private var yearsMin : Int = 0
    private var yearsMax : Int = 0
    private var montsMin : Int = 0
    private var monthsMax : Int = 0
    private var daysMin : Int = 0
    private var daysMax : Int = 0

    constructor(
        yearsMin: Int,
        yearsMax : Int,
        montsMin : Int,
        monthsMax : Int,
        daysMin : Int,
        daysMax : Int) : this() {
        this.yearsMin = yearsMin
        this.yearsMax = yearsMax
        this.montsMin = montsMin
        this.monthsMax = monthsMax
        this.daysMin = daysMin
        this.daysMax = daysMax
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AlertAgePickerBinding.inflate(layoutInflater, null, false)
        val alertBinding = AlertAgePickerBinding.inflate(layoutInflater, binding.root, false)
        alertBinding.dialogNumberPickerYears.minValue = yearsMin
        alertBinding.dialogNumberPickerYears.maxValue = yearsMax

        alertBinding.dialogNumberPickerMonths.minValue = montsMin
        alertBinding.dialogNumberPickerMonths.maxValue = monthsMax

        alertBinding.dialogNumberPickerDays.minValue = daysMin
        alertBinding.dialogNumberPickerDays.maxValue = daysMax

        return MaterialAlertDialogBuilder(requireContext()).setView(alertBinding.root).create()
    }

    companion object {
        const val TAG = "AgePickerDialog"
    }
}