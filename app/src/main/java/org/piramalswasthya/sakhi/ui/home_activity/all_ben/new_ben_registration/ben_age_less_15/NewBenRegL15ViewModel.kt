package org.piramalswasthya.sakhi.ui.home_activity.all_ben.new_ben_registration.ben_age_less_15

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.BenKidRegFormDataset
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.repositories.BenRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewBenRegL15ViewModel @Inject constructor(
    application : Application,
    private val benRepo: BenRepo
): AndroidViewModel(application) {
    enum class State{
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

    fun setMTabPosition(position : Int){
        _mTabPosition = position
    }

    private val _state = MutableLiveData(State.IDLE)
    val state : LiveData<State>
        get() = _state


    private lateinit var form : BenKidRegFormDataset
    private lateinit var household : HouseholdCache

    suspend fun getFirstPage(): List<FormInput> {
        return withContext(Dispatchers.IO){
            household = benRepo.getBenHousehold(hhId)
            form = benRepo.getDraftForm(getApplication())?: BenKidRegFormDataset(getApplication())
            form.firstPage
        }
    }

    fun getSecondPage(): List<FormInput>{
        return form.secondPage

    }

    fun persistFirstPage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                benRepo.persistFirstPage(form, hhId)
            }
        }
    }

    fun persistForm(){
        viewModelScope.launch {
            _state.value = State.SAVING
            withContext(Dispatchers.IO){
                try {
                    benRepo.persistSecondPage(form, hhId)
                    _state.postValue(State.SAVE_SUCCESS)
                }
                catch (e : Exception){
                    Timber.d("saving HH data failed!!")
                    _state.postValue(State.SAVE_FAILED)
                }
            }
        }

    }


}