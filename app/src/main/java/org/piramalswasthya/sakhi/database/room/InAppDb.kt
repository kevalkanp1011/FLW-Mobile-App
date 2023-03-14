package org.piramalswasthya.sakhi.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.piramalswasthya.sakhi.database.converters.PrimitiveListConverter
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
        FPOTCache::class],
    views = [BenBasicCache::class],
    version = 2, exportSchema = false
)

@TypeConverters(PrimitiveListConverter::class, SyncStateConverter::class)

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