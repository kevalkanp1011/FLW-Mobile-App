package org.piramalswasthya.sakhi.ui.home_activity.general_op_care

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity

class GeneralOpCareFragment : Fragment() {

    companion object {
        fun newInstance() = GeneralOpCareFragment()
    }

    private lateinit var viewModel: GeneralOpCareViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_general_op_care, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GeneralOpCareViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onStart() {
        super.onStart()
        activity?.let{
            (it as HomeActivity).setLogo(R.drawable.ic__general_op)
        }
    }

}