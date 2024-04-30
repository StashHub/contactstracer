package com.drgayno.contactstracer.ui.surveykit.backend.helper.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

fun View.verticalMargin(margin: Int) {
    (this.layoutParams as LinearLayout.LayoutParams).setMargins(0, margin, 0, margin)
}

fun View.horizontalMargin(margin: Int) {
    (this.layoutParams as LinearLayout.LayoutParams).setMargins(margin, 0, margin, 0)
}

fun EditText.afterTextChanged(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) = listener(s.toString())
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })
}

fun GradientDrawable.colorStroke(width: Int, color: Int): Drawable {
    val drawable = this.mutate() as GradientDrawable
    drawable.setStroke(width, color)
    return drawable
}

fun Context.dp(px: Number): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return (px.toFloat() /
            (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()))
}

fun Context.px(dp: Number): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return (dp.toFloat() *
            (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()))
}

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.RED)
    }
}