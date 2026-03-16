package com.example.news.util.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.example.news.R

fun TextView.obtainTypeface(attrs: AttributeSet?) {
    findTypeface(attrs)?.let {
        setTypeface(it.typeface, Typeface.NORMAL)
    }
}

internal fun TextView.findTypeface(attrs: AttributeSet?): NewsTypeface? {
    context.withStyledAttributes(attrs, R.styleable.NewsTextStyle) {
        val fontFamily = NewsFontFamily.parse(
            getInt(
                R.styleable.NewsTextStyle_newsFontFamily,
                NewsFontFamily.Regular.ordinal
            )
        )

        return NewsTypeface(fontFamily.asTypeface(context))
    }

    return null
}

internal data class NewsTypeface(val typeface: Typeface?)

enum class NewsFontFamily(@FontRes val font: Int) {

    Black(NewsFont.nunito_black),
    BlackItalic(NewsFont.nunito_black_italic),
    Bold(NewsFont.nunito_bold),
    BoldItalic(NewsFont.nunito_bold_italic),
    ExtraBold(NewsFont.nunito_extra_bold),
    ExtraBoldItalic(NewsFont.nunito_extra_bold_italic),
    ExtraLight(NewsFont.nunito_extra_light),
    ExtraLightItalic(NewsFont.nunito_extra_light_italic),
    Italic(NewsFont.nunito_italic),
    Light(NewsFont.nunito_light),
    LightItalic(NewsFont.nunito_light_italic),
    Medium(NewsFont.nunito_medium),
    MediumItalic(NewsFont.nunito_medium_italic),
    Regular(NewsFont.nunito_regular),
    SemiBold(NewsFont.nunito_semi_bold),
    SemiBoldItalic(NewsFont.nunito_semi_bold_italic);

    companion object {

        fun parse(value: Int): NewsFontFamily {
            return entries.firstOrNull { it.ordinal == value } ?: Regular
        }
    }
}

fun NewsFontFamily.asTypeface(context: Context): Typeface? {
    return ResourcesCompat.getFont(
        context,
        this.font
    )
}
