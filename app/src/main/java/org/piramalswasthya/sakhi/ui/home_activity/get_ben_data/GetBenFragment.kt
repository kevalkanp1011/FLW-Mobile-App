package org.piramalswasthya.sakhi.ui.home_activity.get_ben_data

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class GetBenFragment : Fragment() {

    companion object {
        fun newInstance() = GetBenFragment()
    }

    private lateinit var viewModel: GetBenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_ben, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GetBenViewModel::class.java)
        // TODO: Use the ViewModel
    }

}