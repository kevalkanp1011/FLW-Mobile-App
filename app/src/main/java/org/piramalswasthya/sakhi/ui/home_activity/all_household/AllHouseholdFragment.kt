package org.piramalswasthya.sakhi.ui.home_activity.all_household

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.HouseHoldListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentAllHouseholdBinding
import org.piramalswasthya.sakhi.model.HouseHoldBasicDomain

class AllHouseholdFragment : Fragment() {

    companion object {
        fun newInstance() = AllHouseholdFragment()
    }

    private val binding : FragmentAllHouseholdBinding by lazy{
        FragmentAllHouseholdBinding.inflate(layoutInflater)
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
        val householdAdapter = HouseHoldListAdapter(HouseHoldListAdapter.HouseholdClickListener {
            Toast.makeText(context,"Clicked $it",Toast.LENGTH_SHORT).show()
        })
        binding.rvHousehold.adapter = householdAdapter
        householdAdapter.submitList(listOf(
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),
            HouseHoldBasicDomain(93256934795823715,"Head", "Tail"),

        ))

        binding.btnNhhr.setOnClickListener {
            findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment())
        }
    }

}