package com.zky.basics.common.binding.viewadapter.daisyrefresh

import androidx.databinding.BindingAdapter
import com.refresh.lib.DaisyRefreshLayout
import com.zky.basics.common.binding.command.BindingCommand

object ViewAdapter {
    @JvmStatic
    @BindingAdapter(
        value = ["onRefreshCommand", "onLoadMoreCommand", "onAutoRefreshCommand"],
        requireAll = false
    )
    fun onRefreshCommand(
        refreshLayout: DaisyRefreshLayout,
        onRefreshCommand: BindingCommand<*>?,
        onLoadMoreCommond: BindingCommand<*>?,
        onAutoRerefeshCommond: BindingCommand<*>?
    ) {
        refreshLayout.setOnRefreshListener {
            onRefreshCommand?.execute()
        }
        refreshLayout.setOnLoadMoreListener {
            onLoadMoreCommond?.execute()
        }
        refreshLayout.setOnAutoLoadListener {
            onAutoRerefeshCommond?.execute()
        }
    }
}