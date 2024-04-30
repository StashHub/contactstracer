package com.drgayno.contactstracer.ui.surveykit.backend.views.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.Gravity
import android.widget.Checkable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.px

@Suppress("unused")
internal fun Checkable.createSelectableThemedBackground(
    context: Context,
    border: BackgroundDrawable.Border
): Drawable = BackgroundCreationHelper().createSelectableBackground(
    context, border
)

private class BackgroundCreationHelper {

    fun createSelectableBackground(
        context: Context,
        border: BackgroundDrawable.Border
    ): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(
            listOf(android.R.attr.state_checked, android.R.attr.state_pressed).toIntArray(),
            createLayerDrawable(
                context,
                BackgroundDrawable.Pressed(border),
                IconDrawable.Check()
            )
        )
        stateListDrawable.addState(
            listOf(android.R.attr.state_pressed).toIntArray(),
            createLayerDrawable(
                context,
                BackgroundDrawable.Pressed(border),
                IconDrawable.Default()
            )
        )
        stateListDrawable.addState(
            listOf(android.R.attr.state_checked).toIntArray(),
            createLayerDrawable(
                context,
                BackgroundDrawable.Default(border),
                IconDrawable.Check()
            )
        )
        stateListDrawable.addState(
            emptyArray<Int>().toIntArray(),
            createLayerDrawable(
                context,
                BackgroundDrawable.Default(border),
                IconDrawable.Default()
            )
        )
        return stateListDrawable
    }

    private fun createLayerDrawable(
        context: Context,
        @DrawableRes backgroundDrawableId: Int,
        @DrawableRes iconDrawableId: Int?
    ): LayerDrawable {
        val layerList = mutableListOf<Drawable>()
        layerList.add(ContextCompat.getDrawable(context, backgroundDrawableId)!!)
        if (iconDrawableId != null) {
            val item = ContextCompat.getDrawable(context, iconDrawableId)
            item?.let { layerList.add(it) }
        }
        val layerDrawable = LayerDrawable(layerList.toTypedArray())

        // this positions the icon correctly
        if (iconDrawableId != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layerDrawable.setLayerGravity(1, Gravity.CENTER_VERTICAL or Gravity.END)
                layerDrawable.setLayerInset(
                    1,
                    0,
                    context.px(8).toInt(),
                    context.px(20).toInt(),
                    context.px(8).toInt()
                )
            } else {
                layerDrawable.setLayerInset(
                    1,
                    context.px(320).toInt(),
                    context.px(8).toInt(),
                    context.px(20).toInt(),
                    context.px(8).toInt()
                )
            }
        }
        return layerDrawable
    }

    private enum class IconDrawable {
        Default, Check;

        @DrawableRes
        operator fun invoke(): Int? = when (this) {
            Default -> R.drawable.ic_check_default
            Check -> R.drawable.ic_check_circle_24px
        }
    }
}

internal enum class BackgroundDrawable {
    Default, Pressed;

    @DrawableRes
    operator fun invoke(border: Border): Int = when (border) {
        Border.Both -> when (this) {
            Default -> R.drawable.input_border
            Pressed -> R.drawable.input_border_pressed
        }
        Border.Bottom -> when (this) {
            Default -> R.drawable.input_border_bottom_border
            Pressed -> R.drawable.input_border_bottom_border_pressed
        }
    }

    enum class Border { Both, Bottom }
}