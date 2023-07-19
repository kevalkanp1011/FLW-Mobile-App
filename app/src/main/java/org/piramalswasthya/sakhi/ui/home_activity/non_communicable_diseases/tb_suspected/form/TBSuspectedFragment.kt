package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_diseases.tb_suspected.form

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
class TBSuspectedFragment : Fragment() {

    private var _binding: FragmentNewFormBinding? = null
    private val binding: FragmentNewFormBinding
        get() = _binding!!

    private val viewModel: TBSuspectedViewModel by viewModels()

//    private val tbSuspectedAlert by lazy {
//        AlertDialog.Builder(requireContext())
//            .setTitle("TB Screening")
//            .setMessage("it")
//            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
//            .create()
//    }
//
//    private val tbSuspectedFamilyAlert by lazy {
//        AlertDialog.Builder(requireContext())
//            .setTitle("TB Screening")
//            .setMessage("it")
//            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
//            .create()
//    }

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
                val adapter = FormInputAdapter(
                    formValueListener = FormInputAdapter.FormValueListener { formId, index ->
                        viewModel.updateListOnValueChanged(formId, index)
                    }, isEnabled = !recordExists
                )
                binding.btnSubmit.isEnabled = !recordExists
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
            submitTBSuspectedForm()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                TBSuspectedViewModel.State.SAVE_SUCCESS -> {
                    findNavController().navigateUp()
                }

                else -> {}
            }
        }
    }

    private fun submitTBSuspectedForm() {
        if (validateCurrentPage()) {
//            showAlerts()
            viewModel.saveForm()
        }
    }

//    private fun showAlerts() {
//        viewModel.getAlerts()
//        viewModel.suspectedTB?.let {
//            tbSuspectedAlert.setMessage(it)
//            tbSuspectedAlert.show()
//        }
//
//        viewModel.suspectedTBFamily?.let {
//            tbSuspectedFamilyAlert.setMessage(it)
//            tbSuspectedFamilyAlert.show()
//        }
//
//    }

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

}