package com.zky.basics.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


/**
 * Created by lk
 * Date 2020/2/27
 * Time 10:03
 * Detail:
 */
class TitleFragmentAdapter(
    private var mFragmentManager: FragmentManager,
    var titles: ArrayList<String>,
    var pages: List<Fragment>,
    var viewPager: ViewPager
) : FragmentPagerAdapter(mFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return pages[position]
    }


    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun refreshViewPager(listFragments: List<Fragment>) {
        val fragmentTransaction = mFragmentManager.beginTransaction()
        for (fragment in pages) {
            fragmentTransaction.remove(fragment)
        }
        fragmentTransaction.commit()
        mFragmentManager.executePendingTransactions()
        pages = listFragments
        notifyDataSetChanged()

        viewPager.currentItem = 0
    }
}
