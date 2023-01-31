package org.piramalswasthya.sakhi.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PrimitiveListConverter {

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntList(list: List<Int>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}