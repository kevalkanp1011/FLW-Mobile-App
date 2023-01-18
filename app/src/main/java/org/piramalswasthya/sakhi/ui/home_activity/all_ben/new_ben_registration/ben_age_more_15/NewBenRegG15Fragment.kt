package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_more_15

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class NewBenRegG15Fragment : Fragment() {

    companion object {
        fun newInstance() = NewBenRegG15Fragment()
    }

    private lateinit var viewModel: NewBenRegG15ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_ben_reg, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewBenRegG15ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}