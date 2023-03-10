package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_more_15

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
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.configuration.BenGenRegFormDataset
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewBenRegG15ViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context,
    private val benRepo: BenRepo
) : ViewModel() {
    enum class State {
        IDLE,
        SAVING,
        SAVE_SUCCESS,
        SAVE_FAILED
    }

    private val hhId = NewBenRegG15FragmentArgs.fromSavedStateHandle(savedStateHandle).hhId
    private val benIdFromArgs =
        NewBenRegG15FragmentArgs.fromSavedStateHandle(savedStateHandle).benId

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

    private val _hasReproductiveStatus = MutableLiveData(false)
    val hasReproductiveStatus: LiveData<Boolean>
        get() = _hasReproductiveStatus

    private val _recordExists = MutableLiveData(benIdFromArgs > 0)
    val recordExists: LiveData<Boolean>
        get() = _recordExists


    private var form: BenGenRegFormDataset = BenGenRegFormDataset(context)
    private lateinit var household: HouseholdCache

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
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
            a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(
                Calendar.DAY_OF_MONTH
            )
        ) {
            diffY--
        }
        if (diffY != 0)
            return -1
        var diffM = b.get(Calendar.MONTH) - a.get(Calendar.MONTH)
        if (a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH)) {
            diffM--
        }
        if (diffM < 0)
            diffM += 12

        return diffM
    }

    suspend fun getFirstPage(adapter: FormInputAdapter): List<FormInput> {
        withContext(Dispatchers.IO) {
            household = benRepo.getHousehold(hhId)!!
            if (benIdFromArgs > 0)
                form = benRepo.getBenGenForm(benIdFromArgs, hhId)

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
                                form.maritalStatus.list = form.maritalStatusMale

                            }
                            "Female" -> {
                                form.relationToHead.list = form.relationToHeadListFemale
                                form.maritalStatus.list = form.maritalStatusFemale
                            }
                            else -> {
                                form.relationToHead.list = form.relationToHeadListDefault
                                form.maritalStatus.list = form.maritalStatusMale
                            }
                        }
//                        val currentValue = form.relationToHead.value.value
//                        if(currentValue!=null
//                            && form.relationToHead.list?.contains(currentValue) == false
//                        )

                        form.relationToHead.value.value = null
                        adapter.notifyItemChanged(adapter.currentList.indexOf(form.relationToHead))
                        form.maritalStatus.value.value = null
                        adapter.notifyItemChanged(adapter.currentList.indexOf(form.maritalStatus))
                        val list = adapter.currentList.toMutableList()
                        list.remove(form.otherRelationToHead)
                        list.remove(form.husbandName)
                        list.remove(form.wifeName)
                        list.remove(form.spouseName)
                        adapter.submitList(list)


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
                            return@collect
                        }
                        Timber.d("dob flow emitted $it")
                        if (form.age.value.value == null || form.age.value.value?.toInt() != yearsDiff) {
                            emittedFromDob = true
                            form.age.value.value = yearsDiff.toString()
                            form.age.errorText = null
                            adapter.notifyItemChanged(form.firstPage.indexOf(form.age))
                        }
                    } ?: run {
                        if (emittedFromAge)
                            emittedFromAge = false
                    }
                }
            }
            launch {
                form.age.value.collect { age ->
                    if (age != null) {
                        if (emittedFromDob) {
                            form.ageAtMarriage.max = age.toLong()
                            emittedFromDob = false
                            return@collect
                        }
                        emittedFromAge = true
                        if (age.toLong() < form.age.min!! || age.toLong() > form.age.max!!) {
                            //_errorMessage.value = "Age needs to be greater than or equal to 15 Years"
                            if (form.dob.value.value != null) {
                                form.dob.value.value = null
                                adapter.notifyItemChanged(adapter.currentList.indexOf(form.dob))
                            }
                            return@collect
                        }
                        form.ageAtMarriage.max = age.toLong()
                        val cal = Calendar.getInstance()
                        cal.add(Calendar.YEAR, -1 * age.toInt())
                        val year = cal.get(Calendar.YEAR)
                        val month = cal.get(Calendar.MONTH) + 1
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        val newDob =
                            "${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year"
                        if (form.dob.value.value != newDob) {
                            form.dob.value.value = newDob
                            Timber.d("age : $age ageUnit : ${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year")
                            withContext(Dispatchers.Main) {
                                adapter.notifyItemChanged(form.firstPage.indexOf(form.dob))
                            }
                        }
                    }
                }
            }
            launch {
                form.ageAtMarriage.value.combine(form.age.value) { ageAtMarriage, age ->
                    age == ageAtMarriage

                }.stateIn(viewModelScope, SharingStarted.Eagerly, false).collect {
                    val list = adapter.currentList.toMutableList()
                    form.dateOfMarriage.min = System.currentTimeMillis() - 31536000000
                    if (it) {
                        if (!adapter.currentList.contains(form.dateOfMarriage)) {
                            list.add(list.indexOf(form.ageAtMarriage) + 1, form.dateOfMarriage)
                        }
                    } else
                        list.remove(form.dateOfMarriage)
                    adapter.submitList(list)
                }


            }
            launch {
                form.maritalStatus.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it != "Unmarried") {
                            if (it == "Divorced") {
                                form.husbandName.required = false
                                form.wifeName.required = false
                            } else {
                                form.husbandName.required = true
                                form.wifeName.required = true
                            }
                            if (it == "Married" && form.gender.value.value == "Female") {
                                form.fatherName.required = false
                                form.motherName.required = false
                            } else {
                                form.husbandName.required = true
                                form.wifeName.required = true
                            }
                            if (!adapter.currentList.contains(form.ageAtMarriage)) {
                                list.add(
                                    adapter.currentList.indexOf(form.maritalStatus) + 1,
                                    form.ageAtMarriage
                                )
                            }
                            val itemByGender = when (form.gender.value.value) {
                                "Male" -> form.wifeName
                                "Female" -> form.husbandName
                                "Transgender" -> form.spouseName
                                else -> null
                            }
                            itemByGender?.let { item ->
                                if (!adapter.currentList.contains(item)) {
                                    list.add(
                                        adapter.currentList.indexOf(form.maritalStatus) + 1,
                                        item
                                    )
                                }
                            }
                        } else {
                            if (adapter.currentList.contains(form.ageAtMarriage)) {
                                list.removeAt(list.indexOf(form.ageAtMarriage) - 1)
                                list.remove(form.ageAtMarriage)
                            }
                        }
                        adapter.submitList(list)
                    } ?: run {
                        val list = adapter.currentList.toMutableList()
                        if (adapter.currentList.contains(form.ageAtMarriage))
                            list.remove(form.ageAtMarriage)
                        if (adapter.currentList.contains(form.husbandName))
                            list.remove(form.husbandName)
                        if (adapter.currentList.contains(form.wifeName))
                            list.remove(form.wifeName)
                        if (adapter.currentList.contains(form.spouseName))
                            list.remove(form.spouseName)
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.mobileNoOfRelation.value.collect {
                    it?.let {
                        if (it == "Family Head") {
                            household.familyHeadPhoneNo?.let { mobNo ->
                                form.contactNumber.value.value = mobNo.toString()
                            }
                        } else
                            form.contactNumber.value.value = null
                        val list = adapter.currentList.toMutableList()
                        if (!adapter.currentList.contains(form.otherMobileNoOfRelation)) {
                            if (it == "Other")
                                list.add(
                                    adapter.currentList.indexOf(form.mobileNoOfRelation) + 1,
                                    form.otherMobileNoOfRelation
                                )
                        } else
                            list.remove(form.otherMobileNoOfRelation)
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
                        val list = adapter.currentList.toMutableList()
                        if (it == "Other") {
                            if (!adapter.currentList.contains(form.otherReligion)) {
                                list.add(
                                    adapter.currentList.indexOf(form.religion) + 1,
                                    form.otherReligion
                                )
                            }

                        } else {
                            list.remove(form.otherReligion)
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.gender.value.combine(form.maritalStatus.value) { gender, maritalStatus ->
                    if (gender != null && maritalStatus != null) {
                        gender == "Female" && maritalStatus != "Unmarried"
                    } else
                        false

                }.stateIn(viewModelScope, SharingStarted.Eagerly, false).collect {
                    _hasReproductiveStatus.value = it

                }


            }

        }
        return withContext(Dispatchers.IO) {
            form.firstPage
        }
    }


    fun getSecondPage(adapter: FormInputAdapter): List<FormInput> {

        viewModelScope.launch {
            launch {
                form.hasAadharNo.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Yes") {
                            if (!adapter.currentList.contains(form.aadharNo)) {
                                list.add(list.indexOf(form.hasAadharNo) + 1, form.aadharNo)
                            }
                        } else {
                            list.remove(form.aadharNo)
                        }
                        adapter.submitList(list)

                    }
                }
            }
        }
        return form.secondPage

    }

    fun getThirdPage(adapter: FormInputAdapter): List<FormInput> {

        viewModelScope.launch {
            launch {
                form.reproductiveStatus.value.collect {
                    it?.let {
                        form.lastMenstrualPeriod.value.value = null
                        form.nishchayKitDeliveryStatus.value.value = null
                        form.pregnancyTestResult.value.value = null
                        form.expectedDateOfDelivery.value.value = null
                        form.numPrevLiveBirthOrPregnancy.value.value = null

                        val list = mutableListOf(*form.thirdPage.toTypedArray())
                        when (it) {
                            "Eligible Couple",
                            "Menopause Stage",
                            "Teenager",
                            -> {

                                form.lastMenstrualPeriod.required = false
                                if (adapter.currentList.contains(form.lastMenstrualPeriod))
                                    adapter.notifyItemChanged(adapter.currentList.indexOf(form.lastMenstrualPeriod))
                                Timber.d("list : ${form.thirdPage}")
                            }
                            "Antenatal Mother",
                            "Delivery Stage" -> {
                                form.lastMenstrualPeriod.required = true
                                if (adapter.currentList.contains(form.lastMenstrualPeriod)) {
                                    adapter.notifyItemChanged(adapter.currentList.indexOf(form.lastMenstrualPeriod))
                                }
                                if (adapter.currentList.contains(form.expectedDateOfDelivery)) {
                                    adapter.notifyItemChanged(adapter.currentList.indexOf(form.expectedDateOfDelivery))
                                }
                                if (adapter.currentList.contains(form.numPrevLiveBirthOrPregnancy)) {
                                    adapter.notifyItemChanged(adapter.currentList.indexOf(form.numPrevLiveBirthOrPregnancy))
                                }

                                list.add(
                                    list.indexOf(form.lastMenstrualPeriod) + 1,
                                    form.expectedDateOfDelivery
                                )
                                list.add(
                                    list.indexOf(form.expectedDateOfDelivery) + 1,
                                    form.numPrevLiveBirthOrPregnancy
                                )
                            }
                            "Postnatal Mother-Lactating Mother" -> {
                                list.remove(form.lastMenstrualPeriod)

                                if (adapter.currentList.contains(form.numPrevLiveBirthOrPregnancy)) {
                                    adapter.notifyItemChanged(adapter.currentList.indexOf(form.numPrevLiveBirthOrPregnancy))
                                }

                                list.add(
                                    list.indexOf(form.reproductiveStatus) + 1,
                                    form.dateOfDelivery
                                )

                            }
                            else -> {
                                if (adapter.currentList.contains(form.lastMenstrualPeriod))
                                    adapter.notifyItemChanged(adapter.currentList.indexOf(form.lastMenstrualPeriod))

                                list.add(
                                    1,
                                    form.reproductiveStatusOther
                                )
                            }

                        }
                        adapter.submitList(list)

                    }
                }
            }
            launch {
                form.lastMenstrualPeriod.value.collect {
                    it?.let {
                        val day = it.substring(0, 2).toInt()
                        val month = it.substring(3, 5).toInt() - 1
                        val year = it.substring(6).toInt()
                        val calLmp = Calendar.getInstance()
                        calLmp.set(year, month, day)
                        val calNow = Calendar.getInstance()
                        val monthsDiff = getDiffMonths(calLmp, calNow)
                        Timber.d("monthDiff : $monthsDiff")
                        val list = adapter.currentList.toMutableList()
                        if (monthsDiff >= 1 && form.reproductiveStatus.value.value == "Eligible Couple") {
                            if (!list.contains(form.nishchayKitDeliveryStatus))
                                list.add(
                                    list.indexOf(form.lastMenstrualPeriod) + 1,
                                    form.nishchayKitDeliveryStatus
                                )

                        } else {
                            list.remove(form.nishchayKitDeliveryStatus)
                        }
                        if (list.contains(form.expectedDateOfDelivery)) {
                            form.expectedDateOfDelivery.value.value =
                                getExpectedDoDFromLmp(year, month, day)
                            form.expectedDateOfDelivery.errorText = null
                            adapter.notifyItemChanged(list.indexOf(form.expectedDateOfDelivery))
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.nishchayKitDeliveryStatus.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        if (it == "Delivered") {
                            if (!list.contains(form.pregnancyTestResult)) {
                                list.add(
                                    list.indexOf(form.nishchayKitDeliveryStatus) + 1,
                                    form.pregnancyTestResult
                                )
                            }
                        } else {
                            list.remove(form.pregnancyTestResult)
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.numPrevLiveBirthOrPregnancy.value.collect {
                    it?.let {
                        val count = if (it.isEmpty()) 0 else it.toInt()
                        val list = adapter.currentList.toMutableList()
                        if (count in 1..20) {
                            form.lastDeliveryConducted.value.value = null
                            form.otherPlaceOfDelivery.value.value = null
                            form.whoConductedDelivery.value.value = null
                            form.facility.value.value = null
                            form.otherWhoConductedDelivery.value.value = null
                            if (!list.contains(form.lastDeliveryConducted)) {
                                list.add(
                                    list.indexOf(form.numPrevLiveBirthOrPregnancy) + 1,
                                    form.lastDeliveryConducted
                                )
                                list.add(
                                    list.indexOf(form.lastDeliveryConducted) + 1,
                                    form.whoConductedDelivery
                                )
                            }
                        } else {
                            list.removeAll(
                                listOf(
                                    form.lastDeliveryConducted,
                                    form.whoConductedDelivery,
                                    form.otherPlaceOfDelivery,
                                    form.facility,
                                    form.otherWhoConductedDelivery
                                )
                            )
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.lastDeliveryConducted.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        when (it) {
                            "PHC",
                            "HWC",
                            "CHC",
                            "District Hospital" -> {
                                list.remove(form.otherPlaceOfDelivery)
                                if (!list.contains(form.facility)) {
                                    list.add(
                                        list.indexOf(form.lastDeliveryConducted) + 1,
                                        form.facility
                                    )
                                }
                            }
                            "Medical college Hospital",
                            "Other" -> {
                                list.remove(form.facility)
                                if (!list.contains(form.otherPlaceOfDelivery)) {
                                    list.add(
                                        list.indexOf(form.lastDeliveryConducted) + 1,
                                        form.otherPlaceOfDelivery
                                    )
                                }
                            }
                            else -> {
                                list.remove(form.otherPlaceOfDelivery)
                                list.remove(form.facility)
                            }
                        }
                        adapter.submitList(list)
                    }
                }
            }
            launch {
                form.whoConductedDelivery.value.collect {
                    it?.let {
                        val list = adapter.currentList.toMutableList()
                        when (it) {
                            "Other" -> {
                                if (!list.contains(form.otherWhoConductedDelivery)) {
                                    list.add(
                                        list.indexOf(form.whoConductedDelivery) + 1,
                                        form.otherWhoConductedDelivery
                                    )
                                }
                            }
                            else -> {
                                list.remove(form.otherWhoConductedDelivery)
                            }
                        }
                        adapter.submitList(list)
                    }
                }
            }
        }
        return form.thirdPage

    }

    private fun getExpectedDoDFromLmp(year: Int, month: Int, day: Int): String {

        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        cal.add(Calendar.WEEK_OF_YEAR, 40)
        val dayC = cal.get(Calendar.DAY_OF_MONTH)
        val monthC = cal.get(Calendar.MONTH)
        val yearC = cal.get(Calendar.YEAR)
        return "${if (dayC > 9) dayC else "0$dayC"}-${if (monthC > 8) monthC + 1 else "0${monthC + 1}"}-$yearC"

    }


    fun persistFirstPage() {
        Timber.d("Persist first page called!")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // benRepo.persistGenFirstPage(form, hhId)
            }
        }
    }

    fun persistSecondPage() {
        Timber.d("Persist second page called!")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //benRepo.persistBenGen1(, form, null)
            }
        }
    }

    fun persistForm(locationRecord: LocationRecord) {
        Timber.d("Persist form called!")
        viewModelScope.launch {
            _state.value = State.SAVING
            withContext(Dispatchers.IO) {
                try {
                    if (hasReproductiveStatus.value == false)
                        benRepo.persistBenGen1(hhId, form, locationRecord)
                    else
                        benRepo.persistBenGen2(hhId, form, locationRecord)
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


}