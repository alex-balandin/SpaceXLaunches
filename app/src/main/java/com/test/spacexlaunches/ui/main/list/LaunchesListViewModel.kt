package com.test.spacexlaunches.ui.main.list

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.util.Logger
import com.test.spacexlaunches.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class LaunchesListViewModel @Inject constructor(
    private val repository: SpaceXRepository,
    private val logger: Logger
) : ViewModel() {
    private val logTag = "LaunchesListViewModel"

    val launchesLiveData: MutableLiveData<List<Launch>> = MutableLiveData()
    val lastUpdateTimeData: MutableLiveData<Long> = MutableLiveData()
    var progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()
    val viewAction: SingleLiveEvent<SimpleViewAction> = SingleLiveEvent()
    val startDetailsScreenAction: SingleLiveEvent<Int> = SingleLiveEvent()

    fun onLaunchesListViewCreated() = getLaunchesData(false)

    fun refreshLaunchesData() = getLaunchesData(true)

    fun onClickedListItem(flightNumber: Int) {
        startDetailsScreenAction.value = flightNumber
    }

    @SuppressLint("CheckResult")
    private fun getLaunchesData(forceUpdate: Boolean) {
        logger.debug(logTag, "getLaunchesData(), forceUpdate=${forceUpdate}")
        progressVisibilityData.value = true
        repository.getAllLaunches(forceUpdate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    logger.debug(logTag, "getAllLaunches success")
                    progressVisibilityData.value = false
                    launchesLiveData.value = it
                    lastUpdateTimeData.value = repository.getLaunchesUpdateTimestamp()
                },
                onError = {
                    logger.error(logTag, "getAllLaunches error:${it}")
                    progressVisibilityData.value = false
                    viewAction.value = SimpleViewAction.SHOW_NETWORK_ERROR_MESSAGE
                })
    }

    enum class SimpleViewAction {
        SHOW_NETWORK_ERROR_MESSAGE,
    }

}