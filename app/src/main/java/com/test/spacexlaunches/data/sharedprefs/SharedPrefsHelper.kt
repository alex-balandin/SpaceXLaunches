package com.test.spacexlaunches.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by alex-balandin on 2019-11-21
 */
class SharedPrefsHelper(private val appContext: Context) {
    private val prefsFileName = "SpaceXPrefs"

    private val launchesUpdateTimestampKey = "launchesUpdateTimestamp"

    fun saveLaunchesUpdateTimestamp(timestamp: Long) {
        getSharedPrefs().edit().putLong(launchesUpdateTimestampKey, timestamp).apply()
    }

    fun getLaunchesUpdateTimestamp(): Long {
        return getSharedPrefs().getLong(launchesUpdateTimestampKey, -1)
    }

    private fun getSharedPrefs(): SharedPreferences {
        return appContext.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
    }
}