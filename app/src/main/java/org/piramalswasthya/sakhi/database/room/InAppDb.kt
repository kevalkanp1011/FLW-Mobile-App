package org.piramalswasthya.sakhi.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.piramalswasthya.sakhi.database.converters.StringListConverter
import org.piramalswasthya.sakhi.database.converters.SyncStateConverter
import org.piramalswasthya.sakhi.database.room.dao.BenDao
import org.piramalswasthya.sakhi.database.room.dao.HouseholdDao
import org.piramalswasthya.sakhi.database.room.dao.UserDao
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.HouseholdCache
import org.piramalswasthya.sakhi.model.UserCache

@Database(entities = [
    UserCache::class,
    HouseholdCache::class,
    BenRegCache::class,
    DummyEntity::class], version = 1, exportSchema = false)

@TypeConverters(StringListConverter::class, SyncStateConverter::class)

abstract class InAppDb  : RoomDatabase(){

    abstract val userDao : UserDao
    abstract val dummyDao : DummyDao
    abstract val householdDao : HouseholdDao
    abstract val benDao : BenDao

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