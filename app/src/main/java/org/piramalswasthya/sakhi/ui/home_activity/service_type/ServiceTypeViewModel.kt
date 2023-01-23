package org.piramalswasthya.sakhi.ui.home_activity.service_type

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class ServiceTypeViewModel @Inject constructor(
    userRepo : UserRepo,
    application : Application
) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    val userName : LiveData<String>
        get() = _userName

    private val _stateList = MutableLiveData<FormInput>()
    val stateList : LiveData<FormInput>
        get() = _stateList

    private val _districtList = MutableLiveData<FormInput>()
    val districtList : LiveData<FormInput>
        get() = _districtList

    private val _blockList = MutableLiveData<FormInput>()
    val blockList : LiveData<FormInput>
        get() = _blockList

    private val _villageList = MutableLiveData<FormInput>()
    val villageList : LiveData<FormInput>
        get() = _villageList

    init {
        viewModelScope.launch {

            val user = userRepo.getLoggedInUser()
            _userName.value = user?.userName
            _stateList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = application.getString(R.string.service_type_dd_state_text),
                list = user?.stateEnglish,
                required = true)
            _districtList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = application.getString(R.string.service_type_dd_district_text),
                list = user?.districtEnglish,
                required = true)
            _blockList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = application.getString(R.string.service_type_dd_block_text),
                list = user?.blockEnglish,
                required = true)
            _villageList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = application.getString(R.string.service_type_dd_village_text),
                list =user?.villageEnglish,
                required = true)
        }
    }





}