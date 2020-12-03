package com.zky.basics.main.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.api.room.AppDatabase
import com.zky.basics.api.room.Dao.TestRoomDbDao
import com.zky.basics.common.mvvm.BaseMvvmActivity
import com.zky.basics.common.util.PermissionToSetting
import com.zky.basics.main.R
import com.zky.basics.main.mvvm.factory.MainViewModelFactory
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel
import java.lang.ref.WeakReference

class SplashActivity : BaseMvvmActivity<ViewDataBinding, SplashViewModel>() {
    override fun onBindLayout() = R.layout.activity_splash
    private var handler: CustomHandler? = null
    lateinit var testRoomDbDao: TestRoomDbDao

    @SuppressLint("CheckResult")
    override fun initView() {
        handler = WeakReference(CustomHandler()).get()
//        handler?.sendEmptyMessageDelayed(1, 800)
        XXPermissions.with(this).permission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).request(object :
            OnPermission {
            override fun hasPermission(granted: MutableList<String>?, all: Boolean) {
                if (all) {
                testRoomDbDao =
                    AppDatabase.getDatabase(this@SplashActivity)?.testRoomDbDao()!!
                    handler?.sendEmptyMessageDelayed(1, 800)
                }

            }

            override fun noPermission(denied: MutableList<String>?, never: Boolean) {
                PermissionToSetting( this@SplashActivity, denied!!, never, "获取存储权限失败")
            }

        })




    }


//    fun getRoomData() {
//        lifecycleScope.launch {
//            try {
//                val all = testRoomDbDao.all()
//                val testRoomDb = TestRoomDb(2231, "name", 3, "1", "3")
//                testRoomDbDao?.insertOrUpdate(testRoomDb)
//                val all1 = testRoomDbDao.all()
//
//                Log.e("", "")
//            } catch (e: Exception) {
//                Log.e("", "")
//            }
//        }
//    }

    override fun enableToolbar() = false
    fun startMainActivity() {
        ARouter
            .getInstance()
            .build(ARouterPath.LOGIN)
            .withTransition(R.anim.fade_in, 0)
            .navigation(this, object : NavigationCallback {
                override fun onLost(postcard: Postcard?) {
                    //没有找到
                }

                override fun onFound(postcard: Postcard?) {
                    //找到路由
                }

                override fun onInterrupt(postcard: Postcard?) {
                    //使用拦截器的时候
                }

                override fun onArrival(postcard: Postcard?) {
                    //向android 发出startActivity 请求
                    finishActivity()
                }

            })
//            .navigation()
//        finishActivity()
    }

    override fun onBindViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBindViewModelFactory(): ViewModelProvider.Factory? =
        MainViewModelFactory.getInstance(application)

    override fun initViewObservable() {
        mViewModel?.getmVoidSingleLiveEvent()
            ?.observe(this, Observer { startMainActivity() })
    }

    override fun onBindVariableId() = 0

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    override val isFullScreen = true

    inner class CustomHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            startMainActivity()
        }
    }

}