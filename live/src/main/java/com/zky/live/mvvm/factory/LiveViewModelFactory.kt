package com.zky.live.mvvm.factory

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zky.live.mvvm.model.LiveModel
import com.zky.live.mvvm.viewmodle.LiveListViewModle
import com.zky.live.mvvm.viewmodle.LiveViewModle

class LiveViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiveViewModle::class.java)) {
            return LiveViewModle(mApplication, LiveModel(mApplication)) as T
        } else if (modelClass.isAssignableFrom(LiveListViewModle::class.java)) {
            return LiveListViewModle(mApplication, LiveModel(mApplication)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LiveViewModelFactory? = null
        fun getInstance(application: Application): LiveViewModelFactory? {
            if (INSTANCE == null) {
                synchronized(LiveViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = LiveViewModelFactory(application)
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