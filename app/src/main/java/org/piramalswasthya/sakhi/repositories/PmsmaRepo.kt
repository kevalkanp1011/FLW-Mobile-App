package org.piramalswasthya.sakhi.repositories

import android.app.Application
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.model.PMSMACache
import org.piramalswasthya.sakhi.network.NcdNetworkApiService
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import javax.inject.Inject

class PmsmaRepo @Inject constructor(
    private val context: Application,
    private val database: InAppDb,
    private val userRepo: UserRepo,
    private val tmcNetworkApiService: TmcNetworkApiService,
    private val ncdNetworkApiService: NcdNetworkApiService
) {
    fun savePmsmaData(pmsmaCache: PMSMACache): Boolean {
        return true
    }

}