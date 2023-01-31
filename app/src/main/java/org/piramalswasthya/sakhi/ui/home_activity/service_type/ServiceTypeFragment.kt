package org.piramalswasthya.sakhi.ui.home_activity.service_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentServiceTypeBinding
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import timber.log.Timber

@AndroidEntryPoint
class ServiceTypeFragment : Fragment() {

    private val binding : FragmentServiceTypeBinding by lazy{
        FragmentServiceTypeBinding.inflate(layoutInflater)
    }
    private val viewModel: ServiceTypeViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels({requireActivity()})
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (homeViewModel.isLocationSet())
                    findNavController().navigate(ServiceTypeFragmentDirections.actionServiceTypeFragmentToNavHome())
                else if(dataValid()){
                    binding.btnContinue.performClick()
                }
                else
                    if(! incompleteLocationAlert.isShowing)
                        incompleteLocationAlert.show()

            }
        }
    }
    private val incompleteLocationAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Missing Detail")
            .setMessage("At least one of the following is missing value:\n \n\tState\n\tDistrict\n\tBlock\n\tVillage")
            .setPositiveButton("Understood"){
                dialog,_-> dialog.dismiss()
            }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated() called!")
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        if(homeViewModel.isLocationSet())
            viewModel.loadLocation(homeViewModel.user,homeViewModel.getLocationRecord())
        else
            viewModel.loadLocationFromDatabase()
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
        binding.btnContinue.setOnClickListener {
            if(dataValid()) {
                homeViewModel.setLocationDetails(
                    binding.ddState.actvRvDropdown.text.toString(),
                    binding.ddDistrict.actvRvDropdown.text.toString(),
                    binding.ddBlock.actvRvDropdown.text.toString(),
                    binding.ddVillage.actvRvDropdown.text.toString(),
                )
                findNavController().navigate(ServiceTypeFragmentDirections.actionServiceTypeFragmentToNavHome())
            }
            else
                incompleteLocationAlert.show()

        }
    }

    private fun dataValid(): Boolean {
        return !(binding.ddState.actvRvDropdown.text.isNullOrBlank()||
                binding.ddDistrict.actvRvDropdown.text.isNullOrBlank()||
                binding.ddBlock.actvRvDropdown.text.isNullOrBlank()||
                binding.ddVillage.actvRvDropdown.text.isNullOrBlank())

    }


}