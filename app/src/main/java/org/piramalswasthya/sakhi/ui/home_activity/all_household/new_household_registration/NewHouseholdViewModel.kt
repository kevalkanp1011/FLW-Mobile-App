package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HouseholdFormDataset
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.repositories.HouseholdRepo
import javax.inject.Inject

@HiltViewModel
class NewHouseholdViewModel
    @Inject constructor(
        application : Application,
        private val householdRepo: HouseholdRepo
    ): AndroidViewModel(application) {

    private var _mTabPosition = 0
    val mTabPosition: Int
        get() = _mTabPosition

    fun setMTabPosition(position : Int){
        _mTabPosition = position
    }

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
            withContext(Dispatchers.IO){
                householdRepo.persistThirdPage(form)
            }
        }
    }



}