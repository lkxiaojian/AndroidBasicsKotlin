package com.zky.basics.common.mvvm.model

import android.app.Application
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


open abstract class BaseModel(protected var mApplication: Application?) :  IBaseModel {
    private var mCompositeDisposable: CompositeDisposable?
    fun addSubscribe(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let {
            mCompositeDisposable?.add(disposable)
        }
    }

//    suspend fun <T : Any> request(call: suspend () -> RespDTO<T>): RespDTO<T> {
//        return withContext(Dispatchers.IO) { call.invoke() }.apply {
//        }
//    }

    override fun onCleared() {
        mCompositeDisposable?.clear()
    }
    init {
        mCompositeDisposable = CompositeDisposable()
    }
}