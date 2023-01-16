package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.adapters.NewHouseholdPagerAdapter
import org.piramalswasthya.sakhi.configuration.HouseHoldFormDataset
import org.piramalswasthya.sakhi.databinding.FragmentNewHouseholdFormObjectBinding

class NewHouseholdFormObjectFragment : Fragment() {

    private val binding : FragmentNewHouseholdFormObjectBinding by lazy{
        FragmentNewHouseholdFormObjectBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageNumber = arguments?.getInt(NewHouseholdPagerAdapter.ARG_OBJECT_INDEX) ?: throw IllegalStateException("No argument passed to viewpager object!")
        when(pageNumber){
            1 -> binding.nhhrForm.rvInputForm.apply {
                val adapter = FormInputAdapter()
                this.adapter = adapter
                adapter.submitList(HouseHoldFormDataset.getFirstPage(context))
            }
        }

    }
}
