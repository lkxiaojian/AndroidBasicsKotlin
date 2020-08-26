package com.zky.basics_module

import androidx.multidex.MultiDex
import com.zky.basics.api.RetrofitManager
import com.zky.basics.common.BaseApplication

/**
 * Created by lk
 * Date 2020/8/26
 * Time 16:42
 * Detail:
 */
class Application :BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        RetrofitManager.init(this)
        MultiDex.install(this)
    }
}