package com.zky.live.mvvm.model

import android.app.Application
import com.zky.basics.api.RetrofitManager.Companion.instance
import com.zky.basics.api.apiservice.CommonService
import com.zky.basics.common.mvvm.model.BaseModel

/**
 * Created by lk
 * Date 2019-11-08
 * Time 10:55
 * Detail:
 */
class LiveModel(application: Application?) : BaseModel(application) {
    private val mCommonService: CommonService = instance.commonService
}