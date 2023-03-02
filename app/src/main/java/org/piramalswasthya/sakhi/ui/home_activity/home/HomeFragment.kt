package org.piramalswasthya.sakhi.ui.home_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.IconGridAdapter
import org.piramalswasthya.sakhi.configuration.IconDataset
import org.piramalswasthya.sakhi.databinding.FragmentHomeBinding
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import org.piramalswasthya.sakhi.work.PullFromAmritFullLoadWorker
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

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        setUpHomeIconRvAdapter()

        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(PullFromAmritFullLoadWorker.name)
            .observe(viewLifecycleOwner) {
                it?.let { list ->
                    list.first()?.let { workInfo ->
                        binding.llFullLoadProgress.visibility =
                            if (workInfo.state == WorkInfo.State.RUNNING)
                                View.VISIBLE
                            else
                                View.GONE
                    }
                }
            }


    }

    override fun onStart() {
        super.onStart()
        activity?.let{
            (it as HomeActivity).setLogo(R.drawable.ic_home)
        }
    }

    private fun setUpHomeIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvHomeIcon.rvIconGrid.layoutManager = rvLayoutManager
        val rvAdapter = IconGridAdapter(IconGridAdapter.GridIconClickListener {
            findNavController().navigate(it)
        })
        binding.rvHomeIcon.rvIconGrid.adapter = rvAdapter
        viewModel.iconCount.observe(viewLifecycleOwner) {
            rvAdapter.submitList(IconDataset.getIconDataset(it[0]))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        numViewCopies--
        Timber.d("onDestroyView() called! $numViewCopies")
    }

    override fun onDestroy() {
        super.onDestroy()
        numCopies--
        Timber.d("onDestroy() called! $numCopies")

    }
}