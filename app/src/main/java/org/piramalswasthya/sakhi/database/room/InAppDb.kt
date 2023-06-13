package org.piramalswasthya.sakhi.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.piramalswasthya.sakhi.database.converters.LocationEntityListConverter
import org.piramalswasthya.sakhi.database.converters.SyncStateConverter
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.CbacDao
import org.piramalswasthya.sakhi.database.room.dao.CdrDao
import org.piramalswasthya.sakhi.database.room.dao.FpotDao
import org.piramalswasthya.sakhi.database.room.dao.HbncDao
import org.piramalswasthya.sakhi.database.room.dao.HouseholdDao
import org.piramalswasthya.sakhi.database.room.dao.ImmunizationDao
import org.piramalswasthya.sakhi.database.room.dao.MaternalHealthDao
import org.piramalswasthya.sakhi.database.room.dao.MdsrDao
import org.piramalswasthya.sakhi.database.room.dao.PmjayDao
import org.piramalswasthya.sakhi.database.room.dao.PmsmaDao
import org.piramalswasthya.sakhi.database.room.dao.UserDao
import org.piramalswasthya.sakhi.model.*
import timber.log.Timber

@Database(
    entities = [
        UserCache::class,
        HouseholdCache::class,
        BenRegCache::class,
        BeneficiaryIdsAvail::class,
        CbacCache::class,
        CDRCache::class,
        MDSRCache::class,
        PMSMACache::class,
        PMJAYCache::class,
        FPOTCache::class,
        HBNCCache::class,
        Vaccine::class,
        ImmunizationCache::class,
        PregnantWomanRegistrationCache::class,
        EligibleCoupleTracking::class
    ],
    views = [BenBasicCache::class],
    version = 3, exportSchema = false
)

@TypeConverters(LocationEntityListConverter::class, SyncStateConverter::class)

abstract class InAppDb : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val benIdGenDao: BeneficiaryIdsAvailDao
    abstract val householdDao: HouseholdDao
    abstract val benDao: BenDao
    abstract val cbacDao: CbacDao
    abstract val cdrDao: CdrDao
    abstract val mdsrDao: MdsrDao
    abstract val pmsmaDao: PmsmaDao
    abstract val pmjayDao: PmjayDao
    abstract val fpotDao: FpotDao
    abstract val hbncDao: HbncDao
    abstract val vaccineDao: ImmunizationDao
    abstract val maternalHealthDao : MaternalHealthDao

    companion object {
        @Volatile
        private var INSTANCE: InAppDb? = null

        fun getInstance(appContext: Context): InAppDb {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        appContext,
                        InAppDb::class.java,
                        "Sakhi-2.0-In-app-database"
                    )
                        .fallbackToDestructiveMigration()
                        .setQueryCallback(
                            object : QueryCallback {
                                override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                                    Timber.d("Query to Room : sqlQuery=$sqlQuery with arguments : $bindArgs")
                                }
                            },
                            Dispatchers.IO.asExecutor()
                        )
                        .build()

                    INSTANCE = instance
                }
                return instance

            }
        }
    }
}