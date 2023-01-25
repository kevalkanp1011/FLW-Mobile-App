package org.piramalswasthya.sakhi.ui.home_activity.service_type

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject

@HiltViewModel
class ServiceTypeViewModel @Inject constructor(
    private val userRepo: UserRepo,
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private val _stateList = MutableLiveData<FormInput>()
    val stateList: LiveData<FormInput>
        get() = _stateList

    private val _districtList = MutableLiveData<FormInput>()
    val districtList: LiveData<FormInput>
        get() = _districtList

    private val _blockList = MutableLiveData<FormInput>()
    val blockList: LiveData<FormInput>
        get() = _blockList

    private val _villageList = MutableLiveData<FormInput>()
    val villageList: LiveData<FormInput>
        get() = _villageList

    fun loadLocationFromDatabase() {
        viewModelScope.launch {
            val user = userRepo.getLoggedInUser()
            _userName.value = user?.userName
            _stateList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = context.getString(R.string.service_type_dd_state_text),
                list = user?.stateEnglish,
                value = MutableStateFlow(if (user?.stateEnglish?.size == 1) user.stateEnglish[0] else null),
                required = true
            )
            _districtList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = context.getString(R.string.service_type_dd_district_text),
                list = user?.districtEnglish,
                value = MutableStateFlow(if (user?.districtEnglish?.size == 1) user.districtEnglish[0] else null),
                required = true
            )
            _blockList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = context.getString(R.string.service_type_dd_block_text),
                list = user?.blockEnglish,
                value = MutableStateFlow(if (user?.blockEnglish?.size == 1) user.blockEnglish[0] else null),
                required = true
            )
            _villageList.value = FormInput(
                inputType = FormInput.InputType.DROPDOWN,
                title = context.getString(R.string.service_type_dd_village_text),
                list = user?.villageEnglish,
                //value = MutableStateFlow(if(user?.villageEnglish?.size==1) user.villageEnglish[0]else null),
                required = true
            )
        }
    }

    fun loadLocation(
        user: UserDomain,
        state: String,
        district: String,
        block: String,
        village: String
    ) {
        _userName.value = user.userName
        _stateList.value = FormInput(
            inputType = FormInput.InputType.DROPDOWN,
            title = context.getString(R.string.service_type_dd_state_text),
            list = user.stateEnglish,
            value = MutableStateFlow(state),
            required = true
        )
        _districtList.value = FormInput(
            inputType = FormInput.InputType.DROPDOWN,
            title = context.getString(R.string.service_type_dd_district_text),
            list = user.districtEnglish,
            value = MutableStateFlow(district),
            required = true
        )
        _blockList.value = FormInput(
            inputType = FormInput.InputType.DROPDOWN,
            title = context.getString(R.string.service_type_dd_block_text),
            list = user.blockEnglish,
            value = MutableStateFlow(block),
            required = true
        )
        _villageList.value = FormInput(
            inputType = FormInput.InputType.DROPDOWN,
            title = context.getString(R.string.service_type_dd_village_text),
            list = user.villageEnglish,
            value = MutableStateFlow(village),
            required = true
        )
    }


}