package com.zky.basics.common.mvvm.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import com.zky.basics.common.event.SingleLiveEvent
import com.zky.basics.common.mvvm.model.BaseModel
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.*


open class BaseViewModel<M : BaseModel?>(
    application: Application,
    model: M
) : AndroidViewModel(application), IBaseViewModel,
    Consumer<Disposable?> {
    @JvmField
    protected var mModel: M?

    private var mUIChangeLiveData: UIChangeLiveData? = null

    fun uc(): UIChangeLiveData {
        if (mUIChangeLiveData == null) {
            mUIChangeLiveData = UIChangeLiveData()
        }
        return mUIChangeLiveData!!
    }


    inner class UIChangeLiveData : SingleLiveEvent<Any?>() {
        var showInitLoadViewEvent: SingleLiveEvent<Boolean>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var showTransLoadingViewEvent: SingleLiveEvent<Boolean>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var showNoDataViewEvent: SingleLiveEvent<Boolean>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var showNetWorkErrViewEvent: SingleLiveEvent<Boolean>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var startActivityEvent: SingleLiveEvent<Map<String, Any>>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var finishActivityEvent: SingleLiveEvent<Void>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var onBackPressedEvent: SingleLiveEvent<Void>? = null
            get() = createLiveData(field).also { field = it }
            private set

    }

    protected fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
        var liveDataTmp = liveData
        if (liveData == null) {
            liveDataTmp = SingleLiveEvent<T>()
        }
        return liveDataTmp!!
    }

    object ParameterField {
        var CLASS = "CLASS"
        var CANONICAL_NAME = "CANONICAL_NAME"
        var BUNDLE = "BUNDLE"
    }

    fun postShowInitLoadViewEvent(show: Boolean) {

        mUIChangeLiveData?.showInitLoadViewEvent?.postValue(show)

    }

    fun postShowNoDataViewEvent(show: Boolean) {

        mUIChangeLiveData?.showNoDataViewEvent?.postValue(show)

    }

    fun postShowTransLoadingViewEvent(show: Boolean) {
        mUIChangeLiveData?.showTransLoadingViewEvent?.postValue(show)

    }

    fun postShowNetWorkErrViewEvent(show: Boolean) {
        mUIChangeLiveData?.showNetWorkErrViewEvent!!.postValue(show)
    }

    fun postStartActivityEvent(clz: Class<*>, bundle: Bundle?) {
        val params: MutableMap<String, Any> =
            HashMap()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        mUIChangeLiveData!!.startActivityEvent!!.postValue(params)
    }

    fun postFinishActivityEvent() {
        mUIChangeLiveData!!.finishActivityEvent!!.call()
    }

    fun postOnBackPressedEvent() {
        mUIChangeLiveData!!.onBackPressedEvent!!.call()
    }

    override fun onAny(
        owner: LifecycleOwner?,
        event: Lifecycle.Event?
    ) {
    }

    override fun onCreate() {}
    override fun onDestroy() {}
    override fun onStart() {}
    override fun onStop() {}
    override fun onResume() {}
    override fun onPause() {}
    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {

        mModel?.addSubscribe(disposable)

    }

    override fun onCleared() {
        super.onCleared()
        mModel?.onCleared()

    }

    init {
        mModel = model
    }
}