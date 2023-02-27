package org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.databinding.FragmentMdsrObjectBinding

@AndroidEntryPoint
class MdsrObjectFragment : Fragment() {

    private val binding by lazy {
        FragmentMdsrObjectBinding.inflate(layoutInflater)
    }

    private val viewModel: MdsrObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mdsrForm.inputForm.rvInputForm.apply {
            val adapter = FormInputAdapter()
            this.adapter = adapter
            lifecycleScope.launch{
                adapter.submitList(viewModel.getFirstPage(adapter))
            }
        }

        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        viewModel.address.observe(viewLifecycleOwner) {
            val adapter = binding.mdsrForm.inputForm.rvInputForm.adapter as FormInputAdapter
            viewModel.setAddress(it, adapter)
        }
        binding.btnMdsrSubmit.setOnClickListener{
            viewModel.submitForm()
        }
    }
}