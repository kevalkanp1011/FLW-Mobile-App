package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class NcdFragment : Fragment() {

    companion object {
        fun newInstance() = NcdFragment()
    }

    private lateinit var viewModel: NcdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ncd, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NcdViewModel::class.java)
        // TODO: Use the ViewModel
    }

}