package org.piramalswasthya.sakhi.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15.NewBenRegL15ObjectFragment
import org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration.NewHouseholdFormObjectFragment
import timber.log.Timber

class NewBenKidPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        const val ARG_OBJECT_INDEX = "INDEX"
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = NewBenRegL15ObjectFragment()
        Timber.d("Adapter created Fragment $fragment")
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT_INDEX, position + 1)
        }
        return fragment
    }


}
