package org.piramalswasthya.sakhi.ui.home_activity.all_ben

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
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding
import org.piramalswasthya.sakhi.ui.home_activity.all_household.AllHouseholdFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class AllBenFragment : Fragment() {


    private val binding: FragmentDisplaySearchRvButtonBinding by lazy {
        FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater)
    }

    private val viewModel: AllBenViewModel by viewModels()

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnNextPage.visibility = View.GONE
        val benAdapter = BenListAdapter(BenListAdapter.BenClickListener(
            {
                Toast.makeText(context, "Ben : $it clicked", Toast.LENGTH_SHORT).show()


            },
            {
                Toast.makeText(context, "Household : $it clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    AllBenFragmentDirections.actionAllBenFragmentToNewBenRegTypeFragment(
                        it
                    )
                )
            },
            { hhId, benId ->
                viewModel.manualSync()
            }
        ))
        binding.rvAny.adapter = benAdapter

        viewModel.benList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty())
                binding.flEmpty.visibility = View.VISIBLE
            else
                binding.flEmpty.visibility = View.GONE
            benAdapter.submitList(it)
        }

        binding.btnNextPage.setOnClickListener {
            findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment())
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