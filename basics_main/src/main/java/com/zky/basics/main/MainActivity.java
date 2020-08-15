package com.zky.basics.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.zky.basics.common.mvvm.BaseActivity;
import com.zky.basics.common.provider.ILiveProvider;
import com.zky.basics.common.provider.IMineProvider;
import com.zky.basics.main.entity.MainChannel;


public class MainActivity extends BaseActivity  {

    @Autowired(name = "/live/main")
    ILiveProvider iLiveProvider;
    @Autowired(name = "/me/main")
    IMineProvider mMineProvider;


    private Fragment mFlayFragment;
    private Fragment mMeFragment;
    private Fragment mCurrFragment;
    private static final int TIME_EXIT = 2000;
    private long mBackPressed;

    @Override
    public int onBindLayout() {

        return R.layout.commot_activity_main;
    }

    @Override
    public void initView() {


        BottomNavigationView navigation = findViewById(R.id.common_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int i = menuItem.getItemId();
                if (i == R.id.navigation_trip) {
                    switchContent(mCurrFragment, mFlayFragment, MainChannel.NEWS.name);
                    mCurrFragment = mFlayFragment;
                    return true;
                } else if (i == R.id.navigation_me) {
                    switchContent(mCurrFragment, mMeFragment, MainChannel.ME.name);
                    mCurrFragment = mMeFragment;
                    return true;
                }
                return false;
            }
        });

        if (iLiveProvider != null) {
            mFlayFragment = iLiveProvider.getMainLiveFragment();
        }
        if (mMineProvider != null) {
            mMeFragment = mMineProvider.getMainMineFragment();
        }
        mCurrFragment = mFlayFragment;
        if (iLiveProvider != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mFlayFragment, MainChannel.NEWS.name).commit();
        }
    }

    @Override
    public void initData() {

    }

    public void switchContent(Fragment from, Fragment to, String tag) {
        if (from == null || to == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.frame_content, to, tag).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }

    @Override
    public boolean enableToolbar() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_EXIT > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "再点击一次返回退出程序", Toast.LENGTH_SHORT).show();
            mBackPressed = System.currentTimeMillis();
        }
    }


}
