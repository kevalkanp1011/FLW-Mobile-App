package org.piramalswasthya.sakhi.ui.abha_id_activity.create_abha_id

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.CreateAbhaIdRequest
import org.piramalswasthya.sakhi.network.CreateAbhaIdResponse
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import timber.log.Timber
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

    init {
        _state.value = State.LOADING
        generateAbhaCard()
    }

    private fun generateAbhaCard() {
        viewModelScope.launch {
            abha.value = abhaIdRepo.generateAbhaId(
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
            if (abha == null) {
                _state.value = State.ERROR_SERVER
            } else {
                Timber.i(abha.value.toString())
                abha.value?.healthIdNumber?.let { Log.i("CreateAbhaViewModel", it) }
                _state.value = State.GENERATE_SUCCESS
            }
        }
    }

    fun downloadAbhaCard() {}

    fun resetState() {
        _state.value = State.IDLE
    }
}