package com.zky.basics.main.activity;

import android.arch.lifecycle.ViewModelProvider;

import com.zky.basics.api.splash.entity.TestE;
import com.zky.basics.common.BR;
import com.zky.basics.common.mvvm.BaseMvvmActivity;
import com.zky.basics.main.R;
import com.zky.basics.main.databinding.ActivityLoginBinding;
import com.zky.basics.main.mvvm.factory.MainViewModelFactory;
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel;

public class FrogetActivity extends BaseMvvmActivity<ActivityLoginBinding, SplashViewModel> {
    @Override
    public Class<SplashViewModel> onBindViewModel() {
        return SplashViewModel.class;
    }

    @Override
    public ViewModelProvider.Factory onBindViewModelFactory() {
        return MainViewModelFactory.getInstance(getApplication());
    }

    @Override
    public void initViewObservable() {
        mViewModel.getCaptcha();
        mViewModel.getmVoidSingleLiveEvent().observe(this, aVoid -> finishActivity());
    }

    @Override
    public int onBindVariableId() {
        return BR.forgetViewModel;
    }

    @Override
    public int onBindLayout() {
        return R.layout.activity_froget;
    }


    @Override
    public int onBindToolbarLayout() {
        return R.layout.white_common_toolbar;
    }

    @Override
    public String getTootBarTitle() {
        return "重置密码";
    }
    @Override
    public boolean isFullScreen() {
        return true;
    }

}
