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
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class ChartViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {
    private val logTag = "ChartViewModel"

    private val launchesLiveData: MutableLiveData<List<Launch>> = MutableLiveData()
    private val progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()

    fun getLaunchesLiveData(): MutableLiveData<List<Launch>> = launchesLiveData
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
                    launchesLiveData.value = it
                },
                onError = {
                    Log.e(logTag, "getAllLaunches error:${it}")
                    progressVisibilityData.value = false
                })
    }
}