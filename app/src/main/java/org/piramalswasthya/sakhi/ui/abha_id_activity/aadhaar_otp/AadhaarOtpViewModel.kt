package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_otp

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaGenerateAadhaarOtpRequest
import org.piramalswasthya.sakhi.network.AbhaVerifyAadhaarOtpRequest
import org.piramalswasthya.sakhi.network.NetworkResult
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

    private var txnIdFromArgs = AadhaarOtpFragmentArgs.fromSavedStateHandle(savedStateHandle).txnId
    private val aadhaarNumber =
        AadhaarOtpFragmentArgs.fromSavedStateHandle(savedStateHandle).aadhaarNum
    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private var _errorMessage: String? = null
    val errorMessage: String
        get() = _errorMessage!!

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
            val result = abhaIdRepo.verifyOtpForAadhaar(
                AbhaVerifyAadhaarOtpRequest(
                    otp,
                    txnIdFromArgs
                )
            )
            when (result) {
                is NetworkResult.Success -> {
                    _txnId = result.data.txnId
                    _state.value = State.OTP_VERIFY_SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage = result.message
                    _state.value = State.ERROR_SERVER
                }
                is NetworkResult.NetworkError -> {
                    _state.value = State.ERROR_NETWORK
                }
            }
        }
    }

    fun resendOtp() {
        _state.value = State.LOADING
        viewModelScope.launch {
            when (val result =
                abhaIdRepo.generateOtpForAadhaar(AbhaGenerateAadhaarOtpRequest(aadhaarNumber))) {
                is NetworkResult.Success -> {
                    txnIdFromArgs = result.data.txnId
                    _state.value = State.OTP_GENERATED_SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage = result.message
                    _state.value = State.ERROR_SERVER
                }
                is NetworkResult.NetworkError -> {
                    _state.value = State.ERROR_NETWORK
                }
            }
        }
    }

}