package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

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
import org.piramalswasthya.sakhi.databinding.FragmentHbncBinding
import org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form.HbncViewModel.*
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.fpot.FpotViewModel
import org.piramalswasthya.sakhi.work.WorkerUtils

@AndroidEntryPoint
class HbncFragment : Fragment() {


    private var _binding : FragmentHbncBinding? = null
    private val binding : FragmentHbncBinding
        get() = _binding!!

    private val viewModel: HbncViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHbncBinding.inflate(layoutInflater, container, false)
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
        binding.btnHbncSubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
        }
        viewModel.exists.observe(viewLifecycleOwner) {exists ->
            val adapter = FormInputAdapter(isEnabled = !exists)
            binding.hbncForm.rvInputForm.adapter = adapter
            if (exists) {
                binding.btnHbncSubmit.visibility = View.GONE
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
                State.LOADING -> {
                    binding.hbncForm.rvInputForm.visibility = View.GONE
                    binding.btnHbncSubmit.visibility = View.GONE
                    binding.cvPatientInformation.visibility = View.GONE
                    binding.pbHbnc.visibility = View.VISIBLE
                }
                State.SUCCESS -> {
                    findNavController().navigateUp()
                    WorkerUtils.triggerD2dSyncWorker(requireContext())
                }
                State.FAIL -> {
                    binding.hbncForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnHbncSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbHbnc.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Saving Mdsr to database Failed!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.hbncForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnHbncSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbHbnc.visibility = View.GONE
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