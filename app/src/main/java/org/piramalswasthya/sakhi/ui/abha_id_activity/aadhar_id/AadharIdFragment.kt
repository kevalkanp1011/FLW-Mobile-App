package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhar_id

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R

@AndroidEntryPoint
class AadharIdFragment : Fragment() {

    companion object {
        fun newInstance() = AadharIdFragment()
    }

    private lateinit var viewModel: AadharIdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aadhar_id, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AadharIdViewModel::class.java)
        // TODO: Use the ViewModel
    }

}