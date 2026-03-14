package com.example.news.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.news.R

class LoadingDialog(
    context: Context
) : Dialog(context, R.style.NewsDialogTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
    }

    override fun show() {
        try {
            super.show()
        } catch (_: Exception) {
        }
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (_: Exception) {
        }
    }
}
