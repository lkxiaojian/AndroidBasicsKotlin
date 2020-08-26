package com.zky.basics.main.activity

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xuexiang.xupdate.XUpdate
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.api.config.API
import com.zky.basics.common.BR
import com.zky.basics.common.constant.Constants
import com.zky.basics.common.mvvm.BaseMvvmActivity
import com.zky.basics.main.MainActivity
import com.zky.basics.main.R
import com.zky.basics.main.databinding.ActivityLoginBinding
import com.zky.basics.main.mvvm.factory.MainViewModelFactory.Companion.getInstance
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel

@Route(
    path = ARouterPath.LOGIN,
    group = ARouterPath.GROUP_APP
)
class LoginActivity :
    BaseMvvmActivity<ActivityLoginBinding?, SplashViewModel>() {
    override fun onBindViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBindViewModelFactory(): ViewModelProvider.Factory? = getInstance(application)

    override fun initViewObservable() {
        //check app versionCode if versionCode less than server versionCode  then  app update
        XUpdate.newBuild(this)
            .updateUrl(API.URL_APP_UPDATE)
            .isWifiOnly(false)
            .updateAppUrl(API.ImageFolderPath)
            .update()

        mViewModel!!.getmVoidSingleLiveEvent()
            .observe(this, Observer { aVoid: String? ->
                //online login
                when (aVoid) {
                    "login" -> {
                        Constants.isNet = true
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        showTransLoadingView(false)
                        finishActivity()
                    }
                    "loadShow" -> {
                        showTransLoadingView(true)
                    }
                    "noNet" -> { //school level can offline login
                        Constants.isNet = false
                        ARouter.getInstance().build(ARouterPath.MINE_MAIN).navigation()
                        showTransLoadingView(false)
                        finishActivity()
                    }
                    "miss" -> {
                        showTransLoadingView(false)
                    }
                }
            })
    }

    override fun onBindVariableId() = BR.loginViewModel
    override fun onBindLayout() = R.layout.activity_login
    override fun enableToolbar() = false
    override val isFullScreen: Boolean = true

}