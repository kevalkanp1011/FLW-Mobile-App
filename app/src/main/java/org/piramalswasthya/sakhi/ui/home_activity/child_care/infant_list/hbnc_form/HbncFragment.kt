package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R

@AndroidEntryPoint
class HbncFragment : Fragment() {

    companion object {
        fun newInstance() = HbncFragment()
    }

    private lateinit var viewModel: HbncViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }


}