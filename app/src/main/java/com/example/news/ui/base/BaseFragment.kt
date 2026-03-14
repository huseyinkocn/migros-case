package com.example.news.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.example.news.domain.network.INetworkError
import com.example.news.domain.network.NetworkErrorType
import com.example.news.domain.network.NetworkState
import com.example.news.util.LoadingDialog
import com.example.news.util.extension.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectLatest

abstract class BaseFragment<out VB : ViewBinding, VM : CoreViewModel>(
    @LayoutRes private val layoutResId: Int
) : CoreFragment<VM>(layoutResId) {

    abstract val binding: VB

    private val progressDialog: LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }

    protected val isFragmentAlive: Boolean
        get() = isAdded && !isDetached

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeApiResponses()
    }

    private fun observeApiResponses() {
        launchAndRepeatWithViewLifecycle {
            viewModel.networkLoadingStateFlow.collect { networkState ->
                if (networkState.isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.networkStateFlow.collectLatest { networkState ->
                when (networkState) {
                    is NetworkState.Success -> {
                        onServiceSuccess()
                    }

                    is NetworkState.Error -> {
                        onServiceError(networkState.error)
                    }
                }
            }
        }
    }

    protected open fun onServiceSuccess() {
    }

    @Suppress("ReturnCount", "CyclomaticComplexMethod")
    protected open fun onServiceError(error: INetworkError?) {
        when (error?.code) {
            NetworkErrorType.UNAUTHORIZED.value -> {
                //
            }

            NetworkErrorType.TOO_MANY_REQUESTS.value -> {
            }

            NetworkErrorType.NO_INTERNET_CONNECTION.value -> {
            }

            NetworkErrorType.SECURITY_ERROR.value -> {
            }

            else -> {
                //
            }
        }
    }

    open fun showLoading() {
        if (isAdded && !isDetached && !progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    open fun hideLoading() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}
