package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentNewBenRegBinding

class NewBenRegL15Fragment : Fragment() {

    private val binding : FragmentNewBenRegBinding by lazy{
        FragmentNewBenRegBinding.inflate(layoutInflater)
    }
    private val viewModel: NewBenRegL15ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}