package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapterOld
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.model.FormInputOld
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.TypeOfList
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NewBenRegL15ViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val benRepo: BenRepo
) : ViewModel() {
    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }


    private val hhId = NewBenRegL15FragmentArgs.fromSavedStateHandle(savedStateHandle).hhId
    private val benIdFromArgs =
        NewBenRegL15FragmentArgs.fromSavedStateHandle(savedStateHandle).benId

    private var _mTabPosition = 0

    val mTabPosition: Int
        get() = _mTabPosition

    fun setMTabPosition(position: Int) {
        _mTabPosition = position
    }

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _recordExists = MutableLiveData(benIdFromArgs > 0)
    val recordExists: LiveData<Boolean>
        get() = _recordExists


    private lateinit var form: BenKidRegFormDataset
    private lateinit var household: HouseholdCache

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

            }
        }

    }

    suspend fun getFirstPage(): List<FormInputOld> {
        household = benRepo.getHousehold(hhId)!!
        return if (_recordExists.value == false) {
            if(!this::form.isInitialized) {
                val pncMotherList = benRepo.getPncMothersFromHhId(hhId).map { it.benName }
                form = BenKidRegFormDataset(context, pncMotherList)
            }
            form.firstPage
        } else {
            form = benRepo.getBenKidForm(benIdFromArgs, hhId)
            form.loadFirstPageOnViewMode()

        }


    }

    suspend fun observeFirstPage(adapter: FormInputAdapterOld) {
        viewModelScope.launch {
            var emittedFromDobForAge = false
            var emittedFromDobForAgeUnit = false
            var emittedFromAge = false
            launch {
                form.gender.value.collect {
                    it?.let {
                        when (it) {
                            "Male" -> {
                                form.relationToHead.entries = form.relationToHeadListMale
                            }
                            "Female" -> {
                                form.relationToHead.entries = form.relationToHeadListFemale
                            }
                            else -> {
                                form.relationToHead.entries = form.relationToHeadListDefault
                            }
                        }
//                        val currentValue = form.relationToHead.value.value
//                        if(currentValue!=null
//                            && form.relationToHead.entries?.contains(currentValue) == false
//                        )
                        form.relationToHead.value.value = null
                        adapter.notifyItemChanged(form.firstPage.indexOf(form.relationToHead))
                        if (adapter.currentList.contains(form.otherRelationToHead)) {
                            val list = adapter.currentList.toMutableList()
                            list.remove(form.otherRelationToHead)
                            adapter.submitList(list)
                        }

                    }
                }
            }
            launch {
                form.dob.value.collect {
                    it?.let {
                        val day = it.substring(0, 2).toInt()
                        val month = it.substring(3, 5).toInt() - 1
                        val year = it.substring(6).toInt()
                        val calDob = Calendar.getInstance()
                        calDob.set(year, month, day)
                        val calNow = Calendar.getInstance()
                        val yearsDiff = getDiffYears(calDob, calNow)
                        if (emittedFromAge) {
                            emittedFromAge = false
                            toggleChildRegisteredFieldsVisibility(adapter, yearsDiff)
                            return@collect
                        }

                        Timber.d("dob flow emitted $it")
                        if (yearsDiff > 0) {
                            if (form.ageUnit.value.value != "Year") {
                                emittedFromDobForAgeUnit = true
                                form.ageUnit.errorText = null
                                form.ageUnit.value.value = "Year"
                                adapter.notifyItemChanged(adapter.currentList.indexOf(form.ageUnit))
                            }
                            if (form.age.value.value == null || form.age.value.value?.toInt() != yearsDiff) {
                                emittedFromDobForAge = true
                                form.age.errorText = null
                                form.age.value.value = yearsDiff.toString()
                                adapter.notifyItemChanged(adapter.currentList.indexOf(form.age))
                            }
                            toggleChildRegisteredFieldsVisibility(adapter, yearsDiff)
                        } else {
                            val monthDiff = getDiffMonths(calDob, calNow)
                            if (monthDiff > 0) {
                                if (form.ageUnit.value.value != "Month") {
                                    emittedFromDobForAgeUnit = true
                                    form.ageUnit.errorText = null
                                    form.ageUnit.value.value = "Month"
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.ageUnit))
                                }
                                if (form.age.value.value == null || form.age.value.value?.toInt() != monthDiff) {
                                    emittedFromDobForAge = true
                                    form.age.errorText = null
                                    form.age.value.value = monthDiff.toString()
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.age))
                                }
                            } else {
                                val dayDiff = getDiffDays(calDob, calNow)
                                if (form.ageUnit.value.value != "Day") {
                                    emittedFromDobForAgeUnit = true
                                    form.ageUnit.errorText = null
                                    form.ageUnit.value.value = "Day"
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.ageUnit))
                                }
                                if (form.age.value.value == null || form.age.value.value?.toInt() != dayDiff) {
                                    emittedFromDobForAge = true
                                    form.age.errorText = null
                                    form.age.value.value = dayDiff.toString()
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.age))
                                }
                            }
                        }
                    } ?: run {
                        if (emittedFromAge) emittedFromAge = false
                        form.age.value.value = null
                        adapter.notifyItemChanged(adapter.currentList.indexOf(form.age))
                    }
                }
            }
            launch {
                form.childRegisteredAtSchool.value.collect {
                    it?.let {
                        if (it == "Yes") {
                            val list = adapter.currentList.toMutableList()
                            if (!adapter.currentList.contains(form.typeOfSchool)) {
                                list.add(
                                    adapter.currentList.indexOf(form.rchId), form.typeOfSchool
                                )
                                adapter.submitList(list)
                            }
                        } else {
                            val list = adapter.currentList.toMutableList()
                            if (adapter.currentList.contains(form.typeOfSchool)) {
                                list.remove(form.typeOfSchool)
                                adapter.submitList(list)
                            }
                        }

                    }
                }
            }
            launch {
                form.mobileNoOfRelation.value.collect {
                    it?.let {
                        if (it == "Family Head") {
                            household.family?.familyHeadPhoneNo?.let { mobNo ->
                                form.contactNumber.value.value = mobNo.toString()
                            }
                        } else form.contactNumber.value.value = null
                        val list = adapter.currentList.toMutableList()
                        if (!adapter.currentList.contains(form.otherMobileNoOfRelation)) {
                            if (it == "Other") list.add(
                                adapter.currentList.indexOf(form.mobileNoOfRelation) + 1,
                                form.otherMobileNoOfRelation
                            )
                        } else list.remove(form.otherMobileNoOfRelation)
                        if (!adapter.currentList.contains(form.contactNumber)) {
                            list.add(
                                adapter.currentList.indexOf(form.mobileNoOfRelation) + 1,
                                form.contactNumber
                            )
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.relationToHead.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Other") {
                            list.add(
                                adapter.currentList.indexOf(form.relationToHead) + 1,
                                form.otherRelationToHead
                            )
                            adapter.submitList(list)
                        } else {

                            if (adapter.currentList.contains(form.otherRelationToHead)) {
                                list.remove(form.otherRelationToHead)
                            }
                            adapter.submitList(list)
                        }
                    }
                }
            }
            launch {
                form.religion.value.collect {
                    it?.let {
                        if (it == "Other") {
                            val list = adapter.currentList.toMutableList()
                            list.add(
                                adapter.currentList.indexOf(form.religion) + 1, form.otherReligion
                            )
                            adapter.submitList(list)
                        } else {
                            if (adapter.currentList.contains(form.otherReligion)) {
                                val list = adapter.currentList.toMutableList()
                                list.remove(form.otherReligion)
                                adapter.submitList(list)
                            }
                        }
                    }
                }
            }
            launch {
                form.age.value.combine(form.ageUnit.value) { age, ageUnit ->
                    if (age != null && ageUnit != null) {
                        if (emittedFromDobForAge) {
                            emittedFromDobForAge = false
                            return@combine
                        }
                        if (emittedFromDobForAgeUnit) {
                            emittedFromDobForAgeUnit = false
                            return@combine
                        }
                        emittedFromAge = true
                        if (ageUnit == "Year" && age.toLong() >= 15) {
                            _errorMessage.value = "Age needs to be less than 15 $ageUnit"
                            form.dob.value.value = null
                            adapter.notifyItemChanged(adapter.currentList.indexOf(form.dob))
                            return@combine
                        }
                        if (ageUnit == "Month" && age.toLong() > 12) {
                            _errorMessage.value = "Age needs to be less than 12 $ageUnit"
                            form.dob.value.value = null
                            adapter.notifyItemChanged(adapter.currentList.indexOf(form.dob))
                            return@combine
                        }
                        if (ageUnit == "Day" && age.toLong() > 31) {
                            _errorMessage.value = "Age needs to be less than 31 $ageUnit"
                            adapter.notifyItemChanged(adapter.currentList.indexOf(form.dob))
                            form.dob.value.value = null

                            return@combine
                        }

                        val cal = Calendar.getInstance()
                        when (ageUnit) {
                            "Year" -> {
                                cal.add(
                                    Calendar.YEAR, -1 * age.toInt()
                                )
                            }
                            "Month" -> {
                                cal.add(
                                    Calendar.MONTH, -1 * age.toInt()
                                )
                            }
                            "Day" -> {
                                cal.add(
                                    Calendar.DAY_OF_YEAR, -1 * age.toInt()
                                )
                            }
                        }
                        val year = cal.get(Calendar.YEAR)
                        val month = cal.get(Calendar.MONTH) + 1
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        val newDob =
                            "${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year"
                        if (form.dob.value.value != newDob) {
                            form.dob.value.value = newDob
                            form.dob.errorText = null
                            Timber.d("age : $age ageUnit : $ageUnit ${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year")
                            withContext(Dispatchers.Main) {
                                adapter.notifyItemChanged(form.firstPage.indexOf(form.dob))
                            }
                        }
                    }
                }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
            }
        }
    }

    private fun getDiffYears(a: Calendar, b: Calendar): Int {
        var diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(
                Calendar.DAY_OF_MONTH
            ) > b.get(
                Calendar.DAY_OF_MONTH
            )
        ) {
            diff--
        }
        return diff
    }

    private fun getDiffMonths(a: Calendar, b: Calendar): Int {
        var diffY = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(
                Calendar.DAY_OF_MONTH
            ) > b.get(
                Calendar.DAY_OF_MONTH
            )
        ) {
            diffY--
        }
        if (diffY != 0) return -1
        var diffM = b.get(Calendar.MONTH) - a.get(Calendar.MONTH)
        if (a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH)) {
            diffM--
        }
        if (diffM < 0) diffM += 12

        return diffM
    }

    private fun getDiffDays(a: Calendar, b: Calendar): Int {
        val millisDiff = b.timeInMillis - a.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(millisDiff).toInt()
    }

    private fun toggleChildRegisteredFieldsVisibility(
        adapter: FormInputAdapterOld, yearDiff: Int
    ) {
        val list = adapter.currentList.toMutableList()
        if (yearDiff in 3..14) {
            if (!adapter.currentList.contains(form.childRegisteredAtSchool)) {
                list.add(
                    adapter.currentList.indexOf(form.rchId), form.childRegisteredAtSchool
                )
            }
        } else {
            if (adapter.currentList.contains(form.childRegisteredAtSchool)) {
                form.childRegisteredAtSchool.value.value = null
                list.remove(form.childRegisteredAtSchool)
            }
            if (adapter.currentList.contains(form.typeOfSchool)) {
                list.remove(form.typeOfSchool)
            }
        }
        if (yearDiff in 3..5) {
            if (!adapter.currentList.contains(form.childRegisteredAtAwc)) {
                list.add(
                    adapter.currentList.indexOf(form.rchId), form.childRegisteredAtAwc
                )
            }
        } else {
            if (adapter.currentList.contains(form.childRegisteredAtAwc)) {
                list.remove(form.childRegisteredAtAwc)
            }
        }
        adapter.submitList(list)
    }


    fun getSecondPage(): List<FormInputOld> {

        return if (recordExists.value==false) {
            form.secondPage
        } else form.loadSecondPageOnViewMode()
    }

    fun observeSecondPage(adapter: FormInputAdapterOld) {
        viewModelScope.launch {
            launch {
                form.placeOfBirth.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Health Facility") {
                            if (!adapter.currentList.contains(form.facility)) {
                                list.add(
                                    adapter.currentList.indexOf(form.placeOfBirth) + 1,
                                    form.facility
                                )
                            }
                        } else {
                            list.remove(form.facility)
                            list.remove(form.otherFacility)
                        }
                        if (it == "Any other Place") {
                            if (!list.contains(form.otherPlaceOfBirth)) {
                                list.add(
                                    adapter.currentList.indexOf(form.placeOfBirth) + 1,
                                    form.otherPlaceOfBirth
                                )
                            }
                        } else {
                            if (adapter.currentList.contains(form.otherPlaceOfBirth)) {
                                list.remove(form.otherPlaceOfBirth)
                            }
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.facility.value.collect {
                    it.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Other") {
                            if (!list.contains(form.otherFacility)) {
                                list.add(
                                    adapter.currentList.indexOf(form.facility) + 1,
                                    form.otherFacility
                                )
                            }
                        } else {
                            list.remove(form.otherFacility)

                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.whoConductedDelivery.value.collect {
                    it.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Other") {
                            if (!list.contains(form.otherWhoConductedDelivery)) {
                                list.add(
                                    adapter.currentList.indexOf(form.whoConductedDelivery) + 1,
                                    form.otherWhoConductedDelivery
                                )
                            }
                        } else {
                            if (adapter.currentList.contains(form.whoConductedDelivery)) {
                                list.remove(form.otherWhoConductedDelivery)
                            }
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.complicationsDuringDelivery.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Death") list.removeAll(form.deathRemoveList)
                        else form.deathRemoveList.forEach { leftForm ->
                            if (!list.contains(leftForm)) list.add(leftForm)

                        }
                        adapter.submitList(list)
                    }

                }
            }
            launch {
                form.motherUnselected.value.collect {
                    Timber.d("mother Unselected collect() called for value $it")
                    val list = adapter.currentList.toMutableList()
                    it?.let {
                        if (it == "Yes" && !list.contains(form.motherOfChild)) {
                            list.add(
                                list.indexOf(form.motherUnselected) + 1, form.motherOfChild
                            )
                        }
                    } ?: run {
                        list.remove(form.motherOfChild)
                    }
                    adapter.submitList(list)
                }
            }
            launch {
                form.birthDose.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Given") {
                            if (!list.contains(form.birthDoseGiven)) {
                                list.add(
                                    list.indexOf(form.birthDose) + 1, form.birthDoseGiven
                                )
                            }
                        } else {
                            if (adapter.currentList.contains(form.birthDoseGiven)) {
                                form.birthDoseGiven.value.value = null
                                list.remove(form.birthDoseGiven)
                            }
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.term.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Pre-Term") {
                            if (!list.contains(form.termGestationalAge)) {
                                list.add(
                                    list.indexOf(form.term) + 1, form.termGestationalAge
                                )
                            }
                        } else {
                            list.remove(form.termGestationalAge)
                            form.corticosteroidGivenAtLabor.value.value = null
                            list.remove(form.corticosteroidGivenAtLabor)
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.termGestationalAge.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it.contains("24") && !adapter.currentList.contains(form.corticosteroidGivenAtLabor)) {
                            list.add(
                                list.indexOf(form.termGestationalAge) + 1,
                                form.corticosteroidGivenAtLabor
                            )
                        } else {
                            list.remove(form.corticosteroidGivenAtLabor)
                        }
                        adapter.submitList(list)
                    }
                }
            }
        }
    }

    fun persistFirstPage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //benRepo.persistKidFirstPage(form, hhId)
            }
        }
    }

    fun persistForm(locationRecord: LocationRecord) {
        viewModelScope.launch {
            _state.value = State.SAVING
            withContext(Dispatchers.IO) {
                try {
                    benRepo.persistBenKid(form, hhId, locationRecord)
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: Exception) {
                    Timber.d("saving HH data failed!! $e")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }

    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun getNavPath(): TypeOfList? {
        return form.getBenRegType()
    }


}