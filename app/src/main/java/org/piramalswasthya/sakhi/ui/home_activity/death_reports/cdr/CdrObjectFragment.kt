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
import timber.log.Timber
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf.Visibility

@AndroidEntryPoint
class CdrObjectFragment : Fragment() {

    private val binding by lazy {
        FragmentCdrObjectBinding.inflate(layoutInflater)
    }

    private val viewModel: CdrObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cdrForm.rvInputForm.apply {
            val adapter = FormInputAdapter()
            this.adapter = adapter
            lifecycleScope.launch {
                adapter.submitList(viewModel.getFirstPage(adapter))
            }
        }

        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        viewModel.address.observe(viewLifecycleOwner) {
            val adapter = binding.cdrForm.rvInputForm.adapter as FormInputAdapter
            viewModel.setAddress(it, adapter)
        }
        binding.btnCdrSubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
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
                    triggerCdrSendingWorker(requireContext())
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

    companion object {
        private fun triggerCdrSendingWorker(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<PushToD2DWorker>()
                .setConstraints(PushToD2DWorker.constraint)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    PushToD2DWorker.name,
                    ExistingWorkPolicy.APPEND_OR_REPLACE,
                    workRequest
                )
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
}