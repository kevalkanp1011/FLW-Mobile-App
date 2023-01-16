package org.piramalswasthya.sakhi.ui.home_activity.death_reports

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class DeathReportsFragment : Fragment() {

    companion object {
        fun newInstance() = DeathReportsFragment()
    }

    private lateinit var viewModel: DeathReportsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_death_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DeathReportsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}