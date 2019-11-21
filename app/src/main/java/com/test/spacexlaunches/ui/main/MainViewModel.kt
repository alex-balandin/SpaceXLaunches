package com.test.spacexlaunches.ui.main

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
class MainViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {
    private val logTag = "MainViewModel"

    private val launchesLiveData: MutableLiveData<List<Launch>> = MutableLiveData()
    private val lastUpdateTimestamp: MutableLiveData<Long> = MutableLiveData()
    private val progressBarLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getLaunchesLiveData(): MutableLiveData<List<Launch>> = launchesLiveData

    fun onLaunchesListViewCreated() = getLaunchesData(false)

    fun getLastUpdateTimestamp(): MutableLiveData<Long> = lastUpdateTimestamp

    fun refreshLaunchesData() = getLaunchesData(true)

    @SuppressLint("CheckResult")
    private fun getLaunchesData(forceUpdate: Boolean) {
        Log.d(logTag, "getLaunchesData(), forceUpdate=${forceUpdate}")
        repository.getAllLaunches(forceUpdate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d(logTag, "getAllLaunches success")
                    launchesLiveData.value = it
                    lastUpdateTimestamp.value = repository.getLaunchesUpdateTimestamp()
                },
                onError = {
                    Log.e(logTag, "getAllLaunches error:${it}")
                })
    }

}