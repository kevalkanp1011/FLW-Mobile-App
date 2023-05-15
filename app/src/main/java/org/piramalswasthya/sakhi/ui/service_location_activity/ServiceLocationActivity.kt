package org.piramalswasthya.sakhi.ui.service_location_activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentServiceTypeBinding
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import timber.log.Timber

@AndroidEntryPoint
class ServiceLocationActivity : AppCompatActivity() {

    private var _binding: FragmentServiceTypeBinding? = null
    private val binding: FragmentServiceTypeBinding
        get() = _binding!!

    private val viewModel: ServiceTypeViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (homeViewModel.isLocationSet()) {
                    finish()
                    val goToHome = Intent(this@ServiceLocationActivity, HomeActivity::class.java)
                    startActivity(goToHome)
                } else
                    if (!exitAlert.isShowing)
                        exitAlert.show()

            }
        }
    }
    private val incompleteLocationAlert by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle("Missing Detail")
            .setMessage("At least one of the following is missing value:\n \n\tState\n\tDistrict\n\tBlock\n\tVillage")
            .setPositiveButton("Understood") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
    private val exitAlert by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle("Exit Application")
            .setMessage("Do you want to exit application")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { d, _ ->
                d.dismiss()
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentServiceTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.d("onViewCreated() called!")
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        onBackPressedDispatcher.addCallback(
            this, onBackPressedCallback
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
                finish()
                val goToHome = Intent(this@ServiceLocationActivity, HomeActivity::class.java)
                startActivity(goToHome)
            } else
                incompleteLocationAlert.show()
        }
        viewModel.state.observe(this) {
            it?.let {
                when (it) {
                    ServiceTypeViewModel.State.IDLE -> {}//TODO()
                    ServiceTypeViewModel.State.LOADING -> {}//TODO()
                    ServiceTypeViewModel.State.SUCCESS -> {
                        if (homeViewModel.isLocationSet())
                            viewModel.loadLocation(homeViewModel.getLocationRecord())
                        else {
                            viewModel.loadDefaultLocation()
                        }
                    }
                }
            }
        }
        viewModel.selectedStateId.observe(this) {
            if (it >= 0)
                viewModel.stateList.value?.get(it)
                    ?.let { state -> binding.actvStateDropdown.setText(state) }
        }
        viewModel.selectedDistrictId.observe(this) {
            if (it >= 0)
                viewModel.districtList.value?.get(it)
                    ?.let { state -> binding.actvDistrictDropdown.setText(state) }
        }
        viewModel.selectedBlockId.observe(this) {
            if (it >= 0)
                viewModel.blockList.value?.get(it)
                    ?.let { state -> binding.actvBlockDropdown.setText(state) }
        }
        viewModel.selectedVillageId.observe(this) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}