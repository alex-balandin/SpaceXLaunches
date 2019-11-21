package com.test.spacexlaunches.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class MainViewModel @Inject constructor() : ViewModel() {

    private val testData: MutableLiveData<String> = MutableLiveData()

    fun getTestLiveData(): MutableLiveData<String> {
        return testData
    }

    fun onLaunchesListViewCreated() {
        testData.value = "data from view model:${hashCode()}"
    }

}