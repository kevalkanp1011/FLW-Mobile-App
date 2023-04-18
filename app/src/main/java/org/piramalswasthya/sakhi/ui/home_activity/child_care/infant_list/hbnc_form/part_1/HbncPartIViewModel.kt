package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form.part_1

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
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.HbncRepo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HbncPartIViewModel @Inject constructor(
    private val context: Application,
    state: SavedStateHandle,
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

    private val benId = HbncPartIFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbncPartIFragmentArgs.fromSavedStateHandle(state).hhId
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

    private val dataset = HBNCFormDataset(Konstants.hbncPart1Day)

    fun submitForm() {
        _state.value = State.LOADING
        val hbncCache = HBNCCache(
            benId = benId,
            hhId = hhId,
            homeVisitDate = Konstants.hbncPart1Day,
            processed = "N",
            syncState = SyncState.UNSYNCED
        )
        dataset.mapPartIValues(hbncCache)
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
                hbnc = database.hbncDao.getHbnc(hhId, benId, Konstants.hbncPart1Day)
            }
            _benName.value = "${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _exists.value = hbnc != null


        }
    }

    suspend fun getFirstPage(): List<FormInput> {
        val visitCard = hbncRepo.getHbncCard(benId, hhId)
        return dataset.getPartIPage(visitCard)
    }

    fun observeForm(adapter: FormInputAdapter) {
        viewModelScope.launch {
            launch {
                dataset.babyAlive.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = listOf(
                            dataset.dateOfBabyDeath,
                            dataset.timeOfBabyDeath,
                            dataset.placeOfBabyDeath,
                        )
                        if (it == dataset.babyAlive.entries?.get(1)) {
                            if (!list.containsAll(entriesToAdd))
                                list.addAll(list.indexOf(dataset.babyAlive) + 1, entriesToAdd)
                            _errorMessage.emit(context.getString(R.string.hbnc_baby_dead_alert))
                        } else
                            list.removeAll(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.placeOfBabyDeath.value.collect { nullablePlaceOfDeath ->
                    nullablePlaceOfDeath?.let { placeOfDeath ->
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.otherPlaceOfBabyDeath
                        if (placeOfDeath == dataset.placeOfBabyDeath.entries?.let { it[it.size - 1] }) {
                            if (!list.contains(entriesToAdd))
                                list.add(list.indexOf(dataset.placeOfBabyDeath) + 1, entriesToAdd)
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.motherAlive.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = listOf(
                            dataset.dateOfMotherDeath,
                            dataset.timeOfMotherDeath,
                            dataset.placeOfMotherDeath,
                        )
                        if (it == dataset.motherAlive.entries?.get(1)) {
                            if (!list.contains(dataset.dateOfMotherDeath))
                                list.addAll(list.indexOf(dataset.motherAlive) + 1, entriesToAdd)
                            _errorMessage.emit(context.getString(R.string.hbnc_mother_dead_alert))
                        } else
                            list.removeAll(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.placeOfMotherDeath.value.collect { nullablePlaceOfDeath ->
                    nullablePlaceOfDeath?.let { placeOfDeath ->
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.otherPlaceOfMotherDeath
                        if (placeOfDeath == dataset.placeOfMotherDeath.entries?.let { it[it.size - 1] }) {
                            if (!list.contains(entriesToAdd))
                                list.add(list.indexOf(dataset.placeOfMotherDeath) + 1, entriesToAdd)
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.babyPreterm.value.collect {
                    it?.let {
                        Timber.d("Baby Preterm : $it")
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.gestationalAge
                        if (it == dataset.babyPreterm.entries?.get(0)) {
                            if (!list.contains(dataset.gestationalAge))
                                list.add(list.indexOf(dataset.babyPreterm) + 1, entriesToAdd)
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.gestationalAge.value.collect {
                    it?.let {
                        if (it == dataset.gestationalAge.entries?.get(0)) {
                            _errorMessage.emit(context.getString(R.string.hbnc_baby_gestational_age_alert))
                        }
                    }
                }
            }
            launch {
                dataset.babyFedAfterBirth.value.collect { nullablePlaceOfDeath ->
                    nullablePlaceOfDeath?.let { placeOfDeath ->
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.otherBabyFedAfterBirth
                        if (placeOfDeath == dataset.babyFedAfterBirth.entries?.let { it[it.size - 1] }) {
                            if (!list.contains(entriesToAdd))
                                list.add(list.indexOf(dataset.babyFedAfterBirth) + 1, entriesToAdd)
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.motherHasBreastFeedProblem.value.collect { nullablePlaceOfDeath ->
                    nullablePlaceOfDeath?.let { placeOfDeath ->
                        val list = adapter.currentList.toMutableList()
                        val entriesToAdd = dataset.motherBreastFeedProblem
                        if (placeOfDeath == dataset.motherHasBreastFeedProblem.entries?.first()) {
                            if (!list.contains(entriesToAdd))
                                list.add(
                                    list.indexOf(dataset.motherHasBreastFeedProblem) + 1,
                                    entriesToAdd
                                )
                        } else
                            list.remove(entriesToAdd)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                dataset.motherProblems.value.collect {
                    it?.let {
                        if(it.isNotEmpty()) {
                            Timber.d("Mother any problem emit : $it")
                            _errorMessage.emit(context.getString(R.string.hbnc_mother_problem_alert))
                        }
                    }
                }
            }
        }
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
        dataset.setExistingValuesForPartIPage(hbnc!!)
    }
}

