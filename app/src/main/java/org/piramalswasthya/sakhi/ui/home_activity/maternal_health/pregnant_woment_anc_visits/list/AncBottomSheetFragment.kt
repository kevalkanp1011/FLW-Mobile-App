package org.piramalswasthya.sakhi.ui.home_activity.maternal_health.pregnant_woment_anc_visits.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.AncVisitAdapter
import org.piramalswasthya.sakhi.databinding.BottomSheetAncBinding
import org.piramalswasthya.sakhi.model.AncStatus
import timber.log.Timber

@AndroidEntryPoint
class AncBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAncBinding? = null
    private val binding: BottomSheetAncBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAncBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAnc.adapter =
            AncVisitAdapter(AncVisitAdapter.AncVisitClickListener { benId, visitNumber ->
                findNavController().navigate(
                    PwAncVisitsListFragmentDirections.actionPwAncVisitsFragmentToPwAncFormFragment(
                        benId, visitNumber
                    )
                )
                this.dismiss()
            })
    }


    fun submitListToAncRv(list : List<AncStatus>){
        Timber.d("Called list at bottom sheet $list")
        (_binding?.rvAnc?.adapter as AncVisitAdapter?)?.submitList(list)
    }

    override fun dismiss() {
//        submitListToAncRv(emptyList())
        super.dismiss()
    }

}