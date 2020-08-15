package com.zky.basics.api.apiservice

import com.zky.basics.api.common.entity.OssToken
import com.zky.basics.api.common.entity.UpdataBean
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.splash.entity.RegionOrSchoolBean
import com.zky.basics.api.splash.entity.Userinfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommonService {
    @GET("login.do")
    fun login(@Query("phone") phone: String?, @Query("password") pwd: String?): Observable<RespDTO<Userinfo>>

    //获取最新app信息
    @get:GET("getAppInfo.do")
    val appInfo: Observable<RespDTO<UpdataBean>>

    //忘记密码
    @GET("updateUserPassword.do")
    fun updateUserPassword(
        @Query("oprationType") oprationType: String?,
        @Query("phone") phone: String?,
        @Query("oldPassword") oldPassword: String?,
        @Query("password") password: String?,
        @Query("smsCode") smsCode: String?
    ): Observable<RespDTO<*>?>?

    @GET("getUser.do")
    fun getUser(@Query("phone") phone: String?): Observable<RespDTO<Userinfo>>

    //获取app token
    @GET("getAppToken.do")
    fun getAppToken(
        @Query("phone") phone: String?, @Query(
            "password"
        ) password: String?
    ): Observable<RespDTO<OssToken>>

    //等级列表省市 县学校
    @GET("getRegionOrSchool.do")
    fun getRegionOrSchool(
        @Query("regLevel") regLevel: String?, @Query(
            "regCode"
        ) regCode: String?
    ): Observable<RespDTO<List<RegionOrSchoolBean>>>

    @GET("getSchoolDownload.do")
    fun getSchoolDownload(@Query("schoolId") schoolId: String?): Observable<RespDTO<Any>>

    @POST("deleteProjectFile.do")
    fun deleteProjectFile(@Query("code") code: String?): Observable<RespDTO<*>>
}