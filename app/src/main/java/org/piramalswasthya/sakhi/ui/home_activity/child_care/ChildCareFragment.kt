package org.piramalswasthya.sakhi.ui.home_activity.child_care

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class ChildCareFragment : Fragment() {

    companion object {
        fun newInstance() = ChildCareFragment()
    }

    private lateinit var viewModel: ChildCareViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_care, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChildCareViewModel::class.java)
        // TODO: Use the ViewModel
    }

}