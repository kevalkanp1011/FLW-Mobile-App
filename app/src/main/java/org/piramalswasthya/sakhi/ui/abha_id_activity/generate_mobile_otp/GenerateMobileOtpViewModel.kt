package org.piramalswasthya.sakhi.ui.abha_id_activity.generate_mobile_otp

import androidx.lifecycle.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaCheckAndGenerateMobileOtpResponse
import org.piramalswasthya.sakhi.network.AbhaGenerateMobileOtpRequest
import org.piramalswasthya.sakhi.network.CreateAbhaIdRequest
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

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val txnIdFromArgs =
        GenerateMobileOtpFragmentArgs.fromSavedStateHandle(savedStateHandle).txnId

    private var _apiResponse: AbhaCheckAndGenerateMobileOtpResponse? = null
    val apiResponse: AbhaCheckAndGenerateMobileOtpResponse
        get() = _apiResponse!!

    fun generateOtpClicked(phoneNumber: String) {
        _state.value = State.LOADING
        generateMobileOtp(phoneNumber)
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    private fun generateMobileOtp(phoneNumber: String) {
        viewModelScope.launch {
            val result = abhaIdRepo.checkAndGenerateOtpForMobileNumber(
                AbhaGenerateMobileOtpRequest(
                    phoneNumber,
                    txnIdFromArgs
                )
            )
            when (result) {
                is NetworkResult.Success -> {
                    _apiResponse = result.data
                    _state.value = State.SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.message
                    _state.value = State.ERROR_SERVER
                }
                is NetworkResult.NetworkError -> {
                    _state.value = State.ERROR_NETWORK
                }
            }
        }
    }

    fun getCreateRequest(): String {
        val createRequest = CreateAbhaIdRequest(
            null, null, null, null, null, null, null, txnIdFromArgs
        )
        return Gson().toJson(createRequest)
    }
}