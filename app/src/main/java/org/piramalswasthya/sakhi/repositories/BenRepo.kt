package org.piramalswasthya.sakhi.repositories

import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import javax.inject.Inject

class BenRepo @Inject constructor(
    private val database: InAppDb,
    private val tmcNetworkApiService: TmcNetworkApiService
) {

}