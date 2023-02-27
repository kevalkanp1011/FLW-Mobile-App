package org.piramalswasthya.sakhi.ui.home_activity.service_type

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.UserRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ServiceTypeViewModel @Inject constructor(
    private val pref: PreferenceDao,
    private val userRepo: UserRepo,
    application: Application
) : AndroidViewModel(application) {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS
    }

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private val _stateList = MutableLiveData<Array<String>>()
    val stateList: LiveData<Array<String>>
        get() = _stateList
    private val _selectedStateId = MutableLiveData(-1)
    val selectedStateId: LiveData<Int>
        get() = _selectedStateId
    private var _selectedState: String? = null
    val selectedState: String?
        get() = _selectedState

    private val _districtList = MutableLiveData<Array<String>>()
    val districtList: LiveData<Array<String>>
        get() = _districtList
    private val _selectedDistrictId = MutableLiveData(-1)
    val selectedDistrictId: LiveData<Int>
        get() = _selectedDistrictId
    private var _selectedDistrict: String? = null
    val selectedDistrict: String?
        get() = _selectedDistrict

    private val _blockList = MutableLiveData<Array<String>>()
    val blockList: LiveData<Array<String>>
        get() = _blockList
    private val _selectedBlockId = MutableLiveData(-1)
    val selectedBlockId: LiveData<Int>
        get() = _selectedBlockId
    private var _selectedBlock: String? = null
    val selectedBlock: String?
        get() = _selectedBlock

    private val _villageList = MutableLiveData<Array<String>>()
    val villageList: LiveData<Array<String>>
        get() = _villageList
    private val _selectedVillageId = MutableLiveData(-1)
    val selectedVillageId: LiveData<Int>
        get() = _selectedVillageId
    private var _selectedVillage: String? = null
    val selectedVillage: String?
        get() = _selectedVillage


    private lateinit var stateDefaultList: List<String>
    private lateinit var districtDefaultList: List<String>
    private lateinit var blockDefaultList: List<String>
    private lateinit var villageDefaultList: List<String>
    private lateinit var user: UserDomain

    private val currLanguage = pref.getCurrentLanguage()

    private val _state = MutableLiveData(State.LOADING)
    val state: LiveData<State>
        get() = _state

    init {
        viewModelScope.launch {
            user = userRepo.getLoggedInUser()!!
            _userName.value = user.userName
            stateDefaultList = user.stateEnglish
            districtDefaultList = user.districtEnglish
            blockDefaultList = user.blockEnglish
            villageDefaultList = user.villageEnglish
            Timber.d("$stateDefaultList $districtDefaultList $blockDefaultList $villageDefaultList")
            loadLocationFromDatabase()
            _state.value = State.SUCCESS
        }
    }


    private fun loadLocationFromDatabase() {
        if (currLanguage == Languages.ENGLISH) {
            _stateList.value = user.stateEnglish.toTypedArray()
            _districtList.value = user.districtEnglish.toTypedArray()
            _blockList.value = user.blockEnglish.toTypedArray()
            _villageList.value = user.villageEnglish.toTypedArray()
        } else if (pref.getCurrentLanguage() == Languages.HINDI) {
            _stateList.value = if (user.stateHindi.isNotEmpty()) {
                user.stateHindi.toTypedArray()
            } else {
                user.stateEnglish.toTypedArray()
            }
            _districtList.value = if (user.districtHindi.isNotEmpty()) {
                user.districtHindi.toTypedArray()
            } else {
                user.districtEnglish.toTypedArray()
            }
            _blockList.value = if (user.blockHindi.isNotEmpty()) {
                user.blockHindi.toTypedArray()
            } else {
                user.blockEnglish.toTypedArray()
            }
            _villageList.value = if (user.villageHindi.isNotEmpty()) {
                user.villageHindi.toTypedArray()
            } else {
                user.villageEnglish.toTypedArray()
            }
        }
    }

    fun loadLocation(
        location: LocationRecord
    ) {
        setStateId(stateDefaultList.indexOf(location.state))
        setDistrictId(districtDefaultList.indexOf(location.district))
        setBlockId(blockDefaultList.indexOf(location.block))
        setVillageId(villageDefaultList.indexOf(location.village))

    }

    fun loadDefaultLocation() {
        setStateId(0)
        setDistrictId(0)
        setBlockId(0)
        setVillageId(0)

    }

    fun setStateId(i: Int) {
        _selectedStateId.value = i
        _selectedState = stateDefaultList[i]
        Timber.d("${_selectedStateId.value} $_selectedState")

    }

    fun setDistrictId(i: Int) {
        _selectedDistrictId.value = i
        _selectedDistrict = districtDefaultList[i]
    }

    fun setBlockId(i: Int) {
        _selectedBlockId.value = i
        _selectedBlock = blockDefaultList[i]
    }

    fun setVillageId(i: Int) {
        _selectedVillageId.value = i
        _selectedVillage = villageDefaultList[i]
    }


}