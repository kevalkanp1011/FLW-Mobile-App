package org.piramalswasthya.sakhi.ui.home_activity.all_household.household_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.piramalswasthya.sakhi.R

class HouseholdDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = HouseholdDetailsFragment()
    }

    private lateinit var viewModel: HouseholdDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_household_details, container, false)
    }

}