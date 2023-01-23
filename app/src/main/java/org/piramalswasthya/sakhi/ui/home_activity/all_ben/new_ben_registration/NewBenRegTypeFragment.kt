package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentNewBenRegTypeBinding

@AndroidEntryPoint
class NewBenRegTypeFragment : Fragment() {

    private val binding by lazy {
        FragmentNewBenRegTypeBinding.inflate(layoutInflater)
    }

    private val hhId by lazy {
        NewBenRegTypeFragmentArgs.fromBundle(requireArguments()).hhId
    }

    private val viewModel: NewBenRegTypeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener {
            when (binding.rgBenType.checkedRadioButtonId) {
                R.id.rb_kid_path -> {
                    findNavController().navigate(NewBenRegTypeFragmentDirections.actionNewBenRegTypeFragmentToNewBenRegL15Fragment(hhId))
                }
                R.id.rb_adult_path -> {
                    findNavController().navigate(NewBenRegTypeFragmentDirections.actionNewBenRegTypeFragmentToNewBenRegG15Fragment(hhId))
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