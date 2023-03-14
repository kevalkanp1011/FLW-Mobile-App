package org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import javax.inject.Inject

@HiltViewModel
class GenerateMobileOtpViewModel @Inject constructor(
    private val abhaIdRepo: AbhaIdRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    enum class State {
        IDLE,
        LOADING,
        ERROR_SERVER,
        ERROR_NETWORK,
        SUCCESS
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val txnIdFromArgs =
        GenerateMobileOtpFragmentArgs.fromSavedStateHandle(savedStateHandle).txnId
    private var _txnId: String? = null
    val txnID: String
        get() = _txnId!!

    fun generateOtpClicked(phoneNumber: String) {
        _state.value = State.LOADING
        generateMobileOtp(phoneNumber)
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    private fun generateMobileOtp(phoneNumber: String) {
        viewModelScope.launch {
//            _txnId = abhaIdRepo.generateOtpForMobileNumber(phoneNumber, txnIdFromArgs)
            _txnId = abhaIdRepo.generateOtpForMobileNumberDummy(phoneNumber, txnIdFromArgs)
            _txnId?.also {
                _state.value = State.SUCCESS
            } ?: run {
                _state.value = State.ERROR_NETWORK
            }
        }
    }
}