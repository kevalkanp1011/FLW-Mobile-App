package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.AllBenFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class EligibleCoupleFragment : Fragment() {

    companion object {
        fun newInstance() = EligibleCoupleFragment()
    }

    private val binding: FragmentDisplaySearchRvButtonBinding by lazy {
        FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater)
    }

    private val viewModel: EligibleCoupleViewModel by viewModels()

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnNextPage.visibility = View.GONE
        val benAdapter = BenListAdapter(
            BenListAdapter.BenClickListener(
            {
                Toast.makeText(context, "Ben : $it clicked", Toast.LENGTH_SHORT).show()
            },
            {
                Toast.makeText(context, "Household : $it clicked", Toast.LENGTH_SHORT).show()
            },
            { hhId, benId ->
                viewModel.manualSync(hhId, benId, homeViewModel.getLocationRecord())
            }
        ))
        binding.rvAny.adapter = benAdapter

        viewModel.eligibleCoupleList.observe(viewLifecycleOwner){
            benAdapter.submitList(it)
        }
    }

}