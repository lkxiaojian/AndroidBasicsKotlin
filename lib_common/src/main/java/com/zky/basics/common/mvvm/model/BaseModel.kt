package com.zky.basics.common.mvvm.model

import android.app.Application
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseModel(protected var mApplication: Application) : IBaseModel {
    private var mCompositeDisposable: CompositeDisposable?
    fun addSubscribe(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable!!)
    }

    override fun onCleared() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()
        }
    }

    init {
        mCompositeDisposable = CompositeDisposable()
    }
}