package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AadhaarIdViewModel @Inject constructor() : ViewModel() {
    enum class State {
        IDLE,
        LOADING,
        ERROR_SERVER,
        ERROR_NETWORK,
        SUCCESS
    }

    private val _state = MutableLiveData(State.LOADING)
    val state: LiveData<State>
        get() = _state

    fun generateOtpClicked() {
        _state.value = State.LOADING
    }
}