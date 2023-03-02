package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_more_15

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.NewBenGenPagerAdapter
import org.piramalswasthya.sakhi.databinding.FragmentNewFormViewpagerBinding
import org.piramalswasthya.sakhi.services.UploadSyncService
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.NewBenRegTypeFragment
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_more_15.NewBenRegG15ViewModel.State
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import timber.log.Timber

@AndroidEntryPoint
class NewBenRegG15Fragment : Fragment() {

    private val binding: FragmentNewFormViewpagerBinding by lazy {
        FragmentNewFormViewpagerBinding.inflate(layoutInflater)
    }

    private val hhId: Long by lazy {
        NewBenRegG15FragmentArgs.fromBundle(requireArguments()).hhId
    }
    private val viewModel: NewBenRegG15ViewModel by viewModels()

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    private val requestLocationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ b->
        if(b){
            requestLocationPermission()
        }
        else
            findNavController().navigateUp()
    }

    private fun showSettingsAlert() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        // Setting Dialog Title
        alertDialog.setTitle("Enable GPS")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings"
        ) { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel"
        ) { dialog, _ ->
            findNavController().navigateUp()
            dialog.cancel()
        }
        alertDialog.show()
    }

    private val errorAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error Input")
            //.setMessage("Do you want to continue with previous form, or create a new form and discard the previous form?")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            .create()
    }

    private val pageChangeCallback: ViewPager2.OnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(i: Int) {
                onPageChange(i)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setHHid(hhId)
        binding.vp2Nhhr.adapter = NewBenGenPagerAdapter(
            mutableListOf(
                "Ben Details",
                "ID Details"
            ), this
        )
        when (viewModel.mTabPosition) {
            0 -> {
                binding.btnPrev.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
            }
            1 -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.GONE
            }
        }

        when (viewModel.mTabPosition) {
            0 -> {
                binding.btnPrev.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
                binding.btnToBen.visibility = View.GONE
            }
            1 -> {
                binding.btnNext.visibility = View.VISIBLE
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnToBen.visibility = View.GONE
            }
            2 -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.GONE
                binding.btnToBen.visibility = View.VISIBLE
            }
        }
        TabLayoutMediator(binding.tlNhhr, binding.vp2Nhhr) { tab, position ->
            tab.text = (binding.vp2Nhhr.adapter as NewBenGenPagerAdapter).getPageName(position)
            tab.view.isClickable = false
        }.attach()

        binding.btnPrev.setOnClickListener {
            onPageChange(viewModel.mTabPosition - 1)
        }
        binding.btnNext.setOnClickListener {
            onPageChange(viewModel.mTabPosition + 1)

        }
        binding.btnToBen.setOnClickListener {
            if (validateFormForPage(binding.vp2Nhhr.adapter?.itemCount!!)) {
                viewModel.persistForm(homeViewModel.getLocationRecord())
                Toast.makeText(context, "Beneficiary saved successfully", Toast.LENGTH_LONG).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    activity?.startForegroundService(Intent(context, UploadSyncService::class.java))
                } else {
                    activity?.startService(Intent(context, UploadSyncService::class.java))
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state!!) {
                State.IDLE -> {
                }
                State.SAVING -> {

                }
                State.SAVE_SUCCESS -> {
                    Toast.makeText(context, "Save Successful!!!", Toast.LENGTH_LONG).show()
                    NewBenRegTypeFragment.triggerBenDataSendingWorker(requireContext())
                    findNavController().navigate(NewBenRegG15FragmentDirections.actionNewBenRegG15FragmentToHomeFragment())
                }
                State.SAVE_FAILED -> Toast.makeText(
                    context,
                    "Something wend wong! Contact testing!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                errorAlert.setMessage(it)
                errorAlert.show()
                viewModel.resetErrorMessage()
            }
        }

        viewModel.hasReproductiveStatus.observe(viewLifecycleOwner) {
            val adapter = (binding.vp2Nhhr.adapter as NewBenGenPagerAdapter)
            if (it)
                adapter.addPage("Reproductive Status")
            else
                if (adapter.itemCount == 3) adapter.removePage(2)
        }

    }

    override fun onStart() {
        super.onStart()
        requestLocationPermission()
        binding.vp2Nhhr.registerOnPageChangeCallback(pageChangeCallback)

    }

    override fun onStop() {
        super.onStop()
        binding.vp2Nhhr.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    private fun onPageChange(i: Int) {
        if (i == viewModel.mTabPosition) {
            return
        }
        if (i < viewModel.mTabPosition)
            viewModel.setMTabPosition(i)
        else {
            val validated =
                validateFormForPage(i)
            if (validated) {
                viewModel.setMTabPosition(i)
                when (viewModel.mTabPosition) {
                    1 -> {
                        viewModel.persistFirstPage()
                    }
                    2 -> {
                        viewModel.persistSecondPage()
                    }
                }
            }
        }
        binding.vp2Nhhr.currentItem = viewModel.mTabPosition
        if (binding.vp2Nhhr.adapter?.itemCount == 2) {
            when (viewModel.mTabPosition) {
                0 -> {
                    binding.btnPrev.visibility = View.GONE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnToBen.visibility = View.GONE
                }
                1 -> {
                    binding.btnPrev.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.GONE
                    binding.btnToBen.visibility = View.VISIBLE
                }
            }
        } else {
            when (viewModel.mTabPosition) {
                0 -> {
                    binding.btnPrev.visibility = View.GONE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnToBen.visibility = View.GONE

                }
                1 -> {
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnPrev.visibility = View.VISIBLE
                    binding.btnToBen.visibility = View.GONE

                }
                2 -> {
                    binding.btnPrev.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.GONE
                    binding.btnToBen.visibility = View.VISIBLE

                }
            }
        }
    }
    private fun requestLocationPermission(){
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        else
            if(!isGPSEnabled)
                showSettingsAlert()
    }

    private fun validateFormForPage(i: Int): Boolean {
        val currentItem = "f${viewModel.mTabPosition}"
        Timber.d(
            "item :Current mTab position : $currentItem toChange position $i \n Fragment : ${
                childFragmentManager.findFragmentByTag(
                    currentItem
                )
            }"
        )
        return (childFragmentManager.findFragmentByTag(currentItem) as NewBenRegG15ObjectFragment).validate()
    }
}