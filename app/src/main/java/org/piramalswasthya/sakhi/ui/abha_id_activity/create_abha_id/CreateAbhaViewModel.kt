package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.CreateAbhaIdRequest
import org.piramalswasthya.sakhi.network.CreateAbhaIdResponse
import org.piramalswasthya.sakhi.network.NetworkResult
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertAbhaInterceptor
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateAbhaViewModel @Inject constructor(
    private val abhaIdRepo: AbhaIdRepo, savedStateHandle: SavedStateHandle
) : ViewModel() {
    enum class State {
        IDLE, LOADING, ERROR_NETWORK, ERROR_SERVER, GENERATE_SUCCESS, DOWNLOAD_SUCCESS
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    var abha = MutableLiveData<CreateAbhaIdResponse?>(null)

    private val txnIdFromArgs = CreateAbhaFragmentArgs.fromSavedStateHandle(savedStateHandle).txnId

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    init {
        _state.value = State.LOADING
        generateAbhaCard()
    }

    private fun generateAbhaCard() {
        viewModelScope.launch {
            val result = abhaIdRepo.generateAbhaId(
                CreateAbhaIdRequest(
                    null, null, null, null, null, null, null, txnIdFromArgs
                )
            )
            when (result) {
                is NetworkResult.Success -> {
                    TokenInsertAbhaInterceptor.setXToken(result.data.token)
                    abha.value = result.data
                    _state.value = State.GENERATE_SUCCESS
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

    private fun downloadAbhaCard(context: Context) {
        viewModelScope.launch {
            Timber.d("Download Abha Card")
            val result = abhaIdRepo.getPdfCard(context)
            _state.value = State.DOWNLOAD_SUCCESS
        }
    }

    fun resetState() {
        _state.value = State.IDLE
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun downloadAbhaClicked(context: Context) {
        Timber.d("Download Abha clicked")
        _state.value = State.LOADING
        downloadAbhaCard(context)
    }
}