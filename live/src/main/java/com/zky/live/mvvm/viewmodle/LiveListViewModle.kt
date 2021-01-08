package com.zky.live.mvvm.viewmodle

import android.app.Application
import com.zky.basics.common.mvvm.viewmodel.BaseRefreshViewModel
import com.zky.live.mvvm.model.LiveModel

/**
 * Created by lk
 * Date 2020/2/27
 * Time 17:07
 * Detail:
 */
class LiveListViewModle(application: Application, liveModel: LiveModel) :
    BaseRefreshViewModel<String, LiveModel>(application, liveModel) {
    override fun refreshData() {
        setData()
        postStopRefreshEvent()
    }


    override fun loadMore() {
//        setData()
        postStopLoadMoreEvent()
    }


    private fun setData() {
        for (i in 1..10) {
            mList.add("$i")
        }

    }
}