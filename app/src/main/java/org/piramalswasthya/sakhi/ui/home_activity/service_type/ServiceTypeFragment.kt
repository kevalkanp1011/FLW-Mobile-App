package org.piramalswasthya.sakhi.ui.home_activity.service_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentServiceTypeBinding
import org.piramalswasthya.sakhi.model.FormInput
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
                if (homeViewModel.location)
                    findNavController().navigate(ServiceTypeFragmentDirections.actionServiceTypeFragmentToNavHome())

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
        Timber.d("onViewCreated() called!")
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
        binding.btnContinue.setOnClickListener {
            homeViewModel.location = true
            findNavController().navigate(ServiceTypeFragmentDirections.actionServiceTypeFragmentToNavHome())
        }
    }


}