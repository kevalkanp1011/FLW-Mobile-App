package org.piramalswasthya.sakhi.ui.home_activity.mother_care.pmsma

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
import org.piramalswasthya.sakhi.databinding.FragmentPmsmaBinding
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.pmsma.PmsmaViewModel.State
import org.piramalswasthya.sakhi.work.PushToD2DWorker
import timber.log.Timber


@AndroidEntryPoint
class PmsmaFragment : Fragment() {

    private val binding by lazy {
        FragmentPmsmaBinding.inflate(layoutInflater)
    }

    private val viewModel: PmsmaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pmsmaForm.rvInputForm.apply {
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
            val adapter = binding.pmsmaForm.rvInputForm.adapter as FormInputAdapter
            viewModel.setAddress(it, adapter)
        }
        binding.btnPmsmaSubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                State.SUCCESS -> {
                    triggerPmsmaSendingWorker(
                        requireContext()
                    )
                    findNavController().navigateUp()
                }
                State.FAIL -> Toast.makeText(
                    context,
                    "Saving pmsma to database Failed!",
                    Toast.LENGTH_LONG
                ).show()
                else -> {}
            }
        }

    }

    companion object {
        private fun triggerPmsmaSendingWorker(context: Context) {
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

        val result = binding.pmsmaForm.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput()
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
        else {
            if (result != null) {
                binding.pmsmaForm.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }
}