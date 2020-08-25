package com.zky.basics.api.splash.service

import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.splash.entity.ImageUrl
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
    @GET("getCaptcha.do")
    suspend fun captcha(): RespDTO<ImageUrl>

    // 验证码
    @GET("sendSms.do")
   suspend fun sendSms(
        @Query("token") token: String?,
        @Query("verCode") verCode: String?,
        @Query("phone") phone: String?,
        @Query("smsType") smsType: String?
    ): RespDTO<Any>

    //组册
    @GET("regist.do")
  suspend  fun regist(
        @Query("userName") userName: String?,
        @Query("password") password: String?,
        @Query("accountLevel") accountLevel: String?,
        @Query("province") province: String?,
        @Query("city") city: String?,
        @Query("county") county: String?,
        @Query("college") college: String?,
        @Query("smsCode") smsCode: String?,
        @Query("phone") phone: String?
    ): RespDTO<Any>
}