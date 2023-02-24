package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.adapters.NewHouseholdPagerAdapter
import org.piramalswasthya.sakhi.databinding.FragmentInputFormPageBinding
import timber.log.Timber

@AndroidEntryPoint
class NewHouseholdFormObjectFragment : Fragment() {

    private lateinit var binding : FragmentInputFormPageBinding

    private val viewModel : NewHouseholdViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentInputFormPageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageNumber = arguments?.getInt(NewHouseholdPagerAdapter.ARG_OBJECT_INDEX) ?: throw IllegalStateException("No argument passed to viewpager object!")
        when(pageNumber){
            1 -> binding.inputForm.rvInputForm.apply {
                val adapter = FormInputAdapter()
                this.adapter = adapter
                lifecycleScope.launch {
                    adapter.submitList(viewModel.getFirstPage())
                }
            }
            2 -> binding.inputForm.rvInputForm.apply {
                val adapter = FormInputAdapter()
                this.adapter = adapter
                adapter.submitList(viewModel.getSecondPage(adapter))
            }
            3 -> {
                binding.inputForm.rvInputForm.apply {
                    val adapter = FormInputAdapter()
                    this.adapter = adapter
                    adapter.submitList(viewModel.getThirdPage(adapter))
                }
            }
        }

    }

    fun validate(): Boolean {
//        Timber.d("binding $binding rv ${binding.nhhrForm.rvInputForm} adapter ${binding.nhhrForm.rvInputForm.adapter}")
//        return false
        val result = binding.inputForm.rvInputForm.adapter?.let {
            (it as FormInputAdapter).validateInput()
        }
        Timber.d("Validation : $result")
        return if (result == -1)
            true
        else {
            if (result != null) {
                binding.inputForm.rvInputForm.scrollToPosition(result)
            }
            false
        }
    }
}
