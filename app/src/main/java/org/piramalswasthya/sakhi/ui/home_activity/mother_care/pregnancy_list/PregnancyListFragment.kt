package org.piramalswasthya.sakhi.ui.home_activity.mother_care.pregnancy_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class PregnancyListFragment : Fragment() {

    companion object {
        fun newInstance() = PregnancyListFragment()
    }

    private lateinit var viewModel: PregnancyListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pregnancy_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PregnancyListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}