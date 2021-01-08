package com.zky.basics.common.mvvm

import androidx.databinding.ViewDataBinding
import android.view.View
import androidx.lifecycle.Observer
import com.refresh.lib.DaisyRefreshLayout
import com.zky.basics.common.adapter.BaseAdapter
import com.zky.basics.common.mvvm.viewmodel.BaseRefreshViewModel
import com.zky.basics.common.util.log.KLog


abstract class BaseMvvmRefreshFragment<T, V : ViewDataBinding?, VM : BaseRefreshViewModel<*, *>?> :
    BaseMvvmFragment<V, VM>() {
    private var mRefreshLayout: DaisyRefreshLayout? = null
    private var mItemClickListener: BaseAdapter.OnItemClickListener<*>? = null
    private var mOnItemLongClickListener: BaseAdapter.OnItemLongClickListener<*>? = null
    override fun initCommonView(view: View?) {
        super.initCommonView(view)
        initRefreshView()
    }

    override fun initBaseViewObservable() {
        super.initBaseViewObservable()
        initBaseViewRefreshObservable()
    }

    private fun initBaseViewRefreshObservable() {
        mViewModel?.uCRefresh()?.autoRefresLiveEvent
            ?.observe(this, { autoLoadData() })
        mViewModel?.uCRefresh()?.stopRefresLiveEvent
            ?.observe(this, {
                stopRefresh()
            })
        mViewModel?.uCRefresh()?.stopLoadMoreLiveEvent
            ?.observe(this, { stopLoadMore() })
    }

    abstract fun refreshLayout(): DaisyRefreshLayout?
    fun initRefreshView() {
        mRefreshLayout = refreshLayout()
    }

    fun stopRefresh() {
        mRefreshLayout?.isRefreshing = false
    }

    fun stopLoadMore() {
        mRefreshLayout?.setLoadMore(false)
    }

    fun autoLoadData() {
        KLog.v("MYTAG", "autoLoadData start...")
        if (mRefreshLayout != null) {
            KLog.v("MYTAG", "autoLoadData1 start...")
            mRefreshLayout?.autoRefresh()
        }
    }

    fun setItemClickListener(itemClickListener: BaseAdapter.OnItemClickListener<*>?) {
        mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: BaseAdapter.OnItemLongClickListener<*>?) {
        mOnItemLongClickListener = onItemLongClickListener
    }
}