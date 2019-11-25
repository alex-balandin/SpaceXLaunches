package com.test.spacexlaunches.ui.details

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
class DetailsViewModel @Inject constructor(
    private val repository: SpaceXRepository,
    private val fileDownloadHelper: FileDownloadHelper,
    private val logger: Logger
) : ViewModel() {
    private val logTag = "DetailsViewModel"

    val launchLiveData: MutableLiveData<Launch> = MutableLiveData()
    val progressVisibilityData: MutableLiveData<Boolean> = MutableLiveData()
    val viewAction: SingleLiveEvent<SimpleViewAction> = SingleLiveEvent()

    fun onDetailsViewCreated(flightNumber: Int) = getLaunchData(flightNumber)

    @SuppressLint("CheckResult")
    private fun getLaunchData(flightNumber: Int) {
        logger.debug(logTag, "getLaunchData()")
        progressVisibilityData.value = true
        repository.getLaunchCached(flightNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { launch ->
                    logger.debug(logTag, "getLaunchData success")
                    progressVisibilityData.value = false
                    launchLiveData.value = launch
                },
                onError = { throwable ->
                    logger.error(logTag, "getLaunchData error:${throwable}")
                    progressVisibilityData.value = false
                    viewAction.value = SimpleViewAction.SHOW_GET_DATA_ERROR_MESSAGE
                }
            )
    }

    @SuppressLint("CheckResult")
    fun downloadAndSaveImage(sourceUrl: String, targetPath: String) {
        logger.debug(logTag, "downloadAndSaveImage(): sourceUrl=$sourceUrl, targetPath=$targetPath")
        fileDownloadHelper.downloadAndSave(sourceUrl, targetPath)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onComplete = {
                logger.debug(logTag, "downloadAndSaveImage success")
                viewAction.value = SimpleViewAction.SHOW_SAVE_IMAGE_SUCCESS_MESSAGE
            }, onError = {
                logger.error(logTag, "downloadAndSaveImage error:${it}")
                viewAction.value = SimpleViewAction.SHOW_SAVE_IMAGE_ERROR_MESSAGE
            })
    }

    enum class SimpleViewAction {
        SHOW_SAVE_IMAGE_SUCCESS_MESSAGE,
        SHOW_SAVE_IMAGE_ERROR_MESSAGE,
        SHOW_GET_DATA_ERROR_MESSAGE
    }
}