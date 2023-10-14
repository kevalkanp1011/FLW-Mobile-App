package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.piramalswasthya.sakhi.databinding.AlertAgePickerBinding
import org.piramalswasthya.sakhi.databinding.FragmentInputFormPageHhBinding

class AgePickerDialog (context: Context) : AlertDialog(context) {

    private var _binding : AlertAgePickerBinding? = null

    private val binding: AlertAgePickerBinding
        get() = _binding!!

    private var yearsMin : Int = 0
    private var yearsMax : Int = 0
    private var montsMin : Int = 0
    private var monthsMax : Int = 0
    private var daysMin : Int = 0
    private var daysMax : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = AlertAgePickerBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
    }

    fun  setLimitsAndShow(yearsMin: Int,
                  yearsMax : Int,
                  montsMin : Int,
                  monthsMax : Int,
                  daysMin : Int,
                  daysMax : Int) {
        this.yearsMin = yearsMin
        this.yearsMax = yearsMax
        this.montsMin = montsMin
        this.monthsMax = monthsMax
        this.daysMin = daysMin
        this.daysMax = daysMax
        show()
    }
    override fun show() {
        super.show()
        binding.dialogNumberPickerYears.minValue = yearsMin
        binding.dialogNumberPickerYears.maxValue = yearsMax

        binding.dialogNumberPickerMonths.minValue = montsMin
        binding.dialogNumberPickerMonths.maxValue = monthsMax

        binding.dialogNumberPickerDays.minValue = daysMin
        binding.dialogNumberPickerDays.maxValue = daysMax
    }
    companion object {
        const val TAG = "AgePickerDialog"
    }
}