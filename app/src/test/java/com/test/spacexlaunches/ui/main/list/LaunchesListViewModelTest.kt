package com.test.spacexlaunches.ui.main.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.spacexlaunches.LoggerStub
import com.test.spacexlaunches.RxImmediateSchedulerRule
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Created by alex-balandin on 2019-11-25
 */
class LaunchesListViewModelTest {
    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repositoryMock: SpaceXRepository

    private val loggerStub = LoggerStub()

    private lateinit var viewModel: LaunchesListViewModel

    @Before
    fun setUp() {
        viewModel = LaunchesListViewModel(repositoryMock, loggerStub)
    }

    @Test
    fun onLaunchesListViewCreated() {
        val launches: List<Launch> = listOf(
            Launch(1, "mission1", false, 1275677100L, "rocket1", null, null, null, null, null),
            Launch(2, "mission2", false, 2234567890L, "rocket1", null, null, null, null, null),
            Launch(3, "mission3", true, 3345678901L, "rocket99", null, null, null, null, null)
        )

        Mockito.`when`(repositoryMock.getAllLaunches(Mockito.anyBoolean()))
            .thenReturn(Single.just(launches))
        Mockito.`when`(repositoryMock.getLaunchesUpdateTimestamp())
            .thenReturn(12345L)

        viewModel.onLaunchesListViewCreated()

        Mockito.verify(repositoryMock).getAllLaunches(false)
        assertEquals(launches, viewModel.launchesLiveData.value)
        assertEquals(12345L, viewModel.lastUpdateTimeData.value)
    }

    @Test
    fun onLaunchesListViewCreated__get_data_error() {
        Mockito.`when`(repositoryMock.getAllLaunches(Mockito.anyBoolean()))
            .thenReturn(Single.error(Throwable()))

        viewModel.onLaunchesListViewCreated()

        assertEquals(null, viewModel.launchesLiveData.value)
        assertEquals(null, viewModel.lastUpdateTimeData.value)
        assertEquals(LaunchesListViewModel.SimpleViewAction.SHOW_NETWORK_ERROR_MESSAGE, viewModel.viewAction.value)
    }

    @Test
    fun refreshLaunchesData() {
        val launches: List<Launch> = listOf(
            Launch(1, "mission1", false, 1275677100L, "rocket1", null, null, null, null, null),
            Launch(2, "mission2", false, 2234567890L, "rocket1", null, null, null, null, null),
            Launch(3, "mission3", true, 3345678901L, "rocket99", null, null, null, null, null)
        )

        Mockito.`when`(repositoryMock.getAllLaunches(Mockito.anyBoolean()))
            .thenReturn(Single.just(launches))
        Mockito.`when`(repositoryMock.getLaunchesUpdateTimestamp())
            .thenReturn(12345L)

        viewModel.refreshLaunchesData()

        Mockito.verify(repositoryMock).getAllLaunches(true)
        assertEquals(launches, viewModel.launchesLiveData.value)
        assertEquals(12345L, viewModel.lastUpdateTimeData.value)
    }

    @Test
    fun onClickedListItem() {
        viewModel.onClickedListItem(10)

        assertEquals(10, viewModel.startDetailsScreenAction.value)
    }
}