package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.aadhaar_num_gov

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentAadhaarNumberGovBinding
import org.piramalswasthya.sakhi.network.StateCodeResponse
import java.util.*

@AndroidEntryPoint
class AadhaarNumberGovFragment: Fragment() {
    private var _binding: FragmentAadhaarNumberGovBinding? = null
    private val binding: FragmentAadhaarNumberGovBinding
        get() = _binding!!

    private val viewModel: AadhaarNumberGovViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAadhaarNumberGovBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observing and setting values for state code dropdown
        viewModel.stateCodes.observe(viewLifecycleOwner){stateCodes ->
            stateCodes?.map { res -> res.name }?.toTypedArray()?.let {
                val adapterStateCodes = ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, it)
                binding.actvStateDn.setAdapter(adapterStateCodes)
            }
        }

        // setting district code dropdown values
        binding.actvStateDn.setOnItemClickListener {
            _, _, index, _ ->
            run {
                viewModel.activeState =
                    viewModel.stateCodes.value?.get(index)
                viewModel.activeState?.districts?.map { dt -> dt.name }?.toTypedArray()?.let {
                    val adapterDistrictCodes = ArrayAdapter(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, it)
                    binding.actvDistrictDn.setAdapter(adapterDistrictCodes)
                }
            }
        }

        // observing district dropdown
        binding.actvDistrictDn.setOnItemClickListener {
            _, _, index, _ ->
            run {
                viewModel.activeDistrict = viewModel.activeState?.districts?.get(index)
            }
        }

        // setting date picker values
        val today = Calendar.getInstance()

        binding.et.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                it.context,
                { _, year, month, day ->
                    binding.et.setText(
                        "${if (day > 9) day else "0$day"}-${if (month > 8) month + 1 else "0${month + 1}"}-$year"
                    )
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
            )
            binding.tilEditText.error = null
            datePickerDialog.show()
        }

        // generating abha
        binding.btnGenerateAbha.setOnClickListener{
            viewModel.generateAbha()
        }
    }
}