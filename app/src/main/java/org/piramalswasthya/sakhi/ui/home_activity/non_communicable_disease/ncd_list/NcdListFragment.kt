package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class NcdListFragment : Fragment() {

    companion object {
        fun newInstance() = NcdListFragment()
    }

    private lateinit var viewModel: NcdListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ncd_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NcdListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}