package com.test.spacexlaunches

import android.app.Application
import com.test.spacexlaunches.di.component.AppComponent
import com.test.spacexlaunches.di.component.DaggerAppComponent
import com.test.spacexlaunches.di.module.AppModule

/**
 * Created by alex-balandin on 2019-11-21
 */
class SpaceXLaunchesApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}