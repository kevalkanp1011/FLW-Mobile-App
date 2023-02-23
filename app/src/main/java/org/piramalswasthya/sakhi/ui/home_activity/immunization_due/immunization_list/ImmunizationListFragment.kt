package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.immunization_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.ImmunizationGridAdapter
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class ImmunizationListFragment : Fragment() {

    companion object {
        fun newInstance() = ImmunizationListFragment()
    }

    private val viewModel: ImmunizationListViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })
    private val binding by lazy { RvIconGridBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpImmunizationIconRvAdapter()
    }

    private fun setUpImmunizationIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvIconGrid.layoutManager = rvLayoutManager
        val iconAdapter = ImmunizationGridAdapter(
            ImmunizationGridAdapter.ImmunizationIconClickListener {a,b,c,d,e ->
                findNavController().navigate(
                    ImmunizationListFragmentDirections.actionImmunizationListFragmentToImmunizationObjectFragment(a, b, c, d, e))
            })
        binding.rvIconGrid.adapter = iconAdapter

        iconAdapter.submitList(viewModel.vaccineList)
    }

}