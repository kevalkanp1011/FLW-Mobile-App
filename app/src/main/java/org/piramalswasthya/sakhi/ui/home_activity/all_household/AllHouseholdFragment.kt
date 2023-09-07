package org.piramalswasthya.sakhi.ui.home_activity.all_household

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.HouseHoldListAdapter
import org.piramalswasthya.sakhi.databinding.AlertNewBenBinding
import org.piramalswasthya.sakhi.databinding.FragmentDisplaySearchRvButtonBinding
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import timber.log.Timber


@AndroidEntryPoint
class AllHouseholdFragment : Fragment() {

    private var _binding: FragmentDisplaySearchRvButtonBinding? = null

    private val binding: FragmentDisplaySearchRvButtonBinding
        get() = _binding!!

    private val viewModel: AllHouseholdViewModel by viewModels()


    private var hasDraft = false


    private val draftLoadAlert by lazy {
        MaterialAlertDialogBuilder(requireContext()).setTitle(resources.getString(R.string.incomplete_form_found))
            .setMessage(resources.getString(R.string.do_you_want_to_continue_with_previous_form_or_create_a_new_form_and_discard_the_previous_form))
            .setPositiveButton(resources.getString(R.string.open_draft)) { dialog, _ ->
                viewModel.navigateToNewHouseholdRegistration(false)
                dialog.dismiss()
            }.setNegativeButton(resources.getString(R.string.create_new)) { dialog, _ ->
                viewModel.navigateToNewHouseholdRegistration(true)
                dialog.dismiss()
            }.create()
    }

    private val addBenAlert by lazy {
        val addBenAlertBinding = AlertNewBenBinding.inflate(layoutInflater, binding.root, false)
        addBenAlertBinding.rgGender.setOnCheckedChangeListener { radioGroup, i ->
            addBenAlertBinding.btnOk.isEnabled = false
            Timber.d("RG Gender selected id : $i")
            addBenAlertBinding.cvRelToHead.visibility = when (i) {
                addBenAlertBinding.rbMale.id -> View.VISIBLE
                addBenAlertBinding.rbFemale.id -> View.VISIBLE
                addBenAlertBinding.rbTrans.id -> View.VISIBLE
                else -> View.GONE
            }
            addBenAlertBinding.actvRth.text = null
            val isHoFUnmarried =
                viewModel.householdBenList.firstOrNull { it.householdId == viewModel.selectedHouseholdId }
                    ?.let {
                        (it.genDetails?.maritalStatusId == 1)
                    } ?: false
            val dropdownList = when (i) {
                addBenAlertBinding.rbMale.id -> resources.getStringArray(R.array.nbr_relationship_to_head_male)
                addBenAlertBinding.rbFemale.id -> resources.getStringArray(R.array.nbr_relationship_to_head_female)
                addBenAlertBinding.rbTrans.id -> resources.getStringArray(R.array.nbr_relationship_to_head)
                else -> null
            }?.toMutableList()?.apply {
                if (isHoFUnmarried)
                    removeAll(
                        resources.getStringArray(R.array.nbr_relationship_to_head_unmarried_filter).toSet()
                    )
            }
            dropdownList?.let {
                addBenAlertBinding.actvRth.setAdapter(
                    ArrayAdapter(
                        requireContext(), android.R.layout.simple_spinner_dropdown_item,
                        it,
                    )
                )
            }
        }
        addBenAlertBinding.actvRth.setOnItemClickListener { adapterView, view, i, l ->
            Timber.d("item clicked index : $i")
            addBenAlertBinding.btnOk.isEnabled = true
        }


        val alert = MaterialAlertDialogBuilder(requireContext()).setView(addBenAlertBinding.root)
            .setOnCancelListener {
                viewModel.resetSelectedHouseholdId()
                addBenAlertBinding.rgGender.clearCheck()
                addBenAlertBinding.cvRelToHead.visibility = View.GONE
                addBenAlertBinding.actvRth.text = null
            }.create()

        addBenAlertBinding.btnOk.setOnClickListener {
            findNavController().navigate(
                AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewBenRegFragment(
                    hhId = viewModel.selectedHouseholdId,
                    relToHeadId = resources.getStringArray(R.array.nbr_relationship_to_head_src)
                        .indexOf(addBenAlertBinding.actvRth.text.toString()),
                    gender = when (addBenAlertBinding.rgGender.checkedRadioButtonId) {
                        addBenAlertBinding.rbMale.id -> 1
                        addBenAlertBinding.rbFemale.id -> 2
                        addBenAlertBinding.rbTrans.id -> 3
                        else -> 0
                    }
                )
            )
            viewModel.resetSelectedHouseholdId()
            alert.cancel()
        }
        addBenAlertBinding.btnCancel.setOnClickListener {
            alert.cancel()
            viewModel.resetSelectedHouseholdId()
        }

        alert
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplaySearchRvButtonBinding.inflate(layoutInflater, container, false)
        viewModel.checkDraft()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            (it as HomeActivity).updateActionBar(R.drawable.ic__hh)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextPage.text = resources.getString(R.string.btn_text_frag_home_nhhr)
        binding.tvEmptyContent.text = resources.getString(R.string.no_records_found_hh)
        val householdAdapter = HouseHoldListAdapter(HouseHoldListAdapter.HouseholdClickListener({
            findNavController().navigate(
                AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment(
                    it
                )
            )
        }, {
            findNavController().navigate(
                AllHouseholdFragmentDirections.actionAllHouseholdFragmentToHouseholdMembersFragment(
                    it
                )
            )
        }, {
            if (it.numMembers == 0) {
                findNavController().navigate(
                    AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewBenRegFragment(
                        it.hhId,
                        18
                    )
                )
            } else {
                viewModel.setSelectedHouseholdId(it.hhId)
                addBenAlert.show()
            }

        }))
        binding.rvAny.adapter = householdAdapter

        lifecycleScope.launch {
            viewModel.householdList.collect {
                if (it.isEmpty()) binding.flEmpty.visibility = View.VISIBLE
                else binding.flEmpty.visibility = View.GONE
                householdAdapter.submitList(it)
            }
        }


        viewModel.hasDraft.observe(viewLifecycleOwner) {
            hasDraft = it
        }
        viewModel.navigateToNewHouseholdRegistration.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(AllHouseholdFragmentDirections.actionAllHouseholdFragmentToNewHouseholdFragment())
                viewModel.navigateToNewHouseholdRegistrationCompleted()
            }
        }

        binding.btnNextPage.setOnClickListener {
            if (hasDraft) draftLoadAlert.show()
            else viewModel.navigateToNewHouseholdRegistration(false)
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
            if (b) (searchView as EditText).addTextChangedListener(searchTextWatcher)
            else (searchView as EditText).removeTextChangedListener(searchTextWatcher)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}