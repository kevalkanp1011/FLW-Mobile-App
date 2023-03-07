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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.AlertConsentBinding
import org.piramalswasthya.sakhi.databinding.FragmentPmsmaBinding
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.pmsma.PmsmaViewModel.State
import org.piramalswasthya.sakhi.work.PushToD2DWorker
import timber.log.Timber

@AndroidEntryPoint
class PmsmaFragment : Fragment() {

    private var _binding : FragmentPmsmaBinding? = null
    private val binding : FragmentPmsmaBinding
        get() = _binding!!


    private val viewModel: PmsmaViewModel by viewModels()
    private val errorAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Alert")
            //.setMessage("Do you want to continue with previous form, or create a new form and discard the previous form?")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
    private val consentAlert by lazy {
        val alertBinding = AlertConsentBinding.inflate(layoutInflater,binding.root,false)
        alertBinding.textView4.text = getString(R.string.consent_alert_title)
        alertBinding.checkBox.text = getString(R.string.consent_text)
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(alertBinding.root)
            .setCancelable(false)
            .create()
        alertBinding.btnNegative.setOnClickListener {
            alertDialog.dismiss()
            findNavController().navigateUp()
        }
        alertBinding.btnPositive.setOnClickListener {
            if(alertBinding.checkBox.isChecked) {
                alertDialog.dismiss()
                alertBinding.checkBox.isChecked = false
            }
            else
                Toast.makeText(context,"Please tick the checkbox", Toast.LENGTH_SHORT).show()
        }
        alertDialog
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView called!!")
        _binding = FragmentPmsmaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated called!!")
        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        binding.btnPmsmaSubmit.setOnClickListener {
            if (validate()) viewModel.submitForm()
        }
        viewModel.exists.observe(viewLifecycleOwner) {exists ->
            Timber.d("observing exists : $exists")
            val adapter = FormInputAdapter(isEnabled = !exists)
            binding.pmsmaForm.rvInputForm.adapter = adapter
            if (exists) {
                binding.btnPmsmaSubmit.visibility = View.GONE
//                binding.cdrForm.rvInputForm.apply {
//                    isClickable = false
//                    isFocusable = false
//                }
                viewModel.setExistingValues()
            }
            else {
                consentAlert.show()
                viewModel.address.observe(viewLifecycleOwner) {
                    viewModel.setAddress(it, adapter)
                }
            }
            lifecycleScope.launch {
                adapter.submitList(viewModel.getFirstPage(adapter))
            }
        }
        viewModel.popupString.observe(viewLifecycleOwner){
            it?.let {
                errorAlert.setMessage(it)
                errorAlert.show()
                viewModel.resetPopUpString()
            }

        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                State.LOADING -> {
                    binding.pmsmaForm.rvInputForm.visibility = View.GONE
                    binding.btnPmsmaSubmit.visibility = View.GONE
                    binding.cvPatientInformation.visibility = View.GONE
                    binding.pbPmsma.visibility = View.VISIBLE
                }
                State.SUCCESS -> {
                    triggerPmsmaSendingWorker(requireContext())
                    findNavController().navigateUp()
                }
                State.FAIL -> {
                    binding.pmsmaForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnPmsmaSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbPmsma.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Saving pmsma to database Failed!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.pmsmaForm.rvInputForm.visibility = View.VISIBLE
                    binding.btnPmsmaSubmit.visibility = View.VISIBLE
                    binding.cvPatientInformation.visibility = View.VISIBLE
                    binding.pbPmsma.visibility = View.GONE
                }
            }
        }

        Timber.d("onViewCreated completed!!")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}