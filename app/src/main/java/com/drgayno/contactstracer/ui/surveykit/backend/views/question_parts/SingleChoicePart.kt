package com.drgayno.contactstracer.ui.surveykit.backend.views.question_parts

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.surveykit.TextChoice
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.px
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.themeColor
import com.drgayno.contactstracer.ui.surveykit.backend.views.helper.BackgroundDrawable
import com.drgayno.contactstracer.ui.surveykit.backend.views.helper.createSelectableThemedBackground

internal class SingleChoicePart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    @ColorInt
    var radioButtonTextColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
        set(color) {
            update(options)
            field = color
        }

    @ColorInt
    var defaultColor: Int = context.themeColor(R.attr.colorOnSurface)

    var options: List<TextChoice> = emptyList()
        set(value) {
            update(value)
            field = value
        }

    var selected: TextChoice?
        get() = selectedChoice()
        set(choice) {
            if (choice != null)
                this.findViewWithTag<RadioButton>(options.indexOf(choice))?.isChecked = true
        }

    fun isOneSelected(): Boolean = this.checkedRadioButtonId != -1

    var onCheckedChangeListener: (RadioGroup, Int) -> Unit = { _, _ -> }

    private val fields: List<RadioButton>
        get() = (0 until this.childCount).mapNotNull { this.getChildAt(it) as? RadioButton }

    private fun update(list: List<TextChoice>) {
        val selectedChoice = selected
        this.removeAllViews()
        list.forEachIndexed { index, textChoice ->
            val item = createRadioButton(
                textChoice.text,
                index,
                if (index == 0) BackgroundDrawable.Border.Both else BackgroundDrawable.Border.Bottom
            )
            if (textChoice == selectedChoice) {
                item.isChecked = true
                item.setTextColor(radioButtonTextColor)
            }
            this.addView(item)
        }
    }

    private fun selectedChoice(): TextChoice? {
        val radioButtonIndex: Int = this
            .findViewById<RadioButton>(this.checkedRadioButtonId)?.tag as? Int
            ?: return null

        return options[radioButtonIndex]
    }

    private fun selectedRadioButton(): RadioButton? = this.findViewById(this.checkedRadioButtonId)

    private val internalCheckedChangeListener: (RadioGroup, Int) -> Unit = { group, checkedId ->
        fields.forEach { it.setTextColor(defaultColor) }
        selectedRadioButton()?.setTextColor(radioButtonTextColor)
        onCheckedChangeListener(group, checkedId)
    }

    private fun createRadioButton(
        label: String,
        tag: Int,
        border: BackgroundDrawable.Border
    ): RadioButton {
        val verticalPaddingEditText = context.px(
            context.resources.getDimension(R.dimen.text_field_vertical_padding)
        ).toInt()
        val horizontalPaddingEditTextLeft = context.px(
            context.resources.getDimension(R.dimen.text_field_horizontal_padding_left)
        ).toInt()
        val horizontalPaddingEditTextRight = context.px(
            context.resources.getDimension(R.dimen.text_field_horizontal_padding_right)
        ).toInt()

        return RadioButton(context).apply {
            id = View.generateViewId()
            text = label
            this.tag = tag
            isFocusable = true
            isClickable = true
            buttonDrawable = null
            textSize = 18f

            background = createSelectableThemedBackground(context, border)

            setPadding(
                horizontalPaddingEditTextLeft,
                verticalPaddingEditText,
                horizontalPaddingEditTextRight,
                verticalPaddingEditText
            )

            val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            this.layoutParams = layoutParams
        }
    }

    init {
        this.id = R.id.singleChoicePart
        this.gravity = Gravity.CENTER

        this.setOnCheckedChangeListener { group, checkedId ->
            internalCheckedChangeListener(group, checkedId)
        }
    }
}