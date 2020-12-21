package com.zky.basics.common.util.security

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 *create_time : 20-12-21 下午5:14
 *author: lk
 *description： MMKVUtli
 */
object MMKVUtil {
    var mmkv = lazy { MMKV.defaultMMKV() }.value

    fun setMMKV(uId: String?) {
        mmkv = if (uId.isNullOrEmpty()) {
            MMKV.defaultMMKV()
        } else MMKV.mmkvWithID(uId)
    }

    /**
     * TODO mmkv 存储
     *
     * @param key     存储的key
     * @param value   存储的值
     * @param security 是否加密 默认false  不加密
     */
    fun put(key: String, value: Any, security: Boolean = false) {
        when (value) {
            is String -> {
                mmkv.encode(key, value)
            }
            is Int -> {
                mmkv.encode(key, value)
            }
            is Boolean -> {
                mmkv.encode(key, value)
            }
            is Float -> {
                mmkv.encode(key, value)
            }
            is Long -> {
                mmkv.encode(key, value)
            }
            is ByteArray -> {
                mmkv.encode(key, value)
            }

            is Nothing -> return
        }
    }
}