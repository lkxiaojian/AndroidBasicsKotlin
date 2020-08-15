package com.zky.basics.api.common.entity

/**
 * Created by lk
 * Date 2019-11-13
 * Time 10:29
 * Detail:
 */
data class OssToken(
    var statusCode: Int?,
    var accessKeyId: String?,
    var accessKeySecret: String?,
    var securityToken: String?,
    var expiration: String?
)