@file:Suppress("DEPRECATION")

package com.zky.basics.api.http

import android.net.ParseException
import android.widget.Toast
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.zky.basics.api.RetrofitManager
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException


object ExceptionHandler {
    fun handleException(e: Throwable?): ResponseThrowable {
        val ex: ResponseThrowable
        return if (e is HttpException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.HTTP_ERROR)
            when (e.code()) {
                SYSTEM_ERROR.UNAUTHORIZED -> {
                    ex.message = "操作未授权"
                    ex.code = SYSTEM_ERROR.UNAUTHORIZED
                    Toast.makeText(RetrofitManager.mContext, "您的登录已失效,请重新登录", Toast.LENGTH_SHORT)
                        .show()
                }
                SYSTEM_ERROR.FORBIDDEN -> ex.message = "请求被拒绝"
                SYSTEM_ERROR.NOT_FOUND -> ex.message = "资源不存在"
                SYSTEM_ERROR.REQUEST_TIMEOUT -> ex.message = "服务器执行超时"
                SYSTEM_ERROR.INTERNAL_SERVER_ERROR -> ex.message = "服务器内部错误"
                SYSTEM_ERROR.SERVICE_UNAVAILABLE -> ex.message = "服务器不可用"
                else -> ex.message = "网络错误"
            }
            ex
        } else if (e is JsonParseException || e is JSONException || e is ParseException || e is MalformedJsonException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.PARSE_ERROR)
            ex.message = "解析错误"
            ex
        } else if (e is ConnectException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.NETWORD_ERROR)
            ex.message = "连接失败"
            ex
        } else if (e is SSLException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.SSL_ERROR)
            ex.message = "证书验证失败"
            ex
        } else if (e is ConnectTimeoutException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR)
            ex.message = "连接超时"
            ex
        } else if (e is SocketTimeoutException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR)
            ex.message = "连接超时"
            ex
        } else if (e is UnknownHostException) {
            ex = ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR)
            ex.message = "主机地址未知"
            ex
        } else {
            ex = ResponseThrowable(e, SYSTEM_ERROR.UNKNOWN)
            ex.message = "未知错误"
            ex
        }
    }

    object SYSTEM_ERROR {
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val REQUEST_TIMEOUT = 409
        const val INTERNAL_SERVER_ERROR = 500
        const val SERVICE_UNAVAILABLE = 503
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * SSL_ERROR         * 网络错误
         */
        const val NETWORD_ERROR = 1002
        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003
        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005
        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006
    }

    interface APP_ERROR {
        companion object {
            const val SUCC = 200 //	处理成功，无错误
            const val INTERFACE_PROCESSING_TIMEOUT = 1 //	接口处理超时
            const val INTERFACE_INTERNAL_ERROR = 2 //	接口内部错误
            const val PARAMETERS_EMPTY = 3 //	必需的参数为空
            const val AUTHENTICATION_FAILED = 4 //	鉴权失败，用户没有使用该项功能（服务）的权限。
            const val PARAMETERS_ERROR = 5 //	参数错误
            /**
             * 企业激活码无效
             */
            const val CODE_ACTIVECODE_INVALIDATE = 100201
            /**
             * 激活码已被激活
             */
            const val CODE_ACTIVECODE_ACTIVED = 100202
            /**
             * 用户不存在
             */
            const val CODE_USER_NOT_EXIST = 110401
            /**
             * 用户被禁用
             */
            const val CODE_USER_DISABLE = 110203
            /**
             * 盒子号无效
             */
            const val INVALIDATE_BOX_CODE = 110907
            /**
             * 盒子号已绑定在当前企业下的其他车辆上
             */
            const val BOX_BINDED_VEHICLE = 110908
            /**
             * 验证码无效
             */
            const val AUTH_CODE_INVALIDATE = 110801
            /**
             * 企业可绑定盒子已达上限，请联系客服，升级权限
             */
            const val BIND_BOX_LIMIT = 110802
            /**
             * 授权车辆不允许修改此信息。
             */
            const val VHEICLE_NOT_EDIT_CODE = 110904
        }
    }
}