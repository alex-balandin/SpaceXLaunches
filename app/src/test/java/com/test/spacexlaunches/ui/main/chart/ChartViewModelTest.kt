package com.test.spacexlaunches.ui.main.chart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.spacexlaunches.LoggerStub
import com.test.spacexlaunches.RxImmediateSchedulerRule
import com.test.spacexlaunches.data.SpaceXRepository
import com.test.spacexlaunches.data.model.Launch
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Created by alex-balandin on 2019-11-25
 */
class ChartViewModelTest {
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

    private lateinit var viewModel: ChartViewModel

    @Before
    fun setUp() {
        viewModel = ChartViewModel(repositoryMock, loggerStub)
    }

    @Test
    fun onChartViewResumed() {
        val launches: List<Launch> = listOf(
            Launch(77, "mission1", false, 1555022100L, "rocket1", null, null, null, null, null),
            Launch(78, "mission2", false, 1556952480L, "rocket1", null, null, null, null, null),
            Launch(79, "mission3", false, 1558665000L, "rocket99", null, null, null, null, null),
            Launch(80, "mission4", false, 1560349020L, "rocket1", null, null, null, null, null),
            Launch(81, "mission5", false, 1561433400L, "rocket1", null, null, null, null, null),
            Launch(82, "mission6", false, 1564092060L, "rocket99", null, null, null, null, null),
            Launch(83, "mission7", false, 1565131920L, "rocket1", null, null, null, null, null),
            Launch(84, "mission8", false, 1573484160L, "rocket1", null, null, null, null, null)
        )
        Mockito.`when`(repositoryMock.getAllLaunches(Mockito.anyBoolean()))
            .thenReturn(Single.just(launches))

        val expectedChartItems: List<ChartItem> = listOf(
            ChartItem(1, "Apr 2019"),
            ChartItem(2, "May 2019"),
            ChartItem(2, "Jun 2019"),
            ChartItem(1, "Jul 2019"),
            ChartItem(1, "Aug 2019"),
            ChartItem(0, "Sep 2019"),
            ChartItem(0, "Oct 2019"),
            ChartItem(1, "Nov 2019")
        )

        viewModel.onChartViewResumed()

        Assert.assertEquals(expectedChartItems, viewModel.chartItemsLiveData.value)
    }
}