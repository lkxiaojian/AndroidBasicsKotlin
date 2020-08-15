package com.zky.basics.common.provider;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;


public interface IMineProvider extends IProvider {
    Fragment getMainMineFragment();
}
