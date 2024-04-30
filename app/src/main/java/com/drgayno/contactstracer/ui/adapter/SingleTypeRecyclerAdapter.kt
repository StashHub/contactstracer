package com.drgayno.contactstracer.ui.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ObservableArrayList

import com.drgayno.contactstracer.nav.viewmodel.BaseArchViewModel

class SingleTypeRecyclerAdapter<T> : BaseRecyclerAdapter<T> {

    @LayoutRes
    private var layoutId: Int = 0

    constructor(
        items: ObservableArrayList<T>,
        viewModel: BaseArchViewModel?,
        itemLayoutId: Int
    ) : super(items, viewModel) {
        this.layoutId = itemLayoutId
    }

    constructor(items: ObservableArrayList<T>, itemLayoutId: Int) : super(items) {
        this.layoutId = itemLayoutId
    }

    override fun getLayoutId(itemType: Int): Int {
        return layoutId
    }
}
