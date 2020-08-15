package com.zky.basics.common.provider;

import android.support.v4.app.Fragment;
import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by lk
 * Date 2019-11-08
 * Time 14:23
 * Detail:
 */
public interface IMapProvider extends IProvider {
    Fragment getMapFragment();
}
