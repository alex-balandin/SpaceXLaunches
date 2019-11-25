package com.test.spacexlaunches

import com.test.spacexlaunches.util.Logger

/**
 * Created by alex-balandin on 2019-11-25
 */
class LoggerStub : Logger() {

    override fun debug(tag: String, message: String) {
    }

    override fun error(tag: String, message: String) {
    }
}