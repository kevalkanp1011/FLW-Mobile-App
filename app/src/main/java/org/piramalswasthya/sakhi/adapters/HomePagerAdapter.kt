package org.piramalswasthya.sakhi.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15.NewBenRegL15ObjectFragment
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeIconsFragment
import org.piramalswasthya.sakhi.ui.home_activity.home.SchedulerFragment
import timber.log.Timber

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when(position){
            1 ->HomeIconsFragment()
            0 -> SchedulerFragment()
            else -> throw IllegalStateException("Index >1 called!")

        }
        Timber.d("Adapter created Fragment $fragment")

        return fragment
    }


}
