package org.piramalswasthya.sakhi.ui.home_activity.mother_care.pmjay

import androidx.lifecycle.ViewModelProvider
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
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentCdrObjectBinding
import org.piramalswasthya.sakhi.databinding.FragmentPmjayBinding
import org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr.CdrObjectViewModel
import org.piramalswasthya.sakhi.work.WorkerUtils
import timber.log.Timber

@AndroidEntryPoint
class PmjayFragment : Fragment() {

    private var _binding : FragmentPmjayBinding? = null
    private val binding : FragmentPmjayBinding
        get() = _binding!!

    private val viewModel: PmjayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPmjayBinding.inflate(layoutInflater, container, false)
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
        binding.btnPmjaySubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
        }
        viewModel.exists.observe(viewLifecycleOwner) {exists ->
            val adapter = FormInputAdapter(isEnabled = !exists)
            binding.pmjayForm.rvInputForm.adapter = adapter
            if (exists) {
                binding.btnPmjaySubmit.visibility = View.GONE
//                binding.cdrForm.rvInputForm.apply {
//                    isClickable = false
//                    isFocusable = false
//                }
                viewModel.setExistingValues()
            }
            else {
                viewModel.address.observe(viewLifecycleOwner) {
                    viewModel.setAddress(it, adapter)
                }
            }
            lifecycleScope.launch {
                adapter.submitList(viewModel.getFirstPage(adapter))
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                PmjayViewModel.State.LOADING -> {
                    binding.pmjayForm.rvInputForm.visibility = View.GONE
                    binding.btnPmjaySubmit.visibility = View.GONE
                    binding.cvPatientInformation.visibility = View.GONE
                    binding.pbPmjay.visibility = View.VISIBLE
                }
                PmjayViewModel.State.SUCCESS -> {
                    findNavController().navigateUp()
                    WorkerUtils.triggerD2dSyncWorker(requireContext())
                }
                PmjayViewModel.State.FAIL -> {
                    binding.pmjayForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnPmjaySubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbPmjay.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Saving Mdsr to database Failed!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.pmjayForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnPmjaySubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbPmjay.visibility = View.GONE
                }
            }
        }
    }


    fun validate(): Boolean {
//        Timber.d("binding $binding rv ${binding.nhhrForm.rvInputForm} adapter ${binding.nhhrForm.rvInputForm.adapter}")
//        return false
        val result = binding.pmjayForm.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput()
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
        else {
            if (result != null) {
                binding.pmjayForm.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}