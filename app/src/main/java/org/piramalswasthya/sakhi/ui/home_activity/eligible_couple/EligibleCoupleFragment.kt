package org.piramalswasthya.sakhi.ui.home_activity.eligible_couple

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class EligibleCoupleFragment : Fragment() {

    companion object {
        fun newInstance() = EligibleCoupleFragment()
    }

    private lateinit var viewModel: EligibleCoupleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_eligible_couple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EligibleCoupleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}