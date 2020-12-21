package com.zky.basics.main

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.common.mvvm.BaseActivity
import com.zky.basics.common.provider.ILiveProvider
import com.zky.basics.common.provider.IMineProvider
import com.zky.basics.main.entity.MainChannel
import kotlinx.android.synthetic.main.commot_activity_main.*

class MainActivity : BaseActivity() {
    @JvmField
    @Autowired(name = ARouterPath.LIVE_MAIN)
    var iLiveProvider: ILiveProvider? = null

    @JvmField
    @Autowired(name = ARouterPath.MINE_MAIN)
    var mMineProvider: IMineProvider? = null
    private var mFlayFragment: Fragment? = null
    private var mMeFragment: Fragment? = null
    private var mCurrFragment: Fragment? = null
    private var mBackPressed: Long = 0
    override fun onBindLayout() = R.layout.commot_activity_main
    override fun initView() {
        val navigation =
            findViewById<BottomNavigationView>(R.id.common_navigation)
        navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_trip -> {
                    switchContent(mCurrFragment, mFlayFragment, MainChannel.NEWS.name)
                    mCurrFragment = mFlayFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_me -> {
                    switchContent(mCurrFragment, mMeFragment, MainChannel.ME.name)
                    mCurrFragment = mMeFragment
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        mFlayFragment = iLiveProvider?.mainLiveFragment
        mMeFragment = mMineProvider?.mainMineFragment

        mCurrFragment = mFlayFragment
        iLiveProvider.let {
            supportFragmentManager.beginTransaction().replace(
                R.id.frame_content,
                mFlayFragment!!,
                MainChannel.NEWS.name
            ).commit()
        }
    }

    override fun initData() {
        try {
            val menuView = common_navigation.getChildAt(0) as BottomNavigationMenuView
            for (i in 0 until menuView.childCount) {
                menuView.getChildAt(i).setOnLongClickListener { true }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun switchContent(from: Fragment?, to: Fragment?, tag: String?) {
        if (from == null || to == null) {
            return
        }
        val transaction =
            supportFragmentManager.beginTransaction()
        if (!to.isAdded) {
            transaction.hide(from).add(R.id.frame_content, to, tag).commit()
        } else {
            transaction.hide(from).show(to).commit()
        }
    }

    override fun enableToolbar() = false
    override val isFullScreen = false
    override fun onBackPressed() {
        mBackPressed = if (mBackPressed + TIME_EXIT > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(this, "再点击一次返回退出程序", Toast.LENGTH_SHORT).show()
            System.currentTimeMillis()
        }
    }

    companion object {
        private const val TIME_EXIT = 2000
    }
}