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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentGenerateMobileOtpBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.AbhaIdActivity
import org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp.GenerateMobileOtpViewModel.State
import timber.log.Timber

@AndroidEntryPoint
class GenerateMobileOtpFragment : Fragment() {

    private var _binding: FragmentGenerateMobileOtpBinding? = null
    private val binding: FragmentGenerateMobileOtpBinding
        get() = _binding!!

    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button press here
                Timber.d("handleOnBackPressed")
                exitAlert.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            (it as AbhaIdActivity).updateActionBar(
                R.drawable.ic__abha_logo_v1_24,
                getString(R.string.generate_abha)
            )
        }
    }

    private val exitAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.exit))
            .setMessage(resources.getString(R.string.do_you_want_to_go_back))
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                navController.navigate(R.id.aadhaarIdFragment)
            }
            .setNegativeButton(resources.getString(R.string.no)) { d, _ ->
                d.dismiss()
            }
            .create()
    }

    private val viewModel: GenerateMobileOtpViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateMobileOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        binding.btnGenerateMobileOtp.setOnClickListener {
            viewModel.generateOtpClicked(binding.tietMobileNumber.text!!.toString())
        }

        binding.mobileNoInfo.setOnClickListener {
            it.performLongClick()
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
                    if (viewModel.apiResponse.mobileLinked) {
                        navController.navigate(
                            GenerateMobileOtpFragmentDirections.actionGenerateMobileOtpFragmentToCreateAbhaFragment(
                                viewModel.txnIdFromArgs, "", "", ""
                            )
                        )
                    } else {
                        navController.navigate(
                            GenerateMobileOtpFragmentDirections.actionGenerateMobileOtpFragmentToVerifyMobileOtpFragment(
                                viewModel.apiResponse.txnId,
                                binding.tietMobileNumber.text!!.toString(), "", "", ""
                            )
                        )
                    }
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

                State.ABHA_GENERATED_SUCCESS -> {
                    navController.navigate(
                        GenerateMobileOtpFragmentDirections.actionGenerateMobileOtpFragmentToCreateAbhaFragment(
                            viewModel.txnIdFromArgs, "", "", ""
                        )
                    )
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

    private fun checkValidity() {
        val no = binding.tietMobileNumber.text.toString()
        binding.btnGenerateMobileOtp.isEnabled = no.length == 10 && (no.toLong() >= 6000000000)
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
        _binding = null
    }
}