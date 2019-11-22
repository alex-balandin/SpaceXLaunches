package com.test.spacexlaunches.ui.details

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
class DetailsViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {
    private val logTag = "DetailsViewModel"

    private val launchLiveData: MutableLiveData<Launch> = MutableLiveData()
    private val progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()

    fun getLaunchLiveData(): MutableLiveData<Launch> = launchLiveData
    fun getProgressVisibilityData(): MutableLiveData<Boolean> = progressVisibilityData

    fun onDetailsViewCreated(flightNumber: Int) = getLaunchData(flightNumber)

    @SuppressLint("CheckResult")
    private fun getLaunchData(flightNumber: Int) {
        Log.d(logTag, "getLaunchData()")
        progressVisibilityData.value = true
        repository.getLaunchCached(flightNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { launch ->
                    Log.d(logTag, "getLaunchData success")
                    progressVisibilityData.value = false
                    launchLiveData.value = launch
                },
                onError = { throwable ->
                    Log.e(logTag, "getLaunchData error:${throwable}")
                    progressVisibilityData.value = false
                }
            )
    }
}