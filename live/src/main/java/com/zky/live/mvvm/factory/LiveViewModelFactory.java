package com.zky.live.mvvm.factory;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zky.live.mvvm.model.LiveModel;
import com.zky.live.mvvm.viewmodle.LiveListViewModle;
import com.zky.live.mvvm.viewmodle.LiveViewModle;


public class LiveViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile LiveViewModelFactory INSTANCE;
    private final Application mApplication;

    public static LiveViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (LiveViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }
    private LiveViewModelFactory(Application application) {
        this.mApplication = application;
    }
    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LiveViewModle.class)) {
            return (T) new LiveViewModle(mApplication, new LiveModel(mApplication));
        }else if(modelClass.isAssignableFrom(LiveListViewModle.class)){
            return (T) new LiveListViewModle(mApplication, new LiveModel(mApplication));

        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
