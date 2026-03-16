package com.example.news.util.extension

import android.view.View
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

const val TIME_NOW = 1L
const val TIME_AGO = 60L
const val TIME_HOURS = 24L
const val TIME_DAYS = 7L

fun String.toRelativeTime(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(this) ?: return this
        val now = Date()
        val diffMs = now.time - date.time

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMs)
        val hours = TimeUnit.MILLISECONDS.toHours(diffMs)
        val days = TimeUnit.MILLISECONDS.toDays(diffMs)

        when {
            minutes < TIME_NOW -> "Just now"
            minutes < TIME_AGO -> "${minutes}m ago"
            hours < TIME_HOURS -> "${hours}h ago"
            days < TIME_DAYS -> "${days}d ago"
            else -> {
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                outputFormat.format(date)
            }
        }
    } catch (e: Exception) {
        this
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
