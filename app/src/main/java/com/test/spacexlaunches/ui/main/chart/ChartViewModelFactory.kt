package com.test.spacexlaunches.ui.main.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class ChartViewModelFactory @Inject constructor(
    private val chartViewModel: ChartViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            return chartViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}