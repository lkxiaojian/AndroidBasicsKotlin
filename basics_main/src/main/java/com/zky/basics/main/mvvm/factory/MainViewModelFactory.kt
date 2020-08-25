package com.zky.basics.main.mvvm.factory

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zky.basics.main.mvvm.model.SplashModel
import com.zky.basics.main.mvvm.viewmodel.SplashViewModel

class MainViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            val splashModel = SplashModel(mApplication)
            return SplashViewModel(mApplication, splashModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): MainViewModelFactory? {
            if (INSTANCE == null) {
                synchronized(MainViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MainViewModelFactory(application)
                    }
                }
            }
            return INSTANCE
        }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}