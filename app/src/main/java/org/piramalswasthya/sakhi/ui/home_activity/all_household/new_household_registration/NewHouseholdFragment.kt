package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import org.piramalswasthya.sakhi.adapters.NewHouseholdPagerAdapter
import org.piramalswasthya.sakhi.databinding.FragmentNewHouseholdBinding
import timber.log.Timber


class NewHouseholdFragment : Fragment() {

    private val binding by lazy {
        FragmentNewHouseholdBinding.inflate(layoutInflater)
    }
    private var mTabPosition = 0

    private val pageChangeCallback: OnPageChangeCallback by lazy {
        object : OnPageChangeCallback() {

            override fun onPageSelected(i: Int) {
                onPageChange(i)

            }
        }
    }

    private fun onPageChange(i: Int) {
        when (mTabPosition) {
            0 -> {
                binding.btnPrev.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
            }
            1 -> {
                binding.btnNext.visibility = View.VISIBLE
                binding.btnPrev.visibility = View.VISIBLE
            }
            2 -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.GONE
            }
        }
        if (i == mTabPosition) {
            return
        }
        val currentItem = "f${mTabPosition}"
        Timber.d(
            "item : $currentItem Fragment : ${
                childFragmentManager.findFragmentByTag(
                    currentItem
                )
            }"
        )
        val validated =
            (childFragmentManager.findFragmentByTag(currentItem) as NewHouseholdFormObjectFragment).validate()
        if (validated) {
            mTabPosition = i
        } else {
            //binding.tlNhhr.setScrollPosition(mTabPosition, 0f, false)
            binding.vp2Nhhr.currentItem = mTabPosition
        }

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
        binding.tlNhhr.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { onPageChange(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        TabLayoutMediator(binding.tlNhhr, binding.vp2Nhhr) { tab, position ->
            tab.text = when (position) {
                0 -> "Family Details"
                1 -> "Household Details"
                2 -> "Household Amenities"
                else -> "NA"
            }
            tab.view.isClickable = false
        }.attach()

        binding.btnPrev.setOnClickListener {
            onPageChange(mTabPosition+1)
        }
        binding.btnNext.setOnClickListener {
            onPageChange(mTabPosition-1)
        }


    }

    override fun onStart() {
        super.onStart()
        binding.vp2Nhhr.registerOnPageChangeCallback(pageChangeCallback)

    }

    override fun onStop() {
        super.onStop()
        binding.vp2Nhhr.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}
