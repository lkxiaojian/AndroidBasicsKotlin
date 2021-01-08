package com.zky.live.fragment.live

import android.view.View
import com.zky.basics.common.adapter.BaseBindAdapter
import com.zky.basics.common.mvvm.BaseMvvmRefreshFragment
import com.zky.basics.common.util.ObservableListUtil
import com.zky.basics.common.util.spread.showToast
import com.zky.live.BR
import com.zky.live.R
import com.zky.live.adapter.TestListAdapter
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
    BaseMvvmRefreshFragment<Objects, LiveListFragemnetBinding, LiveListViewModle>(),BaseBindAdapter.OnItemClickListener<Any> {
    override fun onBindViewModelFactory() = LiveViewModelFactory.getInstance(activity!!.application)
    override fun refreshLayout() = mBinding?.drlLive
    override fun onBindViewModel() = LiveListViewModle::class.java
    override fun initView(view: View?) {
        val testAdapter = TestListAdapter(activity!!, mViewModel?.mList)
        mViewModel?.mList?.addOnListChangedCallback(
            ObservableListUtil.getListChangedCallback(
                testAdapter
            )
        )
        mBinding?.recview?.adapter = testAdapter
    }

    override fun onBindLayout(): Int = R.layout.live_list_fragemnet

    override fun onBindVariableId(): Int = BR.liveListViewModel
    override fun initViewObservable() {
    }

    override fun getToolbarTitle() = "直播"

    override fun initData() {
        mViewModel?.mList?.add("212")
    }


    override fun onItemClick(e: Any, position: Int) {
        e.toString().showToast()
    }
}