package com.zky.basics.common.util

import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.recyclerview.widget.RecyclerView

object ObservableListUtil {
    fun getListChangedCallback(adapter: RecyclerView.Adapter<*>): OnListChangedCallback<*> {
        return object : OnListChangedCallback<ObservableList<*>>() {
            override fun onChanged(observableList: ObservableList<*>?) {
                adapter.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(observableList: ObservableList<*>?, i: Int, i1: Int) {
                adapter.notifyItemRangeChanged(i, i1)
            }

            override fun onItemRangeInserted(observableList: ObservableList<*>?, i: Int, i1: Int) {
                adapter.notifyItemRangeInserted(i, i1)
            }

            override fun onItemRangeMoved(
                observableList: ObservableList<*>?,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (i2 == 1) {
                    adapter.notifyItemMoved(i, i1)
                } else {
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onItemRangeRemoved(observableList: ObservableList<*>?, i: Int, i1: Int) {
                adapter.notifyItemRangeRemoved(i, i1)
            }
        }
    }
}