package com.zky.live.mvvm.viewmodle

import android.app.Application
import com.zky.basics.common.mvvm.viewmodel.BaseRefreshViewModel
import com.zky.live.mvvm.model.LiveModel
import java.util.*

/**
 * Created by lk
 * Date 2020/2/27
 * Time 17:07
 * Detail:
 */
class LiveListViewModle(application: Application,liveModel: LiveModel)
    : BaseRefreshViewModel<Objects, LiveModel>(application,liveModel) {
    override fun refreshData() {
    }

    override fun loadMore() {
    }
}