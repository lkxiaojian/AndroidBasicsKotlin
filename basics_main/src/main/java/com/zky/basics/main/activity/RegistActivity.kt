package com.zky.basics.main.activity

import androidx.lifecycle.Observer
import com.zky.basics.common.BR
import com.zky.basics.common.mvvm.BaseMvvmActivity
import com.zky.basics.main.R
import com.zky.basics.main.databinding.ActivityRegistBinding
import com.zky.basics.main.mvvm.factory.MainViewModelFactory.Companion.getInstance
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel

class RegistActivity :
    BaseMvvmActivity<ActivityRegistBinding?, SplashViewModel>() {
    override fun onBindViewModel() = SplashViewModel::class.java

    override fun onBindViewModelFactory() = getInstance(application)
    override fun initViewObservable() {
        mViewModel!!.captcha()
        mViewModel!!.getmVoidSingleLiveEvent().observe(
            this,
            Observer { finishActivity() }
        )
    }

    override fun onBindVariableId()= BR.viewModel

    override fun onBindLayout()=R.layout.activity_regist

    override fun onBindToolbarLayout()=R.layout.white_common_toolbar

    override val tootBarTitle="账号注册"

    override val isFullScreen: Boolean=true
}