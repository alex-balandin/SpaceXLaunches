package com.test.spacexlaunches.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * Created by alex-balandin on 2019-11-21
 */
class Converters {

    @TypeConverter
    fun stringListToJson(list: List<String>) : String {
        return return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToStringList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }
}