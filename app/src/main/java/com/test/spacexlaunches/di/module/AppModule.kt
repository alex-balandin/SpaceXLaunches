package com.test.spacexlaunches.di.module

import android.content.Context
import androidx.room.Room
import com.test.spacexlaunches.data.db.Database
import com.test.spacexlaunches.data.db.LaunchesDao
import com.test.spacexlaunches.data.sharedprefs.SharedPrefsHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-21
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideApplication(): Context = context

    @Provides
    @Singleton
    fun providesDatabase(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "launches_db").build()
    }

    @Provides
    @Singleton
    fun providesLaunchesDao(database: Database): LaunchesDao {
        return database.launchesDao()
    }

    @Provides
    @Singleton
    fun providesSharedPrefsHelper(): SharedPrefsHelper {
        return SharedPrefsHelper(context)
    }
}