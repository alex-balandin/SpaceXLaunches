package com.test.spacexlaunches.data

import android.util.Log
import com.test.spacexlaunches.data.api.ApiHelper
import com.test.spacexlaunches.data.db.LaunchesDao
import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.data.sharedprefs.SharedPrefsHelper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-21
 */
@Singleton
class SpaceXRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val launchesDao: LaunchesDao,
    private val sharedPrefsHelper: SharedPrefsHelper
) {
    private val logTag = "SpaceXRepository"

    fun getLaunchesUpdateTimestamp(): Long = sharedPrefsHelper.getLaunchesUpdateTimestamp()

    fun getLaunchCached(flightNumber: Int): Single<Launch> {
        Log.d(logTag, "getLaunchCached()")
        return launchesDao.queryLaunch(flightNumber)
    }

    fun getAllLaunches(forceUpdate: Boolean): Single<List<Launch>> {
        Log.d(logTag, "getAllLaunches()")
        return if (forceUpdate) {
            getAllLaunchesFromApi()
        } else {
            launchesDao.queryAllLaunches()
                .flatMap { launches: List<Launch> ->
                    if (launches.isNotEmpty()) {
                        Log.d(logTag, "getAllLaunches() - get from cache")
                        Single.just(launches)
                    } else {
                        Log.d(logTag, "getAllLaunches() - get from api")
                        getAllLaunchesFromApi()
                    }
                }
        }
    }

    private fun getAllLaunchesFromApi(): Single<List<Launch>> {
        Log.d(logTag, "getAllLaunchesFromApi()")
        return apiHelper.getAllLaunches()
            .flatMap { list: List<Launch> ->
                launchesDao.deleteAllLaunches()
                    .toSingle { list }
            }
            .flatMap { list: List<Launch> ->
                launchesDao.insertAllAllLaunches(list)
                    .toSingle { list }
            }.doOnSuccess {
                val timestamp = System.currentTimeMillis()
                sharedPrefsHelper.saveLaunchesUpdateTimestamp(timestamp)
            }
    }
}