package com.zky.live.fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zky.basics.common.BR
import com.zky.basics.common.adapter.FragmentPager2Adapter
import com.zky.basics.common.mvvm.BaseMvvmFragment
import com.zky.basics.common.view.animotion.ZoomOutPageTransformer
import com.zky.live.R
import com.zky.live.fragment.live.LiveListFragment
import com.zky.live.mvvm.factory.LiveViewModelFactory
import com.zky.live.mvvm.viewmodle.LiveViewModle
import kotlinx.android.synthetic.main.main_live_fragment.*
import java.util.*


/**
 * Created by lk
 * Date 2019-10-28
 * Time 16:45
 * Detail:测试
 */
class LiveMainFragment : BaseMvvmFragment<ViewDataBinding, LiveViewModle>() {
    private val titles = arrayListOf("无人机直播", "手机直播")
    private val mListFragments = ArrayList<Fragment>()
    override fun initView(view: View?) {
    }

    override fun initData() {
        mListFragments.add(LiveListFragment())
        mListFragments.add(LiveListFragment())
        val fragmentPager2Adapter = FragmentPager2Adapter(activity!!, mListFragments)

        pager_tour?.adapter = fragmentPager2Adapter
        pager_tour.currentItem = 0
        //添加动画
        pager_tour.setPageTransformer(ZoomOutPageTransformer())
        //切换tab页
        TabLayoutMediator(layout_tour, pager_tour,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = titles[position]
            }).attach()
    }

    override fun onBindViewModel() = LiveViewModle::class.java

    override fun onBindViewModelFactory() = LiveViewModelFactory.getInstance(activity!!.application)

    override fun initViewObservable() {
        mViewModel?.getmVoidSingleLiveEvent()
            ?.observe(this, androidx.lifecycle.Observer { t ->
                when (t) {
                    "show" -> {
                        showTransLoadingView(true)
                    }
                    "dismiss" -> showTransLoadingView(false)
                    "exit" -> finishActivity()
                }
            })
    }

    override fun onBindVariableId() = BR.liveViewModle

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return LiveMainFragment()
        }
    }

    override fun onBindLayout() = R.layout.main_live_fragment
    override fun getToolbarTitle() = ""
    override fun enableToolbar() = false
}