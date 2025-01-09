package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.aadhaar_num_asha

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.activity_contracts.RDServiceCapturePIDContract
import org.piramalswasthya.sakhi.activity_contracts.RDServiceInfoContract
import org.piramalswasthya.sakhi.activity_contracts.RDServiceInitContract
import org.piramalswasthya.sakhi.databinding.FragmentAadhaarNumberAshaBinding
import org.piramalswasthya.sakhi.network.AadhaarVerifyBioRequest
import org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.AadhaarIdViewModel


@AndroidEntryPoint
class AadhaarNumberAshaFragment : Fragment() {

    private var _binding: FragmentAadhaarNumberAshaBinding? = null
    private val binding: FragmentAadhaarNumberAshaBinding
        get() = _binding!!

    private val parentViewModel: AadhaarIdViewModel by viewModels({ requireActivity() })

    private val viewModel: AadhaarNumberAshaViewModel by viewModels()

    private val aadhaarDisclaimer by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.individual_s_consent_for_creation_of_abha_number))
            .setMessage(resources.getString(R.string.aadhar_disclaimer_consent_text))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .create()
    }

    private val rdServiceCapturePIDContract =
        registerForActivityResult(RDServiceCapturePIDContract()) {
            Toast.makeText(requireContext(), "pid captured $it", Toast.LENGTH_SHORT).show()
            binding.pid.text = Gson().toJson(
                AadhaarVerifyBioRequest(
                    binding.tietAadhaarNumber.text.toString(),
                    "FMR", it.toString()
                )
            )
            viewModel.verifyBio(binding.tietAadhaarNumber.text.toString(), it)
            binding.pid.text = Gson().toJson(viewModel.responseData)
        }

    private val rdServiceDeviceInfoContract = registerForActivityResult(RDServiceInfoContract()) {
        binding.pid.text = Gson().toJson(
            AadhaarVerifyBioRequest(
                binding.tietAadhaarNumber.toString(),
                "FMR", it.toString()
            )
        )
        viewModel.verifyBio(binding.tietAadhaarNumber.text.toString(), it)
    }
    private val rdServiceInitContract = registerForActivityResult(RDServiceInitContract()) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAadhaarNumberAshaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isValidAadhaar = false

        parentViewModel.verificationType.observe(viewLifecycleOwner) {
            when (it) {
                "OTP" -> binding.btnVerifyAadhaar.text = resources.getString(R.string.generate_otp)
                "FP" -> {
                    checkApp()
                    binding.btnVerifyAadhaar.text = resources.getString(R.string.validate_fp)
                }
            }
        }

        val intent = requireActivity().intent

        val benId = intent.getLongExtra("benId", 0)
        val benRegId = intent.getLongExtra("benRegId", 0)

        if (benId > 0) {
            viewModel.getBen(benId)
        }
        binding.btnVerifyAadhaar.setOnClickListener {
            verifyAadhaar()
        }

        viewModel.ben.observe(viewLifecycleOwner) {
            it?.let {
                binding.benName.visibility = View.VISIBLE
                binding.benName.text =
                    String.format("%s%s%s", getString(R.string.generating_abha_for), " ", it)
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            if (it == AadhaarIdViewModel.State.SUCCESS) {
                viewModel.resetState()
            }
            parentViewModel.setState(it)
        }

        viewModel.mobileNumber.observe(viewLifecycleOwner) {
            it?.let {
                parentViewModel.setMobileNumber(it)
            }
        }

        viewModel.txnId.observe(viewLifecycleOwner) {
            it?.let {
                parentViewModel.setTxnId(it)
            }
        }

        binding.aadharConsentCheckBox.setOnCheckedChangeListener { _, ischecked ->
            binding.btnVerifyAadhaar.isEnabled = isValidAadhaar && ischecked
        }

        binding.aadharDisclaimer.setOnClickListener {
            aadhaarDisclaimer.show()
        }

        binding.tietAadhaarNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isValidAadhaar = (s != null) && (s.length == 12)
                binding.btnVerifyAadhaar.isEnabled = isValidAadhaar
                        && binding.aadharConsentCheckBox.isChecked
            }

        })

        // observing error message from parent and updating error text field
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvErrorText.visibility = View.VISIBLE
                binding.tvErrorText.text = it
                viewModel.resetErrorMessage()
            }
        }
    }

    private fun verifyAadhaar() {
        Toast.makeText(requireContext(), parentViewModel.verificationType.value, Toast.LENGTH_SHORT)
            .show()
        when (parentViewModel.verificationType.value) {
            "OTP" -> viewModel.generateOtpClicked(binding.tietAadhaarNumber.text.toString())
            "FP" -> rdServiceCapturePIDContract.launch(Unit)
        }
    }

    private fun checkApp() {

    }
}