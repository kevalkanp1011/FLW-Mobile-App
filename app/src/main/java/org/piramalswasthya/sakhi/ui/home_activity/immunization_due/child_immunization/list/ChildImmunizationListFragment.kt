package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.child_immunization.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.VaccineListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentChildImmunizationListBinding
import org.piramalswasthya.sakhi.model.ImmunizationDetailsHeader
import org.piramalswasthya.sakhi.model.VaccineClickListener
import timber.log.Timber

@AndroidEntryPoint
class ChildImmunizationListFragment : Fragment() {

    private var _binding: FragmentChildImmunizationListBinding? = null
    private val binding: FragmentChildImmunizationListBinding
        get() = _binding!!


    private val viewModel: ChildImmunizationListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildImmunizationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvList.adapter = VaccineListAdapter(VaccineClickListener { a, b ->
            findNavController().navigate(
                ChildImmunizationListFragmentDirections.actionChildImmunizationListFragmentToImmunizationFormFragment(
                    b, a
                )
            )
        })
        lifecycleScope.launch {
            viewModel.headerList.collect {
                binding.header = ImmunizationDetailsHeader(it)
            }
        }
        lifecycleScope.launch {
            viewModel.benWithVaccineDetails.collect {
                Timber.d("Collecting list : $it")
                binding.rvList.apply {
                    (adapter as VaccineListAdapter).submitList(it)
                }
            }
        }
    }

}