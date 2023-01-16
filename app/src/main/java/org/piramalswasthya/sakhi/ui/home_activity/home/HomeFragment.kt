package org.piramalswasthya.sakhi.ui.home_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.HomeIconAdapter
import org.piramalswasthya.sakhi.configuration.IconDataset
import org.piramalswasthya.sakhi.databinding.FragmentHomeBinding
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        var numViewCopies = 0
        var numCopies = 0
    }

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel : HomeViewModel by viewModels ({ requireActivity() })
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.d("Back pressed()")

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
        if (!viewModel.location) {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToServiceTypeFragment())
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        binding.etSelectVillage.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToServiceTypeFragment())
        }
        setUpHomeIconRvAdapter()

    }

    private fun setUpHomeIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvHomeIcon.rvIconGrid.layoutManager = rvLayoutManager
        binding.rvHomeIcon.rvIconGrid.adapter = HomeIconAdapter(IconDataset.getIconDataset(),HomeIconAdapter.HomeIconClickListener {
            Toast.makeText(context, "Clicked $it", Toast.LENGTH_LONG).show()
            findNavController().navigate(it)
        })
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