package org.piramalswasthya.sakhi.ui.home_activity.all_household

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.HouseHoldListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding

@AndroidEntryPoint
class AllHouseholdFragment : Fragment() {

    private val binding : FragmentDisplaySearchRvButtonBinding by lazy{
        FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater)
    }

    private val viewModel: AllHouseholdViewModel by viewModels()


    private var hasDraft = false

    private val draftLoadAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Incomplete form found")
            .setMessage("Do you want to continue with previous form, or create a new form and discard the previous form?")
            .setPositiveButton("OPEN DRAFT"){
                    dialog,_->
                viewModel.navigateToNewHouseholdRegistration(false)
                dialog.dismiss()
            }
            .setNegativeButton("CREATE NEW"){
                    dialog,_->
                viewModel.navigateToNewHouseholdRegistration(true)
                dialog.dismiss()
            }
            .create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.checkDraft()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnNextPage.text = getString(R.string.btn_text_frag_home_nhhr)
        val householdAdapter = HouseHoldListAdapter(HouseHoldListAdapter.HouseholdClickListener( {
            Toast.makeText(context,"Clicked $it",Toast.LENGTH_SHORT).show()
        },{
            Toast.makeText(context,"Clicked $it", Toast.LENGTH_SHORT).show()
            findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewBenRegTypeFragment(it))
        }))
        binding.rvAny.adapter = householdAdapter

        viewModel.householdList.observe(viewLifecycleOwner){
            householdAdapter.submitList(it)
        }
        viewModel.hasDraft.observe(viewLifecycleOwner){
            hasDraft = it
        }
        viewModel.navigateToNewHouseholdRegistration.observe(viewLifecycleOwner){
            if(it) {
                findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment())
                viewModel.navigateToNewHouseholdRegistrationCompleted()
            }
        }

        binding.btnNextPage.setOnClickListener {
            if (hasDraft)
                draftLoadAlert.show()
            else
                viewModel.navigateToNewHouseholdRegistration(false)
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