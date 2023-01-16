package org.piramalswasthya.sakhi.ui.home_activity.village_level_forms

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.piramalswasthya.sakhi.R

class VillageLevelFormsFragment : Fragment() {

    companion object {
        fun newInstance() = VillageLevelFormsFragment()
    }

    private lateinit var viewModel: VillageLevelFormsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_village_level_forms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VillageLevelFormsViewModel::class.java)
        // TODO: Use the ViewModel
    }
}