package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.NewHouseholdPagerAdapter
import org.piramalswasthya.sakhi.databinding.FragmentNewHouseholdBinding


class NewHouseholdFragment : Fragment() {

    private val binding by lazy {
        FragmentNewHouseholdBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vp2Nhhr.adapter = NewHouseholdPagerAdapter(this)
        TabLayoutMediator(binding.tlNhhr,binding.vp2Nhhr){
            tab, position ->
            tab.text = when(position){
                0-> "Family Details"
                1 -> "Household Details"
                2 -> "Household Amenities"
                else -> "NA"
            }
        }.attach()

    }
}