package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import javax.inject.Inject

@HiltViewModel
class AadhaarIdViewModel @Inject constructor(
    private val abhaIdRepo: AbhaIdRepo
) : ViewModel() {
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

    private var _txnId: String? = null
    val txnId: String
        get() = _txnId!!

    fun generateOtpClicked(aadharNo: String) {
        _state.value = State.LOADING
        generateAadharOtp(aadharNo)


    }

    fun resetState() {
        _state.value = State.IDLE
    }

    private fun generateAadharOtp(aadharNo: String) {
        viewModelScope.launch {
            //_txnId = abhaIdRepo.generateOtpForAadhar(aadharNo)
            _txnId = abhaIdRepo.generateOtpForAadharDummy(aadharNo)
            _txnId?.also {
                _state.value = State.SUCCESS
            } ?: run {
                _state.value = State.ERROR_NETWORK
            }
        }
    }
}