package org.piramalswasthya.sakhi.ui.home_activity.mother_care.delivery_stage_list.pmsma

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.piramalswasthya.sakhi.database.room.InAppDb
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PMSMAViewModel @Inject constructor(
    private val context: Application,
    state: SavedStateHandle,
    private val database: InAppDb
) : ViewModel() {

    private val resources by lazy {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale("en"))
        context.createConfigurationContext(configuration).resources
    }


}