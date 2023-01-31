package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class NcdEligibleListFragment : Fragment() {

    companion object {
        fun newInstance() = NcdEligibleListFragment()
    }

    private lateinit var viewModel: NcdEligibleListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ncd_eligible_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NcdEligibleListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}