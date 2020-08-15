package com.zky.zky_mine.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.zky.basics.common.BR
import com.zky.basics.common.adapter.TitleFragmentAdapter
import com.zky.basics.common.mvvm.BaseMvvmFragment
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
    private var titleFragmentAdapter: TitleFragmentAdapter? = null


    override fun initView(view: View?) {
    }

    override fun initData() {
        mListFragments.add(LiveListFragment())
        mListFragments.add(LiveListFragment())
        titleFragmentAdapter =
            TitleFragmentAdapter(childFragmentManager, titles, mListFragments, pager_tour)
        pager_tour?.adapter = titleFragmentAdapter
        titleFragmentAdapter?.refreshViewPager(mListFragments)
        layout_tour.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                mListFragments[tab.position].autoLoadData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        layout_tour.setupWithViewPager(pager_tour)
    }

    override fun onBindViewModel(): Class<LiveViewModle> {
        return LiveViewModle::class.java
    }

    override fun onBindViewModelFactory(): ViewModelProvider.Factory {
        return LiveViewModelFactory.getInstance(activity?.application)

    }

    override fun initViewObservable() {


        mViewModel?.getmVoidSingleLiveEvent()?.observe(this, Observer<String?> { t ->
            when (t) {
                "show" -> {
                    showTransLoadingView(true)

                }
                "dismiss" -> showTransLoadingView(false)
                "exit" -> finishActivity()
            }
        })
    }

    override fun onBindVariableId(): Int {
        return BR.liveViewModle
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return LiveMainFragment()
        }
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onBindLayout() = R.layout.main_live_fragment


    override fun getToolbarTitle() = ""





    override fun enableToolbar(): Boolean {
        return false
    }

}