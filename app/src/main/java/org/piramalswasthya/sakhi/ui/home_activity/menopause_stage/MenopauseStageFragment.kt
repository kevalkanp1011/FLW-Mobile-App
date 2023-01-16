package org.piramalswasthya.sakhi.ui.home_activity.menopause_stage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class MenopauseStageFragment : Fragment() {

    companion object {
        fun newInstance() = MenopauseStageFragment()
    }

    private lateinit var viewModel: MenopauseStageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menopause_stage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MenopauseStageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}