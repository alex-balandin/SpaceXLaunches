package com.test.spacexlaunches.ui.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import com.test.spacexlaunches.util.SingleLiveEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.lang.Exception
import java.net.URL
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

    @SuppressLint("CheckResult")
    fun downloadAndSaveImage(sourceUrl: String, targetPath: String) {
        Log.d(logTag, "downloadAndSaveImage(): sourceUrl=$sourceUrl, targetPath=$targetPath")
        downloadAndSave(sourceUrl, targetPath)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onComplete = {
                Log.d(logTag, "downloadAndSaveImage success")
                viewAction.value = SimpleViewAction.SHOW_SAVE_IMAGE_SUCCESS_MESSAGE
            }, onError = {
                Log.e(logTag, "downloadAndSaveImage error:${it}")
                viewAction.value = SimpleViewAction.SHOW_SAVE_IMAGE_ERROR_MESSAGE
            })
    }

    private fun downloadAndSave(sourceUrl: String, targetPath: String): Completable {
        return Completable.create { emitter ->
            val targetDir = File(targetPath)
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
            val parts = sourceUrl.split("/")
            val fileName = parts[parts.size - 1]

            val targetFile = File(targetDir, fileName)

            var inputStream: BufferedInputStream? = null
            var outputStream: ByteArrayOutputStream? = null
            var fileOutputStream: FileOutputStream? = null
            try {
                val url = URL(sourceUrl)
                inputStream = BufferedInputStream(url.openStream())
                outputStream = ByteArrayOutputStream()
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                val byteArray = outputStream.toByteArray()

                fileOutputStream = FileOutputStream(targetFile)
                fileOutputStream.write(byteArray)

            } catch (e: Exception) {
                emitter.onError(e)

            } finally {
                outputStream?.close()
                inputStream?.close()
                fileOutputStream?.close()
            }

            emitter.onComplete()
        }
    }

    enum class SimpleViewAction {
        SHOW_SAVE_IMAGE_SUCCESS_MESSAGE,
        SHOW_SAVE_IMAGE_ERROR_MESSAGE
    }
}