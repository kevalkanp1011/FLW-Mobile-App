package org.piramalswasthya.sakhi.ui.home_activity.service_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class ServiceTypeViewModel @Inject constructor(
    userRepo : UserRepo
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName : LiveData<String>
        get() = _userName

    private val _stateList = MutableLiveData<List<String>>()
    val stateList : LiveData<List<String>>
        get() = _stateList

    private val _districtList = MutableLiveData<List<String>>()
    val districtList : LiveData<List<String>>
        get() = _districtList

    private val _blockList = MutableLiveData<List<String>>()
    val blockList : LiveData<List<String>>
        get() = _blockList

    private val _villageList = MutableLiveData<List<String>>()
    val villageList : LiveData<List<String>>
        get() = _villageList

    init {
        viewModelScope.launch {
            val user = userRepo.getLoggedInUser()
            _userName.value = user?.userName
            _stateList.value = user?.stateEnglish
            _districtList.value = user?.districtEnglish
            _blockList.value = user?.blockEnglish
            _villageList.value = user?.villageEnglish
        }
    }





}