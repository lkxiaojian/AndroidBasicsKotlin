package com.zky.live.provider;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.zky.basics.common.provider.ILiveProvider;
import com.zky.zky_mine.fragment.LiveMainFragment;

import static com.zky.basics.ArouterPath.ARouterPath.LIVE_MAIN;


@Route(path = LIVE_MAIN, name = "直播")
public class LiveProvider implements ILiveProvider {

    @Override
    public void init(Context context) {

    }



    @Override
    public Fragment getMainLiveFragment() {
        return LiveMainFragment.newInstance();
    }
}
