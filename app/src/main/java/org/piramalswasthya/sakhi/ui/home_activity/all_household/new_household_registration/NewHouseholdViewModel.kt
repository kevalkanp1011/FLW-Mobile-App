package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HouseholdFormDataset
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewHouseholdViewModel
    @Inject constructor(
        application : Application,
        private val householdRepo: HouseholdRepo
    ): AndroidViewModel(application) {

    enum class State{
        IDLE,
        SAVING,
        SAVE_SUCCESS,
        SAVE_FAILED
    }

    private var _mTabPosition = 0
    private var hhId = 0L

    fun getHHId()  = hhId.takeIf { it>0 } ?: throw IllegalStateException("Not got no HHId!!!!")

    val mTabPosition: Int
        get() = _mTabPosition

    fun setMTabPosition(position : Int){
        _mTabPosition = position
    }

    private val _state = MutableLiveData(State.IDLE)
    val state : LiveData<State>
        get() = _state


    private lateinit var form : HouseholdFormDataset

    suspend fun getFirstPage(): List<FormInput> {
        return withContext(Dispatchers.IO){
            form = householdRepo.getDraftForm(getApplication())?:HouseholdFormDataset(getApplication())
            form.firstPage
        }
    }

    fun getSecondPage(): List<FormInput>{
        return form.secondPage

    }

    fun persistFirstPage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                householdRepo.persistFirstPage(form)
            }
        }
    }

    fun getThirdPage(): List<FormInput> {
        return form.thirdPage
    }

    fun persistSecondPage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                householdRepo.persistSecondPage(form)
            }
        }
    }

    fun persistForm(){
        viewModelScope.launch {
            _state.value = State.SAVING
            withContext(Dispatchers.IO){
                try {
                    hhId = householdRepo.persistThirdPage(form)
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