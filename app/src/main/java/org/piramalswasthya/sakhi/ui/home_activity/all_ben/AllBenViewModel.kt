package org.piramalswasthya.sakhi.ui.home_activity.all_ben

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class AllBenViewModel @Inject constructor(
    benRepo: BenRepo
) : ViewModel() {


    val benList = benRepo.benList
}