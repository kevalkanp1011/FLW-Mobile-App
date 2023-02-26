package org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentMdsrObjectBinding
import java.text.SimpleDateFormat
import java.util.*

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

        viewModel.maternalDeath.observe(viewLifecycleOwner){
            if(it) {
                binding.llMdsrReasonMaternal.visibility = View.VISIBLE
            } else {
                binding.llMdsrReasonMaternal.visibility = View.GONE
            }
        }

        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        binding.rgDeathType.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_mdsr1 -> viewModel.deathReasonChanged(1)
                R.id.rb_mdsr2 -> viewModel.deathReasonChanged(2)

            }
        }
        binding.rgActionTaken.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_mdsr_yes -> viewModel.deathReasonChanged(1)
                R.id.rb_mdsr_no -> viewModel.deathReasonChanged(2)

            }
        }
        val date = Date()
        val formatter = SimpleDateFormat("dd/MM/yy")
        val now = formatter.format(date)

//        binding.etMdsrAddress = viewModel.ben.
        binding.etMdsrFieldInvestigation.setText(now)
        binding.etMdsrDateOfDeceased.setText(now)
    }
}