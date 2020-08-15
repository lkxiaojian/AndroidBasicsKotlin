package com.zky.basics_module;

import android.support.multidex.MultiDex;
import com.zky.basics.api.RetrofitManager;
import com.zky.basics.common.BaseApplication;

/**
 * Created by lk
 * Date 2020-01-08
 * Time 09:52
 * Detail:
 */
public class Application extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.init(this);
        MultiDex.install(this);
    }
}
