package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.CreateAbhaIdRequest
import org.piramalswasthya.sakhi.network.CreateAbhaIdResponse
import org.piramalswasthya.sakhi.network.NetworkResult
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import javax.inject.Inject

@HiltViewModel
class CreateAbhaViewModel @Inject constructor(
    private val abhaIdRepo: AbhaIdRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    enum class State {
        IDLE,
        LOADING,
        ERROR_NETWORK,
        ERROR_SERVER,
        GENERATE_SUCCESS,
        DOWNLOAD_SUCCESS
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    var abha = MutableLiveData<CreateAbhaIdResponse?>(null)

    private val txnIdFromArgs = CreateAbhaFragmentArgs.fromSavedStateHandle(savedStateHandle).txnId

    private var _errorMessage: String? = null
    val errorMessage: String
        get() = _errorMessage!!

    init {
        _state.value = State.LOADING
        generateAbhaCard()
    }

    private fun generateAbhaCard() {
        viewModelScope.launch {
            val result = abhaIdRepo.generateAbhaId(
                CreateAbhaIdRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    txnIdFromArgs
                )
            )
            when (result) {
                is NetworkResult.Success -> {
                    abha.value = result.data
                    abha.value?.healthIdNumber?.let { Log.i("CreateAbhaViewModel", it) }
                    _state.value = State.GENERATE_SUCCESS
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

    fun downloadAbhaCard() {}

    fun resetState() {
        _state.value = State.IDLE
    }
}