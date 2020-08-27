package com.zky.basics.common.mvvm.model

import android.app.Application
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.api.RetrofitManager
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.http.ExceptionHandler
import com.zky.basics.common.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseModel(protected var mApplication: Application?) : IBaseModel {
    private var mCompositeDisposable: CompositeDisposable?
    fun addSubscribe(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let {
            mCompositeDisposable?.add(it)
        }
    }

    suspend fun <T : Any> request(call: suspend () -> RespDTO<T>): RespDTO<T> {
        return withContext(Dispatchers.IO) { call.invoke() }.apply {
            when {
                code == 408 -> {
                    ARouter.getInstance().build(ARouterPath.LOGIN).navigation()
                    Toast.makeText(
                        RetrofitManager.mContext,
                        R.string.long_time,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ExceptionHandler.SYSTEM_ERROR.INTERNAL_SERVER_ERROR == code -> {
                    Toast.makeText(
                        RetrofitManager.mContext,
                        R.string.server_error,
                        Toast.LENGTH_SHORT
                    ).show()
                    throw Exception(msg)
                }
                code != ExceptionHandler.APP_ERROR.SUCC -> {
                    if (msg.isNullOrEmpty()) {
                        Toast.makeText(
                            RetrofitManager.mContext,
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    throw Exception(msg)
                }
            }
        }
    }


    override fun onCleared() {
        mCompositeDisposable?.clear()
    }

    init {
        mCompositeDisposable = CompositeDisposable()
    }
}