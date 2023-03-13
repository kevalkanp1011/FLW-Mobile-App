package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentCreateAbhaBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id.CreateAbhaViewModel.State

@AndroidEntryPoint
class CreateAbhaFragment : Fragment() {

    private var _binding: FragmentCreateAbhaBinding? = null
    private val binding: FragmentCreateAbhaBinding
        get() = _binding!!
    private val viewModel: CreateAbhaViewModel by viewModels()
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

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.IDLE -> {}
                State.LOADING -> {
                    binding.pbCai.visibility = View.VISIBLE
                    binding.clCreateAbhaId.visibility = View.INVISIBLE
                }
                State.ERROR_NETWORK -> {}
                State.ERROR_SERVER -> {}
                State.GENERATE_SUCCESS -> {
                    binding.pbCai.visibility = View.INVISIBLE
                    binding.clCreateAbhaId.visibility = View.VISIBLE
                }
                State.DOWNLOAD_SUCCESS -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}