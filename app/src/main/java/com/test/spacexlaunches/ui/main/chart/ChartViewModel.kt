package com.test.spacexlaunches.ui.main.chart

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class ChartViewModel @Inject constructor(
    private val repository: SpaceXRepository,
    private val logger: Logger
) : ViewModel() {
    private val logTag = "ChartViewModel"

    private val chartDateFormat: SimpleDateFormat
            = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)

    val chartItemsLiveData: MutableLiveData<List<ChartItem>> = MutableLiveData()
    val progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()

    fun onChartViewResumed() = getLaunchesData()

    @SuppressLint("CheckResult")
    private fun getLaunchesData() {
        logger.debug(logTag, "getLaunchesData()")
        progressVisibilityData.value = true
        repository.getAllLaunches(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    logger.debug(logTag, "getAllLaunches success")
                    progressVisibilityData.value = false
                    chartItemsLiveData.value = buildChartItems(it)
                },
                onError = {
                    logger.error(logTag, "getAllLaunches error:${it}")
                    progressVisibilityData.value = false
                })
    }

    private fun buildChartItems(launches: List<Launch>): List<ChartItem> {
        val launchCalendars: MutableList<Calendar> = mutableListOf()

        var startDateUnix: Long = Long.MAX_VALUE
        for (launch in launches) {
            if (launch.launchDateUnix != null && !launch.upcoming) {
                val launchCalendar = GregorianCalendar()
                launchCalendar.time = Date(launch.launchDateUnix * 1000L)
                launchCalendars.add(launchCalendar)

                if (launch.launchDateUnix < startDateUnix) {
                    startDateUnix = launch.launchDateUnix
                }
            }
        }
        val calendar = GregorianCalendar()
        calendar.time = Date(startDateUnix * 1000L)

        val currentCalendar = GregorianCalendar()
        currentCalendar.time = Date(System.currentTimeMillis())

        val chartItems: MutableList<ChartItem> = mutableListOf()

        while (calendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR)
            || calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
            && calendar.get(Calendar.MONTH) <= currentCalendar.get(Calendar.MONTH)) {

            val dateStr = chartDateFormat.format(calendar.time)

            var count = 0
            for (launchCalendar in launchCalendars) {
                if (calendar.get(Calendar.YEAR) == launchCalendar.get(Calendar.YEAR)
                    && calendar.get(Calendar.MONTH) == launchCalendar.get(Calendar.MONTH)) {
                    count++
                }
            }

            chartItems.add(ChartItem(count, dateStr))

            calendar.add(Calendar.MONTH, 1)
        }
        return chartItems
    }
}