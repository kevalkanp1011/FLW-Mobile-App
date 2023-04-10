package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentCreateAbhaBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id.CreateAbhaViewModel.State
import timber.log.Timber

@AndroidEntryPoint
class CreateAbhaFragment : Fragment() {

    private lateinit var navController: NavController

    private var _binding: FragmentCreateAbhaBinding? = null

    private val binding: FragmentCreateAbhaBinding
        get() = _binding!!
    private val viewModel: CreateAbhaViewModel by viewModels()

    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button press here
                Timber.d("handleOnBackPressed")
                exitAlert.show()
            }
        }
    }

    private val exitAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit")
            .setMessage("Do you want to go back?")
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
    ): View? {
        _binding = FragmentCreateAbhaBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        navController = findNavController()

        binding.btnDownloadAbhaYes.setOnClickListener {
            viewModel.downloadAbhaClicked(requireActivity())
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.IDLE -> {}
                State.LOADING -> {
                    binding.pbCai.visibility = View.VISIBLE
                    binding.clCreateAbhaId.visibility = View.INVISIBLE
                    binding.clError.visibility = View.INVISIBLE
                }
                State.ERROR_NETWORK -> {
                    binding.pbCai.visibility = View.INVISIBLE
                    binding.clCreateAbhaId.visibility = View.INVISIBLE
                    binding.clError.visibility = View.VISIBLE
                }
                State.ERROR_SERVER -> {
                    binding.pbCai.visibility = View.INVISIBLE
                    binding.clCreateAbhaId.visibility = View.INVISIBLE
                    binding.clError.visibility = View.INVISIBLE
                    binding.tvErrorText.visibility = View.VISIBLE
                }
                State.GENERATE_SUCCESS -> {
                    binding.pbCai.visibility = View.INVISIBLE
                    binding.clCreateAbhaId.visibility = View.VISIBLE
                    binding.clError.visibility = View.INVISIBLE
                }
                State.DOWNLOAD_SUCCESS -> {
                    binding.pbCai.visibility = View.INVISIBLE
                    binding.clCreateAbhaId.visibility = View.VISIBLE
                    binding.clError.visibility = View.INVISIBLE
                    binding.txtDownloadAbha.visibility = View.INVISIBLE
                    binding.downloadAbha.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvErrorText.text = it
                viewModel.resetErrorMessage()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
        _binding = null
    }
}