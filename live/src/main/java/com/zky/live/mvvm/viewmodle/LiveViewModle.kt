package com.zky.live.mvvm.viewmodle

import android.app.Application
import android.view.View
import com.zky.basics.common.event.SingleLiveEvent
import com.zky.basics.common.mvvm.viewmodel.BaseViewModel
import com.zky.live.mvvm.model.LiveModel

/**
 * Created by lk
 * Date 2020-02-24
 * Time 15:43
 * Detail:
 */
class LiveViewModle(application: Application, model: LiveModel) : BaseViewModel<LiveModel>(application, model) {

    private var mVoidSingleLiveEvent: SingleLiveEvent<String>? = null


    fun startClick(view: View) {
        when (view.id) {

        }
    }

    fun getmVoidSingleLiveEvent(): SingleLiveEvent<String>? {
        mVoidSingleLiveEvent = createLiveData(mVoidSingleLiveEvent)
        return mVoidSingleLiveEvent
    }


}