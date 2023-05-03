package org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapterOld
import org.piramalswasthya.sakhi.configuration.MDSRFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.MdsrRepo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MdsrObjectViewModel @Inject constructor(
    state: SavedStateHandle,
    context: Application,
    private val database: InAppDb,
    private val benRepo: BenRepo,
    private val mdsrRepo: MdsrRepo
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS,
        FAIL
    }

    private val benId = MdsrObjectFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = MdsrObjectFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache
    private lateinit var household: HouseholdCache
    private lateinit var user: UserCache
    private var mdsr: MDSRCache? = null

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender
    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address
    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state
    private val _exists = MutableLiveData<Boolean>()
    val exists: LiveData<Boolean>
        get() = _exists

    private val dataset = MDSRFormDataset(context)

    private fun toggleFieldOnTrigger(
        causeField: FormInputOld,
        effectField: FormInputOld,
        value: String?,
        adapter: FormInputAdapterOld
    ) {
        value?.let {
            if (it == "Maternal") {
                val list = adapter.currentList.toMutableList()
                if(!list.contains(effectField)) {
                    list.add(
                        adapter.currentList.indexOf(causeField) + 1,
                        effectField
                    )
                    adapter.submitList(list)
                }
            } else {
                if (adapter.currentList.contains(effectField)) {
                    val list = adapter.currentList.toMutableList()
                    list.remove(effectField)
                    adapter.submitList(list)
                }
            }
        }
    }

    fun submitForm() {
        _state.value = State.LOADING
        val mdsrCache = MDSRCache(benId = benId, hhId = hhId, processed = "N", createdBy = user.userName , syncState = SyncState.UNSYNCED)
        dataset.mapValues(mdsrCache)
        viewModelScope.launch {
            val saved = mdsrRepo.saveMdsrData(mdsrCache)
            Timber.d("mdsr saved: $mdsrCache")
            if (saved)
                _state.value = State.SUCCESS
            else
                _state.value = State.FAIL
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ben = benRepo.getBeneficiary(benId, hhId)!!
                household = benRepo.getHousehold(hhId)!!
                user = database.userDao.getLoggedInUser()!!
                mdsr = database.mdsrDao.getMDSR(hhId, benId)
            }
            _benName.value = "${ben.firstName} ${if(ben.lastName== null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _address.value = getAddress(household)
            _exists.value = mdsr != null
        }
    }

    fun getAddress(household: HouseholdCache): String {
        val houseNo = household.family?.houseNo
        val wardNo = household.family?.wardNo
        val name = household.family?.wardName
        val mohalla = household.family?.mohallaName
        val district = household.locationRecord.district
        val city = household.locationRecord.village
        val state = household.locationRecord.state

        var address = "$houseNo, $wardNo, $name, $mohalla, $city, $district, $state"
        address = address.replace(", ,", ",")
        address = address.replace(",,", ",")
        address = address.replace(" ,", "")
        address = address.replace("null, ", "")
        address = address.replace(", null", "")

        return address
    }

    suspend fun getFirstPage(adapter: FormInputAdapterOld): List<FormInputOld> {
        viewModelScope.launch {
            dataset.causeOfDeath.value.collect {
                toggleFieldOnTrigger(
                    dataset.causeOfDeath,
                    dataset.reasonOfDeath,
                    it,
                    adapter
                )
            }
        }
        return dataset.firstPage
    }

    fun setAddress(it: String?, adapter: FormInputAdapterOld) {
        dataset.address.value.value = it
        dataset.husbandName.value.value = ben.genDetails?.spouseName
        dataset.date.min = ben.dob
        dataset.dateOfDeath.min = ben.dob
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.address))
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.husbandName))
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
        Timber.d("saved mdsr: $mdsr")
        dataset.dateOfDeath.value.value = getDateFromLong(mdsr?.dateOfDeath)
        dataset.address.value.value = mdsr?.address
        dataset.husbandName.value.value = mdsr?.husbandName
        dataset.causeOfDeath.value.value = if(mdsr?.causeOfDeath == 1) "Maternal" else "Non-maternal"
        dataset.reasonOfDeath.value.value = mdsr?.reasonOfDeath
        dataset.investigationDate.value.value = getDateFromLong(mdsr?.investigationDate)
        dataset.actionTaken.value.value = if(mdsr?.actionTaken == 1) "Yes" else "No"
        dataset.blockMOSign.value.value = mdsr?.blockMOSign
        dataset.date.value.value = getDateFromLong(mdsr?.date)
    }


}