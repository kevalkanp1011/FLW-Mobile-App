package org.piramalswasthya.sakhi.ui.home_activity.child_care

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
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding

@AndroidEntryPoint
class ChildCareFragment : Fragment() {

    companion object {
        fun newInstance() = ChildCareFragment()
    }

    private val viewModel: ChildCareViewModel by viewModels()
    private val binding by lazy { RvIconGridBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Use the ViewModel
        setUpChildCareIconRvAdapter()
    }

    private fun setUpChildCareIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, 3)
        binding.rvIconGrid.layoutManager = rvLayoutManager
        binding.rvIconGrid.adapter = IconGridAdapter(
            IconGridAdapter.GridIconClickListener {
            findNavController().navigate(it)
        })
    }
}