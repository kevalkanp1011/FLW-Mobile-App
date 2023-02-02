package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NewBenRegL15ViewModel @Inject constructor(
    private val context: Application,
    private val benRepo: BenRepo
) : AndroidViewModel(context) {
    enum class State {
        IDLE,
        SAVING,
        SAVE_SUCCESS,
        SAVE_FAILED
    }

    private var _mTabPosition = 0
    private var hhId = 0L
    fun setHHid(hhId: Long) {
        this.hhId = hhId
    }

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


    private lateinit var form: BenKidRegFormDataset
    private lateinit var household: HouseholdCache

    suspend fun getFirstPage(adapter: FormInputAdapter): List<FormInput> {
        withContext(Dispatchers.IO) {
            household = benRepo.getBenHousehold(hhId)
            form = benRepo.getDraftForm(hhId, true)?.let {
                BenKidRegFormDataset(context, it)
            } ?: run {
                BenKidRegFormDataset(context)
            }
        }
        viewModelScope.launch {
            var emittedFromDob = false
            var emittedFromAge = false
            launch {
                form.gender.value.collect {
                    it?.let {
                        when (it) {
                            "Male" -> {
                                form.relationToHead.list = form.relationToHeadListMale
                            }
                            "Female" -> {
                                form.relationToHead.list = form.relationToHeadListFemale
                            }
                            else -> {
                                form.relationToHead.list = form.relationToHeadListDefault
                            }
                        }
//                        val currentValue = form.relationToHead.value.value
//                        if(currentValue!=null
//                            && form.relationToHead.list?.contains(currentValue) == false
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
                                emittedFromDob = true
                                form.ageUnit.value.value = "Year"
                                adapter.notifyItemChanged(form.firstPage.indexOf(form.ageUnit))
                            }
                            if (form.age.value.value == null || form.age.value.value?.toInt() != yearsDiff) {
                                emittedFromDob = true
                                form.age.value.value = yearsDiff.toString()
                                adapter.notifyItemChanged(form.firstPage.indexOf(form.age))
                            }
                            toggleChildRegisteredFieldsVisibility(adapter, yearsDiff)
                        } else {
                            val monthDiff = getDiffMonths(calDob, calNow)
                            if (monthDiff > 0) {
                                if (form.ageUnit.value.value != "Month") {
                                    emittedFromDob = true
                                    form.ageUnit.value.value = "Month"
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.ageUnit))
                                }
                                if (form.age.value.value == null || form.age.value.value?.toInt() != monthDiff) {
                                    emittedFromDob = true
                                    form.age.value.value = monthDiff.toString()
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.age))
                                }
                            } else {
                                val dayDiff = getDiffDays(calDob, calNow)
                                if (form.ageUnit.value.value != "Day") {
                                    emittedFromDob = true
                                    form.ageUnit.value.value = "Day"
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.ageUnit))
                                }
                                if (form.age.value.value == null || form.age.value.value?.toInt() != dayDiff) {
                                    emittedFromDob = true
                                    form.age.value.value = dayDiff.toString()
                                    adapter.notifyItemChanged(form.firstPage.indexOf(form.age))
                                }
                            }
                        }
                    } ?: run {
                        if (emittedFromAge)
                            emittedFromAge = false
                        form.age.value.value = null
                        adapter.notifyItemChanged(adapter.currentList.indexOf(form.age))
                    }
                }
            }
            launch {
                form.childRegisteredAtSchool.value.collect {
                    if (it == "Yes") {
                        val list = adapter.currentList.toMutableList()
                        if (!adapter.currentList.contains(form.typeOfSchool)) {
                            list.add(
                                adapter.currentList.indexOf(form.rchId),
                                form.typeOfSchool
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
            launch {
                form.mobileNoOfRelation.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (!adapter.currentList.contains(form.contactNumber)) {
                            list.add(
                                adapter.currentList.indexOf(form.mobileNoOfRelation) + 1,
                                form.contactNumber
                            )
                            adapter.submitList(list)
                        }
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
                                adapter.currentList.indexOf(form.religion) + 1,
                                form.otherReligion
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
                        if (emittedFromDob) {
                            emittedFromDob = false
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
                                    Calendar.YEAR,
                                    -1 * age.toInt()
                                )
                            }
                            "Month" -> {
                                cal.add(
                                    Calendar.MONTH,
                                    -1 * age.toInt()
                                )
                            }
                            "Day" -> {
                                cal.add(
                                    Calendar.DAY_OF_YEAR,
                                    -1 * age.toInt()
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
                            Timber.d("age : $age ageUnit : $ageUnit ${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year")
                            withContext(Dispatchers.Main) {
                                adapter.notifyItemChanged(form.firstPage.indexOf(form.dob))
                            }
                        }
                    }
                }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
            }
        }
        return withContext(Dispatchers.IO) {
            form.firstPage
        }
    }

    private fun getDiffYears(a: Calendar, b: Calendar): Int {
        var diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
        if (a.get(Calendar.YEAR) > b.get(Calendar.YEAR) ||
            a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(
                Calendar.DAY_OF_MONTH
            )
        ) {
            diff--
        }
        return diff
    }

    private fun getDiffMonths(a: Calendar, b: Calendar): Int {
        var diffY = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
        if (a.get(Calendar.YEAR) > b.get(Calendar.YEAR) ||
            a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(
                Calendar.DAY_OF_MONTH
            )
        ) {
            diffY--
        }
        if (diffY != 0)
            return -1
        val diffM = b.get(Calendar.MONTH) - a.get(Calendar.MONTH)
        if (diffM == 1 &&
            a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH)
        ) {
            return 0
        }
        return diffM
    }

    private fun getDiffDays(a: Calendar, b: Calendar): Int {
        val millisDiff = b.timeInMillis - a.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(millisDiff).toInt()
    }

    private fun toggleChildRegisteredFieldsVisibility(
        adapter: FormInputAdapter,
        yearDiff: Int
    ) {
        val list = adapter.currentList.toMutableList()
        if (yearDiff in 3..14) {
            if (!adapter.currentList.contains(form.childRegisteredAtSchool)) {
                list.add(
                    adapter.currentList.indexOf(form.rchId),
                    form.childRegisteredAtSchool
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
                    adapter.currentList.indexOf(form.rchId),
                    form.childRegisteredAtAwc
                )
            }
        } else {
            if (adapter.currentList.contains(form.childRegisteredAtAwc)) {
                list.remove(form.childRegisteredAtAwc)
            }
        }
        adapter.submitList(list)
    }


    fun getSecondPage(adapter: FormInputAdapter): List<FormInput> {

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
                            if (adapter.currentList.contains(form.facility)) {
                                list.remove(form.facility)
                            }
                        }
                        if (it == "Any other Place") {
                            if (!adapter.currentList.contains(form.otherPlaceOfBirth)) {
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
                form.whoConductedDelivery.value.collect {
                    it.let {
                        if (it == "Other") {
                            val list = adapter.currentList.toMutableList()
                            list.add(
                                adapter.currentList.indexOf(form.whoConductedDelivery) + 1,
                                form.otherWhoConductedDelivery
                            )
                            adapter.submitList(list)
                        } else {
                            if (adapter.currentList.contains(form.whoConductedDelivery)) {
                                val list = adapter.currentList.toMutableList()
                                list.remove(form.otherWhoConductedDelivery)
                                adapter.submitList(list)
                            }
                        }
                    }
                }
            }
            launch {
                form.birthDose.value.collect {
                    it.let {
                        if (it == "Given") {
                            val list = adapter.currentList.toMutableList()
                            list.add(
                                adapter.currentList.indexOf(form.birthDose) + 1,
                                form.birthDoseGiven
                            )
                            adapter.submitList(list)
                        } else {
                            if (adapter.currentList.contains(form.birthDoseGiven)) {
                                form.birthDoseGiven.value.value = null
                                val list = adapter.currentList.toMutableList()
                                list.remove(form.birthDoseGiven)
                                adapter.submitList(list)
                            }
                        }
                    }
                }
            }
            launch {
                form.term.value.collect {
                    it.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Pre-Term") {
                            list.add(
                                adapter.currentList.indexOf(form.term) + 1,
                                form.termGestationalAge
                            )
                        } else {
                            if (adapter.currentList.contains(form.termGestationalAge)) {
                                list.remove(form.termGestationalAge)
                            }
                        }
                        adapter.submitList(list)
                    }
                }
            }
        }
        return form.secondPage

    }

    fun persistFirstPage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                benRepo.persistKidFirstPage(form, hhId)
            }
        }
    }

    fun persistForm(locationRecord: LocationRecord) {
        viewModelScope.launch {
            _state.value = State.SAVING
            withContext(Dispatchers.IO) {
                try {
                    benRepo.persistKidSecondPage(form, locationRecord)
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: Exception) {
                    Timber.d("saving HH data failed!!")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }

    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }


}