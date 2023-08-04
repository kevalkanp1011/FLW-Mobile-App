package org.piramalswasthya.sakhi.ui.home_activity.maternal_health.infant_reg.form

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.InfantRegistrationDataset
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.DeliveryOutcomeCache
import org.piramalswasthya.sakhi.model.InfantRegCache
import org.piramalswasthya.sakhi.model.PregnantWomanRegistrationCache
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.DeliveryOutcomeRepo
import org.piramalswasthya.sakhi.repositories.InfantRegRepo
import org.piramalswasthya.sakhi.repositories.MaternalHealthRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InfantRegViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceDao: PreferenceDao,
    @ApplicationContext context: Context,
    private val infantRegRepo: InfantRegRepo,
    private val deliveryOutcomeRepo: DeliveryOutcomeRepo,
    private val maternalHealthRepo: MaternalHealthRepo,
    private val benRepo: BenRepo
) : ViewModel() {
    val benId =
        InfantRegFragmentArgs.fromSavedStateHandle(savedStateHandle).benId

    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender

    private val _recordExists = MutableLiveData<Boolean>()
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    private val dataset =
        InfantRegistrationDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow

    private lateinit var infantReg: InfantRegCache
    private var deliveryOutcome: DeliveryOutcomeCache? = null
    private var pwrCache: PregnantWomanRegistrationCache? = null

    init {
        viewModelScope.launch {
            val ben = benRepo.getBenFromId(benId)?.also { ben ->
                _benName.value =
                    "${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}"
                _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
                infantReg = InfantRegCache(
                    benId = ben.beneficiaryId,
                    syncState = SyncState.UNSYNCED
                )
            }

            infantRegRepo.getInfantReg(benId)?.let {
                infantReg = it
                _recordExists.value = true
            } ?: run {
                _recordExists.value = false
            }

            deliveryOutcomeRepo.getDeliveryOutcome(benId)?.let {
                deliveryOutcome = it
            }
            maternalHealthRepo.getSavedRegistrationRecord(benId)?.let {
                pwrCache = it
            }

            dataset.setUpPage(
                ben,
                deliveryOutcome,
                pwrCache,
                if (recordExists.value == true) infantReg else null
            )
        }
    }

    fun updateListOnValueChanged(formId: Int, index: Int) {
        viewModelScope.launch {
            dataset.updateList(formId, index)
        }

    }

    fun saveForm() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _state.postValue(State.SAVING)
                    dataset.mapValues(infantReg, 1)
                    infantRegRepo.saveInfantReg(infantReg)
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: Exception) {
                    Timber.d("saving infant registration data failed!!")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }
    }

    fun resetState() {
        _state.value = State.IDLE
    }

}
