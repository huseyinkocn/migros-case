package com.example.news.util.widget

import android.content.Context
import android.graphics.Paint
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

    private fun setAsCopyable() {
        setTextColor(ContextCompat.getColor(context, NewsColor.black))
        setAsUnderline()
    }

    private fun setAsUnderline() {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun removeUnderline() {
        paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
    }

    private fun setAsStrikeThruLine() {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}
