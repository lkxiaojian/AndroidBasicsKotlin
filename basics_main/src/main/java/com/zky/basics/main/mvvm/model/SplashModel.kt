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


    suspend fun login(username: String?, password: String?): RespDTO<Userinfo> = request {
        mCommonService.login(username, password)
    }


    suspend fun captcha(): RespDTO<ImageUrl> = request {
        splashService.captcha()
    }


    suspend fun getRegionOrSchool(
        regLevel: String?,
        regCode: String?
    ): RespDTO<List<RegionOrSchoolBean>> = request {
        mCommonService.getRegionOrSchool(regLevel, regCode)
    }


    suspend fun sendSms(
        token: String?,
        code: String?,
        phone: String?,
        type: String?
    ): RespDTO<Any> = request {
        splashService.sendSms(token, code, phone, type)
    }

    suspend fun regist(
        userName: String?,
        password: String?,
        accountLevel: String?,
        province: String?,
        city: String?,
        county: String?,
        college: String?,
        smsCode: String?,
        phone: String?
    ): RespDTO<Any> = request {
        splashService.regist(
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