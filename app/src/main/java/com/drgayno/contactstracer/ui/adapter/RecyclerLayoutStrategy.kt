package com.drgayno.contactstracer.ui.adapter

interface RecyclerLayoutStrategy {
    fun getLayoutId(item: Any): Int
}