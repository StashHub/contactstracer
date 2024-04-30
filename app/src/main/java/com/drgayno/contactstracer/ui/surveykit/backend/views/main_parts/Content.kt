package com.drgayno.contactstracer.ui.surveykit.backend.views.main_parts

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.drgayno.contactstracer.R

class Content @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : NestedScrollView(context, attrs, defStyleRes) {

    private val root: View = View.inflate(context, R.layout.layout_content, this)
    private val container: ViewGroup = root.findViewById(R.id.content_container)
    private val footerContainer: ViewGroup = root.findViewById(R.id.footer_container)
    private val footer: Footer = Footer(context).apply { id = R.id.questionFooter }

    fun <T : View> add(view: T): T {
        container.addView(view)
        return view
    }

    fun clear() {
        container.removeAllViews()
    }

    init {
        this.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        ).apply {
            this.weight = 1f
        }
        footerContainer.addView(footer)
    }
}