package org.piramalswasthya.sakhi.ui.home_activity.mother_care.pnc_mother_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class PncMotherListFragment : Fragment() {

    companion object {
        fun newInstance() = PncMotherListFragment()
    }

    private lateinit var viewModel: PncMotherListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pnc_mother_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PncMotherListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}