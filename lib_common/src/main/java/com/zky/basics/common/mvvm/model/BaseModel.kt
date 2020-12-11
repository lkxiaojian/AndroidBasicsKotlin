package com.zky.basics.common.mvvm.model

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.zky.basics.ArouterPath.ARouterPath
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.http.ExceptionHandler
import com.zky.basics.common.R
import com.zky.basics.common.util.ToastUtil
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseModel(protected var mApplication: Application?) : IBaseModel {
    private var mCompositeDisposable = CompositeDisposable()
    fun addSubscribe(disposable: Disposable?) {
        disposable?.let {
            mCompositeDisposable.add(it)
        }
    }

    suspend fun <T : Any> request(call: suspend () -> RespDTO<T>): T? {
        return withContext(Dispatchers.IO) { call.invoke() }.run {
            when {
                ExceptionHandler.SYSTEM_ERROR.LONG_TIME_NO_ACTION == code -> {
                    ToastUtil.showToast(R.string.long_time)
                    ARouter.getInstance().build(ARouterPath.LOGIN).navigation()
                    null
                }
                ExceptionHandler.SYSTEM_ERROR.INTERNAL_SERVER_ERROR == code -> {
                    ToastUtil.showToast(R.string.server_error)
                    throw Exception(msg)
                }
                code != ExceptionHandler.APP_ERROR.SUCC -> {
                    if (!msg.isNullOrEmpty()) {
                        ToastUtil.showToast(msg)
                    }
                    throw Exception(msg)
                }
                else -> {
                    data
                }
            }
        }
    }


    override fun onCleared() {
        mCompositeDisposable.clear()
    }
}