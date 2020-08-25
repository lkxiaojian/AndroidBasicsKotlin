package com.zky.basics.main.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.common.mvvm.BaseMvvmActivity
import com.zky.basics.main.R
import com.zky.basics.main.mvvm.factory.MainViewModelFactory
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

class SplashActivity : BaseMvvmActivity<ViewDataBinding, SplashViewModel>(), CoroutineScope {
    override fun onBindLayout() = R.layout.activity_splash
    private var handler: CustomHandler? = null
    var job: Job = Job()
//    lateinit var testRoomDbDao: TestRoomDbDao
    @SuppressLint("CheckResult")
    override fun initView() {
        handler = WeakReference(CustomHandler()).get()
        handler?.sendEmptyMessageDelayed(1, 800)
//        RxPermissions(this).request(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ).subscribe { permission: Boolean ->
//            if (permission) {
//                handler!!.sendEmptyMessageDelayed(1, 2000)
//                //同意sd 权限后创建room 数据库
//                testRoomDbDao =
//                    AppDatabase.getDatabase(this)?.testRoomDbDao()!!
//                val roomData = getRoomData<List<TestRoomDb>>()
//                Log.e("tag", "roomData--->$roomData")
//            } else {
//                ToastUtil.showToast("读取sd卡权限被拒绝")
//            }
//        }
    }


//    fun getRoomData() {
//        val coroutineScope = CoroutineScope(coroutineContext)
//        coroutineScope.launch(Dispatchers.Main) {
//            val withContext = withContext(Dispatchers.IO) {
//                // val testRoomDb = TestRoomDb(223, "name", 3, "1", "3")
//                // testRoomDbDao?.insertAll(testRoomDb)
//                testRoomDbDao.all
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

    override val coroutineContext = Dispatchers.Main + job
}