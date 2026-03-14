package com.example.news.util.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.widget.doAfterTextChanged
import com.example.news.R

open class NewsEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var isError: Boolean = false
        set(value) {
            field = value
            refreshDrawableState()
        }
    var isFilled: Boolean = false
        set(value) {
            field = value
            refreshDrawableState()
        }

    init {
        obtainTypeface(attrs)

        context.withStyledAttributes(attrs, R.styleable.NewsEditText) {
            val defaultTextColor = ContextCompat.getColor(context, NewsColor.news_000000)
            val textColor = getColor(R.styleable.NewsEditText_android_textColor, defaultTextColor)
            setTextColor(textColor)
        }
        doAfterTextChanged { text ->
            isFilled = text.isNullOrEmpty().not() && isError.not()
        }
    }
}
