package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentCreateAbhaBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id.CreateAbhaViewModel.State
import org.piramalswasthya.sakhi.utils.NotificationUtils
import org.piramalswasthya.sakhi.work.DownloadCardWorker
import org.piramalswasthya.sakhi.work.WorkerUtils
import timber.log.Timber
import java.time.LocalDate

@AndroidEntryPoint
class CreateAbhaFragment : Fragment() {

    private lateinit var navController: NavController

    private var _binding: FragmentCreateAbhaBinding? = null

    private val binding: FragmentCreateAbhaBinding
        get() = _binding!!
    private val viewModel: CreateAbhaViewModel by viewModels()

    private val channelId = "download abha card"

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
            val abhaVal = viewModel.abha.value
            val fileName =
                "${abhaVal?.name}_${System.currentTimeMillis()}.pdf"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(channelId,channelId,
                    NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            val state: LiveData<Operation.State> = WorkerUtils
                .triggerDownloadCardWorker(requireContext(), fileName)

            state.observe(viewLifecycleOwner) {
                when(it) {
                    is Operation.State.SUCCESS -> {
                        binding.txtDownloadAbha.visibility = View.INVISIBLE
                        binding.downloadAbha.visibility = View.INVISIBLE
                        Snackbar.make(binding.root,
                            "Downloaded $fileName successfully", Snackbar.LENGTH_SHORT).show()
                    }
                    is Operation.State.FAILURE -> {
                        Toast.makeText(context, "Failed to download , Please retry", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnDownloadAbhaNo.setOnClickListener{
            binding.txtDownloadAbha.visibility = View.INVISIBLE
            binding.downloadAbha.visibility = View.INVISIBLE
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