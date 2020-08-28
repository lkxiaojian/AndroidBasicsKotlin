package com.zky.basics.api.apiservice

import com.zky.basics.api.common.entity.OssToken
import com.zky.basics.api.common.entity.UpdataBean
import com.zky.basics.api.dto.RespDTO
import com.zky.basics.api.splash.entity.RegionOrSchoolBean
import com.zky.basics.api.splash.entity.Userinfo
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommonService {
    @GET("login.do")
    suspend fun login(@Query("phone") phone: String?, @Query("password") pwd: String?): RespDTO<Userinfo>

    //获取最新app信息
    @GET("getAppInfo.do")
    suspend fun appInfo(): RespDTO<UpdataBean>

    //忘记密码
    @GET("updateUserPassword.do")
    suspend fun updateUserPassword(
        @Query("oprationType") oprationType: String?,
        @Query("phone") phone: String?,
        @Query("oldPassword") oldPassword: String?,
        @Query("password") password: String?,
        @Query("smsCode") smsCode: String?
    ): RespDTO<Any>

    @GET("getUser.do")
    fun getUser(@Query("phone") phone: String?): RespDTO<Userinfo>

    //获取app token
    @GET("getAppToken.do")
    suspend fun getAppToken(
        @Query("phone") phone: String?
        , @Query("password") password: String?
    ): RespDTO<OssToken>

    //等级列表省市 县学校
    @GET("getRegionOrSchool.do")
    suspend fun getRegionOrSchool(
        @Query("regLevel") regLevel: String?
        , @Query("regCode") regCode: String?
    ): RespDTO<List<RegionOrSchoolBean>>

    @GET("getSchoolDownload.do")
    suspend fun getSchoolDownload(@Query("schoolId") schoolId: String?): RespDTO<Any>

    @POST("deleteProjectFile.do")
    suspend fun deleteProjectFile(@Query("code") code: String?): RespDTO<Any>
}