package com.example.news.util

import logcat.LogPriority
import logcat.logcat

interface NewsLogger {

    fun v(message: String?, tag: String? = null)

    fun d(message: String?, tag: String? = null)

    fun i(message: String?, tag: String? = null)

    fun i(e: Throwable?, tag: String? = null)

    fun e(message: String?, tag: String? = null)

    fun e(e: Throwable?, tag: String? = null)

    fun wtf(message: String?, tag: String? = null)

    companion object : NewsLogger {

        override fun v(message: String?, tag: String?) {
            logcat(LogPriority.VERBOSE, tag) { message.orEmpty() }
        }

        override fun d(message: String?, tag: String?) {
            logcat(LogPriority.DEBUG, tag) { message.orEmpty() }
        }

        override fun i(message: String?, tag: String?) {
            logcat(LogPriority.INFO, tag) { message.orEmpty() }
        }

        override fun i(e: Throwable?, tag: String?) {
            logcat(LogPriority.INFO, tag) { e?.message.orEmpty() }
        }

        override fun e(message: String?, tag: String?) {
            logcat(LogPriority.ERROR, tag) { message.orEmpty() }
        }

        override fun e(e: Throwable?, tag: String?) {
            logcat(LogPriority.ERROR, tag) { e?.message.orEmpty() }
        }

        override fun wtf(message: String?, tag: String?) {
            logcat(LogPriority.ASSERT, tag) { message.orEmpty() }
        }
    }
}
