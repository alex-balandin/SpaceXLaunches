package com.test.spacexlaunches.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by alex-balandin on 2019-11-21
 */
class DetailsViewModelFactory @Inject constructor(
    private val detailsViewModel: DetailsViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return detailsViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}