package org.piramalswasthya.sakhi.ui.home_activity.village_level_forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class VillageLevelFormsViewModel @Inject constructor() : ViewModel() {
    val scope: CoroutineScope
        get() = viewModelScope
}