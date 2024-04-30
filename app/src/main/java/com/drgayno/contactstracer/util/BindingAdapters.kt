@file:Suppress("UNCHECKED_CAST")

package com.drgayno.contactstracer.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drgayno.contactstracer.nav.viewmodel.BaseArchViewModel
import com.drgayno.contactstracer.ui.adapter.BaseRecyclerAdapter
import com.drgayno.contactstracer.ui.adapter.RecyclerLayoutStrategy
import com.drgayno.contactstracer.ui.adapter.SingleTypeRecyclerAdapter
import com.drgayno.contactstracer.ui.adapter.StrategyRecyclerAdapter
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

@BindingAdapter("layoutFullscreen")
fun View.bindLayoutFullscreen(previousFullscreen: Boolean, fullscreen: Boolean) {
    if (previousFullscreen != fullscreen && fullscreen) {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}

@BindingAdapter("visibleOrGone")
fun setVisibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrInvisible")
fun setVisibleOrInvisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/** Extension function to validate a phone address pattern. */
fun String?.isValidPhone(context: Context, region: String = "US"): Boolean {
    val phoneNumberKit = PhoneNumberUtil.createInstance(context)
    if (!phoneNumberKit.isPossibleNumber(this, region))
        return false

    val number = phoneNumberKit.parse(this, region)
    return phoneNumberKit.isValidNumber(number)
}

/** Extension function to add [TextWatcher] to the EditText */
inline fun EditText.onTextChanged(crossinline listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

@BindingAdapter("textColorResource")
fun setTextColor(editText: TextView, resId: Int?) {
    if (resId != null && resId != 0) {
        editText.setTextColor(ContextCompat.getColor(editText.context, resId))
    }
}

@BindingAdapter("textColorResource")
fun setTextColor(constraintLayout: ConstraintLayout, resId: Int?) {
    if (resId != null && resId != 0) {
        constraintLayout.setBackgroundColor(ContextCompat.getColor(constraintLayout.context, resId))
    }
}

@BindingAdapter(
    value = ["viewModel", "items", "layoutId", "layoutStrategy", "orientation", "spanCount", "lifecycle", "parentItem"],
    requireAll = false
)
fun <T> bindItems(
    view: RecyclerView,
    vm: BaseArchViewModel?,
    items: ObservableArrayList<T>,
    layoutId: Int?,
    layoutStrategy: RecyclerLayoutStrategy?,
    orientation: Int?,
    spanCount: Int?,
    lifecycleOwner: LifecycleOwner?,
    parentItem: Any?
) {
    if (view.adapter == null) {
        if (view.layoutManager == null) {
            if (spanCount == null) {
                view.layoutManager = object : LinearLayoutManager(
                    view.context, orientation
                        ?: RecyclerView.VERTICAL, false
                ) {
                    override fun supportsPredictiveItemAnimations(): Boolean {
                        return false
                    }
                }
            } else {
                view.layoutManager = object : GridLayoutManager(
                    view.context, spanCount
                ) {
                    override fun supportsPredictiveItemAnimations(): Boolean {
                        return false
                    }
                }
            }
        }
        if (layoutStrategy == null) {
            if (layoutId != null) {
                view.adapter = SingleTypeRecyclerAdapter(items, vm, layoutId)
            }
        } else {
            view.adapter =
                StrategyRecyclerAdapter(items as ObservableArrayList<Any>, layoutStrategy, vm)
        }
    } else {
        (view.adapter as BaseRecyclerAdapter<T>).setItems(items)
    }
    (view.adapter as BaseRecyclerAdapter<*>).lifecycleOwner = lifecycleOwner
    (view.adapter as BaseRecyclerAdapter<*>).parentItem = parentItem

}