package org.piramalswasthya.sakhi.ui.home_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.HomePagerAdapter
import org.piramalswasthya.sakhi.databinding.FragmentHomeBinding
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import org.piramalswasthya.sakhi.work.PullFromAmritWorker
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        var numViewCopies = 0
        var numCopies = 0
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

    private var _binding : FragmentHomeBinding? = null
    private val binding : FragmentHomeBinding
    get() = _binding!!
    private val viewModel: HomeViewModel by viewModels({ requireActivity() })
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!exitAlert.isShowing)
                    exitAlert.show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numCopies++
        Timber.d("onCreate() called! $numCopies")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        numViewCopies++
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity?)?.setHomeMenuItemVisibility(false)
        Timber.d("onViewCreated() called! $numViewCopies")
        if (!viewModel.isLocationSet()) {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToServiceTypeFragment())
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        binding.etSelectVillage.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToServiceTypeFragment())
        }
//        binding.btnNhhr.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewHouseholdFragment())
//        }

        if (viewModel.isLocationSet())
            binding.etSelectVillage.setText(viewModel.getLocationRecord().village)
        setUpViewPager()
        WorkManager.getInstance(requireContext())
            .getWorkInfosLiveData(WorkQuery.fromUniqueWorkNames(PullFromAmritWorker.name))
            .observe(viewLifecycleOwner) { workInfoMutableList ->
                workInfoMutableList?.let { list ->
                    list.takeIf { it.isNotEmpty() }?.last()?.let { workInfo ->
                        binding.llFullLoadProgress.visibility =
                            if (workInfo.state == WorkInfo.State.RUNNING)
                                View.VISIBLE
                            else
                                View.GONE
                    }
                }
            }


    }

    private fun setUpViewPager() {

        binding.vp2Home.adapter = HomePagerAdapter(this)
        TabLayoutMediator(binding.tlHomeViewpager,binding.vp2Home) { tab, position ->
            tab.text = when (position) {
                0 -> "Scheduler"
                1 -> "Home"
                else -> "NA"
            }
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        activity?.let{
            (it as HomeActivity).setLogo(R.drawable.ic_home)
        }
        binding.vp2Home.setCurrentItem(1,false)
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity?)?.setHomeMenuItemVisibility(true)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        numViewCopies--
        Timber.d("onDestroyView() called! $numViewCopies")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding  =null
        numCopies--
        Timber.d("onDestroy() called! $numCopies")

    }
}