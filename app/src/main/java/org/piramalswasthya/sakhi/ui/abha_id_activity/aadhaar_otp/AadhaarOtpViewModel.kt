package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_otp

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaGenerateAadhaarOtpRequest
import org.piramalswasthya.sakhi.network.AbhaVerifyAadhaarOtpRequest
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import javax.inject.Inject

@HiltViewModel
class AadhaarOtpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val abhaIdRepo: AbhaIdRepo
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        ERROR_SERVER,
        ERROR_NETWORK,
        OTP_VERIFY_SUCCESS,
        OTP_GENERATED_SUCCESS
    }

    private val txnIdFromArgs = AadhaarOtpFragmentArgs.fromSavedStateHandle(savedStateHandle).txnId
    private val aadhaarNumber =
        AadhaarOtpFragmentArgs.fromSavedStateHandle(savedStateHandle).aadhaarNum
    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private var _txnId: String? = null
    val txnId: String
        get() = _txnId!!

    fun verifyOtpClicked(otp: String) {
        _state.value = State.LOADING
        verifyAadhaarOtp(otp)
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    private fun verifyAadhaarOtp(otp: String) {
        viewModelScope.launch {
            _txnId = abhaIdRepo.verifyOtpForAadhaar(
                AbhaVerifyAadhaarOtpRequest(
                    otp,
                    txnIdFromArgs
                )
            )?.txnId
            _txnId?.also {
                _state.value = State.OTP_VERIFY_SUCCESS
            } ?: run {
                _state.value = State.ERROR_NETWORK
            }
        }
    }

    fun resendOtp() {
        _state.value = State.LOADING
        viewModelScope.launch {
            _txnId =
                abhaIdRepo.generateOtpForAadhaar(AbhaGenerateAadhaarOtpRequest(aadhaarNumber))?.txnId
            _txnId?.also {
                _state.value = State.OTP_GENERATED_SUCCESS
            } ?: run {
                _state.value = State.ERROR_NETWORK
            }
        }
    }

}