package org.piramalswasthya.sakhi.ui.home_activity.get_ben_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.model.BenBasicDomain
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

    private var _benDataList = listOf<BenBasicDomain>()
    val benDataList: List<BenBasicDomain>
        get() =_benDataList

    fun getBeneficiaries(pageNumber: Int) {
        viewModelScope.launch {
            _state.value = State.LOADING
            val list = benRepo.getBeneficiariesFromServer(pageNumber)

            _state.value = if (list.isEmpty()) State.ERROR_SERVER else State.SUCCESS
            if (list.isNotEmpty()) _benDataList = list
        }
    }

    init {
        getBeneficiaries(0)
    }
}