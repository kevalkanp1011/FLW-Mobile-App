package org.piramalswasthya.sakhi.ui.home_activity.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.IconGridAdapter
import org.piramalswasthya.sakhi.configuration.IconDataset
import org.piramalswasthya.sakhi.databinding.FragmentHomeBinding
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding

class HomeIconsFragment : Fragment() {

    private var _binding: RvIconGridBinding? = null
    private val binding: RvIconGridBinding
        get() = _binding!!

    private val viewModel: HomeViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = RvIconGridBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHomeIconRvAdapter()

    }

    private fun setUpHomeIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvIconGrid.layoutManager = rvLayoutManager
        val rvAdapter = IconGridAdapter(IconGridAdapter.GridIconClickListener {
            findNavController().navigate(it)
        })
        binding.rvIconGrid.adapter = rvAdapter
        viewModel.iconCount.observe(viewLifecycleOwner) {
            rvAdapter.submitList(IconDataset.getIconDataset(it[0]))
        }
    }
}