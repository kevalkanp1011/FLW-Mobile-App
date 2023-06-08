package org.piramalswasthya.sakhi.ui.home_activity.mother_care.pregnant_woment_anc_visits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class PwAncVisitsFragment : Fragment() {

    companion object {
        fun newInstance() = PwAncVisitsFragment()
    }

    private lateinit var viewModel: PwAncVisitsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PwAncVisitsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}