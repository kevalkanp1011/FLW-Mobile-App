package org.piramalswasthya.sakhi.ui.home_activity.immunization_due.child_immunization.form

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.ImmunizationDataset
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.ImmunizationDao
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.model.ImmunizationCache
import org.piramalswasthya.sakhi.model.UserDomain
import javax.inject.Inject

@HiltViewModel
class ImmunizationFormViewModel @Inject constructor(
    @ApplicationContext context: Context,
    preferenceDao: PreferenceDao,
    savedStateHandle: SavedStateHandle,
    vaccineDao: ImmunizationDao,
    benDao: BenDao,
) : ViewModel() {

    private val benId = ImmunizationFormFragmentArgs.fromSavedStateHandle(savedStateHandle).benId
    private val vaccineId =
        ImmunizationFormFragmentArgs.fromSavedStateHandle(savedStateHandle).vaccineId

    private val _recordExists = MutableLiveData<Boolean>()
    val recordExists: LiveData<Boolean>
        get() = _recordExists

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender

    private lateinit var user: UserDomain
    private val dataset = ImmunizationDataset(context, preferenceDao.getCurrentLanguage())
    val formList = dataset.listFlow
    private lateinit var immCache: ImmunizationCache

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val savedRecord = vaccineDao.getImmunizationRecord(benId, vaccineId)
                immCache = savedRecord?.also { _recordExists.postValue(true) } ?: run {
                    ImmunizationCache(benId, vaccineId)
                }.also { _recordExists.postValue(false) }
                val ben = benDao.getBen(benId)!!
                _benName.postValue("${ben.firstName} ${if (ben.lastName == null) "" else ben.lastName}")
                _benAgeGender.postValue("${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}")
                val vaccine = vaccineDao.getVaccineById(vaccineId)
                    ?: throw IllegalStateException("Unknown Vaccine Injected, contact HAZMAT team!")
                dataset.setFirstPage(ben, vaccine, savedRecord)
            }
        }

    }

    fun updateListOnValueChanged(formId: Int, index: Int) {
        viewModelScope.launch {
            dataset.updateList(formId, index)
        }

    }


}