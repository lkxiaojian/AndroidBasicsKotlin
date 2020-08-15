package com.zky.basics.common.mvvm

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import com.refresh.lib.DaisyRefreshLayout
import com.zky.basics.common.mvvm.viewmodel.BaseRefreshViewModel

/**
 * Description: <下拉刷新></下拉刷新>、上拉加载更多的Activity><br></br>
 *
 * Date:        2019/07/02<br></br>
 * Version:     V1.0.0<br></br>
 * Update:     <br></br>
 */
abstract class BaseMvvmRefreshActivity<V : ViewDataBinding?, VM : BaseRefreshViewModel<*, *>?> :
    BaseMvvmActivity<V, VM>() {
    protected var mRefreshLayout: DaisyRefreshLayout? = null
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
            ?.observe(this, Observer<Any?> { autoLoadData() })
        mViewModel?.uCRefresh()?.stopRefresLiveEvent
            ?.observe(this, Observer<Any?> { stopRefresh() })
        mViewModel?.uCRefresh()?.stopLoadMoreLiveEvent
            ?.observe(this, Observer<Any?> { stopLoadMore() })
    }

    abstract val refreshLayout: DaisyRefreshLayout?
    fun initRefreshView() {
        mRefreshLayout = refreshLayout
    }

    fun stopRefresh() {
        mRefreshLayout!!.isRefreshing = false
    }

    fun stopLoadMore() {
        mRefreshLayout!!.setLoadMore(false)
    }

    fun autoLoadData() {
        mRefreshLayout!!.autoRefresh()
    }
}