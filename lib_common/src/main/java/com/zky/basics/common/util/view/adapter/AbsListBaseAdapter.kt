package com.zky.basics.common.util.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter
import java.util.*

/**
 * Created by lk
 * Date 2019-11-14
 * Time 14:37
 * Detail:
 */
abstract class AbsListBaseAdapter<T>(val context: Context) : BaseAdapter() {
    private var list: MutableList<T> = ArrayList()
    protected var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): T? {
        return if (position >= 0 && position < count) {
            list[position]
        } else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun add(t: T?) {
        if (t == null) {
            return
        }
        list.add(t)
        notifyDataSetChanged()
    }

    fun add(index: Int, t: T?) {
        var indexTmp = index
        if (t == null) {
            return
        }
        if (index < 0) {
            indexTmp = 0
        } else if (index > count) {
            indexTmp = count
        }
        list.add(indexTmp, t)
        notifyDataSetChanged()
    }

    fun addAll(list: List<T>?) {
        if (list == null || list.size == 0) {
            return
        }
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addAll( index: Int, list: List<T>?) {
        if (list == null || list.isEmpty()) {
            return
        }
        var tmpIndex=index
        if (index < 0) {
            tmpIndex = 0
        } else if (index > count) {
            tmpIndex = count
        }
        this.list.addAll(tmpIndex, list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }
}