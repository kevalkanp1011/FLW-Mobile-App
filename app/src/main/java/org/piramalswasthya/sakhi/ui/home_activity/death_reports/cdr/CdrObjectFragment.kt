package org.piramalswasthya.sakhi.ui.home_activity.death_reports.cdr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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

    }
}