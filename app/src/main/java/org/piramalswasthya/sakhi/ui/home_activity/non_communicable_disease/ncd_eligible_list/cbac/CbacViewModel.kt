package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list.cbac

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.AgeUnit
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.CbacCache
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class CbacViewModel @Inject constructor(
    private val context: Application,
    state: SavedStateHandle,
    private val database: InAppDb
) : ViewModel() {

    private val resources by lazy {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale("en"))
        context.createConfigurationContext(configuration).resources
    }

    private val _raAgeScore = MutableLiveData<Int>()
    val raAgeScore = Transformations.map(_raAgeScore) { it.toString() }
    val raAgeText = Transformations.map(_raAgeScore) {
        val text = resources.getStringArray(R.array.cbac_age)[it + 1]
        cbac.cbac_age = text
        cbac.cbac_age_posi = it + 1
        text
    }
    private val benId = CbacFragmentArgs.fromSavedStateHandle(state).benId
    private val hhId = CbacFragmentArgs.fromSavedStateHandle(state).hhId
    private val ashaId = CbacFragmentArgs.fromSavedStateHandle(state).userId
    private val cbac = CbacCache(benId = benId, hhId = hhId, ashaId = ashaId)
    private lateinit var ben: BenRegCache

    init {
        viewModelScope.launch {
            ben = database.benDao.getBen(hhId, benId)!!
            if (ben.ageUnit != AgeUnit.YEARS)
                throw IllegalStateException("Age not in years for CBAC form!!")
            when (ben.age) {
                in 0..29 -> {
                    _raAgeScore.value = 0
                }
                in 30..39 -> {
                    _raAgeScore.value = 1
                }
                in 40..49 -> {
                    _raAgeScore.value = 2
                }
                in 50..59 -> {
                    _raAgeScore.value = 3
                }
                else -> {
                    _raAgeScore.value = 4
                }
            }

        }
    }

    fun getBenName(): String = ben.firstName + ben.lastName
    fun getAgeGender(): String = "$ben.age ${ben.ageUnit?.name} | ${ben.gender?.name}"
    fun setSmoke(i: Int) {
        cbac.cbac_smoke_posi = i + 1
        cbac.cbac_smoke = resources.getStringArray(R.array.cbac_smoke)[i]
        Timber.d("id : $i val: ${cbac.cbac_smoke}")
    }

    fun setAlcohol(i: Int) {
        cbac.cbac_alcohol_posi = i + 1
        cbac.cbac_alcohol = resources.getStringArray(R.array.cbac_alcohol)[i]
        Timber.d("id : $i val: ${cbac.cbac_alcohol}")
    }
}