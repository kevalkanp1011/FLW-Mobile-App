package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.adapters.BenListAdapterForForm
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding

@AndroidEntryPoint
class NcdEligibleListFragment : Fragment() {

    private val binding: FragmentDisplaySearchRvButtonBinding by lazy {
        FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater)
    }

    private val viewModel: NcdEligibleListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launch {
            viewModel.loadUser()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnNextPage.visibility = View.GONE

        val benAdapter = BenListAdapterForForm(
            BenListAdapterForForm.ClickListener(
                {
                    Toast.makeText(context, "Ben : $it clicked", Toast.LENGTH_SHORT).show()

                },
                {
                    Toast.makeText(context, "Household : $it clicked", Toast.LENGTH_SHORT).show()
                },
                { hhId, benId ->
                    viewModel.manualSync()
                },
                { hhId, benId ->
                    findNavController().navigate(
                        NcdEligibleListFragmentDirections.actionNcdEligibleListFragmentToCbacFragment(
                            hhId,
                            benId,
                            viewModel.getUserId()
                        )
                    )
                }
            ), "CBAC Form")
        binding.rvAny.adapter = benAdapter

        viewModel.ncdEligibleList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty())
                binding.flEmpty.visibility = View.VISIBLE
            else
                binding.flEmpty.visibility = View.GONE

            benAdapter.submitList(it)
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.filterText(p0?.toString() ?: "")
            }

        }
        binding.searchView.setOnFocusChangeListener { searchView, b ->
            if (b)
                (searchView as EditText).addTextChangedListener(searchTextWatcher)
            else
                (searchView as EditText).removeTextChangedListener(searchTextWatcher)

        }
    }

}