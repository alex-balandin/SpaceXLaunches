package com.test.spacexlaunches.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.spacexlaunches.data.model.Launch

/**
 * Created by alex-balandin on 2019-11-21
 */
@Database(entities = [Launch::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun launchesDao(): LaunchesDao
}