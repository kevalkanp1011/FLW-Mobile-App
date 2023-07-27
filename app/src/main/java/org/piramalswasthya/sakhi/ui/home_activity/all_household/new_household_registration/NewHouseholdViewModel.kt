package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HouseholdFormDataset
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewHouseholdViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceDao: PreferenceDao,
    @ApplicationContext context: Context,
    private val householdRepo: HouseholdRepo,
    userRepo: UserRepo
) : ViewModel() {

    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }

    private var isConsentAgreed = false

    fun setConsentAgreed() {
        isConsentAgreed = true
    }
    fun getIsConsentAgreed() = isConsentAgreed

    private val hhIdFromArgs = NewHouseholdFragmentArgs.fromSavedStateHandle(savedStateHandle).hhId

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()
    val prevPageButtonVisibility = currentPage.transform {
        emit(it > 1)
    }
    val nextPageButtonVisibility = currentPage.transform {
        emit(it < 3)
    }
    val submitPageButtonVisibility = currentPage.transform {
        emit(it == 3 && recordExists.value == false)
    }


    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val _recordExists = MutableLiveData(hhIdFromArgs > 0)
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    private lateinit var user: User
    private val dataset = HouseholdFormDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow
    private lateinit var household: HouseholdCache


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user = preferenceDao.getLoggedInUser()!!
                val locationRecord = preferenceDao.getLocationRecord()!!
                household = householdRepo.getRecord(hhIdFromArgs) ?: householdRepo.getDraftRecord()
                        ?: HouseholdCache(
                    householdId = 0,
                    ashaId = user.userId,
                    isDraft = true,
                    processed = "N",
                    locationRecord = locationRecord
                )
                currentPage.collect {
                    when (it) {
                        1 -> dataset.setFirstPage(household.family)
                        2 -> {
                            dataset.setSecondPage(household.details)
                            dataset.mapValues(household, 1)
                            householdRepo.persistRecord(household)
                        }
                        3 -> {
                            dataset.setThirdPage(household.amenities)
                            dataset.mapValues(household, 2)
                            householdRepo.persistRecord(household)
                        }
                    }
                }
            }

        }
    }

    fun saveForm() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _state.postValue(State.SAVING)
                    dataset.mapValues(household, 3)
                    household.apply {
                        if (householdId == 0L) {
                            dataset.freezeHouseholdId(household, user.userId)
                            householdRepo.substituteHouseholdIdForDraft(household)
                            serverUpdatedStatus = 1
                            processed = "N"
                        } else {
                            serverUpdatedStatus = 2
                            processed = "U"
                        }

                        if (createdTimeStamp == null) {
                            createdTimeStamp = System.currentTimeMillis()
                            createdBy = user.userName
                        }
                        updatedTimeStamp = System.currentTimeMillis()
                        updatedBy = user.userName
                    }
                    householdRepo.persistRecord(household)
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: Exception) {
                    Timber.d("saving HH data failed!!")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }
    }

    /**
     * Household Id to be passed to Ben registration upon persisting household data to Room
     */
    fun getHHId() = household.householdId

    fun goToPreviousPage() {
        viewModelScope.launch {
            _currentPage.emit(currentPage.value - 1)
        }
    }

    fun goToNextPage() {
        viewModelScope.launch {
            _currentPage.emit(currentPage.value + 1)
        }
    }


    fun updateListOnValueChanged(formId: Int, index: Int) {
        viewModelScope.launch {
            dataset.updateList(formId, index)
        }

    }

    fun setRecordExists(b: Boolean) {
        _recordExists.value = b

    }

    fun updateValueByIdAndReturnListIndex(id: Int, value: String): Int {
        dataset.setValueById(id, value)
        return dataset.getIndexById(id )
    }


//    suspend fun getFirstPage(): List<FormElement> {
//        return dataset.firstPage
//    }

//    suspend fun getThirdPage(adapter: FormInputAdapterOld): List<FormElement> {
//        viewModelScope.launch {
//            launch {
//                dataset.fuelForCookingTrigger.value.collect {
//                    toggleFieldOnTrigger(
//                        dataset.fuelForCookingTrigger, dataset.otherFuelForCooking, it, adapter
//                    )
//                }
//            }
//            launch {
//                dataset.sourceOfWaterTrigger.value.collect {
//                    toggleFieldOnTrigger(
//                        dataset.sourceOfWaterTrigger, dataset.otherSourceOfWater, it, adapter
//                    )
//                }
//            }
//            launch {
//                dataset.sourceOfElectricityTrigger.value.collect {
//                    toggleFieldOnTrigger(
//                        dataset.sourceOfElectricityTrigger, dataset.otherSourceOfElectricity, it, adapter
//                    )
//                }
//            }
//            launch {
//                dataset.availOfToiletTrigger.value.collect {
//                    toggleFieldOnTrigger(
//                        dataset.availOfToiletTrigger, dataset.otherAvailOfToilet, it, adapter
//                    )
//                }
//            }
//
//        }
//        dataset.setThirdPage(household?.amenities)
//    }

//    private fun toggleFieldOnTrigger(
//        causeField: FormInputOld,
//        effectField: FormInputOld,
//        value: String?,
//        adapter: FormInputAdapterOld
//    ) {
//        value?.let {
//            if (it == context.getString(R.string.nhhr_fuel_cooking_7)) {
//                val list = adapter.currentList.toMutableList()
//                if (!list.contains(effectField)) {
//                    list.add(
//                        adapter.currentList.indexOf(causeField) + 1, effectField
//                    )
//                    adapter.submitList(list)
//                }
//            } else {
//                if (adapter.currentList.contains(effectField)) {
//                    val list = adapter.currentList.toMutableList()
//                    list.remove(effectField)
//                    adapter.submitList(list)
//                }
//            }
//        }
//    }


//    fun persistFirstPage() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                household?.let {
//                    dataset.mapValues(it, mTabPosition)
//                    householdRepo.persistRegisterRecord(it)
//                }
//
//            }
//        }
//    }
//
//    fun persistSecondPage() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                householdRepo.persistSecondPage(dataset)
//            }
//        }
//    }
//
//
//    fun saveForm(locationRecord: LocationRecord) {
//        viewModelScope.launch {
//            _state.value = State.SAVING
//            withContext(Dispatchers.IO) {
//                try {
//                    hhId = householdRepo.persistThirdPage(dataset, locationRecord)
//                    _state.postValue(State.SAVE_SUCCESS)
//                } catch (e: Exception) {
//                    Timber.d("saving HH data failed!!")
//                    _state.postValue(State.SAVE_FAILED)
//                }
//            }
//        }
//
//    }
//


}