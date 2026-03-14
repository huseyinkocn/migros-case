package com.example.news.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.news.util.NewsLogger
import com.example.news.util.extension.hideKeyboard

abstract class CoreFragment<VM : CoreViewModel>(
    @LayoutRes private val layoutResId: Int
) : Fragment(layoutResId) {

    abstract val viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logLifecycleEvents("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycleEvents("onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifecycleEvents("onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        activity?.hideKeyboard()
        logLifecycleEvents("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycleEvents("onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifecycleEvents("onPause")
    }

    override fun onStop() {
        super.onStop()
        logLifecycleEvents("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logLifecycleEvents("onDestroyView")
    }

    override fun onDestroy() {
        logLifecycleEvents("onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        logLifecycleEvents("onDetach")
    }

    private fun logLifecycleEvents(lifeCycleName: String) {
        NewsLogger.d("life cycle event - $lifeCycleName $this")
    }
}
