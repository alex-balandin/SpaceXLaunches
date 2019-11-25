package com.test.spacexlaunches.util

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by alex-balandin on 2019-11-25
 */
@Singleton
open class Logger @Inject constructor() {

    open fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    open fun error(tag: String, message: String) {
        Log.e(tag, message)
    }
}