package org.piramalswasthya.sakhi.ui.home_activity.child_care.child_list.hbyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.HbycMonthGridAdapter
import org.piramalswasthya.sakhi.databinding.RvIconGridBinding

@AndroidEntryPoint
class HbycMonthListFragment : Fragment() {

    private var _binding : RvIconGridBinding? = null

    private val viewModel: HbycMonthListViewModel by viewModels()

    private val binding : RvIconGridBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RvIconGridBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHbycIconRvAdapter()
    }

    private fun setUpHbycIconRvAdapter() {
        val rvLayoutManager = GridLayoutManager(context, requireContext().resources.getInteger(R.integer.icon_grid_span))
        binding.rvIconGrid.layoutManager = rvLayoutManager
        val iconAdapter = HbycMonthGridAdapter(
            HbycMonthGridAdapter.HbycIconClickListener {
//                Timber.d("benId : $benId hhId : $hhId $count")
                findNavController().navigate(it)
//                    HbncDayListFragmentDirections.actionHbncDayListFragmentToHbncFragment(hhId, benId, count,))
            })

        binding.rvIconGrid.adapter = iconAdapter

        lifecycleScope.launch{
            viewModel.dayList.collect{
                iconAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}