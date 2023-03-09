package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentAadhaarOtpBinding

@AndroidEntryPoint
class AadhaarOtpFragment : Fragment() {

    private var _binding: FragmentAadhaarOtpBinding? = null
    private val binding: FragmentAadhaarOtpBinding
        get() = _binding!!

    private lateinit var viewModel: AadhaarOtpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAadhaarOtpBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        fun newInstance() = AadhaarOtpViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVerifyOTP.setOnClickListener {
            findNavController().navigate(AadhaarOtpFragmentDirections.actionAadhaarOtpFragmentToCreateAbhaFragment())
        }

        binding.tietAadhaarOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.btnVerifyOTP.isEnabled = p0 != null && p0.length == 6
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AadhaarOtpViewModel::class.java)
    }
}
