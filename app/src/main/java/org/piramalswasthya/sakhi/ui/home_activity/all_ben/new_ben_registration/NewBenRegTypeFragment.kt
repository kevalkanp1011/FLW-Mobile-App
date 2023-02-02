package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.AlertConsentBinding
import org.piramalswasthya.sakhi.databinding.FragmentNewBenRegTypeBinding

@AndroidEntryPoint
class NewBenRegTypeFragment : Fragment() {

    private val binding by lazy {
        FragmentNewBenRegTypeBinding.inflate(layoutInflater)
    }

    private val hhId by lazy {
        NewBenRegTypeFragmentArgs.fromBundle(requireArguments()).hhId
    }

    private var hasDraftKid = false
    private var hasDraftGen = false

    private val consentAlert by lazy {
        val alertBinding = AlertConsentBinding.inflate(layoutInflater,binding.root,false)
        alertBinding.textView4.text = getString(R.string.consent_alert_title)
        alertBinding.checkBox.text = getString(R.string.consent_text)
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(alertBinding.root)
            .setCancelable(false)
            .create()
        alertBinding.btnNegative.setOnClickListener {
            alertDialog.dismiss()
            findNavController().navigateUp()
        }
        alertBinding.btnPositive.setOnClickListener {
            if(alertBinding.checkBox.isChecked) {
                alertDialog.dismiss()
                alertBinding.checkBox.isChecked = false
                viewModel.setConsentAgreed()
            }
            else
                Toast.makeText(context,"Please tick the checkbox", Toast.LENGTH_SHORT).show()
        }
        alertDialog
    }

    private val draftLoadAlert by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Incomplete form found")
            .setMessage("Do you want to continue with previous form, or create a new form and discard the previous form?")
            .setPositiveButton("OPEN DRAFT"){
                    dialog,_->
                val isKid = binding.rgBenType.checkedRadioButtonId==binding.rbKidPath.id
                viewModel.navigateToNewBenRegistration(hhId,false, isKid)
                dialog.dismiss()

            }
            .setNegativeButton("CREATE NEW"){
                    dialog,_->
                val isKid = binding.rgBenType.checkedRadioButtonId==binding.rbKidPath.id
                viewModel.navigateToNewBenRegistration(hhId,true, isKid)
                dialog.dismiss()
            }
            .create()
    }

    private val viewModel: NewBenRegTypeViewModel by viewModels()

    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(context,"Back Pressed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.checkDraft(hhId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.isConsentAgreed)
            consentAlert.show()
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
        viewModel.hasDraftForKid.observe(viewLifecycleOwner){
            this.hasDraftKid = it
        }
        viewModel.hasDraftForGen.observe(viewLifecycleOwner){
            this.hasDraftGen = it
        }
        viewModel.navigateToNewBenKidRegistration.observe(viewLifecycleOwner){
            if(it) {
                findNavController().navigate(
                    NewBenRegTypeFragmentDirections.actionNewBenRegTypeFragmentToNewBenRegL15Fragment(
                        hhId
                    )
                )
                viewModel.navigateToBenKidRegistrationCompleted()
            }
        }
        viewModel.navigateToNewBenGenRegistration.observe(viewLifecycleOwner){
            if(it) {
                findNavController().navigate(
                    NewBenRegTypeFragmentDirections.actionNewBenRegTypeFragmentToNewBenRegG15Fragment(
                        hhId
                    )
                )
                viewModel.navigateToBenGenRegistrationCompleted()
            }
        }

        binding.btnContinue.setOnClickListener {
            when (binding.rgBenType.checkedRadioButtonId) {
                R.id.rb_kid_path -> {
                    if(hasDraftKid)
                        draftLoadAlert.show()
                    else
                        viewModel.navigateToNewBenRegistration(hhId=hhId, delete = false, isKid = true)
                }
                R.id.rb_adult_path -> {
                    if(hasDraftGen)
                        draftLoadAlert.show()
                    else
                        viewModel.navigateToNewBenRegistration(hhId=hhId, delete = false, isKid = false)
                }
                else -> Toast.makeText(
                    context,
                    "Please select type of beneficiary",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

    }

}