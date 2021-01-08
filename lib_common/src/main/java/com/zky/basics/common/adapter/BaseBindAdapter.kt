package com.zky.basics.common.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseBindAdapter<T, B : ViewDataBinding?>(
    protected var context: Context,
    private var items: ObservableArrayList<T>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mItemClickListener: OnItemClickListener<Any>? = null
    var mOnItemLongClickListener: OnItemLongClickListener<Any>? = null

    override fun getItemCount(): Int {
        return if (items != null && items!!.size > 0) items!!.size else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val binding: B = DataBindingUtil.inflate<B>(
            LayoutInflater.from(context), getLayoutItemId(viewType),
            parent,
            false
        )
        return BaseBindingViewHolder(binding?.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: B = DataBindingUtil.getBinding<B>(holder.itemView)!!
        onBindItem(binding, items!![position], position)
    }

    open class BaseBindingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    @LayoutRes
    protected abstract fun getLayoutItemId(viewType: Int): Int

    protected abstract fun onBindItem(binding: B?, item: T, position: Int)
    fun setItemClickListener(itemClickListener: OnItemClickListener<Any>) {
        mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<Any>?) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    interface OnItemClickListener<E> {
        fun onItemClick(e: E, position: Int)
    }

    interface OnItemLongClickListener<E> {
        fun onItemLongClick(e: E, postion: Int): Boolean
    }

}