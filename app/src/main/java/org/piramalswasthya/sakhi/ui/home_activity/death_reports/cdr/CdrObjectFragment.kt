package org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentCdrObjectBinding
import org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr.MdsrObjectViewModel
import org.piramalswasthya.sakhi.work.PushToD2DWorker
import timber.log.Timber

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

        binding.cdrForm.inputForm.rvInputForm.apply {
            val adapter = FormInputAdapter()
            this.adapter = adapter
            lifecycleScope.launch{
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
            val adapter = binding.cdrForm.inputForm.rvInputForm.adapter as FormInputAdapter
            viewModel.setAddress(it, adapter)
        }
        binding.btnMdsrSubmit.setOnClickListener{
            if(validate()) viewModel.submitForm()
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                CdrObjectViewModel.State.SUCCESS -> triggerCdrSendingWorker(requireContext())
                CdrObjectViewModel.State.FAIL -> Toast.makeText(context, "Saving Mdsr to database Failed!", Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    companion object {
        fun triggerCdrSendingWorker(context: Context) {
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
        val result = binding.cdrForm.inputForm.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput()
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
        else {
            if (result != null) {
                binding.cdrForm.inputForm.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }
}