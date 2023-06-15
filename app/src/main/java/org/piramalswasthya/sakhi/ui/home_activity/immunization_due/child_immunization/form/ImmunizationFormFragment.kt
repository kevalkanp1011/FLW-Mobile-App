package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.child_immunization.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentNewFormBinding

@AndroidEntryPoint
class ImmunizationFormFragment : Fragment() {
    private var _binding: FragmentNewFormBinding? = null

    private val binding: FragmentNewFormBinding
        get() = _binding!!


    private val viewModel: ImmunizationFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.btnNext.visibility = View.INVISIBLE
//        binding.btnPrev.visibility = View.INVISIBLE
//        binding.btnSubmitForm.visibility = View.VISIBLE
        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        viewModel.recordExists.observe(viewLifecycleOwner) { notIt ->
            notIt?.let { recordExists ->
//                binding.fabEdit.visibility = if(recordExists) View.VISIBLE else View.GONE
                val adapter = FormInputAdapter(
                    formValueListener = FormInputAdapter.FormValueListener { formId, index ->
                        viewModel.updateListOnValueChanged(formId, index)
                    },
                    isEnabled = !recordExists
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
    }


}