package org.piramalswasthya.sakhi.ui.login_activity.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepo: UserRepo
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

    fun authUser(username : String, password : String){
        viewModelScope.launch {
            val result = userRepo.authenticateUser(username,password)
            if(result){
                _state.value = State.SUCCESS
            }
            else{
                _state.value = State.ERROR_NETWORK
            }
        }
    }




}