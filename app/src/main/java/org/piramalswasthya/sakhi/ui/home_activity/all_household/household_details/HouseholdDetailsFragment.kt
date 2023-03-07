package org.piramalswasthya.sakhi.ui.home_activity.all_household.household_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentHouseholdDetailsBinding

@AndroidEntryPoint
class HouseholdDetailsFragment : Fragment() {

    private var _binding : FragmentHouseholdDetailsBinding? = null
    private val binding : FragmentHouseholdDetailsBinding
        get() = _binding!!

    private lateinit var viewModel: HouseholdDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseholdDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}