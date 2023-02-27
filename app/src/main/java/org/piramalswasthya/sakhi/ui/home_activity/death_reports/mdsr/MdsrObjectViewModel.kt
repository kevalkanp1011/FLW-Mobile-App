package org.piramalswasthya.sakhi.ui.home_activity.death_reports.mdsr

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.adapters.FormInputAdapter
import org.piramalswasthya.sakhi.configuration.BenGenRegFormDataset
import org.piramalswasthya.sakhi.configuration.MDSRFormDataset
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.*
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MdsrObjectViewModel @Inject constructor(
    state: SavedStateHandle,
    context: Application,
    private val database: InAppDb,
    private val benRepo: BenRepo
) : ViewModel() {

    private val benId = MdsrObjectFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = MdsrObjectFragmentArgs.fromSavedStateHandle(state).hhId
    private lateinit var ben: BenRegCache
    private lateinit var household: HouseholdCache

    private val _benName = MutableLiveData<String>()
    val benName: LiveData<String>
        get() = _benName
    private val _benAgeGender = MutableLiveData<String>()
    val benAgeGender: LiveData<String>
        get() = _benAgeGender
    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private val dataset = MDSRFormDataset(context)

    private fun toggleFieldOnTrigger(
        causeField: FormInput,
        effectField: FormInput,
        value: String?,
        adapter: FormInputAdapter
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
        val mdsrCache = MDSRCache(benId, hhId)
        dataset.mapValues(mdsrCache)
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ben = benRepo.getBeneficiary(benId, hhId)!!

                household = benRepo.getBenHousehold(hhId)!!
            }
            _benName.value = ben.firstName + ben.lastName
            _benAgeGender.value = "${ben.age} ${ben.ageUnit?.name} | ${ben.gender?.name}"
            _address.value = getAddress(household)
        }
    }

    fun getAddress(household: HouseholdCache): String {
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

    suspend fun getFirstPage(adapter: FormInputAdapter): List<FormInput> {
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

    fun setAddress(it: String?, adapter: FormInputAdapter) {
        dataset.address.value.value = it
        adapter.notifyItemChanged(adapter.currentList.indexOf(dataset.address))
    }
}