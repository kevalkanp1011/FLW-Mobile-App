package org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentGenerateMobileOtpBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.AbhaIdViewModel
import org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp.GenerateMobileOtpViewModel.State

@AndroidEntryPoint
class GenerateMobileOtpFragment : Fragment() {

    private var _binding: FragmentGenerateMobileOtpBinding? = null
    private val binding: FragmentGenerateMobileOtpBinding
        get() = _binding!!

    private val viewModel: GenerateMobileOtpViewModel by viewModels()

    private val activityViewModel: AbhaIdViewModel by viewModels({ requireActivity() })

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenerateMobileOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.btnGenerateMobileOtp.setOnClickListener {
            viewModel.generateOtpClicked(binding.tietMobileNumber.text!!.toString())
        }

        binding.tietMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.btnGenerateMobileOtp.isEnabled = p0 != null && p0.length == 10
            }
        })

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.IDLE -> {}
                State.LOADING -> {
                    binding.clGenerateMobileOtp.visibility = View.INVISIBLE
                    binding.progressBarGmotp.visibility = View.VISIBLE
                }
                State.SUCCESS -> {
                    navController.navigate(
                        GenerateMobileOtpFragmentDirections.actionGenerateMobileOtpFragmentToVerifyMobileOtpFragment(
                            viewModel.txnID, binding.tietMobileNumber.text!!.toString()
                        )
                    )
                    viewModel.resetState()
                }
                State.ERROR_SERVER -> {}
                State.ERROR_NETWORK -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}