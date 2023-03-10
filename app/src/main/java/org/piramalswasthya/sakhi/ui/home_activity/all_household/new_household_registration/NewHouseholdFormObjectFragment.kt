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

    private var _binding: FragmentInputFormPageBinding?= null
    private val binding : FragmentInputFormPageBinding
    get() = _binding!!

    private val viewModel: NewHouseholdViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputFormPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageNumber = arguments?.getInt(NewHouseholdPagerAdapter.ARG_OBJECT_INDEX)
            ?: throw IllegalStateException("No argument passed to viewpager object!")
        viewModel.recordExists.observe(viewLifecycleOwner){
            it?.let{
                val adapter = FormInputAdapter(isEnabled = !it)
                binding.inputForm.rvInputForm.adapter = adapter
                lifecycleScope.launch {
                    when (pageNumber) {
                        1 ->
                            adapter.submitList(viewModel.getFirstPage())
                        2 ->
                            adapter.submitList(viewModel.getSecondPage(adapter))
                        3 ->
                            adapter.submitList(viewModel.getThirdPage(adapter))

                    }
                }
            }
        }
    }

    fun validate(): Boolean {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
