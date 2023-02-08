package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.NewBenKidPagerAdapter
import org.piramalswasthya.sakhi.databinding.FragmentNewBenRegBinding
import org.piramalswasthya.sakhi.services.UploadSyncService
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15.NewBenRegL15ViewModel.State
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import org.piramalswasthya.sakhi.work.BenDataSendingWorker
import timber.log.Timber

@AndroidEntryPoint
class NewBenRegL15Fragment : Fragment() {

    private val binding : FragmentNewBenRegBinding by lazy{
        FragmentNewBenRegBinding.inflate(layoutInflater)
    }

    private val hhId: Long by lazy {
        NewBenRegL15FragmentArgs.fromBundle(requireArguments()).hhId
    }
    private val viewModel: NewBenRegL15ViewModel by viewModels()

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

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

        binding.vp2Nhhr.adapter = NewBenKidPagerAdapter(this)
        when (viewModel.mTabPosition) {
            0 -> {
                binding.btnPrev.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
            }
            2 -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.GONE
            }
        }
        TabLayoutMediator(binding.tlNhhr, binding.vp2Nhhr) { tab, position ->
            tab.text = when (position) {
                0 -> "Registration"
                1 -> "Birth Details"
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
                Toast.makeText(context,"Beneficiary saved successfully", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(context, "Save Successful!!!", Toast.LENGTH_LONG).show()
                    triggerBenDataSendingWorker()
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

    }

    override fun onStart() {
        super.onStart()
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
                }
            }
        }
        binding.vp2Nhhr.currentItem = viewModel.mTabPosition
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
        return (childFragmentManager.findFragmentByTag(currentItem) as NewBenRegL15ObjectFragment).validate()
    }

    private fun triggerBenDataSendingWorker() {
        val workRequest = OneTimeWorkRequestBuilder<BenDataSendingWorker>()
            .setConstraints(BenDataSendingWorker.constraint)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork(BenDataSendingWorker.name, ExistingWorkPolicy.APPEND, workRequest)
    }
}