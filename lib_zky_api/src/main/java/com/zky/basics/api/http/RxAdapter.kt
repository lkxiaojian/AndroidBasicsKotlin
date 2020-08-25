package com.zky.basics.api.http

import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent

import com.zky.basics.api.RetrofitManager
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.http.ExceptionHandler.APP_ERROR
import com.zky.basics.api.http.ExceptionHandler.SYSTEM_ERROR

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers


class RxAdapter {

    /**
     * 生命周期绑定
     *
     * @param lifecycle Activity
     */
    fun <T> bindUntilEvent(lifecycle: LifecycleProvider<Any>): LifecycleTransformer<T> {
        return lifecycle.bindUntilEvent(ActivityEvent.DESTROY)
    }

    companion object {
        /**
         * 线程调度器
         */
        @JvmStatic
        fun <T> schedulersTransformer(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        @JvmStatic
        fun <T> exceptionTransformer(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream
                    .map(HandleFuc()) //这里可以取出BaseResponse中的Result
                    .onErrorResumeNext(HttpResponseFunc())
            }
        }
    }

    internal class HttpResponseFunc<T> : Function<Throwable, Observable<T>> {
        override fun apply(t: Throwable): Observable<T> {
            val exception = ExceptionHandler.handleException(t)
            if (exception.code == SYSTEM_ERROR.TIMEOUT_ERROR) {
                Toast.makeText(RetrofitManager.mContext, "网络不给力哦！", Toast.LENGTH_SHORT).show()
            } else {

            }
            return Observable.error(exception)
        }
    }

    private class HandleFuc<T> : Function<T, T> {
        @Throws(Exception::class)
        override fun apply(o: T): T {
            if (o is RespDTO<*>) {
                val respDTO = o
                when {
                    respDTO.code == 408 -> { //token 过期 返回登入界面
                        ARouter.getInstance().build("/app/login" ).navigation()
                        Toast.makeText(
                            RetrofitManager.mContext,
                            "长时间未操作，需要重写登入",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    respDTO.code != APP_ERROR.SUCC -> {
                        Toast.makeText(
                            RetrofitManager.mContext,
                            respDTO.msg + "",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            return o
        }
    }
}