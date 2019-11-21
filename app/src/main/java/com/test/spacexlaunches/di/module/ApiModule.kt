package com.test.spacexlaunches.di.module

import com.test.spacexlaunches.data.api.SpaceXApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-21
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SpaceXApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesSpaceXApi(retrofit: Retrofit) : SpaceXApi {
        return retrofit.create(SpaceXApi::class.java)
    }
}