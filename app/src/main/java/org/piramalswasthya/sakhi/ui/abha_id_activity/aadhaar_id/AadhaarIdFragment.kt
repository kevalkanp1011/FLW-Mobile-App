package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentAadhaarIdBinding
import org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.AadhaarIdViewModel.State

@AndroidEntryPoint
class AadhaarIdFragment : Fragment() {

    private var _binding: FragmentAadhaarIdBinding? = null
    private val binding: FragmentAadhaarIdBinding
        get() = _binding!!


    companion object {
        fun newInstance() = AadhaarIdFragment()
    }

    private lateinit var viewModel: AadhaarIdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAadhaarIdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGenerateOtp.setOnClickListener {
            findNavController().navigate(AadhaarIdFragmentDirections.actionAadhaarIdFragmentToAadhaarOtpFragment())
        }
        binding.tietAadhaarNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnGenerateOtp.isEnabled = s != null && s.length == 12
            }

        })

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state!!) {
                State.LOADING -> ""
                else -> ""
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AadhaarIdViewModel::class.java)
    }

}