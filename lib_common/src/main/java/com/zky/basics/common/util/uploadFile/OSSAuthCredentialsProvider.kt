package com.zky.basics.common.util.uploadFile

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken
import com.zky.basics.api.common.entity.OssToken

/**
 * Created by lk
 * Date 2019-11-13
 * Time 11:37
 * Detail:
 */
class OSSAuthCredentialsProvider(ossToken: OssToken) : OSSFederationCredentialProvider() {
    private val it: OssToken = ossToken


    override fun getFederationToken(): OSSFederationToken {
        return OSSFederationToken(it.accessKeyId, it.accessKeySecret, it.securityToken, it.expiration)
    }

}