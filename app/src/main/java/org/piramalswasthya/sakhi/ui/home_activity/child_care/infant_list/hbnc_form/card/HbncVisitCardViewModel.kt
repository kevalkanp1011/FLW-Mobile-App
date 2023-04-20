package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form.card

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HBNCFormDatasetV2
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.HBNCCache
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.UserCache
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.HbncRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HbncVisitCardViewModel @Inject constructor(
    state: SavedStateHandle,
    private val preferenceDao : PreferenceDao,
    private val database: InAppDb,
    private val hbncRepo: HbncRepo,
    private val benRepo: BenRepo
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS,
        FAIL
    }

    private val benId = HbncVisitCardFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbncVisitCardFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache
    private lateinit var household: HouseholdCache
    private lateinit var user: UserCache
    private var hbnc: HBNCCache? = null

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender
    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state
    private val _exists = MutableLiveData<Boolean>()
    val exists: LiveData<Boolean>
        get() = _exists

    private val dataset = HBNCFormDatasetV2(Konstants.hbncCardDay)
//    private val _formList = MutableStateFlow<List<FormInputV2>>(emptyList())
    val formList = dataset.listFlow

    fun submitForm() {
        _state.value = State.LOADING
        val hbncCache = HBNCCache(
            benId = benId,
            hhId = hhId,
            homeVisitDate = Konstants.hbncCardDay,
            processed = "N",
            syncState = SyncState.UNSYNCED
        )
        dataset.mapCardValues(hbncCache)
        Timber.d("saving hbnc: $hbncCache")
        viewModelScope.launch {
            val saved = hbncRepo.saveHbncData(hbncCache)
            if (saved) {
                Timber.d("saved hbnc: $hbncCache")
                _state.value = State.SUCCESS
            } else {
                Timber.d("saving hbnc to local db failed!!")
                _state.value = State.FAIL
            }
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Timber.d("benId : $benId hhId : $hhId")
                ben = benRepo.getBeneficiary(benId, hhId)!!
                household = benRepo.getHousehold(hhId)!!
                user = database.userDao.getLoggedInUser()!!
                hbnc = database.hbncDao.getHbnc(hhId, benId, Konstants.hbncCardDay)
            }
            _benName.value = "${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _exists.value = hbnc != null
            if(hbnc==null) {
                val location = preferenceDao.getLocationRecord()
                location?.let {
                    dataset.setVillageName(it.village)
                    dataset.setBlockName(it.block)
                }
                dataset.setAshaName(user.userName)
            }
            dataset.setCardPageToList(user,ben, null, hbnc?.visitCard)
            Timber.d("Emitting formList")
//            _formList.emit(dataset.list)

        }
    }

//    fun getFirstPage(exists: Boolean): List<FormInput> {
//
//        return dataset.getCardPage(user,ben,null, hbnc?.visitCard, exists)
//    }


//    private fun getDateFromLong(dateLong: Long?): String? {
//        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
//        dateLong?.let {
//            return dateFormat.format(dateLong)
//        } ?: run {
//            return null
//        }
//    }

     fun updateListOnValueChanged(formId: Int, index: Int) {
//        dataset.handleListOnValueChanged(Konstants.hbncCardDay, formId, index)
    }

//    private fun setExistingValues() {
//        dataset.setExistingValuesForCardPage(hbnc!!)
//    }
}