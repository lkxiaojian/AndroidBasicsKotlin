package com.zky.live.fragment.live

import android.view.View
import com.refresh.lib.DaisyRefreshLayout
import com.zky.basics.common.mvvm.BaseMvvmRefreshFragment
import com.zky.live.BR
import com.zky.live.R
import com.zky.live.databinding.LiveListFragemnetBinding
import com.zky.live.mvvm.factory.LiveViewModelFactory
import com.zky.live.mvvm.viewmodle.LiveListViewModle
import java.util.*

/**
 * Created by lk
 * Date 2020/2/27
 * Time 16:50
 * Detail:
 */
class LiveListFragment :
    BaseMvvmRefreshFragment<Objects, LiveListFragemnetBinding, LiveListViewModle>() {
    override fun onBindViewModelFactory() = LiveViewModelFactory.getInstance(activity!!.application)
    override val refreshLayout: DaisyRefreshLayout? = mBinding?.drlLive
    override fun onBindViewModel() = LiveListViewModle::class.java
    override fun initView(view: View?) {
    }

    override fun onBindLayout(): Int = R.layout.live_list_fragemnet

    override fun onBindVariableId(): Int = BR.liveListViewModel
    override fun initViewObservable() {
    }

    override fun getToolbarTitle(): String = "直播"

    override fun initData() {
    }
}