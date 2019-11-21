package com.test.spacexlaunches.data.api

import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.exceptions.NetworkException
import com.test.spacexlaunches.data.api.response.RawLaunch
import io.reactivex.Single
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-21
 */
@Singleton
class ApiHelper @Inject constructor(
    private val spaceXApi: SpaceXApi
) {

    fun getAllLaunches(): Single<List<Launch>> {
        return spaceXApi.getAllLaunches()
            .onErrorResumeNext { throwable: Throwable ->
                handleApiCallError(throwable)
            }
            .map { rawLaunches: List<RawLaunch> ->
                val launches = mutableListOf<Launch>()
                for (rawLaunch in rawLaunches) {
                    val launch = Launch(rawLaunch)
                    launches.add(launch)
                }
                launches
            }
    }

    private fun <T> handleApiCallError(throwable: Throwable): Single<T> {
        return when (throwable) {
            is IOException -> Single.error(NetworkException())
            else -> Single.error(throwable)
        }
    }
}