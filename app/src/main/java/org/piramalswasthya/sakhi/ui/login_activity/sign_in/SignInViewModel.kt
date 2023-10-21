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
import org.piramalswasthya.sakhi.helpers.NetworkResponse
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val pref: PreferenceDao
) : ViewModel() {

    private val _state: MutableLiveData<NetworkResponse<User?>> =
        MutableLiveData(NetworkResponse.Idle())
    val state: LiveData<NetworkResponse<User?>>
        get() = _state

    fun loginInClicked() {
        _state.value = NetworkResponse.Loading()

    }

    fun authUser(username: String, password: String/* state: String*/) {
        viewModelScope.launch {
            try {   //Temporary Placement - need to move to  assets and load from there.
                _state.value = userRepo.authenticateUser(username, password /*state*/)
//                userRepo.checkAndAddVaccines()
            } catch (e: Exception) {
                _state.value =
                    NetworkResponse.Error("Network Call failed.\nUnknown error : ${e.message} stack-trace : ${e.stackTrace}")
                pref.deleteLoginCred()
                pref.deleteAmritToken()
            }
        }
    }

    fun fetchRememberedUserName(): String? =
        pref.getRememberedUserName()

    fun fetchRememberedPassword(): String? =
        pref.getRememberedPassword()

    fun fetchRememberedState(): String? =
        pref.getRememberedState()


    fun rememberUser(username: String, password: String /*state: String*/) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pref.registerLoginCred(username, password/* state*/)
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