package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple.tracking.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentNewFormBinding
import timber.log.Timber

@AndroidEntryPoint
class EligibleCoupleTrackingFormFragment : Fragment() {

    private var _binding: FragmentNewFormBinding? = null
    private val binding: FragmentNewFormBinding
        get() = _binding!!

    private val viewModel: EligibleCoupleTrackingFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.recordExists.observe(viewLifecycleOwner) { notIt ->
            notIt?.let { recordExists ->
//                binding.fabEdit.visibility = if(recordExists) View.VISIBLE else View.GONE
                val adapter = FormInputAdapter(
                    formValueListener = FormInputAdapter.FormValueListener { formId, index ->
                        viewModel.updateListOnValueChanged(formId, index)
                        hardCodedListUpdate(formId)
                    }, isEnabled = !recordExists
                )
                binding.form.rvInputForm.adapter = adapter
                lifecycleScope.launch {
                    viewModel.formList.collect {
                        adapter.submitList(it)

                    }
                }
            }
        }
        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        binding.btnSubmit.setOnClickListener {
            submitEligibleTrackingForm()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                EligibleCoupleTrackingFormViewModel.State.SAVE_SUCCESS -> {
                    if (viewModel.isPregnant) {
                        findNavController().navigate(
                            EligibleCoupleTrackingFormFragmentDirections.actionEligibleCoupleTrackingFormFragmentToPregnancyRegistrationFormFragment(benId = viewModel.benId)
                        )
                        viewModel.resetState()
                    } else {
                        findNavController().navigate(
                            EligibleCoupleTrackingFormFragmentDirections.actionEligibleCoupleTrackingFormFragmentToEligibleCoupleTrackingListFragment()
                        )
                        Toast.makeText(requireContext(), "Tracking form filled successfully", Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                    }
                }
                else -> {}
            }
        }
    }

    private fun submitEligibleTrackingForm() {
        if (validateCurrentPage()) {
            viewModel.saveForm()
        }
    }

    private fun validateCurrentPage(): Boolean {
        val result = binding.form.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput(resources)
        }
        Timber.d("Validation : $result")
        return if (result == -1) true
        else {
            if (result != null) {
                binding.form.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }


    private fun hardCodedListUpdate(formId: Int) {
        binding.form.rvInputForm.adapter?.apply {
            when (formId) {
                7 -> {
//                    notifyItemChanged(viewModel.getIndexOfEdd())
//                    notifyItemChanged(viewModel.getIndexOfWeeksOfPregnancy())
                }
//                19 -> notifyItemChanged(viewModel.getIndexOfPastIllness())
            }
        }
    }

}