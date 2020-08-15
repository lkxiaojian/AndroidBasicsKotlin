package com.zky.basics.main.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.Nullable;
import com.zky.basics.common.BR;
import com.zky.basics.common.mvvm.BaseMvvmActivity;
import com.zky.basics.main.R;
import com.zky.basics.main.databinding.ActivityRegistBinding;
import com.zky.basics.main.mvvm.factory.MainViewModelFactory;
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel;

public class RegistActivity extends BaseMvvmActivity<ActivityRegistBinding, SplashViewModel> {
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
        mViewModel.getmVoidSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String aVoid) {
                finishActivity();
            }
        });
    }

    @Override
    public int onBindVariableId() {
        return BR.viewModel;
    }

    @Override
    public int onBindLayout() {
        return R.layout.activity_regist;
    }

    @Override
    public int onBindToolbarLayout() {
        return R.layout.white_common_toolbar;
    }

    @Override
    public String getTootBarTitle() {
        return "账号注册";
    }
    @Override
    public boolean isFullScreen() {
        return true;
    }
}
