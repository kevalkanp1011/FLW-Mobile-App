package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple.eligible_couple_reg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class EligibleCoupleRegFragment : Fragment() {

    private var _binding: FragmentNewFormBinding? = null
    private val binding: FragmentNewFormBinding
        get() = _binding!!


    private val viewModel: EligibleCoupleRegViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewFormBinding.inflate(layoutInflater, container, false)
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
                        if (it.isNotEmpty())

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
            submitEligibleCoupleForm()
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                EligibleCoupleRegViewModel.State.SAVE_SUCCESS -> {
                    findNavController().navigateUp()
                }

                else -> {}
            }
        }
    }

    private fun submitEligibleCoupleForm() {
        if (validate()) {
            viewModel.saveForm()
        }
    }

    fun validate(): Boolean {
        val result = binding.form.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput(resources)
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
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
                17 -> {
                    notifyItemChanged(18)
                    notifyItemChanged(20)
                }
                22 -> {
                    notifyItemChanged(23)
                    notifyItemChanged(25)
                }
                27 -> {
                    notifyItemChanged(28)
                    notifyItemChanged(30)
                }
                32 -> {
                    notifyItemChanged(33)
                    notifyItemChanged(35)
                }
                37 -> {
                    notifyItemChanged(38)
                    notifyItemChanged(40)
                }
                42 -> {
                    notifyItemChanged(43)
                    notifyItemChanged(44)
                }
                47 -> {
                    notifyItemChanged(48)
                    notifyItemChanged(50)
                }
                52 -> {
                    notifyItemChanged(53)
                    notifyItemChanged(55)
                }
                57 -> {
                    notifyItemChanged(58)
                    notifyItemChanged(60)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}