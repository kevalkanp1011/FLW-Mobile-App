package org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaGenerateMobileOtpRequest
import org.piramalswasthya.sakhi.network.NetworkResult
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

    private var _errorMessage: String? = null
    val errorMessage: String
        get() = _errorMessage!!

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
            val result = abhaIdRepo.generateOtpForMobileNumber(
                AbhaGenerateMobileOtpRequest(
                    phoneNumber,
                    txnIdFromArgs
                )
            )
            when (result) {
                is NetworkResult.Success -> {
                    _txnId = result.data.txnId
                    _state.value = State.SUCCESS
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