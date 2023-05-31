package org.piramalswasthya.sakhi.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.piramalswasthya.sakhi.database.converters.LocationEntityListConverter
import org.piramalswasthya.sakhi.database.converters.SyncStateConverter
import org.piramalswasthya.sakhi.database.room.dao.*
import org.piramalswasthya.sakhi.model.*

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
        HBYCCache::class,
        EligibleCoupleRegCache::class,
               ],
    views = [BenBasicCache::class],
    version = 1, exportSchema = false
)

@TypeConverters(LocationEntityListConverter::class, SyncStateConverter::class)

abstract class InAppDb  : RoomDatabase(){

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
    abstract val hbycDao: HbycDao
    abstract val ecrDao : EcrDao

    companion object{
        @Volatile
        private var INSTANCE : InAppDb? = null

        fun getInstance(appContext : Context) : InAppDb {

            synchronized(this){
                var instance = INSTANCE
                if(instance ==null){
                    instance = Room.databaseBuilder(
                        appContext,
                        InAppDb::class.java,
                        "Sakhi-2.0-In-app-database")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance

            }
        }
    }
}