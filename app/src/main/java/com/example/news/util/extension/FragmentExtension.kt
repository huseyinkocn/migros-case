package com.example.news.util.extension

import android.R
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        runCatching {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
                block()
            }
        }.onFailure {
            val message = this@launchAndRepeatWithViewLifecycle.tag.orEmpty()
            Log.e(message, it.message, it)

        }
    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.observeKeyboard(onKeyboardVisibilityChanged: (Boolean) -> Unit) {
    val rootView: View = requireActivity().window.decorView.findViewById(R.id.content)

    var lastState: Boolean? = null

    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)

        val screenHeight = rootView.rootView.height
        val keypadHeight = screenHeight - rect.bottom
        val isKeyboardOpen = keypadHeight > screenHeight * HEIGHT_PERCENTAGE

        if (lastState == null || lastState != isKeyboardOpen) {
            onKeyboardVisibilityChanged(isKeyboardOpen)
            lastState = isKeyboardOpen
        }
    }
}

private const val HEIGHT_PERCENTAGE = .15
