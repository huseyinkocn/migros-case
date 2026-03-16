package com.example.news.util.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.example.news.R
import com.google.android.material.textview.MaterialTextView

open class NewsTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr) {

    init {
        context.withStyledAttributes(attrs, R.styleable.NewsTextView) {
            val defaultTextColor = ContextCompat.getColor(context, NewsColor.news_000000)
            val textColor = getColor(R.styleable.NewsTextView_android_textColor, defaultTextColor)
            setTextColor(textColor)

            val defaultTextSize = resources.getDimension(NewsDimen.sp_12)
            val textSize = getDimension(R.styleable.NewsTextView_android_textSize, defaultTextSize)
            setTextSize(textSize / resources.displayMetrics.density)
        }

        obtainTypeface(attrs)
    }
}
