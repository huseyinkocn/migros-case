package com.example.news.util.widget

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.example.news.R

fun TextView.obtainTypeface(attrs: AttributeSet?) {
    findTypeface(attrs)?.let {
        setTypeface(it.typeface, Typeface.NORMAL)
    }
}

fun TextView.setNewsFontFamily(newsFontFamilyId: Int, type: Int = Typeface.NORMAL) {
    setTypeface(NewsFontFamily.parse(newsFontFamilyId).asTypeface(context), type)
}

internal fun TextView.findTypeface(attrs: AttributeSet?): LcwTypeface? {
    context.withStyledAttributes(attrs, R.styleable.NewsTextStyle) {
        val fontFamily = NewsFontFamily.parse(
            getInt(
                R.styleable.NewsTextStyle_newsFontFamily,
                NewsFontFamily.Regular.ordinal
            )
        )

        return LcwTypeface(fontFamily.asTypeface(context))
    }

    return null
}

fun TextView.setEndDrawable(@DrawableRes resId: Int) {
    val drawable = ResourcesCompat.getDrawable(context.resources, resId, null)?.mutate()?.apply {
        isAutoMirrored = true
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
}

fun TextView.setStartDrawable(@DrawableRes resId: Int) {
    val drawable = ResourcesCompat.getDrawable(context.resources, resId, null)?.mutate()?.apply {
        isAutoMirrored = true
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
}
fun TextView.setTopDrawable(@DrawableRes resId: Int) {
    val drawable = ResourcesCompat.getDrawable(context.resources, resId, null)
    setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}
internal data class LcwTypeface(val typeface: Typeface?)

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

fun TextView.isEllipsized(text: String?) = layout.text.toString() != text

fun TextView.setSpannableText(spannableColor: String?, coloredText: String?, colorRes: Int?) {
    try {
        spannableColor?.let {
            val spannable = SpannableString(spannableColor)
            coloredText?.let {
                val start = spannableColor.indexOf(coloredText)
                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            colorRes ?: NewsColor.news_000000
                        )
                    ),
                    start,
                    start + coloredText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                this.text = spannable
            }
        }
    } catch (e: Exception) {
        text = spannableColor.orEmpty()
    }
}

fun TextView.strikeThrough(isStrike: Boolean) {
    if (isStrike) {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

fun TextView.setNewsFont(family: NewsFontFamily) {
    setTypeface(family.asTypeface(context), Typeface.NORMAL)
}

fun TextView.setTextWithContentDesc(text: CharSequence?, contentDesc: CharSequence? = text) {
    this.text = text
    this.contentDescription = contentDesc
}

fun TextView.setContentDesc(contentDesc: CharSequence? = text) {
    this.contentDescription = contentDesc
}
