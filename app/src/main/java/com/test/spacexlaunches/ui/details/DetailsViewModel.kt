package com.test.spacexlaunches.ui.details

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
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
    private val viewAction: SingleLiveEvent<SimpleViewAction> = SingleLiveEvent()

    fun getLaunchLiveData(): MutableLiveData<Launch> = launchLiveData
    fun getProgressVisibilityData(): MutableLiveData<Boolean> = progressVisibilityData
    fun getViewAction() : SingleLiveEvent<SimpleViewAction> = viewAction

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

    fun saveImageIntoStorage(bitmap: Bitmap, targetPath: String, fileName: String) {
        Log.d(logTag, "saveImageIntoStorage(): targetPath=$targetPath, fileName=$fileName")

        var fileOutputStream: FileOutputStream? = null
        try {
            val targetDir = File(targetPath)

            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }

            val file = File(targetDir, fileName)

            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        } catch (e: Exception) {
            Log.e(logTag, "saveImageIntoStorage(): failed to save image: $e")
            viewAction.value = SimpleViewAction.SHOW_SAVE_IMAGE_ERROR_MESSAGE
        } finally {
            fileOutputStream?.flush()
            fileOutputStream?.close()
        }

        Log.d(logTag, "saveImageIntoStorage() success")
        viewAction.value = SimpleViewAction.SHOW_SAVE_IMAGE_SUCCESS_MESSAGE
    }

    enum class SimpleViewAction {
        SHOW_SAVE_IMAGE_SUCCESS_MESSAGE,
        SHOW_SAVE_IMAGE_ERROR_MESSAGE
    }
}