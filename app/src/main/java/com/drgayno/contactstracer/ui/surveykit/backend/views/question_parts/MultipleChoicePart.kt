package com.drgayno.contactstracer.ui.surveykit.backend.views.question_parts

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.surveykit.TextChoice
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.px
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.themeColor
import com.drgayno.contactstracer.ui.surveykit.backend.views.helper.BackgroundDrawable
import com.drgayno.contactstracer.ui.surveykit.backend.views.helper.createSelectableThemedBackground

internal class MultipleChoicePart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    @ColorInt
    var checkBoxTextColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
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

    var selected: List<TextChoice>
        get() = selectedChoices()
        set(list) {
            list.forEach { selected ->
                this.findViewWithTag<CheckBox>(options.indexOf(selected))?.isChecked = true
            }
        }

    fun isOneSelected(): Boolean = this.selectedChoices().isNotEmpty()

    var onCheckedChangeListener: (View, Boolean) -> Unit = { _, _ -> }

    private val fields: List<CheckBox>
        get() = (0 until this.childCount).mapNotNull { this.findViewWithTag(it) as? CheckBox }

    private fun update(list: List<TextChoice>) {
        val selectedChoices = selected
        this.removeAllViews()
        list.forEachIndexed { index, textChoice ->
            val item = createCheckBox(
                textChoice.text,
                index,
                if (index == 0) BackgroundDrawable.Border.Both else BackgroundDrawable.Border.Bottom
            )
            if (selectedChoices.contains(textChoice)) {
                item.isChecked = true
                item.setTextColor(checkBoxTextColor)
            }
            this.addView(item)
        }
    }

    private fun selectedChoices(): List<TextChoice> {
        return fields
            .filter { it.isChecked }
            .map { options[it.tag as Int] }
    }

    private fun selectedCheckBoxes(): List<CheckBox> = fields.filter { it.isChecked }

    private val internalCheckedChangeListener: (View, Boolean) -> Unit = { checkBox, checked ->
        fields.forEach { it.setTextColor(defaultColor) }
        val nothing = resources.getString(R.string.none_of_the_above)
        if ((checkBox as? CheckBox)?.text == nothing) {
            selectedCheckBoxes().forEach {
                it.isChecked = it.text == checkBox.text
                checkBox.setTextColor(checkBoxTextColor)
            }
        } else {
            selectedCheckBoxes().filter { it.text == nothing }.forEach {
                it.setTextColor(defaultColor)
                it.isChecked = false
            }
            selectedCheckBoxes().filter { it.text != nothing }.forEach {
                it.setTextColor(checkBoxTextColor)
                it.isChecked = true
            }
        }
        onCheckedChangeListener(checkBox, checked)
    }

    private fun createCheckBox(
        label: String,
        tag: Int,
        border: BackgroundDrawable.Border
    ): CheckBox {
        val verticalPaddingEditText = context.px(
            context.resources.getDimension(R.dimen.text_field_vertical_padding)
        ).toInt()
        val horizontalPaddingEditTextLeft = context.px(
            context.resources.getDimension(R.dimen.text_field_horizontal_padding_left)
        ).toInt()
        val horizontalPaddingEditTextRight = context.px(
            context.resources.getDimension(R.dimen.text_field_horizontal_padding_right)
        ).toInt()

        val checkBox = CheckBox(context).apply {
            id = View.generateViewId()
            text = label
            this.tag = tag
            isFocusable = true
            isClickable = true
            buttonDrawable = null
            textSize = 18f

            background = createSelectableThemedBackground(context, border)

            setOnClickListener { internalCheckedChangeListener(this, this.isChecked) }

            setPadding(
                horizontalPaddingEditTextLeft,
                verticalPaddingEditText,
                horizontalPaddingEditTextRight,
                verticalPaddingEditText
            )

            val layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.layoutParams = layoutParams
        }

        Handler().post {
            checkBox.background = checkBox.createSelectableThemedBackground(
                context, BackgroundDrawable.Border.Both
            )
        }
        return checkBox
    }

    init {
        this.let {
            id = R.id.multipleChoicePart
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = VERTICAL
        }
    }
}