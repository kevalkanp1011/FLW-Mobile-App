package org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.Gender
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MdsrObjectViewModel @Inject constructor(
    state: SavedStateHandle,
    private val database: InAppDb
) : ViewModel() {

    private val benId = MdsrObjectFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = MdsrObjectFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender
    private val _gender = MutableLiveData<Gender>()
    val gender: LiveData<Gender>
        get() = _gender
    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int>
        get() = _age
    private val _maternalDeath = MutableLiveData(false)
    val maternalDeath: LiveData<Boolean>
        get() = _maternalDeath

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ben = database.benDao.getBen(hhId, benId)!!
                Timber.d("MDSR: $hhId, $benId: ${ben.firstName}")
            }
            ben.gender?.let { _gender.value = it }
            _age.value = ben.age
            _benName.value = ben.firstName + ben.lastName
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
        }
    }

    fun deathReasonChanged(i: Int) {
        if(i == 1) _maternalDeath.value = true
        else if(i == 2) _maternalDeath.value = false
    }
}