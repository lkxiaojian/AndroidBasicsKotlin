package com.zky.live.provider

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.common.provider.ILiveProvider
import com.zky.zky_mine.fragment.LiveMainFragment.Companion.newInstance

@Route(path = ARouterPath.LIVE_MAIN, name = "直播")
class LiveProvider : ILiveProvider {
    override fun init(context: Context) {}
    override val mainLiveFragment: Fragment
        get() = newInstance()
}