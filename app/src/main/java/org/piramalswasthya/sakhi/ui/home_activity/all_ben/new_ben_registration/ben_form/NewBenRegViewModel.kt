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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.BenRegFormDataset
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.AgeUnit
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.BenRegKid
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15.NewBenRegL15FragmentDirections
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewBenRegViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceDao: PreferenceDao,
    @ApplicationContext context: Context,
    private val benRepo: BenRepo,
    private val householdRepo: HouseholdRepo,
    userRepo: UserRepo
) : ViewModel() {
    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }

    private val hhId = NewBenRegFragmentArgs.fromSavedStateHandle(savedStateHandle).hhId
    val isHoF = NewBenRegFragmentArgs.fromSavedStateHandle(savedStateHandle).relToHeadId==0
    private val benIdFromArgs =
        NewBenRegFragmentArgs.fromSavedStateHandle(savedStateHandle).benId

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val _recordExists = MutableLiveData(benIdFromArgs > 0)
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    private lateinit var user: User
    private val dataset: BenRegFormDataset =
        BenRegFormDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow
    private lateinit var household: HouseholdCache
    private lateinit var ben: BenRegCache

    private var lastImageFormId: Int = 0

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user = preferenceDao.getLoggedInUser()!!
                household = benRepo.getHousehold(hhId)!!
                val benIdToSet = minOf(benRepo.getMinBenId()-1L, -1L)
                ben = benRepo.getBeneficiaryRecord(benIdFromArgs, hhId) ?: BenRegCache(
                    ashaId = user.userId,
                    beneficiaryId = benIdToSet,
                    householdId = hhId,
                    isAdult = false,
                    isKid = true,
                    isDraft = true,
                    kidDetails = BenRegKid(),
                    syncState = SyncState.UNSYNCED,
                    locationRecord = preferenceDao.getLocationRecord()!!
                )
                if (isHoF) dataset.setPageForHof(ben, household) else {
                    val hoFBen = household.benId?.let { benRepo.getBenFromId(it) }
                    dataset.setPageForFamilyMember(ben, household, hoFBen)
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
                        if (beneficiaryId <0L) {
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
                    if(isHoF) {
                        household.benId = ben.beneficiaryId
                        householdRepo.updateHousehold(household)
                    }
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: IllegalAccessError) {
                    Timber.d("saving Ben data failed!! $e")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
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
        return if (ben.ageUnit in arrayOf(
                AgeUnit.DAYS,
                AgeUnit.MONTHS
            ) || (ben.ageUnit == AgeUnit.YEARS && ben.age == 1)
        )
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToInfantListFragment()
        else if (ben.age <= Konstants.maxAgeForChild)
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToChildListFragment()
        else
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToAdolescentListFragment()
    }

    fun updateValueByIdAndReturnListIndex(id: Int, value: String?): Int {
        dataset.setValueById(id, value)
        return dataset.getIndexById(id)
    }
    fun getIndexOfAgeAtMarriage() = dataset.getIndexOfAgeAtMarriage()

}