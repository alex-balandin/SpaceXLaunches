package com.test.spacexlaunches.ui.main.chart

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
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
    private val repository: SpaceXRepository
) : ViewModel() {
    private val logTag = "ChartViewModel"

    private val chartDateFormat: SimpleDateFormat
            = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)

    private val chartItemsLiveData: MutableLiveData<List<ChartItem>> = MutableLiveData()
    private val progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()

    fun getChartItemsData(): MutableLiveData<List<ChartItem>> = chartItemsLiveData
    fun getProgressVisibilityData(): MutableLiveData<Boolean> = progressVisibilityData

    fun onChartViewResumed() = getLaunchesData()

    @SuppressLint("CheckResult")
    private fun getLaunchesData() {
        Log.d(logTag, "getLaunchesData()")
        progressVisibilityData.value = true
        repository.getAllLaunches(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d(logTag, "getAllLaunches success")
                    progressVisibilityData.value = false
                    chartItemsLiveData.value = buildChartItems(it)
                },
                onError = {
                    Log.e(logTag, "getAllLaunches error:${it}")
                    progressVisibilityData.value = false
                })
    }

    private fun buildChartItems(launches: List<Launch>): List<ChartItem> {
        var launchCalendars: MutableList<Calendar> = mutableListOf()

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