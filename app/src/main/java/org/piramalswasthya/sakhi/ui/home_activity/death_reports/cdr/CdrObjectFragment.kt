package org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr

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
import org.piramalswasthya.sakhi.databinding.FragmentCdrObjectBinding

@AndroidEntryPoint
class CdrObjectFragment : Fragment() {

    private val binding by lazy {
        FragmentCdrObjectBinding.inflate(layoutInflater)
    }

    private val viewModel: CdrObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cdrForm.inputForm.rvInputForm.apply {
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
            val adapter = binding.cdrForm.inputForm.rvInputForm.adapter as FormInputAdapter
            viewModel.setAddress(it, adapter)
        }
        binding.btnMdsrSubmit.setOnClickListener{
            viewModel.submitForm()
        }
    }
}