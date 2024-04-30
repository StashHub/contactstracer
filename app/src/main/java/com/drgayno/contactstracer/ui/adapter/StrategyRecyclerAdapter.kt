package com.drgayno.contactstracer.ui.adapter

import androidx.databinding.ObservableArrayList
import com.drgayno.contactstracer.nav.viewmodel.BaseArchViewModel

class StrategyRecyclerAdapter(
    items: ObservableArrayList<Any>,
    private var strategy: RecyclerLayoutStrategy,
    vm: BaseArchViewModel?
) :
    BaseRecyclerAdapter<Any>(items, vm) {

    override fun getLayoutId(itemType: Int): Int {
        return itemType
    }

    override fun getItemViewType(position: Int): Int {
        return strategy.getLayoutId(getItem(position))
    }
}
