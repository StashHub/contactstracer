package com.drgayno.contactstracer.ui.surveykit.backend.views.main_parts

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.colorStroke
import com.drgayno.contactstracer.ui.surveykit.backend.helper.ext.px

class Footer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : Toolbar(context, attrs, defStyleRes) {

    private var themeColor = ContextCompat.getColor(context, R.color.colorPrimary)
        set(newColor) {
            buttonContinue.colorMainButtonEnabledState(canContinue, newColor)
            field = newColor
        }

    var canBack: Boolean
        get() = buttonBack.visibility != View.VISIBLE
        set(value) {
            buttonBack.visibility = if (value) View.VISIBLE else View.GONE
        }

    var canContinue: Boolean
        get() = buttonContinue.isEnabled
        set(state) {
            buttonContinue.isEnabled = state
            buttonContinue.colorMainButtonEnabledState(state, themeColor)
        }

    fun setContinueButtonText(text: String) {
        buttonContinue.text = text
    }

    var onContinue: () -> Unit = {}
    var onBack: () -> Unit = {}

    private val root: View = View.inflate(context, R.layout.layout_footer, this)
    private val buttonContinue = root.findViewById<Button>(R.id.button_continue).apply {
        setOnClickListener {
            hideKeyboard()
            onContinue()
        }
        colorMainButtonEnabledState(true, themeColor)
    }

    private val buttonBack = root.findViewById<Button>(R.id.back_button).apply {
        setOnClickListener {
            hideKeyboard()
            onBack()
        }
    }

    private fun Button.colorMainButtonEnabledState(enabled: Boolean, color: Int) {
        val drawable = context.resources.getDrawable(R.drawable.main_button_background, null)
        if (enabled) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            (drawable as GradientDrawable).colorStroke(context.px(1f).toInt(), color)
            setTextColor(color)
        } else {
            setTextColor(ContextCompat.getColor(context, R.color.disabled_grey))
        }
        background = drawable
    }

    // TODO this should probably not be done here
    private fun hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}