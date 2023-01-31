package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.NewHouseholdPagerAdapter
import org.piramalswasthya.sakhi.databinding.AlertConsentBinding
import org.piramalswasthya.sakhi.databinding.FragmentNewHouseholdBinding
import org.piramalswasthya.sakhi.services.UploadSyncService
import org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration.NewHouseholdViewModel.*
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import timber.log.Timber

@AndroidEntryPoint
class NewHouseholdFragment : Fragment() {

    private val binding by lazy {
        FragmentNewHouseholdBinding.inflate(layoutInflater)
    }

    private val viewModel: NewHouseholdViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels({requireActivity()})

    private val consentAlert by lazy {
        val alertBinding = AlertConsentBinding.inflate(layoutInflater,binding.root,false)
        alertBinding.textView4.text = getString(R.string.consent_alert_title)
        alertBinding.checkBox.text = getString(R.string.consent_text)
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(alertBinding.root)
            .setCancelable(false)
            .create()
        alertBinding.btnNegative.setOnClickListener {
            alertDialog.dismiss()
            findNavController().navigateUp()
        }
        alertBinding.btnPositive.setOnClickListener {
            if(alertBinding.checkBox.isChecked)
                alertDialog.dismiss()
            else
                Toast.makeText(context,"Please tick the checkbox", Toast.LENGTH_SHORT).show()
        }
        alertDialog
    }


    private val pageChangeCallback: OnPageChangeCallback by lazy {
        object : OnPageChangeCallback() {

            override fun onPageSelected(i: Int) {
                onPageChange(i)

            }
        }
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
        //binding.tlNhhr.setScrollPosition(mTabPosition, 0f, false)
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

    private fun validateFormForPage(i: Int): Boolean {
        val currentItem = "f${viewModel.mTabPosition}"
        Timber.d(
            "item :Current mTab position : $currentItem toChange position $i \n Fragment : ${
                childFragmentManager.findFragmentByTag(
                    currentItem
                )
            }"
        )
        return (childFragmentManager.findFragmentByTag(currentItem) as NewHouseholdFormObjectFragment).validate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vp2Nhhr.adapter = NewHouseholdPagerAdapter(this)
        consentAlert.show()
        when (viewModel.mTabPosition) {
            0 -> {
                binding.btnPrev.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
            }
            1 -> {
                binding.btnNext.visibility = View.VISIBLE
                binding.btnPrev.visibility = View.VISIBLE
            }
            2 -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.GONE
            }
        }

/*        binding.tlNhhr.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { onPageChange(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })*/
        TabLayoutMediator(binding.tlNhhr, binding.vp2Nhhr) { tab, position ->
            tab.text = when (position) {
                0 -> "Family Details"
                1 -> "Household Details"
                2 -> "Household Amenities"
                else -> "NA"
            }
            tab.view.isClickable = false
        }.attach()

        binding.btnPrev.setOnClickListener {
            onPageChange(viewModel.mTabPosition - 1)
        }
        binding.btnNext.setOnClickListener {
            onPageChange(viewModel.mTabPosition + 1)

        }
        binding.btnToBen.setOnClickListener {
            if (validateFormForPage(2)) {
                viewModel.persistForm(homeViewModel.getLocationRecord())
                //TODO(Move to Add Ben)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    activity?.startForegroundService(Intent(context, UploadSyncService::class.java))
                } else {
                    activity?.startService(Intent(context, UploadSyncService::class.java))
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner){ state->
            when(state!!){
                State.IDLE -> {
                }
                State.SAVING -> {

                }
                State.SAVE_SUCCESS -> {
                    findNavController().navigate(NewHouseholdFragmentDirections.actionNewHouseholdFragmentToNewBenRegTypeFragment(viewModel.getHHId()))
                }
                State.SAVE_FAILED -> Toast.makeText(context,"Something wend wong! Contact testing!", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        binding.vp2Nhhr.registerOnPageChangeCallback(pageChangeCallback)

    }

    override fun onStop() {
        super.onStop()
        binding.vp2Nhhr.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}
