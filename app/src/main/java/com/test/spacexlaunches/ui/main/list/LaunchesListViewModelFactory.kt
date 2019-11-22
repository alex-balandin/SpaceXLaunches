package com.test.spacexlaunches.ui.main.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class LaunchesListViewModelFactory @Inject constructor(
    private val launchesListViewModel: LaunchesListViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaunchesListViewModel::class.java)) {
            return launchesListViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}