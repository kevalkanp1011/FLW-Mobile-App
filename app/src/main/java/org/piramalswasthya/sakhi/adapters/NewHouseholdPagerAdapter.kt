package org.piramalswasthya.sakhi.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration.NewHouseholdFormObjectFragment


class NewHouseholdPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        const val ARG_OBJECT_INDEX = "INDEX"
    }

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = NewHouseholdFormObjectFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT_INDEX, position + 1)
        }
        return fragment
    }


}
