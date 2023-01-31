package org.piramalswasthya.sakhi.ui.home_activity.child_care.child_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class ChildListFragment : Fragment() {

    companion object {
        fun newInstance() = ChildListFragment()
    }

    private lateinit var viewModel: ChildListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChildListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}