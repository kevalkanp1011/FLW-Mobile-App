package org.piramalswasthya.sakhi.ui.home_activity.immunization_due

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class ImmunizationDueFragment : Fragment() {

    companion object {
        fun newInstance() = ImmunizationDueFragment()
    }

    private lateinit var viewModel: ImmunizationDueViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_immunization_due, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImmunizationDueViewModel::class.java)
        // TODO: Use the ViewModel
    }

}