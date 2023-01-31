package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class InfantListFragment : Fragment() {

    companion object {
        fun newInstance() = InfantListFragment()
    }

    private lateinit var viewModel: InfantListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_infant_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfantListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}