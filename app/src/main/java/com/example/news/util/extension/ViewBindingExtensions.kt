package com.example.news.util.extension

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
    onDestroyBinding: ((T) -> Unit)? = {
        disposeAllViews(it)
    }
) = FragmentViewBindingDelegate(
    this,
    viewBindingFactory,
    onDestroyBinding
)

private fun disposeAllViews(viewBinding: ViewBinding) {
    runCatching {
        disposeAll(viewBinding.root)
    }
}

private fun disposeAll(view: View) {
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            disposeAll(child)
        }
    }
}
