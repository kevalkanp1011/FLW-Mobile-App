package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.IconGridAdapter
import org.piramalswasthya.sakhi.configuration.IconDataset
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class NcdFragment : Fragment() {

    companion object {
        fun newInstance() = NcdFragment()
    }

    private val viewModel: NcdViewModel by viewModels()
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
        // TODO: Use the ViewModel
        setUpNcdCasesRvAdapter()
    }

    private fun setUpNcdCasesRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvIconGrid.layoutManager = rvLayoutManager
        val iconAdapter = IconGridAdapter(
            IconGridAdapter.GridIconClickListener {
                findNavController().navigate(it)
            })
        binding.rvIconGrid.adapter = iconAdapter
        homeViewModel.iconCount.observe(viewLifecycleOwner) {
            it?.let {
                iconAdapter.submitList(IconDataset.getNCDDataset(it[0]))
            }
        }
    }

}