package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    fun generateOtpClicked() {
        _state.value = State.LOADING

    }

    fun resetState() {
        _state.value = State.IDLE
    }

    fun setStateSuccess() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(4000)
            }
            _state.value = State.SUCCESS
        }

    }

}