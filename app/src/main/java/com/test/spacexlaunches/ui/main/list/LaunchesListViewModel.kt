package com.test.spacexlaunches.ui.main.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class LaunchesListViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {
    private val logTag = "LaunchesListViewModel"

    private val launchesLiveData: MutableLiveData<List<Launch>> = MutableLiveData()
    private val lastUpdateTimeData: MutableLiveData<Long> = MutableLiveData()
    private val progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()
    private val viewAction: SingleLiveEvent<SimpleViewAction> = SingleLiveEvent()
    private val startDetailsScreenAction: SingleLiveEvent<Int> = SingleLiveEvent()

    fun getLaunchesLiveData(): MutableLiveData<List<Launch>> = launchesLiveData
    fun getLastUpdateTimeData(): MutableLiveData<Long> = lastUpdateTimeData
    fun getProgressVisibilityData(): MutableLiveData<Boolean> = progressVisibilityData
    fun getViewAction() : SingleLiveEvent<SimpleViewAction> = viewAction
    fun getStartDetailsScreenAction() : SingleLiveEvent<Int> = startDetailsScreenAction

    fun onLaunchesListViewCreated() = getLaunchesData(false)

    fun refreshLaunchesData() = getLaunchesData(true)

    fun onClickedListItem(flightNumber: Int) {
        startDetailsScreenAction.value = flightNumber
    }

    @SuppressLint("CheckResult")
    private fun getLaunchesData(forceUpdate: Boolean) {
        Log.d(logTag, "getLaunchesData(), forceUpdate=${forceUpdate}")
        progressVisibilityData.value = true
        repository.getAllLaunches(forceUpdate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d(logTag, "getAllLaunches success")
                    progressVisibilityData.value = false
                    launchesLiveData.value = it
                    lastUpdateTimeData.value = repository.getLaunchesUpdateTimestamp()
                },
                onError = {
                    Log.e(logTag, "getAllLaunches error:${it}")
                    progressVisibilityData.value = false
                    viewAction.value = SimpleViewAction.SHOW_NETWORK_ERROR_MESSAGE
                })
    }

    enum class SimpleViewAction {
        SHOW_NETWORK_ERROR_MESSAGE,
    }

}