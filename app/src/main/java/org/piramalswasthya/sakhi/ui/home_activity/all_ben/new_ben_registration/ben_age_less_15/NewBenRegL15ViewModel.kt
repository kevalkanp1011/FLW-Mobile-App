package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.model.AgeUnit
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.BenRegKid
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.UserDomain
import org.piramalswasthya.sakhi.repositories.BenRepo
import org.piramalswasthya.sakhi.repositories.UserRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewBenRegL15ViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceDao: PreferenceDao,
    @ApplicationContext context: Context,
    private val benRepo: BenRepo,
    userRepo: UserRepo
) : ViewModel() {
    enum class State {
        IDLE, SAVING, SAVE_SUCCESS, SAVE_FAILED
    }


    private val hhId = NewBenRegL15FragmentArgs.fromSavedStateHandle(savedStateHandle).hhId
    private val benIdFromArgs =
        NewBenRegL15FragmentArgs.fromSavedStateHandle(savedStateHandle).benId

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()
    val prevPageButtonVisibility = currentPage.transform {
        emit(it > 1)
    }
    val nextPageButtonVisibility = currentPage.transform {
        emit(it < 2)
    }
    val submitPageButtonVisibility = currentPage.transform {
        emit(it == 2 && recordExists.value == false)
    }
//    private var _mTabPosition = 0
//
//    val mTabPosition: Int
//        get() = _mTabPosition
//
//    fun setMTabPosition(position: Int) {
//        _mTabPosition = position
//    }

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

//    private val _errorMessage = MutableLiveData<String?>(null)
//    val errorMessage: LiveData<String?>
//        get() = _errorMessage

    private val _recordExists = MutableLiveData(benIdFromArgs > 0)
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    private lateinit var user: UserDomain
    private val dataset: BenKidRegFormDataset =
        BenKidRegFormDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow
    private lateinit var household: HouseholdCache
    private lateinit var ben: BenRegCache

    private var lastImageFormId: Int = 0

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user = userRepo.getLoggedInUser()!!
                household = benRepo.getHousehold(hhId)!!
                ben = benRepo.getBeneficiaryRecord(benIdFromArgs, hhId) ?: BenRegCache(
                    ashaId = user.userId,
                    beneficiaryId = -2,
                    householdId = hhId,
                    isAdult = false,
                    isKid = true,
                    isDraft = true,
                    kidDetails = BenRegKid(),
                    syncState = SyncState.UNSYNCED,
                    locationRecord = preferenceDao.getLocationRecord()!!
                )
                currentPage.collect {
                    when (it) {
                        1 -> dataset.setFirstPage(ben, household.family?.familyHeadPhoneNo)
                        2 -> {
                            dataset.setSecondPage(ben)
//                            dataset.mapValues(household, 1)
//                            householdRepo.persistRecord(household)
                        }
                    }
                }


            }
        }

    }

    fun saveForm() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _state.postValue(State.SAVING)
                    dataset.mapValues(ben, 2)
                    ben.apply {
                        if (beneficiaryId == -2L) {
                            benRepo.substituteBenIdForDraft(ben)
                            serverUpdatedStatus = 1
                            processed = "N"
                        } else {
                            serverUpdatedStatus = 2
                            processed = "U"
                        }

                        if (createdDate == null) {
                            createdDate = System.currentTimeMillis()
                            createdBy = user.userName
                        }
                        updatedDate = System.currentTimeMillis()
                        updatedBy = user.userName
                    }
                    benRepo.persistRecord(ben)
                    _state.postValue(State.SAVE_SUCCESS)
                } catch (e: IllegalAccessError) {
                    Timber.d("saving Ben data failed!! $e")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }
    }

    fun goToPreviousPage() {
        viewModelScope.launch {
            _currentPage.emit(currentPage.value - 1)
        }
    }

    fun goToNextPage() {
        viewModelScope.launch {
            _currentPage.emit(currentPage.value + 1)
        }
    }

    fun updateListOnValueChanged(formId: Int, index: Int) {
        viewModelScope.launch {
            dataset.updateList(formId, index)
        }

    }

//    suspend fun getFirstPage(): List<FormInputOld> {
//        household = benRepo.getHousehold(hhId)!!
//        return if (_recordExists.value == false) {
//            if(!this::dataset.isInitialized) {
//                val pncMotherList = benRepo.getPncMothersFromHhId(hhId).map { it.benName }
//                dataset = BenKidRegFormDataset(context, pncMotherList)
//            }
//            dataset.firstPage
//        } else {
//            dataset = benRepo.getBenKidForm(benIdFromArgs, hhId)
//            dataset.loadFirstPageOnViewMode()
//
//        }
//
//
//    }
//
//    suspend fun observeFirstPage(adapter: FormInputAdapterOld) {
//        viewModelScope.launch {
//            var emittedFromDobForAge = false
//            var emittedFromDobForAgeUnit = false
//            var emittedFromAge = false
//            launch {
//                dataset.gender.value.collect {
//                    it?.let {
//                        when (it) {
//                            "Male" -> {
//                                dataset.relationToHead.entries = dataset.relationToHeadListMale
//                            }
//                            "Female" -> {
//                                dataset.relationToHead.entries = dataset.relationToHeadListFemale
//                            }
//                            else -> {
//                                dataset.relationToHead.entries = dataset.relationToHeadListDefault
//                            }
//                        }
////                        val currentValue = dataset.relationToHead.value.value
////                        if(currentValue!=null
////                            && dataset.relationToHead.entries?.contains(currentValue) == false
////                        )
//                        dataset.relationToHead.value.value = null
//                        adapter.notifyItemChanged(dataset.firstPage.indexOf(dataset.relationToHead))
//                        if (adapter.currentList.contains(dataset.otherRelationToHead)) {
//                            val list = adapter.currentList.toMutableList()
//                            list.remove(dataset.otherRelationToHead)
//                            adapter.submitList(list)
//                        }
//
//                    }
//                }
//            }
//            launch {
//                dataset.dob.value.collect {
//                    it?.let {
//                        val day = it.substring(0, 2).toInt()
//                        val month = it.substring(3, 5).toInt() - 1
//                        val year = it.substring(6).toInt()
//                        val calDob = Calendar.getInstance()
//                        calDob.set(year, month, day)
//                        val calNow = Calendar.getInstance()
//                        val yearsDiff = getDiffYears(calDob, calNow)
//                        if (emittedFromAge) {
//                            emittedFromAge = false
//                            toggleChildRegisteredFieldsVisibility(adapter, yearsDiff)
//                            return@collect
//                        }
//
//                        Timber.d("dob flow emitted $it")
//                        if (yearsDiff > 0) {
//                            if (dataset.ageUnit.value.value != "Year") {
//                                emittedFromDobForAgeUnit = true
//                                dataset.ageUnit.errorText = null
//                                dataset.ageUnit.value.value = "Year"
//                                adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.ageUnit))
//                            }
//                            if (dataset.age.value.value == null || dataset.age.value.value?.toInt() != yearsDiff) {
//                                emittedFromDobForAge = true
//                                dataset.age.errorText = null
//                                dataset.age.value.value = yearsDiff.toString()
//                                adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.age))
//                            }
//                            toggleChildRegisteredFieldsVisibility(adapter, yearsDiff)
//                        } else {
//                            val monthDiff = getDiffMonths(calDob, calNow)
//                            if (monthDiff > 0) {
//                                if (dataset.ageUnit.value.value != "Month") {
//                                    emittedFromDobForAgeUnit = true
//                                    dataset.ageUnit.errorText = null
//                                    dataset.ageUnit.value.value = "Month"
//                                    adapter.notifyItemChanged(dataset.firstPage.indexOf(dataset.ageUnit))
//                                }
//                                if (dataset.age.value.value == null || dataset.age.value.value?.toInt() != monthDiff) {
//                                    emittedFromDobForAge = true
//                                    dataset.age.errorText = null
//                                    dataset.age.value.value = monthDiff.toString()
//                                    adapter.notifyItemChanged(dataset.firstPage.indexOf(dataset.age))
//                                }
//                            } else {
//                                val dayDiff = getDiffDays(calDob, calNow)
//                                if (dataset.ageUnit.value.value != "Day") {
//                                    emittedFromDobForAgeUnit = true
//                                    dataset.ageUnit.errorText = null
//                                    dataset.ageUnit.value.value = "Day"
//                                    adapter.notifyItemChanged(dataset.firstPage.indexOf(dataset.ageUnit))
//                                }
//                                if (dataset.age.value.value == null || dataset.age.value.value?.toInt() != dayDiff) {
//                                    emittedFromDobForAge = true
//                                    dataset.age.errorText = null
//                                    dataset.age.value.value = dayDiff.toString()
//                                    adapter.notifyItemChanged(dataset.firstPage.indexOf(dataset.age))
//                                }
//                            }
//                        }
//                    } ?: run {
//                        if (emittedFromAge) emittedFromAge = false
//                        dataset.age.value.value = null
//                        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.age))
//                    }
//                }
//            }
//            launch {
//                dataset.childRegisteredAtSchool.value.collect {
//                    it?.let {
//                        if (it == "Yes") {
//                            val list = adapter.currentList.toMutableList()
//                            if (!adapter.currentList.contains(dataset.typeOfSchool)) {
//                                list.add(
//                                    adapter.currentList.indexOf(dataset.rchId), dataset.typeOfSchool
//                                )
//                                adapter.submitList(list)
//                            }
//                        } else {
//                            val list = adapter.currentList.toMutableList()
//                            if (adapter.currentList.contains(dataset.typeOfSchool)) {
//                                list.remove(dataset.typeOfSchool)
//                                adapter.submitList(list)
//                            }
//                        }
//
//                    }
//                }
//            }
//            launch {
//                dataset.mobileNoOfRelation.value.collect {
//                    it?.let {
//                        if (it == "Family Head") {
//                            household.family?.familyHeadPhoneNo?.let { mobNo ->
//                                dataset.contactNumber.value.value = mobNo.toString()
//                            }
//                        } else dataset.contactNumber.value.value = null
//                        val list = adapter.currentList.toMutableList()
//                        if (!adapter.currentList.contains(dataset.otherMobileNoOfRelation)) {
//                            if (it == "Other") list.add(
//                                adapter.currentList.indexOf(dataset.mobileNoOfRelation) + 1,
//                                dataset.otherMobileNoOfRelation
//                            )
//                        } else list.remove(dataset.otherMobileNoOfRelation)
//                        if (!adapter.currentList.contains(dataset.contactNumber)) {
//                            list.add(
//                                adapter.currentList.indexOf(dataset.mobileNoOfRelation) + 1,
//                                dataset.contactNumber
//                            )
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//            launch {
//                dataset.relationToHead.value.collect {
//                    it?.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Other") {
//                            list.add(
//                                adapter.currentList.indexOf(dataset.relationToHead) + 1,
//                                dataset.otherRelationToHead
//                            )
//                            adapter.submitList(list)
//                        } else {
//
//                            if (adapter.currentList.contains(dataset.otherRelationToHead)) {
//                                list.remove(dataset.otherRelationToHead)
//                            }
//                            adapter.submitList(list)
//                        }
//                    }
//                }
//            }
//            launch {
//                dataset.religion.value.collect {
//                    it?.let {
//                        if (it == "Other") {
//                            val list = adapter.currentList.toMutableList()
//                            list.add(
//                                adapter.currentList.indexOf(dataset.religion) + 1, dataset.otherReligion
//                            )
//                            adapter.submitList(list)
//                        } else {
//                            if (adapter.currentList.contains(dataset.otherReligion)) {
//                                val list = adapter.currentList.toMutableList()
//                                list.remove(dataset.otherReligion)
//                                adapter.submitList(list)
//                            }
//                        }
//                    }
//                }
//            }
//            launch {
//                dataset.age.value.combine(dataset.ageUnit.value) { age, ageUnit ->
//                    if (age != null && ageUnit != null) {
//                        if (emittedFromDobForAge) {
//                            emittedFromDobForAge = false
//                            return@combine
//                        }
//                        if (emittedFromDobForAgeUnit) {
//                            emittedFromDobForAgeUnit = false
//                            return@combine
//                        }
//                        emittedFromAge = true
//                        if (ageUnit == "Year" && age.toLong() >= 15) {
//                            _errorMessage.value = "Age needs to be less than 15 $ageUnit"
//                            dataset.dob.value.value = null
//                            adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.dob))
//                            return@combine
//                        }
//                        if (ageUnit == "Month" && age.toLong() > 12) {
//                            _errorMessage.value = "Age needs to be less than 12 $ageUnit"
//                            dataset.dob.value.value = null
//                            adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.dob))
//                            return@combine
//                        }
//                        if (ageUnit == "Day" && age.toLong() > 31) {
//                            _errorMessage.value = "Age needs to be less than 31 $ageUnit"
//                            adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.dob))
//                            dataset.dob.value.value = null
//
//                            return@combine
//                        }
//
//                        val cal = Calendar.getInstance()
//                        when (ageUnit) {
//                            "Year" -> {
//                                cal.add(
//                                    Calendar.YEAR, -1 * age.toInt()
//                                )
//                            }
//                            "Month" -> {
//                                cal.add(
//                                    Calendar.MONTH, -1 * age.toInt()
//                                )
//                            }
//                            "Day" -> {
//                                cal.add(
//                                    Calendar.DAY_OF_YEAR, -1 * age.toInt()
//                                )
//                            }
//                        }
//                        val year = cal.get(Calendar.YEAR)
//                        val month = cal.get(Calendar.MONTH) + 1
//                        val day = cal.get(Calendar.DAY_OF_MONTH)
//                        val newDob =
//                            "${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year"
//                        if (dataset.dob.value.value != newDob) {
//                            dataset.dob.value.value = newDob
//                            dataset.dob.errorText = null
//                            Timber.d("age : $age ageUnit : $ageUnit ${if (day > 9) day else "0$day"}-${if (month > 9) month else "0$month"}-$year")
//                            withContext(Dispatchers.Main) {
//                                adapter.notifyItemChanged(dataset.firstPage.indexOf(dataset.dob))
//                            }
//                        }
//                    }
//                }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
//            }
//        }
//    }
//    private fun toggleChildRegisteredFieldsVisibility(
//        adapter: FormInputAdapterOld, yearDiff: Int
//    ) {
//        val list = adapter.currentList.toMutableList()
//        if (yearDiff in 3..14) {
//            if (!adapter.currentList.contains(dataset.childRegisteredAtSchool)) {
//                list.add(
//                    adapter.currentList.indexOf(dataset.rchId), dataset.childRegisteredAtSchool
//                )
//            }
//        } else {
//            if (adapter.currentList.contains(dataset.childRegisteredAtSchool)) {
//                dataset.childRegisteredAtSchool.value.value = null
//                list.remove(dataset.childRegisteredAtSchool)
//            }
//            if (adapter.currentList.contains(dataset.typeOfSchool)) {
//                list.remove(dataset.typeOfSchool)
//            }
//        }
//        if (yearDiff in 3..5) {
//            if (!adapter.currentList.contains(dataset.childRegisteredAtAwc)) {
//                list.add(
//                    adapter.currentList.indexOf(dataset.rchId), dataset.childRegisteredAtAwc
//                )
//            }
//        } else {
//            if (adapter.currentList.contains(dataset.childRegisteredAtAwc)) {
//                list.remove(dataset.childRegisteredAtAwc)
//            }
//        }
//        adapter.submitList(list)
//    }
//
//
//    fun getSecondPage(): List<FormInputOld> {
//
//        return if (recordExists.value==false) {
//            dataset.secondPage
//        } else dataset.loadSecondPageOnViewMode()
//    }
//
//    fun observeSecondPage(adapter: FormInputAdapterOld) {
//        viewModelScope.launch {
//            launch {
//                dataset.placeOfBirth.value.collect {
//                    it?.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Health Facility") {
//                            if (!adapter.currentList.contains(dataset.facility)) {
//                                list.add(
//                                    adapter.currentList.indexOf(dataset.placeOfBirth) + 1,
//                                    dataset.facility
//                                )
//                            }
//                        } else {
//                            list.remove(dataset.facility)
//                            list.remove(dataset.otherFacility)
//                        }
//                        if (it == "Any other Place") {
//                            if (!list.contains(dataset.otherPlaceOfBirth)) {
//                                list.add(
//                                    adapter.currentList.indexOf(dataset.placeOfBirth) + 1,
//                                    dataset.otherPlaceOfBirth
//                                )
//                            }
//                        } else {
//                            if (adapter.currentList.contains(dataset.otherPlaceOfBirth)) {
//                                list.remove(dataset.otherPlaceOfBirth)
//                            }
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//            launch {
//                dataset.facility.value.collect {
//                    it.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Other") {
//                            if (!list.contains(dataset.otherFacility)) {
//                                list.add(
//                                    adapter.currentList.indexOf(dataset.facility) + 1,
//                                    dataset.otherFacility
//                                )
//                            }
//                        } else {
//                            list.remove(dataset.otherFacility)
//
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//            launch {
//                dataset.whoConductedDelivery.value.collect {
//                    it.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Other") {
//                            if (!list.contains(dataset.otherWhoConductedDelivery)) {
//                                list.add(
//                                    adapter.currentList.indexOf(dataset.whoConductedDelivery) + 1,
//                                    dataset.otherWhoConductedDelivery
//                                )
//                            }
//                        } else {
//                            if (adapter.currentList.contains(dataset.whoConductedDelivery)) {
//                                list.remove(dataset.otherWhoConductedDelivery)
//                            }
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//            launch {
//                dataset.complicationsDuringDelivery.value.collect {
//                    it?.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Death") list.removeAll(dataset.deathRemoveList)
//                        else dataset.deathRemoveList.forEach { leftForm ->
//                            if (!list.contains(leftForm)) list.add(leftForm)
//
//                        }
//                        adapter.submitList(list)
//                    }
//
//                }
//            }
//            launch {
//                dataset.motherUnselected.value.collect {
//                    Timber.d("mother Unselected collect() called for value $it")
//                    val list = adapter.currentList.toMutableList()
//                    it?.let {
//                        if (it == "Yes" && !list.contains(dataset.motherOfChild)) {
//                            list.add(
//                                list.indexOf(dataset.motherUnselected) + 1, dataset.motherOfChild
//                            )
//                        }
//                    } ?: run {
//                        list.remove(dataset.motherOfChild)
//                    }
//                    adapter.submitList(list)
//                }
//            }
//            launch {
//                dataset.birthDose.value.collect {
//                    it?.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Given") {
//                            if (!list.contains(dataset.birthDoseGiven)) {
//                                list.add(
//                                    list.indexOf(dataset.birthDose) + 1, dataset.birthDoseGiven
//                                )
//                            }
//                        } else {
//                            if (adapter.currentList.contains(dataset.birthDoseGiven)) {
//                                dataset.birthDoseGiven.value.value = null
//                                list.remove(dataset.birthDoseGiven)
//                            }
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//            launch {
//                dataset.term.value.collect {
//                    it?.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it == "Pre-Term") {
//                            if (!list.contains(dataset.termGestationalAge)) {
//                                list.add(
//                                    list.indexOf(dataset.term) + 1, dataset.termGestationalAge
//                                )
//                            }
//                        } else {
//                            list.remove(dataset.termGestationalAge)
//                            dataset.corticosteroidGivenAtLabor.value.value = null
//                            list.remove(dataset.corticosteroidGivenAtLabor)
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//            launch {
//                dataset.termGestationalAge.value.collect {
//                    it?.let {
//                        val list = adapter.currentList.toMutableList()
//                        if (it.contains("24") && !adapter.currentList.contains(dataset.corticosteroidGivenAtLabor)) {
//                            list.add(
//                                list.indexOf(dataset.termGestationalAge) + 1,
//                                dataset.corticosteroidGivenAtLabor
//                            )
//                        } else {
//                            list.remove(dataset.corticosteroidGivenAtLabor)
//                        }
//                        adapter.submitList(list)
//                    }
//                }
//            }
//        }
//    }
//
//    fun persistFirstPage() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                //benRepo.persistKidFirstPage(dataset, hhId)
//            }
//        }
//    }
//
//    fun saveForm(locationRecord: LocationRecord) {
//        viewModelScope.launch {
//            _state.value = State.SAVING
//            withContext(Dispatchers.IO) {
//                try {
//    benRepo.persistBenKid(dataset, hhId, locationRecord)
//                    _state.postValue(State.SAVE_SUCCESS)
//                } catch (e: Exception) {
//                    Timber.d("saving HH data failed!! $e")
//                    _state.postValue(State.SAVE_FAILED)
//                }
//            }
//        }
//
//    }
//
//    fun resetErrorMessage() {
//        _errorMessage.value = null
//    }

//    fun getNavPath(): TypeOfList {
//        return if (ben.ageUnit in arrayOf(AgeUnit.DAYS, AgeUnit.MONTHS))
//            TypeOfList.INFANT
//        else if(ben.age<Konstants.maxAgeForChild)
//            TypeOfList.CHILD
//        else
//            TypeOfList.ADOLESCENT
//    }

    fun setCurrentImageFormId(id: Int) {
        lastImageFormId = id
    }

    fun setImageUriToFormElement(dpUri: Uri) {
        dataset.setImageUriToFormElement(lastImageFormId, dpUri)

    }

    fun getNavDirection(): NavDirections {
        return if (ben.ageUnit in arrayOf(AgeUnit.DAYS, AgeUnit.MONTHS))
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToInfantListFragment()
        else if(ben.age<Konstants.maxAgeForChild)
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToChildListFragment()
        else
            NewBenRegL15FragmentDirections.actionNewBenRegL15FragmentToAdolescentListFragment()
    }


}