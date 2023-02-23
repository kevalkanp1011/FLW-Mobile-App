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
import org.piramalswasthya.sakhi.ui.home_activity.service_type.ServiceTypeViewModel.State.*
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
                else
                    if (!exitAlert.isShowing)
                        exitAlert.show()

            }
        }
    }
    private val incompleteLocationAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Missing Detail")
            .setMessage("At least one of the following is missing value:\n \n\tState\n\tDistrict\n\tBlock\n\tVillage")
            .setPositiveButton("Understood") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
    private val exitAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit Application")
            .setMessage("Do you want to exit application")
            .setPositiveButton("Yes") { _, _ ->
                activity?.finish()
            }
            .setNegativeButton("No") { d, _ ->
                d.dismiss()
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


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
        binding.actvStateDropdown.setOnItemClickListener { _, _, i, _ ->
            viewModel.setStateId(i)
        }
        binding.actvDistrictDropdown.setOnItemClickListener { _, _, i, _ ->
            viewModel.setDistrictId(i)
        }
        binding.actvBlockDropdown.setOnItemClickListener { _, _, i, _ ->
            viewModel.setBlockId(i)
        }
        binding.actvVillageDropdown.setOnItemClickListener { _, _, i, _ ->
            viewModel.setVillageId(i)
        }
        binding.btnContinue.setOnClickListener {
            if (dataValid()) {
                homeViewModel.setLocationDetails(
                    viewModel.selectedState!!,
                    viewModel.selectedDistrict!!,
                    viewModel.selectedBlock!!,
                    viewModel.selectedVillage!!,
                )
                findNavController().navigate(ServiceTypeFragmentDirections.actionServiceTypeFragmentToNavHome())
            } else
                incompleteLocationAlert.show()

        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                IDLE -> {}//TODO()
                LOADING -> {}//TODO()
                SUCCESS -> {
                    if (homeViewModel.isLocationSet())
                        viewModel.loadLocation(homeViewModel.getLocationRecord())
                    else {
                        viewModel.loadDefaultLocation()
                    }
                }
            }
        }
        viewModel.selectedStateId.observe(viewLifecycleOwner) {
            if (it >= 0)
                viewModel.stateList.value?.get(it)
                    ?.let { state -> binding.actvStateDropdown.setText(state) }
        }
        viewModel.selectedDistrictId.observe(viewLifecycleOwner) {
            if (it >= 0)
                viewModel.districtList.value?.get(it)
                    ?.let { state -> binding.actvDistrictDropdown.setText(state) }
        }
        viewModel.selectedBlockId.observe(viewLifecycleOwner) {
            if (it >= 0)
                viewModel.blockList.value?.get(it)
                    ?.let { state -> binding.actvBlockDropdown.setText(state) }
        }
        viewModel.selectedVillageId.observe(viewLifecycleOwner) {
            if (it >= 0)
                viewModel.villageList.value?.get(it)
                    ?.let { state -> binding.actvVillageDropdown.setText(state) }
        }
    }

    private fun dataValid(): Boolean {
        return !(binding.actvStateDropdown.text.isNullOrBlank() ||
                binding.actvDistrictDropdown.text.isNullOrBlank() ||
                binding.actvBlockDropdown.text.isNullOrBlank() ||
                binding.actvVillageDropdown.text.isNullOrBlank())

    }


}