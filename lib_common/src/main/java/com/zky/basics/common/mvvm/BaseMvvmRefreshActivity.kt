package com.zky.basics.common.mvvm

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.refresh.lib.DaisyRefreshLayout
import com.zky.basics.common.mvvm.viewmodel.BaseRefreshViewModel


abstract class BaseMvvmRefreshActivity<V : ViewDataBinding?, VM : BaseRefreshViewModel<*, *>?> :
    BaseMvvmActivity<V, VM>() {
    private var mRefreshLayout: DaisyRefreshLayout? = null
    override fun initContentView() {
        super.initContentView()
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
            ?.observe(this, { stopRefresh() })
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
        mRefreshLayout?.autoRefresh()
    }
}