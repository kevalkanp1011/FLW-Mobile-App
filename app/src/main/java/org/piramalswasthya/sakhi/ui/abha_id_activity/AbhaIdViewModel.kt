package org.piramalswasthya.sakhi.ui.abha_id_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaTokenResponse
import org.piramalswasthya.sakhi.network.NetworkResult
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertAbhaInterceptor
import org.piramalswasthya.sakhi.repositories.AbhaIdRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AbhaIdViewModel @Inject constructor(
    private val abhaIdRepo: AbhaIdRepo
) :
    ViewModel() {

    enum class State {
        LOADING_TOKEN,
        ERROR_SERVER,
        ERROR_NETWORK,
        SUCCESS
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        generateAccessToken()
    }


    private var _accessToken: AbhaTokenResponse? = null
    private val accessToken: AbhaTokenResponse
        get() = _accessToken!!

    fun generateAccessToken() {
        _state.value = State.LOADING_TOKEN
        viewModelScope.launch {
            when (val result = abhaIdRepo.getAccessToken()) {
                is NetworkResult.Success -> {
                    _accessToken = result.data
                    _state.value = State.SUCCESS
                    TokenInsertAbhaInterceptor.setToken(accessToken.accessToken)
                    Timber.i(accessToken.toString())
                }
                is NetworkResult.Error -> {
                    _state.value = State.ERROR_SERVER
                    _errorMessage.value = result.message
                }
                is NetworkResult.NetworkError -> {
                    _state.value = State.ERROR_NETWORK
                }
            }
        }
    }
}