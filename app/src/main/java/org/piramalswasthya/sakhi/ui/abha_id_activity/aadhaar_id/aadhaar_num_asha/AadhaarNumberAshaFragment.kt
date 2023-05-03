package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.aadhaar_num_asha

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentAadhaarNumberAshaBinding
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
            .setTitle("Individual’s consent for creation of ABHA Number.")
            .setMessage(context?.getString(R.string.aadhar_disclaimer_consent_text))
            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            .create()
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

        binding.btnGenerateOtp.setOnClickListener {
            viewModel.generateOtpClicked(binding.tietAadhaarNumber.text.toString())

            viewModel.state.observe(viewLifecycleOwner) {
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
        }

        binding.aadharConsentCheckBox.setOnCheckedChangeListener{ _, ischecked ->
            binding.btnGenerateOtp.isEnabled = isValidAadhaar && ischecked
        }

        binding.aadharDisclaimer.setOnClickListener{
            aadhaarDisclaimer.show()
        }

        binding.tietAadhaarNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isValidAadhaar = s != null && s.length == 12
                binding.btnGenerateOtp.isEnabled = isValidAadhaar
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
}