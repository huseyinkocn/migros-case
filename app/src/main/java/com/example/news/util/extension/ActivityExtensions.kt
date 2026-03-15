package com.example.news.util.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

inline fun AppCompatActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(minActiveState) {
        block()
    }
}

fun Activity.hideKeyboard() {
    val service = this.getSystemService(Context.INPUT_METHOD_SERVICE)
    val inputManager = service as InputMethodManager
    this.currentFocus?.let { focusedView ->
        val windowToken = focusedView.windowToken
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
