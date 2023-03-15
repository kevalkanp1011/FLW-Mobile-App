package org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentGenerateMobileOtpBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp.GenerateMobileOtpViewModel.State
import timber.log.Timber

@AndroidEntryPoint
class GenerateMobileOtpFragment : Fragment() {

    private var _binding: FragmentGenerateMobileOtpBinding? = null
    private val binding: FragmentGenerateMobileOtpBinding
        get() = _binding!!

    private lateinit var callback: OnBackPressedCallback
    private val viewModel: GenerateMobileOtpViewModel by viewModels()

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
        binding.lifecycleOwner = viewLifecycleOwner

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button press here
                Timber.d("handleOnBackPressed")
                navController.navigate(R.id.aadhaarIdFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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
                    binding.clError.visibility = View.INVISIBLE
                }
                State.SUCCESS -> {
                    navController.navigate(
                        GenerateMobileOtpFragmentDirections.actionGenerateMobileOtpFragmentToVerifyMobileOtpFragment(
                            viewModel.txnID, binding.tietMobileNumber.text!!.toString()
                        )
                    )
                    viewModel.resetState()
                }
                State.ERROR_SERVER -> {
                    binding.progressBarGmotp.visibility = View.INVISIBLE
                    binding.clGenerateMobileOtp.visibility = View.VISIBLE
                    binding.clError.visibility = View.INVISIBLE
                    binding.tvErrorText.visibility = View.VISIBLE
                }
                State.ERROR_NETWORK -> {
                    binding.progressBarGmotp.visibility = View.INVISIBLE
                    binding.clGenerateMobileOtp.visibility = View.INVISIBLE
                    binding.clError.visibility = View.VISIBLE
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvErrorText.text = it
                viewModel.resetErrorMessage()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}