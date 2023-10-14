package org.piramalswasthya.sakhi.ui.home_activity.incentives

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.repositories.UserRepo
import javax.inject.Inject


@HiltViewModel
class IncentivesViewModel @Inject constructor(
    userRepo: UserRepo
): ViewModel() {
}