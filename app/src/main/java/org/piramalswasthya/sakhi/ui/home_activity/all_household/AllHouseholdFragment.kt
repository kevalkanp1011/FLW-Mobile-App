package org.piramalswasthya.sakhi.ui.home_activity.all_household

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.HouseHoldListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding

@AndroidEntryPoint
class AllHouseholdFragment : Fragment() {

    private val binding : FragmentDisplaySearchRvButtonBinding by lazy{
        FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater)
    }

    private val viewModel: AllHouseholdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnNextPage.text = getString(R.string.btn_text_frag_home_nhhr)
        val householdAdapter = HouseHoldListAdapter(HouseHoldListAdapter.HouseholdClickListener( {
            Toast.makeText(context,"Clicked $it",Toast.LENGTH_SHORT).show()
        },{
            Toast.makeText(context,"Clicked $it", Toast.LENGTH_SHORT).show()
            findNavController().navigate(org.piramalswasthya.sakhi.ui.home_activity.all_household.AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewBenRegTypeFragment(it))
        }))
        binding.rvAny.adapter = householdAdapter

        viewModel.householdList.observe(viewLifecycleOwner){
            householdAdapter.submitList(it)
        }

        binding.btnNextPage.setOnClickListener {
            findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment())
        }
    }

}