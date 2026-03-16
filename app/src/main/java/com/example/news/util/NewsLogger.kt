package com.example.news.util

import logcat.LogPriority
import logcat.logcat

interface NewsLogger {

    fun d(message: String?, tag: String? = null)

    companion object : NewsLogger {

        override fun d(message: String?, tag: String?) {
            logcat(LogPriority.DEBUG, tag) { message.orEmpty() }
        }
    }
}
