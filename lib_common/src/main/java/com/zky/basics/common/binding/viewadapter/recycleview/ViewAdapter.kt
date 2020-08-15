package com.zky.basics.common.binding.viewadapter.recycleview

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

object ViewAdapter {
    @JvmStatic
    @BindingAdapter("linearLayoutManager")
    fun setLinearLayoutManager(recyclerView: RecyclerView, b: Boolean) {
        val layoutManager = LinearLayoutManager(recyclerView.context)
        layoutManager.orientation =
            if (b) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
    }
}