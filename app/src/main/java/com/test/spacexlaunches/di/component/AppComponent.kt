package com.test.spacexlaunches.di.component

import com.test.spacexlaunches.di.module.ApiModule
import com.test.spacexlaunches.di.module.AppModule
import com.test.spacexlaunches.ui.main.chart.ChartFragment
import com.test.spacexlaunches.ui.main.list.LaunchesListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-21
 */
@Singleton
@Component(modules = [ApiModule::class, AppModule::class])
interface AppComponent {

    fun inject(launchesListFragment: LaunchesListFragment)

    fun inject(chartFragment: ChartFragment)
}