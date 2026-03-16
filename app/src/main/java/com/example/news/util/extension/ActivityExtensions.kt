package com.example.news.util.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val service = this.getSystemService(Context.INPUT_METHOD_SERVICE)
    val inputManager = service as InputMethodManager
    this.currentFocus?.let { focusedView ->
        val windowToken = focusedView.windowToken
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
