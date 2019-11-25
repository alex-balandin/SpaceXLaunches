package com.test.spacexlaunches.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.spacexlaunches.LoggerStub
import com.test.spacexlaunches.RxImmediateSchedulerRule
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Created by alex-balandin on 2019-11-25
 */
class DetailsViewModelTest {

    @Rule @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repositoryMock: SpaceXRepository

    @Mock
    lateinit var fileDownloadHelperMock: FileDownloadHelper

    private val loggerStub = LoggerStub()

    private lateinit var detailsViewModel: DetailsViewModel

    @Before
    fun setUp() {
        detailsViewModel = DetailsViewModel(repositoryMock, fileDownloadHelperMock, loggerStub)
    }

    @Test
    fun onDetailsViewCreated() {
        val testLaunch = Launch(1, "mission1", false, 1275677100L, "rocket1", null, null, null, null, null)

        Mockito.`when`(repositoryMock.getLaunchCached(1)).thenReturn(Single.just(testLaunch))

        detailsViewModel.onDetailsViewCreated(1)

        Mockito.verify(repositoryMock).getLaunchCached(1)
        assertEquals(testLaunch, detailsViewModel.launchLiveData.value)
    }

    @Test
    fun onDetailsViewCreated__get_data_error() {
        Mockito.`when`(repositoryMock.getLaunchCached(1)).thenReturn(Single.error(Throwable()))

        detailsViewModel.onDetailsViewCreated(1)

        assertEquals(DetailsViewModel.SimpleViewAction.SHOW_GET_DATA_ERROR_MESSAGE,
            detailsViewModel.viewAction.value)
    }

    @Test
    fun downloadAndSaveImage() {
        Mockito.`when`(fileDownloadHelperMock.downloadAndSave(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Completable.complete())

        detailsViewModel.downloadAndSaveImage("url", "targetPath")

        Mockito.verify(fileDownloadHelperMock).downloadAndSave("url", "targetPath")
        assertEquals(DetailsViewModel.SimpleViewAction.SHOW_SAVE_IMAGE_SUCCESS_MESSAGE,
            detailsViewModel.viewAction.value)
    }

    @Test
    fun downloadAndSaveImage__download_or_save_error() {
        Mockito.`when`(fileDownloadHelperMock.downloadAndSave(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Completable.error(Throwable()))

        detailsViewModel.downloadAndSaveImage("url", "targetPath")

        assertEquals(DetailsViewModel.SimpleViewAction.SHOW_SAVE_IMAGE_ERROR_MESSAGE,
            detailsViewModel.viewAction.value)
    }
}