package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_object

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentImmunizationObjectBinding
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ImmunizationObjectFragment : Fragment() {

    private val binding by lazy { FragmentImmunizationObjectBinding.inflate(layoutInflater) }

    private val viewModel: ImmunizationObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = Date()
        val formatter = SimpleDateFormat("dd/MM/yy")
        val now = formatter.format(date)
//        binding.benNameTv.text = viewModel.getBenName()
//        binding.ageGenderTv.text = viewModel.getAgeGender()
//        binding.etMothername.setText(viewModel.ben?.motherName?: "Not Available")
//        binding.etDob.setText(getDateTimeStringFromLong(viewModel.ben?.dob)?: "Not Available")
//        binding.etLastVaccineDates.setText(getDateTimeStringFromLong(0)?: "Not Available")
//        binding.etLastVaccineDates.setText(now?: "Not Available")
//        binding.etVaccineName.setText(viewModel.vaccine)
//        binding.etDoseNum.setText(viewModel.dosage)

    }

    fun getDateTimeStringFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        dateLong?.let {
            val dateString = dateFormat.format(dateLong)
            val timeString = timeFormat.format(dateLong)
            return "${dateString}T${timeString}.000Z"
        } ?: run {
            return null
        }
    }
}