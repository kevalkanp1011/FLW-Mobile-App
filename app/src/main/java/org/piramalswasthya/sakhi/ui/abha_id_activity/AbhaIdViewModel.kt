package org.piramalswasthya.sakhi.ui.abha_id_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.network.AbhaApiService
import org.piramalswasthya.sakhi.network.AbhaTokenResponse
import timber.log.Timber
import javax.inject.Inject

class AbhaIdViewModel @Inject constructor(private val abhaApiService: AbhaApiService) :
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

    init {
        _state.value = State.LOADING_TOKEN
        retrieveAccessToken()
    }

    private var _accessToken: AbhaTokenResponse? = null
    private val accessToken: AbhaTokenResponse
        get() = _accessToken!!

    private fun retrieveAccessToken() {
        viewModelScope.launch {
            _accessToken = abhaApiService.getToken()
            Timber.i(accessToken.toString())
            _state.value = State.SUCCESS
        }
    }
}