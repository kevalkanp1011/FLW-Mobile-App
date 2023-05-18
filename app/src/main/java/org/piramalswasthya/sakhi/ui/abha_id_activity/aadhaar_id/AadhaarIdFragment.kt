package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentAadhaarIdBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.AadhaarIdViewModel.State


@AndroidEntryPoint
class AadhaarIdFragment : Fragment() {

    private var _binding: FragmentAadhaarIdBinding? = null
    private val binding: FragmentAadhaarIdBinding
        get() = _binding!!

    private val viewModel: AadhaarIdViewModel by viewModels({requireActivity()})

    private lateinit var navController: NavController
    private val aadhaarNavController by lazy {
        val navHostFragment: NavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_aadhaar_id) as NavHostFragment
        navHostFragment.navController
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAadhaarIdBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        var userType = ""
        binding.rgGovAsha.setOnCheckedChangeListener{
                _, id ->
            when(id) {
                R.id.rb_asha -> {
                    userType = "ASHA"
                    aadhaarNavController.navigate(R.id.aadhaarNumberAshaFragment)
                }
                R.id.rb_gov -> {
                    userType = "GOV"
                    aadhaarNavController.navigate(R.id.aadhaarNumberGovFragment)
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state!!) {
                State.IDLE -> {}
                State.LOADING -> {
                    binding.clContentAadharId.visibility = View.INVISIBLE
                    binding.pbLoadingAadharId.visibility = View.VISIBLE
                    binding.clError.visibility = View.INVISIBLE
                }
                State.SUCCESS -> {
                    if (userType == "ASHA") {
                        viewModel.resetState()
                        findNavController().navigate(
                            AadhaarIdFragmentDirections.actionAadhaarIdFragmentToAadhaarOtpFragment(
                                viewModel.txnId, viewModel.mobileNumber
                            )
                        )
                    }
                }
                State.ERROR_SERVER -> {
                    binding.pbLoadingAadharId.visibility = View.INVISIBLE
                    binding.clContentAadharId.visibility = View.VISIBLE
                    binding.clError.visibility = View.INVISIBLE
                }
                State.ERROR_NETWORK -> {
                    binding.clContentAadharId.visibility = View.INVISIBLE
                    binding.pbLoadingAadharId.visibility = View.INVISIBLE
                    binding.clError.visibility = View.VISIBLE
                }
                State.STATE_DETAILS_SUCCESS -> {
                    binding.clContentAadharId.visibility = View.VISIBLE
                    binding.pbLoadingAadharId.visibility = View.INVISIBLE
                }
                State.ABHA_GENERATED_SUCCESS -> {
                    findNavController().navigate(
                        AadhaarIdFragmentDirections.actionAadhaarIdFragmentToCreateAbhaFragment(
                            viewModel.abhaResponse
                        )
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

