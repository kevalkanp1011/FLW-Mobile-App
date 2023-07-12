package org.piramalswasthya.sakhi.ui.service_location_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Languages.ASSAMESE
import org.piramalswasthya.sakhi.helpers.Languages.ENGLISH
import org.piramalswasthya.sakhi.helpers.Languages.HINDI
import org.piramalswasthya.sakhi.model.LocationEntity
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.User
import javax.inject.Inject

@HiltViewModel
class ServiceTypeViewModel @Inject constructor(
    private val pref: PreferenceDao,
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS
    }

    private lateinit var stateDropdownEntry: String
    val stateList: Array<String>
        get() = arrayOf(stateDropdownEntry)

    private lateinit var districtDropdownEntry: String
    val districtList: Array<String>
        get() = arrayOf(districtDropdownEntry)

    private lateinit var blockDropdownEntry: String
    val blockList: Array<String>
        get() = arrayOf(blockDropdownEntry)

    private lateinit var villageDropdownEntries: Array<String>
    val villageList: Array<String>
        get() = villageDropdownEntries

    private lateinit var _userName: String
    val userName: String
        get() = _userName

    private var _selectedVillage: LocationEntity? = null
    val selectedVillage: LocationEntity?
        get() = _selectedVillage
    val selectedVillageName : String?
        get() = when(pref.getCurrentLanguage()){
            ENGLISH -> selectedVillage?.name
            HINDI -> selectedVillage?.nameHindi?:selectedVillage?.name
            ASSAMESE -> selectedVillage?.nameAssamese?:selectedVillage?.name
        }


    private val _state = MutableLiveData(State.LOADING)
    val state: LiveData<State>
        get() = _state


    private var currentLocation: LocationRecord? = null
    private lateinit var user : User

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user = pref.getLoggedInUser()!!
                _userName = user.name
                currentLocation = pref.getLocationRecord()
                _selectedVillage = currentLocation?.village
                when (pref.getCurrentLanguage()) {
                    ENGLISH -> {
                        stateDropdownEntry = user.state.name
                        districtDropdownEntry = user.district.name
                        blockDropdownEntry = user.block.name
                        villageDropdownEntries = user.villages.map { it.name }.toTypedArray()

                    }
                    HINDI -> {
                        stateDropdownEntry =
                            user.state.let { it.nameHindi ?: it.name }
                        districtDropdownEntry =
                            user.district.let { it.nameHindi ?: it.name }
                        blockDropdownEntry =
                            user.block.let { it.nameHindi ?: it.name }
                        villageDropdownEntries =
                            user.villages.map { it.nameHindi ?: it.name }.toTypedArray()
//                        _selectedVillage =
//                            currentLocation?.villages?.let { it.nameHindi ?: it.name }
                    }
                    ASSAMESE -> {
                        stateDropdownEntry =
                            user.state.let { it.nameAssamese ?: it.name }
                        districtDropdownEntry =
                            user.district.let { it.nameAssamese ?: it.name }
                        blockDropdownEntry =
                            user.block.let { it.nameAssamese ?: it.name }
                        villageDropdownEntries =
                            user.villages.map { it.nameAssamese ?: it.name }.toTypedArray()
//                        _selectedVillage =
//                            currentLocation?.villages?.let { it.nameAssamese ?: it.name }
                    }
                }

            }
            _state.value = State.SUCCESS
        }
    }

    fun isLocationSet(): Boolean {
        return if (state.value == State.LOADING)
            false
        else
            currentLocation != null
    }

    fun setVillage(i: Int) {
        _selectedVillage = user.villages[i]

    }

    fun saveCurrentLocation() {
        val locationRecord = LocationRecord(
            LocationEntity(1, "India"),
            user.state,
            user.district,
            user.block,
            selectedVillage!!
        )
        pref.saveLocationRecord(locationRecord)
    }


//
//    private val _userName = MutableLiveData<String>()
//    val userName: LiveData<String>
//        get() = _userName
//
//    private val _stateList = MutableLiveData<Array<String>>()
//    val stateList: LiveData<Array<String>>
//        get() = _stateList
//    private val _selectedStateId = MutableLiveData(-1)
//    val selectedStateId: LiveData<Int>
//        get() = _selectedStateId
//    private var _selectedState: String? = null
//    val selectedState: String?
//        get() = _selectedState
//
//    private val _districtList = MutableLiveData<Array<String>>()
//    val districtList: LiveData<Array<String>>
//        get() = _districtList
//    private val _selectedDistrictId = MutableLiveData(-1)
//    val selectedDistrictId: LiveData<Int>
//        get() = _selectedDistrictId
//    private var _selectedDistrict: String? = null
//    val selectedDistrict: String?
//        get() = _selectedDistrict
//
//    private val _blockList = MutableLiveData<Array<String>>()
//    val blockList: LiveData<Array<String>>
//        get() = _blockList
//    private val _selectedBlockId = MutableLiveData(-1)
//    val selectedBlockId: LiveData<Int>
//        get() = _selectedBlockId
//    private var _selectedBlock: String? = null
//    val selectedBlock: String?
//        get() = _selectedBlock
//
//    private val _villageList = MutableLiveData<Array<String>>()
//    val villageList: LiveData<Array<String>>
//        get() = _villageList
//    private val _selectedVillageId = MutableLiveData(-1)
//    val selectedVillageId: LiveData<Int>
//        get() = _selectedVillageId
//
//
//
//    private lateinit var stateDefaultList: List<String>
//    private lateinit var districtDefaultList: List<String>
//    private lateinit var blockDefaultList: List<String>
//    private lateinit var villageDefaultList: List<String>
//    private lateinit var user: UserDomain
//
//    private val currLanguage = pref.getCurrentLanguage()
//
//
//    init {
//        viewModelScope.launch {
//            user = userRepo.getLoggedInUser()!!
//            _userName.value = user.userName
//            stateDefaultList = user.stateEnglish
//            districtDefaultList = user.districtEnglish
//            blockDefaultList = user.blockEnglish
//            villageDefaultList = user.villageEnglish
//            Timber.d("$stateDefaultList $districtDefaultList $blockDefaultList $villageDefaultList")
//            loadLocationFromDatabase()
//            _state.value = State.SUCCESS
//        }
//    }
//    private fun loadLocationFromDatabase() {
//        if (currLanguage == Languages.ENGLISH) {
//            _stateList.value = user.stateEnglish.toTypedArray()
//            _districtList.value = user.districtEnglish.toTypedArray()
//            _blockList.value = user.blockEnglish.toTypedArray()
//            _villageList.value = user.villageEnglish.toTypedArray()
//        } else if (pref.getCurrentLanguage() == Languages.HINDI) {
//            _stateList.value = if (user.stateHindi.isNotEmpty()) {
//                user.stateHindi.toTypedArray()
//            } else {
//                user.stateEnglish.toTypedArray()
//            }
//            _districtList.value = if (user.districtHindi.isNotEmpty()) {
//                user.districtHindi.toTypedArray()
//            } else {
//                user.districtEnglish.toTypedArray()
//            }
//            _blockList.value = if (user.blockHindi.isNotEmpty()) {
//                user.blockHindi.toTypedArray()
//            } else {
//                user.blockEnglish.toTypedArray()
//            }
//            _villageList.value = if (user.villageHindi.isNotEmpty()) {
//                user.villageHindi.toTypedArray()
//            } else {
//                user.villageEnglish.toTypedArray()
//            }
//        }
//    }
//    fun loadLocation(
//        name: LocationRecord
//    ) {
//        setStateId(stateDefaultList.indexOf(name.state))
//        setDistrictId(districtDefaultList.indexOf(name.district))
//        setBlockId(blockDefaultList.indexOf(name.block))
//        setVillageId(villageDefaultList.indexOf(name.villages))
//
//    }
//    fun loadDefaultLocation() {
//        setStateId(0)
//        setDistrictId(0)
//        setBlockId(0)
//        setVillageId(0)
//
//    }
//    fun setStateId(i: Int) {
//        _selectedStateId.value = i
//        _selectedState = stateDefaultList[i]
//        Timber.d("${_selectedStateId.value} $_selectedState")
//
//    }
//
//    fun setDistrictId(i: Int) {
//        _selectedDistrictId.value = i
//        _selectedDistrict = districtDefaultList[i]
//    }
//
//    fun setBlockId(i: Int) {
//        _selectedBlockId.value = i
//        _selectedBlock = blockDefaultList[i]
//    }
//
//    fun setVillageId(i: Int) {
//        _selectedVillageId.value = i
//        _selectedVillage = villageDefaultList[i]
//    }


}