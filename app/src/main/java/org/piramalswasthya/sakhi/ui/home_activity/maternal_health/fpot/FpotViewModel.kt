package org.piramalswasthya.sakhi.ui.home_activity.maternal_health.fpot

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapterOld
import org.piramalswasthya.sakhi.configuration.FPOTFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FPOTCache
import org.piramalswasthya.sakhi.model.FormInputOld
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.User
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.FpotRepo
import org.piramalswasthya.sakhi.ui.home_activity.maternal_health.pmjay.PmjayFragmentArgs
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FpotViewModel @Inject constructor(
    state: SavedStateHandle,
    context: Application,
    private val database: InAppDb,
    private val fpotRepo: FpotRepo,
    private val benRepo: BenRepo,
    private val preferenceDao: PreferenceDao
) : ViewModel() {

    enum class State {
        IDLE,
        LOADING,
        SUCCESS,
        FAIL
    }

    private val benId = PmjayFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = PmjayFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache
    private lateinit var household: HouseholdCache
    private lateinit var user: User
    private var fpot: FPOTCache? = null

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

    private val dataset = FPOTFormDataset(context)

    fun submitForm() {
        _state.value = State.LOADING
        val fpotCache = FPOTCache(benId = benId, hhId = hhId, processed = "N")
//        dataset.mapValues(fpotCache)
        Timber.d("saving fpot: $fpotCache")
        viewModelScope.launch {
            val saved = fpotRepo.saveFpotData(fpotCache)
            if (saved) {
                Timber.d("saved fpot: $fpotCache")
                _state.value = State.SUCCESS
            }
            else {
                Timber.d("saving fpot to local db failed!!")
                _state.value = State.FAIL
            }
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ben = benRepo.getBeneficiaryRecord(benId, hhId)!!
                household = benRepo.getHousehold(hhId)!!
                user = preferenceDao.getLoggedInUser()!!
                fpot = database.fpotDao.getFpot(hhId, benId)
            }
            _benName.value = "${ben.firstName} ${if(ben.lastName== null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _address.value = getAddress(household)
            _exists.value = fpot != null
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

    fun getFirstPage(): List<FormInputOld> {
        return dataset.firstPage
    }

    fun setAddress(it: String?, adapter: FormInputAdapterOld) {
        dataset.contactNumber.value.value = ben.contactNumber.toString()
        dataset.spouseName.value.value = ben.genDetails?.spouseName
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
//        dataset.address.value.value = cdr?.address
//        dataset.childName.value.value = cdr?.childName
//        dataset.gender.value.value = cdr?.gender
//        dataset.age.value.value = "${cdr?.age} ${ben.ageUnit?.name}"
//        dataset.dateOfBirth.value.value = getDateFromLong(cdr?.dateOfBirth)
//        dataset.firstInformant.value.value = cdr?.firstInformant
//        dataset.motherName.value.value = cdr?.motherName
//        dataset.fatherName.value.value = cdr?.fatherName
//        dataset.mobileNumber.value.value = cdr?.mobileNumber.toString()
//        dataset.dateOfNotification.value.value = getDateFromLong(cdr?.dateOfNotification)
//        dataset.childName.value.value = cdr?.childName
//        dataset.visitDate.value.value = getDateFromLong(cdr?.visitDate)
//        dataset.houseNumber.value.value = cdr?.houseNumber
//        dataset.mohalla.value.value = cdr?.mohalla
//        dataset.landmarks.value.value = cdr?.landmarks
//        dataset.pincode.value.value = cdr?.pincode.toString()
//        dataset.landline.value.value = cdr?.landline.toString()
//        dataset.dateOfDeath.value.value = getDateFromLong(cdr?.dateOfDeath)
//        dataset.timeOfDeath.value.value = cdr?.timeOfDeath?.toString()
//        dataset.ashaSign.value.value = cdr?.ashaSign
//        dataset.placeOfDeath.value.value = cdr?.placeOfDeath
    }
}