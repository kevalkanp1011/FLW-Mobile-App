package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.IconGridAdapter
import org.piramalswasthya.sakhi.configuration.IconDataset
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding

@AndroidEntryPoint
class NcdFragment : Fragment() {

    companion object {
        fun newInstance() = NcdFragment()
    }

    private val viewModel: NcdViewModel by viewModels()
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
        setUpNcdIconRvAdapter()
    }

    private fun setUpNcdIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvIconGrid.layoutManager = rvLayoutManager
        binding.rvIconGrid.adapter = IconGridAdapter(
            IconDataset.getNCDDataset(),
            IconGridAdapter.GridIconClickListener {
                findNavController().navigate(it)
            })
    }

}