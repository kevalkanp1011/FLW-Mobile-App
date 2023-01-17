package org.piramalswasthya.sakhi.ui.home_activity.all_household.new_household_registration

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.configuration.HouseHoldFormDataset
import org.piramalswasthya.sakhi.database.room.DummyEntity
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.FormInput
import javax.inject.Inject

@HiltViewModel
class NewHouseholdViewModel
    @Inject constructor(
        application : Application,
        private val database : InAppDb
    ): AndroidViewModel(application) {

    private val context = application
    private var _mTabPosition = 0
    val mTabPosition: Int
        get() = _mTabPosition

    fun setMTabPosition(position : Int){
        _mTabPosition = position
    }

    private val form = HouseHoldFormDataset()

    fun getFirstPage(): List<FormInput> {
        return form.getFirstPage(context)

    }

    fun getSecondPage(): List<FormInput>{
        return form.getSecondPage(context)

    }

    fun getThirdPage(): List<FormInput> {
        return form.getThirdPage(context)
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database.dummyDao.insert(DummyEntity(data= "Entry 1", sync = SyncState.UNSYNCED))
            }
        }
    }



}