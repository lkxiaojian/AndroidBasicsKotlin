package com.zky.basics.common.binding.viewadapter.recycleview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object ViewAdapter {
    @JvmStatic
    @BindingAdapter("linearLayoutManager")
    fun setLinearLayoutManager(recyclerView: RecyclerView, b: Boolean) {
        val layoutManager =
            LinearLayoutManager(recyclerView.context)
        layoutManager.orientation =
            if (b) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
    }
}