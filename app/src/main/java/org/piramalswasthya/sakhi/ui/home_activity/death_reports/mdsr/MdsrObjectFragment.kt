package org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentMdsrObjectBinding
import org.piramalswasthya.sakhi.work.PushToAmritWorker
import org.piramalswasthya.sakhi.work.PushToD2DWorker
import org.piramalswasthya.sakhi.work.WorkerUtils
import timber.log.Timber

@AndroidEntryPoint
class MdsrObjectFragment : Fragment() {

    private val binding by lazy {
        FragmentMdsrObjectBinding.inflate(layoutInflater)
    }

    private val viewModel: MdsrObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.btnMdsrSubmit.setOnClickListener{
            if(validate()) viewModel.submitForm()
        }
        viewModel.exists.observe(viewLifecycleOwner) { exists ->
            val adapter = FormInputAdapter(isEnabled = !exists)
            binding.mdsrForm.inputForm.rvInputForm.adapter = adapter
            if (exists) {
                binding.btnMdsrSubmit.visibility = View.GONE
//                binding.mdsrForm.inputForm.rvInputForm.apply {
//                    isClickable = false
//                    isFocusable = false
//                }
                viewModel.setExistingValues()
            } else {

                viewModel.address.observe(viewLifecycleOwner) {
                    viewModel.setAddress(it, adapter)
                }
            }
            lifecycleScope.launch{
                adapter.submitList(viewModel.getFirstPage(adapter))
            }
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                MdsrObjectViewModel.State.LOADING -> {
                    binding.cvPatientInformation.visibility = View.GONE
                    binding.mdsrForm.inputForm.rvInputForm.visibility = View.GONE
                    binding.btnMdsrSubmit.visibility = View.GONE
                    binding.pbMdsr.visibility = View.VISIBLE
                }
                MdsrObjectViewModel.State.SUCCESS -> {
                    findNavController().navigateUp()
                    WorkerUtils.triggerD2dSyncWorker(requireContext())
                }
                MdsrObjectViewModel.State.FAIL -> {
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.mdsrForm.inputForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnMdsrSubmit.visibility = View.VISIBLE
                    binding.pbMdsr.visibility = View.GONE
                    Toast.makeText(context, "Saving Mdsr to database Failed!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.mdsrForm.inputForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnMdsrSubmit.visibility = View.VISIBLE
                    binding.pbMdsr.visibility = View.GONE
                }
            }
        }
    }



    fun validate(): Boolean {
//        Timber.d("binding $binding rv ${binding.nhhrForm.rvInputForm} adapter ${binding.nhhrForm.rvInputForm.adapter}")
//        return false
        val result = binding.mdsrForm.inputForm.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput()
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
        else {
            if (result != null) {
                binding.mdsrForm.inputForm.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }
}