package com.zky.basics.main.mvvm.model

import android.app.Application
import com.zky.basics.api.RetrofitManager.Companion.instance
import com.zky.basics.api.apiservice.CommonService
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.http.RxAdapter.Companion.exceptionTransformer
import com.zky.basics.api.http.RxAdapter.Companion.schedulersTransformer
import com.zky.basics.api.splash.entity.ImageUrl
import com.zky.basics.api.splash.entity.RegionOrSchoolBean
import com.zky.basics.api.splash.entity.Userinfo
import com.zky.basics.api.splash.service.SplashService
import com.zky.basics.common.mvvm.model.BaseModel
import io.reactivex.rxjava3.core.Observable

class SplashModel(application: Application?) : BaseModel(application) {
    private val mCommonService: CommonService = instance.commonService
    private val splashService: SplashService = instance.splashService
    fun login(username: String?,password: String?): Observable<RespDTO<Userinfo>> {

        return mCommonService.login(username, password)
            .compose(schedulersTransformer())
            .compose(exceptionTransformer())
    }

    val captcha: Observable<RespDTO<ImageUrl?>>?
        get() = splashService.captcha
            ?.compose(schedulersTransformer())
            ?.compose(exceptionTransformer())

    fun getRegionOrSchool(
        regLevel: String?,
        regCode: String?
    ): Observable<RespDTO<List<RegionOrSchoolBean>>> {
        return mCommonService.getRegionOrSchool(regLevel, regCode)
            .compose(schedulersTransformer())
            .compose(exceptionTransformer())
    }

    fun sendSms(
        token: String?,
        code: String?,
        phone: String?,
        type: String?
    ): Observable<RespDTO<*>>? {
        return splashService.sendSms(token, code, phone, type)
            ?.compose(schedulersTransformer())
            ?.compose(exceptionTransformer())
    }

    fun regist(
        userName: String?,
        password: String?,
        accountLevel: String?,
        province: String?,
        city: String?,
        county: String?,
        college: String?,
        smsCode: String?,
        phone: String?
    ): Observable<RespDTO<*>>? {
        return splashService.regist(
            userName,
            password,
            accountLevel,
            province,
            city,
            county,
            college,
            smsCode,
            phone
        )
            ?.compose(schedulersTransformer())
            ?.compose(exceptionTransformer())
    }

    fun updateUserPassword(
        oprationType: String?,
        phone: String?,
        oldPassword: String?,
        password: String?,
        smsCode: String?
    ): Observable<RespDTO<*>>? {
        return mCommonService.updateUserPassword(
            oprationType,
            phone,
            oldPassword,
            password,
            smsCode
        )?.compose(schedulersTransformer())?.compose(exceptionTransformer())
    }

}