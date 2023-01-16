package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class AllBenFragment : Fragment() {

    companion object {
        fun newInstance() = AllBenFragment()
    }

    private lateinit var viewModel: AllBenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_ben, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllBenViewModel::class.java)
        // TODO: Use the ViewModel
    }

}