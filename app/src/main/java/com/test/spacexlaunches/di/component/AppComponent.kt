package com.test.spacexlaunches.di.component

import com.test.spacexlaunches.ui.main.chart.ChartFragment
import com.test.spacexlaunches.ui.main.list.LaunchesListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-21
 */
@Singleton
@Component
interface AppComponent {

    fun inject(launchesListFragment: LaunchesListFragment)

    fun inject(chartFragment: ChartFragment)
}