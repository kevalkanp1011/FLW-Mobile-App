package org.piramalswasthya.sakhi.ui.home_activity.hrp_cases

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class HrpCasesFragment : Fragment() {

    companion object {
        fun newInstance() = HrpCasesFragment()
    }

    private lateinit var viewModel: HrpCasesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hrp_cases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HrpCasesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}