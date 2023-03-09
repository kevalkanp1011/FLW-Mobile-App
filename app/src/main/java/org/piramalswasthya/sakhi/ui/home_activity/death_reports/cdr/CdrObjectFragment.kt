package org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentCdrObjectBinding
import org.piramalswasthya.sakhi.work.PushToD2DWorker
import org.piramalswasthya.sakhi.work.WorkerUtils
import timber.log.Timber
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf.Visibility

@AndroidEntryPoint
class CdrObjectFragment : Fragment() {

    private var _binding : FragmentCdrObjectBinding? = null
    private val binding : FragmentCdrObjectBinding
        get() = _binding!!

    private val viewModel: CdrObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCdrObjectBinding.inflate(layoutInflater, container, false)
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
        binding.btnCdrSubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
        }
        viewModel.exists.observe(viewLifecycleOwner) {exists ->
            val adapter = FormInputAdapter(isEnabled = !exists)
            binding.cdrForm.rvInputForm.adapter = adapter
            if (exists) {
                binding.btnCdrSubmit.visibility = View.GONE
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
                CdrObjectViewModel.State.LOADING -> {
                    binding.cdrForm.rvInputForm.visibility = View.GONE
                    binding.btnCdrSubmit.visibility = View.GONE
                    binding.cvPatientInformation.visibility = View.GONE
                    binding.pbCdr.visibility = View.VISIBLE
                }
                CdrObjectViewModel.State.SUCCESS -> {
                    findNavController().navigateUp()
                    WorkerUtils.triggerD2dSyncWorker(requireContext())
                }
                CdrObjectViewModel.State.FAIL -> {
                    binding.cdrForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnCdrSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbCdr.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Saving Mdsr to database Failed!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.cdrForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnCdrSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbCdr.visibility = View.GONE
                }
            }
        }
    }


    fun validate(): Boolean {
//        Timber.d("binding $binding rv ${binding.nhhrForm.rvInputForm} adapter ${binding.nhhrForm.rvInputForm.adapter}")
//        return false
        val result = binding.cdrForm.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput()
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
        else {
            if (result != null) {
                binding.cdrForm.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}