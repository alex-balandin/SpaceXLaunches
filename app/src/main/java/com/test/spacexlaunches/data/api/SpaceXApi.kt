package com.test.spacexlaunches.data.api

import com.test.spacexlaunches.data.api.response.RawLaunch
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by alex-balandin on 2019-11-21
 */
interface SpaceXApi {
    companion object {
        const val BASE_URL = "https://api.spacexdata.com/v3/"
    }

    @GET("launches/")
    fun getAllLaunches(): Single<List<RawLaunch>>
}