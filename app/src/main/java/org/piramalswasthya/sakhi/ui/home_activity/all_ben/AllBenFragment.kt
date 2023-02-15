package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding
import org.piramalswasthya.sakhi.ui.home_activity.all_household.AllHouseholdFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class AllBenFragment : Fragment() {


    private val binding: FragmentDisplaySearchRvButtonBinding by lazy {
        FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater)
    }

    private val viewModel: AllBenViewModel by viewModels()

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
        val benAdapter = BenListAdapter(BenListAdapter.BenClickListener(
            {
                Toast.makeText(context, "Ben : $it clicked", Toast.LENGTH_SHORT).show()


            },
            {
                Toast.makeText(context, "Household : $it clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    AllBenFragmentDirections.actionAllBenFragmentToNewBenRegTypeFragment(
                        it
                    )
                )
            },
            { hhId, benId ->
                viewModel.manualSync()
            }
        ))
        binding.rvAny.adapter = benAdapter

        viewModel.benList.observe(viewLifecycleOwner){
            benAdapter.submitList(it)
        }

        binding.btnNextPage.setOnClickListener {
            findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment())
        }
    }
}