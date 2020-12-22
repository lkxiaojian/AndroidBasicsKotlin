package com.zky.basics.common

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.stetho.Stetho
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKV.initialize
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.entity.UpdateError
import com.xuexiang.xupdate.entity.UpdateError.ERROR
import com.xuexiang.xupdate.utils.UpdateUtils
import com.zhy.http.okhttp.OkHttpUtils
import com.zky.basics.api.RetrofitManager
import com.zky.basics.common.util.OKHttpUpdateHttpService
import com.zky.basics.common.util.log.KLog
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        RetrofitManager.is_debug = BuildConfig.IS_DEBUG
        KLog.init(BuildConfig.IS_DEBUG)
        //web 调试
        Stetho.initializeWithDefaults(this)
        //ali 路由
        if (BuildConfig.IS_DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)

        //app更新
        initOKHttpUtils()
        initUpdate()
        //MMKV
        val initialize = initialize(this)
//        val initialize = initialize("${filesDir.absolutePath} ")
        Log.e("tag","------>$initialize")
    }

    //app 更新
    private fun initUpdate() { //设置版本更新出错的监听
        XUpdate.get()
            .debug(true)
            .isWifiOnly(false) //默认设置只在wifi下检查版本更新
            .isGet(true) //默认设置使用get请求检查版本
            .isAutoMode(false) //默认设置非自动模式，可根据具体使用配置
            .param("versionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
            .param("appKey", packageName)
            .setOnUpdateFailureListener { error: UpdateError ->
                if (error.code != ERROR.CHECK_NO_NEW_VERSION) { //对不同错误进行处理
                }
            }
            .supportSilentInstall(true) //设置是否支持静默安装，默认是true
            .setIUpdateHttpService(OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
            .init(this) //这个必须初始化
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initOKHttpUtils() {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20000L, TimeUnit.MILLISECONDS)
            .readTimeout(20000L, TimeUnit.MILLISECONDS)
            .build()
        OkHttpUtils.initClient(okHttpClient)
    }

    companion object {
        lateinit var instance: BaseApplication
    }
}