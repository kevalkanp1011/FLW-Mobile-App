package org.piramalswasthya.sakhi.ui.home_activity.child_care.infant_list.hbnc_form

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.configuration.FPOTFormDataset
import org.piramalswasthya.sakhi.configuration.HBNCFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.FpotRepo
import org.piramalswasthya.sakhi.repositories.HbncRepo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HbncViewModel @Inject constructor(
    state: SavedStateHandle,
    context: Application,
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

    private val benId = HbncFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = HbncFragmentArgs.fromSavedStateHandle(state).hhId
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
    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address
    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state
    private val _exists = MutableLiveData<Boolean>()
    val exists: LiveData<Boolean>
        get() = _exists

    private val dataset = HBNCFormDataset(context)

    fun submitForm() {
        _state.value = State.LOADING
        val hbncCache = HBNCCache(benId = benId, hhId = hhId, processed = "N")
//        dataset.mapValues(fpotCache)
        Timber.d("saving fpot: $hbncCache")
        viewModelScope.launch {
            val saved = hbncRepo.saveHbncData(hbncCache)
            if (saved) {
                Timber.d("saved fpot: $hbncCache")
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
                ben = benRepo.getBeneficiary(benId, hhId)!!
                household = benRepo.getHousehold(hhId)!!
                user = database.userDao.getLoggedInUser()!!
                hbnc = database.hbncDao.getFpot(hhId, benId)
            }
            _benName.value = "${ben.firstName} ${if(ben.lastName== null) "" else ben.lastName}"
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _address.value = getAddress(household)
            _exists.value = hbnc != null
        }
    }

    private fun getAddress(household: HouseholdCache): String {
        val houseNo = household.houseNo
        val wardNo = household.wardNo
        val name = household.wardName
        val mohalla = household.mohallaName
        val district = household.district
        val city = household.village
        val state = household.state

        var address = "$houseNo, $wardNo, $name, $mohalla, $city, $district, $state"
        address = address.replace(", ,", ",")
        address = address.replace(",,", ",")
        address = address.replace(" ,", "")
        address = address.replace("null, ", "")
        address = address.replace(", null", "")

        return address
    }

    fun getFirstPage(): List<FormInput> {
        return dataset.firstPage
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