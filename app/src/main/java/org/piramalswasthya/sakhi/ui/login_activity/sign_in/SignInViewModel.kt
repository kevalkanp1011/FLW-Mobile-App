package org.piramalswasthya.sakhi.ui.login_activity.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val pref: PreferenceDao
) : ViewModel() {
    enum class State {
        IDLE,
        LOADING,
        ERROR_INPUT,
        ERROR_SERVER,
        ERROR_NETWORK,
        SUCCESS
    }

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    fun loginInClicked() {
        _state.value = State.LOADING

    }

    fun authUser(username: String, password: String, state : String) {
        viewModelScope.launch {
            _state.value = userRepo.authenticateUser(username, password, state)
        }
    }

    fun fetchRememberedUserName(): String? =
        pref.getRememberedUserName()

    fun fetchRememberedPassword(): String? =
        pref.getRememberedPassword()

    fun fetchRememberedState() : String? =
        pref.getRememberedState()


    fun rememberUser(username: String, password: String, state : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pref.registerLoginCred(username, password, state)
            }
        }
    }

    fun forgetUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pref.deleteLoginCred()
            }
        }
    }
}