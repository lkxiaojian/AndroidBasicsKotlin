package com.zky.basics.common.mvvm.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.zky.basics.common.binding.command.BindingAction
import com.zky.basics.common.binding.command.BindingCommand
import com.zky.basics.common.event.SingleLiveEvent
import com.zky.basics.common.mvvm.model.BaseModel


abstract class BaseRefreshViewModel<T, M : BaseModel>(
    application: Application,
    model: M
) : BaseViewModel<M>(application, model) {
    var mList = ObservableArrayList<T>()

    @JvmField
    var orientation: ObservableField<Boolean?> = ObservableField()
    var enableLoadMore: ObservableField<Boolean?> = ObservableField()
    var enableRefresh: ObservableField<Boolean?> = ObservableField()

    /**
     * TODO 是否允许加载更多
     *
     * @return true 允许 false 不允许
     */
    open fun enableLoadMore(): Boolean {
        return true
    }

    /**
     * TODO 是否允许刷新
     *
     * @return true 允许 false 不允许
     */
    open fun enableRefresh(): Boolean {
        return true
    }

    var mUIChangeRefreshLiveData: UIChangeRefreshLiveData? = null

    fun uCRefresh(): UIChangeRefreshLiveData {
        if (mUIChangeRefreshLiveData == null) {
            mUIChangeRefreshLiveData = UIChangeRefreshLiveData()
        }
        return mUIChangeRefreshLiveData!!

    }

    inner class UIChangeRefreshLiveData : SingleLiveEvent<Any?>() {
        var mStopRefresLiveEvent: SingleLiveEvent<Any>? = null
        var mAutoRefresLiveEvent: SingleLiveEvent<Any>? = null
        var mStopLoadMoreLiveEvent: SingleLiveEvent<Any>? = null
        val stopRefresLiveEvent: SingleLiveEvent<Any> =
            createLiveData(mStopRefresLiveEvent).also { mStopRefresLiveEvent = it }

        val autoRefresLiveEvent: SingleLiveEvent<Any> =
            createLiveData(mAutoRefresLiveEvent).also { mAutoRefresLiveEvent = it }

        val stopLoadMoreLiveEvent: SingleLiveEvent<Any> =
            createLiveData(mStopLoadMoreLiveEvent).also { mStopLoadMoreLiveEvent = it }
    }

    fun postStopRefreshEvent() {
        mUIChangeRefreshLiveData?.stopRefresLiveEvent?.call()
    }

    fun postAutoRefreshEvent() {

        mUIChangeRefreshLiveData?.autoRefresLiveEvent?.call()
    }

    fun postStopLoadMoreEvent() {
        mUIChangeRefreshLiveData?.mStopLoadMoreLiveEvent?.call()
    }

    @JvmField
    var onRefreshCommand: BindingCommand<*> = BindingCommand<Any?>(object : BindingAction {
        override fun call() {
            refreshData()
        }
    })

    @JvmField
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(object : BindingAction {
        override fun call() {
            loadMore()
        }
    })

    @JvmField
    var onAutoRefreshCommand: BindingCommand<*> =
        BindingCommand<Any?>(object : BindingAction {
            override fun call() {
                refreshData()
            }
        })

    abstract fun refreshData()
    abstract fun loadMore()

    init {
        enableLoadMore.set(enableLoadMore())
        enableRefresh.set(enableRefresh())
    }
}