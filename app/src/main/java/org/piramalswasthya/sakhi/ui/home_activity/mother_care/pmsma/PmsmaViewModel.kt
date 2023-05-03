package org.piramalswasthya.sakhi.ui.home_activity.mother_care.pmsma

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapterOld
import org.piramalswasthya.sakhi.configuration.PMSMAFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.PmsmaRepo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PmsmaViewModel @Inject constructor(
    state: SavedStateHandle,
    context: Application,
    private val database: InAppDb,
    private val pmsmaRepo: PmsmaRepo,
    private val benRepo: BenRepo
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS,
        FAIL
    }

    private val benId = PmsmaFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = PmsmaFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache
    private lateinit var household: HouseholdCache
    private lateinit var user: UserCache
    private var pmsma: PMSMACache? = null

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

    private val _popupString = MutableLiveData<String?>(null)
    val popupString: LiveData<String?>
        get() = _popupString

    private val form = PMSMAFormDataset(context)

    private fun toggleFieldOnTrigger(
        causeField: FormInputOld,
        effectField: FormInputOld,
        value: String?,
        triggerValue : String,
        adapter: FormInputAdapterOld
    ) {
        value?.let {
            if (it == triggerValue) {
                val list = adapter.currentList.toMutableList()
                if (!list.contains(effectField)) {
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
        val pmsmaCache = PMSMACache(benId = benId, hhId = hhId, processed = "N")
        form.mapValues(pmsmaCache)
        viewModelScope.launch {
            val saved = pmsmaRepo.savePmsmaData(pmsmaCache)
            if (saved)
                _state.value = State.SUCCESS
            else
                _state.value = State.FAIL
        }
    }

    init {
        Timber.d("init called! ")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ben = benRepo.getBeneficiary(benId, hhId)!!
                household = benRepo.getHousehold(hhId)!!
                user = database.userDao.getLoggedInUser()!!
                Timber.d("pmsma ben: $ben")
                pmsma = database.pmsmaDao.getPmsma(hhId,benId)
                Timber.d("init after assigning pmsma ! ")

            }
            Timber.d("init after IO! ")
            _benName.value = "${ben.firstName} ${if(ben.lastName == null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _address.value = getAddress(household)
            _exists.value = pmsma != null
//           if(pmsma==null){
//               form.lastMenstrualPeriod.value.value = getDateFromLong(ben.genDetails?.lastMenstrualPeriod)
//               form.expectedDateOfDelivery.value.value = getDateFromLong(ben.genDetails?.expectedDateOfDelivery)
//           }

            Timber.d("init ended! ")
        }

    }

    private fun getAddress(household: HouseholdCache): String {
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

    fun setAddress(it: String?, adapter: FormInputAdapterOld) {
        form.address.value.value = it
        form.mobileNumber.value.value = ben.contactNumber.toString()
        form.husbandName.value.value = ben.genDetails?.spouseName
        form.lastMenstrualPeriod.value.value = getDateFromLong(ben.genDetails?.lastMenstrualPeriod)
        form.expectedDateOfDelivery.value.value = getDateFromLong(ben.genDetails?.lastMenstrualPeriod?.plus(
            280 * 24 * 60 * 60 * 1000L))
        adapter.notifyItemChanged(adapter.currentList.indexOf(form.address))
        adapter.notifyItemChanged(adapter.currentList.indexOf(form.mobileNumber))
        adapter.notifyItemChanged(adapter.currentList.indexOf(form.husbandName))
        adapter.notifyItemChanged(adapter.currentList.indexOf(form.lastMenstrualPeriod))
        adapter.notifyItemChanged(adapter.currentList.indexOf(form.expectedDateOfDelivery))
    }

    suspend fun getFirstPage(adapter: FormInputAdapterOld): List<FormInputOld> {
        Timber.d("started getFirstPage")
        viewModelScope.launch {
            launch{
                form.haveMCPCard.value.collect {
                    it?.let {
                        toggleFieldOnTrigger(
                            form.haveMCPCard,
                            form.givenMCPCard,
                            it,
                            "No",
                            adapter
                        )
                    }
                }
            }
            launch {
                form.highriskSymbols.value.collect{
                    it?.let {
                        toggleFieldOnTrigger(
                            form.highriskSymbols,
                            form.highRiskReason,
                            it,
                            "Yes",
                            adapter
                        )
                    }
                }
            }
            launch {
                form.systolicBloodPressure.value.combine(form.diastolicBloodPressure.value){
                    sysString, diaString ->
                    if(sysString!=null && diaString!=null) {
                        val sys = sysString.toInt()
                        val dia = diaString.toInt()
                        ((sys < 90 || sys > 140) ||(dia <60 || dia >90))
                    }
                    else false
                }.collect{
                    if(it)
                        _popupString.value = "HRP Case, please visit the nearest HWC or call 104"
                }
            }
            launch {
                form.lastMenstrualPeriod.value.collect{
                    it?.let {
                        Timber.d(it)
                        form.expectedDateOfDelivery.value.value = getDateFromLong((getLongFromDate(it) + 280 * 24 * 60 * 60 * 1000L))
                        adapter.notifyItemChanged(adapter.currentList.indexOf(form.expectedDateOfDelivery))
                    }
                }
            }

        }
        Timber.d("ending getFirstPage")
        return form.firstPage
    }


    private fun getDateFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        dateLong?.let {
            return dateFormat.format(dateLong)
        } ?: run {
            return null
        }
    }

    private fun getLongFromDate(dateString: String): Long {
        val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val date = f.parse(dateString)
        return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
    }

    fun setExistingValues() {
        form.setExistingValues(pmsma)

    }

    fun resetPopUpString() {
        _popupString.value = null
    }
}