package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list.cbac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentCbacBinding

@AndroidEntryPoint
class CbacFragment : Fragment() {


    private val binding by lazy { FragmentCbacBinding.inflate(layoutInflater) }

    private val viewModel: CbacViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBenName.text = viewModel.getBenName()
        binding.tvAgeGender.text = viewModel.getAgeGender()
        binding.actvSmokeDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setSmoke(i) }
        binding.actvSmokeDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setAlcohol(i) }
    }

}