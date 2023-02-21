package org.piramalswasthya.sakhi.ui.home_activity.mother_care.delivery_stage_list.pmsma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.databinding.FragmentPMSMABinding

@AndroidEntryPoint
class PMSMAFragment : Fragment() {

    private val binding by lazy { FragmentPMSMABinding.inflate(layoutInflater) }

    private val viewModel: PMSMAViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}