package org.piramalswasthya.sakhi.ui.home_activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepo : UserRepo
) : ViewModel() {

    private lateinit var _user : UserDomain
    val user : UserDomain
        get() = _user

    init {
        viewModelScope.launch {
            _user = getUserFromRepo()
        }
    }

    private suspend fun getUserFromRepo() : UserDomain {
        return withContext(Dispatchers.IO) {
            userRepo.getLoggedInUser()?:throw IllegalStateException("No Logged in user found in DB!!")
        }
    }
    var location = false
}