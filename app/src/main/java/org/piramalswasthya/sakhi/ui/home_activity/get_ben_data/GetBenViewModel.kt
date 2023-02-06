package org.piramalswasthya.sakhi.ui.home_activity.get_ben_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.repositories.BenRepo
import javax.inject.Inject

@HiltViewModel
class GetBenViewModel @Inject constructor(private val benRepo: BenRepo) : ViewModel() {
    enum class State {
        IDLE,
        LOADING,
        ERROR_SERVER,
        ERROR_NETWORK,
        SUCCESS
    }

    private val _state = MutableLiveData(State.IDLE)
    val state: LiveData<State>
        get() = _state

    fun getBeneficiaries() {
        viewModelScope.launch {
            benRepo.getBeneficiariesFromServer();
        }
    }
}