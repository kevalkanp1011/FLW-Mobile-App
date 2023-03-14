package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaGenerateAadhaarOtpRequest
import org.piramalswasthya.sakhi.network.NetworkResult
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import timber.log.Timber
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

    private var _errorMessage: String? = null
    val errorMessage: String
        get() = _errorMessage!!

    fun generateOtpClicked(aadharNo: String) {
        _state.value = State.LOADING
        generateAadhaarOtp(aadharNo)
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    private fun generateAadhaarOtp(aadhaarNo: String) {
        viewModelScope.launch {
            when (val result =
                abhaIdRepo.generateOtpForAadhaar(AbhaGenerateAadhaarOtpRequest(aadhaarNo))) {
                is NetworkResult.Success -> {
                    _txnId = result.data.txnId
                    _state.value = State.SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage = result.message
                    _state.value = State.ERROR_SERVER
                }
                is NetworkResult.NetworkError -> {
                    Timber.i(result.toString())
                    _state.value = State.ERROR_NETWORK
                }
            }
        }
    }
}
