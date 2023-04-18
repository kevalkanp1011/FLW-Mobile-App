package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form.visit

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.configuration.HBNCFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.HbncRepo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HbncVisitViewModel @Inject constructor(
    state: SavedStateHandle,
    private val context: Application,
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

    private val benId = HbncVisitFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbncVisitFragmentArgs.fromSavedStateHandle(state).hhId
    private val nthDay = HbncVisitFragmentArgs.fromSavedStateHandle(state).nthDay

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
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: Flow<String?>
        get() = _errorMessage

    fun resetErrorMessage() {
        viewModelScope.launch {
            _errorMessage.emit(null)
        }
    }

    private val dataset = HBNCFormDataset(nthDay)

    fun submitForm() {
        _state.value = State.LOADING
        val hbncCache = HBNCCache(
            benId = benId,
            hhId = hhId,
            homeVisitDate = nthDay,
            processed = "N",
            syncState = SyncState.UNSYNCED
        )
        dataset.mapVisitValues(hbncCache)
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
                hbnc = database.hbncDao.getHbnc(hhId, benId, nthDay)
            }
            _benName.value = "${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _exists.value = hbnc != null
        }
    }

    suspend fun getFirstPage(): List<FormInput> {
        val firstDay = hbncRepo.getFirstHomeVisit(hhId, benId)
        return dataset.getVisitPage(firstDay)
    }

    fun observerForm(adapter: FormInputAdapter) {
        viewModelScope.launch {
            launch {
                dataset.timesMotherFed24hr.value.collect { input ->
                    input?.toInt()?.let {
                        if (it < 4)
                            _errorMessage.emit(context.getString(R.string.hbnc_mother_num_eat_alert))
                    }
                }
            }
            launch {
                dataset.timesPadChanged.value.collect { input ->
                    input?.toInt()?.let {
                        if (it > 5)
                            _errorMessage.emit(context.getString(R.string.hbnc_mother_num_pad_alert))
                    }
                }
            }
            launch {
                dataset.babyKeptWarmWinter.value.collect { input ->
                    if (input == dataset.babyKeptWarmWinter.entries?.get(1))
                        _errorMessage.emit(context.getString(R.string.hbnc_baby_warm_winter_alert))
                }
            }
            launch {
                dataset.babyBreastFedProperly.value.collect { input ->
                    if (input == dataset.babyBreastFedProperly.entries?.get(1))
                        _errorMessage.emit(context.getString(R.string.hbnc_baby_fed_properly_alert))
                }
            }
            launch {
                dataset.babyCryContinuously.value.collect { input ->
                    if (input == dataset.babyCryContinuously.entries?.get(1))
                        _errorMessage.emit(context.getString(R.string.hbnc_baby_cry_incessant_alert))
                }
            }
            //TODO handle alert for mother temperature after consulting with M@dh@v
            launch {
                dataset.motherWaterDischarge.value.collect { input ->
                    if (input == dataset.motherWaterDischarge.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_mother_foul_discharge_alert))
                }
            }
            launch {
                dataset.motherWaterDischarge.value.collect { input ->
                    if (input == dataset.motherWaterDischarge.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_mother_foul_discharge_alert))
                }
            }
            launch {
                dataset.motherSpeakAbnormalFits.value.collect { input ->
                    if (input == dataset.motherSpeakAbnormalFits.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_mother_speak_abnormal_fits_alert))
                }
            }
            launch {
                dataset.motherNoOrLessMilk.value.collect { input ->
                    if (input == dataset.motherNoOrLessMilk.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_mother_less_no_milk_alert))
                }
            }
            launch {
                dataset.motherBreastProblem.value.collect { input ->
                    if (input == dataset.motherBreastProblem.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_mother_breast_problem_alert))
                }
            }
            launch {
                dataset.babyEyesSwollen.value.collect { input ->
                    if (input == dataset.babyEyesSwollen.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_baby_eye_pus_alert))
                }
            }
            launch {
                dataset.babyWeight.value.collect { input ->
                    try {
                        input?.takeIf { it.length>=3 }?.toDouble()?.let {
                            if(it<1.8)
                                _errorMessage.emit(context.getString(R.string.hbnc_baby_weight_1_8_alert))
                            else if(it<2.5)
                                _errorMessage.emit(context.getString(R.string.hbnc_baby_weight_2_5_alert))
                        }
                    }catch (e : NumberFormatException){
                        Timber.d("NFE raised! ")
                    }

                    if (input == dataset.babyEyesSwollen.entries?.get(0))
                        _errorMessage.emit(context.getString(R.string.hbnc_baby_eye_pus_alert))
                }
            }
            //TODO handle alert for mother temperature after consulting with M@dh@v

            launch {
                dataset.babyReferred.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = listOf(
                            dataset.dateOfBabyReferral,
                            dataset.placeOfBabyReferral,
                        )
                        if (it == dataset.babyReferred.entries?.first()) {
                            if (!list.containsAll(entriesToAdd))
                                list.addAll(list.indexOf(dataset.babyReferred) + 1, entriesToAdd)
                        } else
                            list.removeAll(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.placeOfBabyReferral.value.collect { nullablePlaceOfDeath ->
                    nullablePlaceOfDeath?.let { placeOfDeath ->
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.otherPlaceOfBabyReferral
                        if (placeOfDeath == dataset.placeOfBabyReferral.entries?.let { it[it.size - 1] }) {
                            if (!list.contains(entriesToAdd))
                                list.add(
                                    list.indexOf(dataset.placeOfBabyReferral) + 1,
                                    entriesToAdd
                                )
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.motherReferred.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = listOf(
                            dataset.dateOfMotherReferral,
                            dataset.placeOfMotherReferral,
                        )
                        if (it == dataset.motherReferred.entries?.first()) {
                            if (!list.containsAll(entriesToAdd))
                                list.addAll(list.indexOf(dataset.motherReferred) + 1, entriesToAdd)
                        } else
                            list.removeAll(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.placeOfMotherReferral.value.collect { nullablePlaceOfDeath ->
                    nullablePlaceOfDeath?.let { placeOfDeath ->
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.otherPlaceOfMotherReferral
                        if (placeOfDeath == dataset.placeOfMotherReferral.entries?.let { it[it.size - 1] }) {
                            if (!list.contains(entriesToAdd))
                                list.add(
                                    list.indexOf(dataset.placeOfMotherReferral) + 1,
                                    entriesToAdd
                                )
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }

        }
    }

    fun setAddress(it: String?, adapter: FormInputAdapter) {
//        dataset.contactNumber.value.value = ben.contactNumber.toString()
//        dataset.spouseName.value.value = ben.genDetails?.spouseName
    }

    private fun getDateFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        dateLong?.let {
            return dateFormat.format(dateLong)
        } ?: run {
            return null
        }
    }

    fun setExistingValues() {
        dataset.setExistingValuesForVisitPage(hbnc!!)
    }
}