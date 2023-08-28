package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_form

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.AgeUnit
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.BenRegKid
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15.NewBenRegL15FragmentArgs
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15.NewBenRegL15FragmentDirections
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewBenRegViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceDao: PreferenceDao,
    @ApplicationContext context: Context,
    private val benRepo: BenRepo,
    userRepo: UserRepo
) : ViewModel() {
    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }
    private val hhId = NewBenRegL15FragmentArgs.fromSavedStateHandle(savedStateHandle).hhId
    private val benIdFromArgs =
        NewBenRegL15FragmentArgs.fromSavedStateHandle(savedStateHandle).benId

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()
    val prevPageButtonVisibility = currentPage.transform {
        emit(it > 1)
    }
    val nextPageButtonVisibility = currentPage.transform {
        emit(it < 2)
    }
    val submitPageButtonVisibility = currentPage.transform {
        emit(it == 2 && recordExists.value == false)
    }

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val _recordExists = MutableLiveData(benIdFromArgs > 0)
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    private lateinit var user: User
    private val dataset: BenKidRegFormDataset =
        BenKidRegFormDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow
    private lateinit var household: HouseholdCache
    private lateinit var ben: BenRegCache

    private var lastImageFormId: Int = 0

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user = preferenceDao.getLoggedInUser()!!
                household = benRepo.getHousehold(hhId)!!
                ben = benRepo.getBeneficiaryRecord(benIdFromArgs, hhId) ?: BenRegCache(
                    ashaId = user.userId,
                    beneficiaryId = -2,
                    householdId = hhId,
                    isAdult = false,
                    isKid = true,
                    isDraft = true,
                    kidDetails = BenRegKid(),
                    syncState = SyncState.UNSYNCED,
                    locationRecord = preferenceDao.getLocationRecord()!!
                )
                currentPage.collect {
                    when (it) {
                        1 -> dataset.setFirstPage(ben, household.family?.familyHeadPhoneNo)
                        2 -> {
                            dataset.setSecondPage(ben)
//                            dataset.mapValues(household, 1)
//                            householdRepo.persistRegisterRecord(household)
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
                    dataset.mapValues(ben, 2)
                    ben.apply {
                        if (beneficiaryId == -2L) {
                            benRepo.substituteBenIdForDraft(ben)
                            serverUpdatedStatus = 1
                            processed = "N"
                        } else {
                            serverUpdatedStatus = 2
                            processed = "U"
                        }

                        if (createdDate == null) {
                            createdDate = System.currentTimeMillis()
                            createdBy = user.userName
                        }
                        updatedDate = System.currentTimeMillis()
                        updatedBy = user.userName
                    }
                    benRepo.persistRecord(ben)
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: IllegalAccessError) {
                    Timber.d("saving Ben data failed!! $e")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }
    }

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
    fun setCurrentImageFormId(id: Int) {
        lastImageFormId = id
    }

    fun setImageUriToFormElement(dpUri: Uri) {
        dataset.setImageUriToFormElement(lastImageFormId, dpUri)

    }

    fun getNavDirection(): NavDirections {
        return if (ben.ageUnit in arrayOf(AgeUnit.DAYS, AgeUnit.MONTHS) ||( ben.ageUnit == AgeUnit.YEARS && ben.age==1))
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToInfantListFragment()
        else if(ben.age<=Konstants.maxAgeForChild)
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToChildListFragment()
        else
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToAdolescentListFragment()
    }

    fun updateValueByIdAndReturnListIndex ( id : Int, value : String?)  : Int{
        dataset.setValueById(id, value)
        return dataset.getIndexById(id )
    }


}