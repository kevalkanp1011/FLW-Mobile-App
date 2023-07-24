package org.piramalswasthya.sakhi.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.helpers.hasPendingAncVisit
import org.piramalswasthya.sakhi.model.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MaternalHealthRepo @Inject constructor(
    private val maternalHealthDao: MaternalHealthDao,
    private val benDao: BenDao
) {

    suspend fun getSavedRegistrationRecord(benId: Long): PregnantWomanRegistrationCache? {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getSavedRecord(benId)
        }
    }

    suspend fun getSavedAncRecord(benId: Long, visitNumber: Int): PregnantWomanAncCache? {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getSavedRecord(benId, visitNumber)
        }
    }

    suspend fun getBenFromId(benId: Long): BenRegCache? {
        return withContext(Dispatchers.IO) {
            benDao.getBen(benId)
        }
    }

    suspend fun persistRegisterRecord(pregnancyRegistrationForm: PregnantWomanRegistrationCache) {
        withContext(Dispatchers.IO) {
            maternalHealthDao.saveRecord(pregnancyRegistrationForm)
        }
    }


    suspend fun getAllAncRecords(benId: Long): List<AncStatus> {
        return withContext(Dispatchers.IO) {
            maternalHealthDao.getAllAncRecordsFor(benId)
        }
    }

    suspend fun persistAncRecord(ancCache: PregnantWomanAncCache) {
        withContext(Dispatchers.IO) {
            maternalHealthDao.saveRecord(ancCache)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val ancDueCount = maternalHealthDao.getAllPregnancyRecords().transformLatest {
        Timber.d("From DB : ${it.count()}")
        var count = 0
        it.map {
            Timber.d(
                "Values emitted : ${
                    it.value.map {
                        AncStatus(
                            it.benId,
                            it.visitNumber,
                            AncFormState.ALREADY_FILLED
                        )
                    }
                }"
            )
            val regis = it.key
            val visitPending = hasPendingAncVisit(
                it.value.map {
                    AncStatus(
                        it.benId,
                        it.visitNumber,
                        AncFormState.ALREADY_FILLED
                    )
                },
                regis.lmpDate,
                regis.benId,
                Calendar.getInstance().apply {
                    set(Calendar.SECOND, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.HOUR_OF_DAY, 0)
                }.timeInMillis
            )
            if (visitPending)
                count++
        }
        emit(count)
    }

}