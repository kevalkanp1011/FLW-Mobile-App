package org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaGenerateAadhaarOtpRequest
import org.piramalswasthya.sakhi.network.AbhaGenerateAadhaarOtpResponseV2
import org.piramalswasthya.sakhi.network.NetworkResult
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class
AadhaarIdViewModel @Inject constructor(
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

    private var _mobileNumber: String? = null
    val mobileNumber: String
        get() = _mobileNumber!!

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun generateOtpClicked(aadhaarNo: String) {
        _state.value = State.LOADING
        generateAadhaarOtp(aadhaarNo)
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    private fun generateAadhaarOtp(aadhaarNo: String) {
        viewModelScope.launch {
            when (val result =
                abhaIdRepo.generateOtpForAadhaarV2(AbhaGenerateAadhaarOtpRequest(aadhaarNo))) {
                is NetworkResult.Success -> {
                    _txnId = result.data.txnId
                    _mobileNumber = result.data.mobileNumber
                    _state.value = State.SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.message
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
