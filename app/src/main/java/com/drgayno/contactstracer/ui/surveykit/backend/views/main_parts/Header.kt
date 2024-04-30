package com.drgayno.contactstracer.ui.surveykit.backend.views.main_parts

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.util.setTextColor

class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : Toolbar(context, attrs, defStyleRes) {

    var themeColor = Color.BLACK
        set(value) {
            cancelButton.setTextColor(value)
            headerBackButtonImage.background.setTint(value)
            field = value
        }

    var canCancel: Boolean
        get() = cancelButton.visibility != View.VISIBLE
        set(value) {
            cancelButton.visibility = if (value) View.VISIBLE else View.GONE
        }

    var canBack: Boolean
        get() = buttonBack.visibility != View.VISIBLE
        set(value) {
            buttonBack.visibility = if (value) View.VISIBLE else View.GONE
        }

    var cancelButtonText: String
        get() = cancelButton.text.toString()
        set(value) {
            cancelButton.text = value
        }

    var label: String
        get() = headerLabel.text.toString()
        set(label) {
            headerLabel.text = label
        }

    var onBack: () -> Unit = {}
    var onCancel: () -> Unit = {}

    private val root: View = View.inflate(context, R.layout.layout_header, this)
    private val headerLabel: TextView = root.findViewById(R.id.headerLabel)
    private val buttonBack = root.findViewById<RelativeLayout>(R.id.headerBackButton).apply {
        setOnClickListener {
            hideKeyboard()
            onBack()
        }
    }
    private val headerBackButtonImage =
        buttonBack.findViewById<ImageView>(R.id.headerBackButtonImage).apply {
            background.setTint(themeColor)
        }
    private val cancelButton = root.findViewById<Button>(R.id.headerCancelButton).apply {
        setTextColor(themeColor)
        setOnClickListener {
            hideKeyboard()
            onCancel()
        }
    }

    // TODO this should probably not be done here
    private fun hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}