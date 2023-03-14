package org.piramalswasthya.sakhi.ui.home_activity.mother_care.fpot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentFpotBinding
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.pmjay.PmjayViewModel
import org.piramalswasthya.sakhi.work.WorkerUtils
import timber.log.Timber

@AndroidEntryPoint
class FpotFragment : Fragment() {

    private var _binding : FragmentFpotBinding? = null
    private val binding : FragmentFpotBinding
        get() = _binding!!

    private val viewModel: FpotViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFpotBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        binding.btnFpotSubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
        }
        viewModel.exists.observe(viewLifecycleOwner) {exists ->
            val adapter = FormInputAdapter(isEnabled = !exists)
            binding.fpotForm.rvInputForm.adapter = adapter
            if (exists) {
                binding.btnFpotSubmit.visibility = View.GONE
                viewModel.setExistingValues()
            }
            else {
                viewModel.address.observe(viewLifecycleOwner) {
                    viewModel.setAddress(it, adapter)
                }
            }
            lifecycleScope.launch {
                adapter.submitList(viewModel.getFirstPage())
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                FpotViewModel.State.LOADING -> {
                    binding.fpotForm.rvInputForm.visibility = View.GONE
                    binding.btnFpotSubmit.visibility = View.GONE
                    binding.cvPatientInformation.visibility = View.GONE
                    binding.pbFpot.visibility = View.VISIBLE
                }
                FpotViewModel.State.SUCCESS -> {
                    findNavController().navigateUp()
                    WorkerUtils.triggerD2dSyncWorker(requireContext())
                }
                FpotViewModel.State.FAIL -> {
                    binding.fpotForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnFpotSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbFpot.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Saving Mdsr to database Failed!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.fpotForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnFpotSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbFpot.visibility = View.GONE
                }
            }
        }
    }

    fun validate(): Boolean {
//        val result = binding.pmjayForm.rvInputForm.adapter?.let {
//            (it as FormInputAdapter).validateInput()
//        }
//        Timber.d("Validation : $result")
//        return if (result == -1)
//            true
//        else {
//            if (result != null) {
//                binding.pmjayForm.rvInputForm.scrollToPosition(result)
//            }
//            false
//        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}