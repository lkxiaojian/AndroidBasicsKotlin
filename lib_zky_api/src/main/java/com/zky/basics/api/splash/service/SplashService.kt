package com.zky.basics.api.splash.service

import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.splash.entity.ImageUrl
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lk
 * Date 2019-11-06
 * Time 17:49
 * Detail:
 */
interface SplashService {
    //图形验证
    @get:GET("getCaptcha.do")
    val captcha: Observable<RespDTO<ImageUrl?>?>?

    // 验证码
    @GET("sendSms.do")
    fun sendSms(
        @Query("token") token: String?,
        @Query("verCode") verCode: String?,
        @Query("phone") phone: String?,
        @Query("smsType") smsType: String?
    ): Observable<RespDTO<*>?>?

    //组册
    @GET("regist.do")
    fun regist(
        @Query("userName") userName: String?,
        @Query("password") password: String?,
        @Query("accountLevel") accountLevel: String?,
        @Query("province") province: String?,
        @Query("city") city: String?,
        @Query("county") county: String?,
        @Query("college") college: String?,
        @Query("smsCode") smsCode: String?,
        @Query("phone") phone: String?
    ): Observable<RespDTO<*>?>?
}