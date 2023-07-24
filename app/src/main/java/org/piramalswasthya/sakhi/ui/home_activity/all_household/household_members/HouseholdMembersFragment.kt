package org.piramalswasthya.sakhi.ui.home_activity.all_household.household_members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding

@AndroidEntryPoint
class HouseholdMembersFragment : Fragment() {

    private var _binding: FragmentDisplaySearchRvButtonBinding? = null
    private val binding: FragmentDisplaySearchRvButtonBinding
        get() = _binding!!

    private val viewModel: HouseholdMembersViewModel by viewModels()

    private val hhId by lazy{
        HouseholdMembersFragmentArgs.fromBundle(requireArguments()).hhId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextPage.visibility = View.GONE
        val benAdapter = BenListAdapter(
            BenListAdapter.BenClickListener(
                { hhId, benId, isKid ->
                    findNavController().navigate(
                        if (isKid) HouseholdMembersFragmentDirections.actionHouseholdMembersFragmentToNewBenRegL15Fragment(
                            hhId,
                            benId
                        )
                        else
                            HouseholdMembersFragmentDirections.actionHouseholdMembersFragmentToNewBenRegG15Fragment(
                                hhId,
                                benId
                            )
                    )

                },
                {

                },
                {_,_ ->}
            ))
        binding.rvAny.adapter = benAdapter

        viewModel.benList.observe(viewLifecycleOwner) {

            if (it.isEmpty())
                binding.flEmpty.visibility = View.VISIBLE
            else
                binding.flEmpty.visibility = View.GONE
            benAdapter.submitList(it)
        }
    }

}