package com.zky.basics.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by lk
 * Date 2020/7/12
 * Time 11:01
 * Detail:
 */
class FragmentPager2Adapter(
    fragmentActivity: FragmentActivity,
    private val mFragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
    override fun getItemCount(): Int {
        return mFragments.size
    }
}