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
import timber.log.Timber

class NewHouseholdFormObjectFragment : Fragment() {

    private lateinit var binding : FragmentNewHouseholdFormObjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentNewHouseholdFormObjectBinding.inflate(layoutInflater,container,false)
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
            2 -> binding.nhhrForm.rvInputForm.apply {
                val adapter = FormInputAdapter()
                this.adapter = adapter
                adapter.submitList(HouseHoldFormDataset.getSecondPage(context))
            }
            3 -> binding.nhhrForm.rvInputForm.apply {
                val adapter = FormInputAdapter()
                this.adapter = adapter
                adapter.submitList(HouseHoldFormDataset.getThirdPage(context))
            }
        }

    }

    fun validate(): Boolean {
        Timber.d("binding $binding rv ${binding.nhhrForm.rvInputForm} adapter ${binding.nhhrForm.rvInputForm.adapter}")
        return false

//        return (binding.nhhrForm.rvInputForm.adapter?.let{
//            (it as FormInputAdapter).validateInput()
//        }?:false)
    }
}
