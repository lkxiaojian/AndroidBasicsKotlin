package com.zky.basics.api

import android.app.Application
import android.content.Context
import android.util.Log
import com.zky.basics.api.apiservice.CommonService
import com.zky.basics.api.config.API
import com.zky.basics.api.splash.service.SplashService
import com.zky.basics.api.util.SSLContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class RetrofitManager private constructor() {
    private val mRetrofit: Retrofit
    var okHttpBuilder: OkHttpClient.Builder?
    /**
     * 创建一个公共服务
     *
     * @return
     */
    val commonService: CommonService
        get() = mRetrofit.create(CommonService::class.java)

    /**
     * 创建登入 注册服务
     *
     * @return
     */
    val splashService: SplashService
        get() = mRetrofit.create(SplashService::class.java)

    /**
     * 添加token
     */
    private fun addToken() {
        if (okHttpBuilder != null) okHttpBuilder!!.addInterceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val requestBuilder = original
                .newBuilder()
                .header("token", TOKEN)
            //   .header("Authorization", "Bearer " + token);
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    companion object {
        var retrofitManager: RetrofitManager? = null
        var mContext: Context? = null
        @JvmField
        var TOKEN = ""
        var is_debug = true
        @JvmStatic
        fun init(application: Application?) {
            mContext = application
        }

        @JvmStatic
        val instance: RetrofitManager?
            get() {
                if (retrofitManager == null) {
                    synchronized(RetrofitManager::class.java) {
                        if (retrofitManager == null) {
                            retrofitManager = RetrofitManager()
                        }
                    }
                }
                return retrofitManager
            }
    }

    init {
        val logging =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message: String? ->
                Log.e("OKHttp----->", message)
            })
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder = OkHttpClient.Builder()
        addToken()
        if (is_debug) {
            okHttpBuilder!!.interceptors().add(logging)
        }
        val sslContext = SSLContextUtil.getDefaultSLLContext()
        sslContext.let {
            okHttpBuilder?.sslSocketFactory(it.socketFactory)
        }
        okHttpBuilder!!.hostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER)
        mRetrofit = Retrofit.Builder()
            .client(okHttpBuilder!!.build())
            .baseUrl(API.URL_HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}