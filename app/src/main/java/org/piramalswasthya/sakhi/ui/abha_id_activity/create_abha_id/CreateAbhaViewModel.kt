package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import androidx.lifecycle.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.CreateAbhaIdGovRequest
import org.piramalswasthya.sakhi.network.CreateAbhaIdRequest
import org.piramalswasthya.sakhi.network.CreateAbhaIdResponse
import org.piramalswasthya.sakhi.network.NetworkResult
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertAbhaInterceptor
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import javax.inject.Inject

@HiltViewModel
class CreateAbhaViewModel @Inject constructor(
    private val abhaIdRepo: AbhaIdRepo, savedStateHandle: SavedStateHandle
) : ViewModel() {
    enum class State {
        IDLE, LOADING, ERROR_NETWORK, ERROR_SERVER, ERROR_INTERNAL, GENERATE_SUCCESS, DOWNLOAD_SUCCESS
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    var abha = MutableLiveData<CreateAbhaIdResponse?>(null)

    private val abhaResponse =
        CreateAbhaFragmentArgs.fromSavedStateHandle(savedStateHandle).abhaResponse

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    init {
        _state.value = State.LOADING
        val abhaVal = Gson().fromJson(abhaResponse, CreateAbhaIdResponse::class.java)
        TokenInsertAbhaInterceptor.setXToken(abhaVal.token)
        abha.value = abhaVal
        _state.value = State.GENERATE_SUCCESS
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}