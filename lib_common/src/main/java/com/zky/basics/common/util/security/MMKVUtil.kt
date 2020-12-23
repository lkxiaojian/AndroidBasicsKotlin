@file:Suppress("UNCHECKED_CAST")

package com.zky.basics.common.util.security

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import com.zky.basics.common.BaseApplication
import com.zky.basics.common.util.log.KLog

/**
 *create_time : 20-12-21 下午5:14
 *author: lk
 *description： MMKVUtli
 */
object MMKVUtil {
    var mmkv = lazy { MMKV.defaultMMKV() }.value
    var encryptUtilInstance = lazy { EncryptUtil.getInstance(BaseApplication.instance) }.value
    var TAG = MMKVUtil.javaClass.name

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
    fun put(_key: String, _value: Any?, security: Boolean = false) {
        if (_value == null) {
            return
        }
        var key = _key
        var value = _value

        if (security) {
            key = encryptUtilInstance?.encrypt(key).toString()
            value = encryptUtilInstance?.encrypt(_value.toString())
        }
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

            is Parcelable -> {
                encode(key, value)
            }
            is Set<*> -> {
                encode(key, value as Set<String>)
            }
            is Nothing -> return
        }
    }


    /**
     * TODO 获取存储值
     *
     * @param key
     * @param security
     */
    fun get(_key: String, defaultValue: Any = "", security: Boolean = false): Any? {
        var value: Any? = null
        var key = _key
        try {
            if (security) {
                key = encryptUtilInstance?.encrypt(_key).toString()
            }

            when (defaultValue) {
                is String -> {
                    value = mmkv.decodeString(key, defaultValue)
                }
                is Int -> {
                    value = mmkv.decodeInt(key, defaultValue)
                }
                is Boolean -> {
                    value = mmkv.decodeBool(key, defaultValue)
                }
                is Float -> {
                    value = mmkv.decodeFloat(key, defaultValue)
                }
                is Long -> {
                    value = mmkv.decodeLong(key, defaultValue)
                }
                is ByteArray -> {
                    value = mmkv.decodeBytes(key, defaultValue)
                }
                is Set<*> -> {
                    mmkv.decodeStringSet(key)
                }
                else -> {
                    value = null
                }
            }
            if (security) {
                value = encryptUtilInstance?.decrypt(value.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    inline fun <reified T : Parcelable> get(_key: String): T? {
        return mmkv.decodeParcelable(_key, T::class.java)
    }


    private fun <T : Parcelable> encode(key: String, t: T) = mmkv.encode(key, t)

    private fun encode(key: String, sets: Set<String>) = mmkv.encode(key, sets)

    /**
     * TODO 清除mmkv 所有存储的值
     *
     */
    fun clearAll() {
        mmkv.clearMemoryCache()
        mmkv.clearAll()
    }

    /**
     * TODO 移除某个key 的value
     *
     * @param key
     */
    fun removeValueForKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    /**
     * TODO 移除多个key 的value
     *
     * @param keys
     */
    fun removeValueForKey(keys: Array<String>) {
        mmkv.removeValuesForKeys(keys)
    }

    /**
     * TODO 判断mmkv 是否包含某个key
     *
     * @param key
     * @return true 包含  false 不包含
     */
    fun containsKey(key: String): Boolean {
        return mmkv.containsKey(key)
    }

    /**
     * TODO 获取 mmkv 所有的键值对
     *
     * @return
     */
    fun getMMKV(): MutableMap<String, *>? {
        return mmkv.all
    }

    /**
     * TODO 获取 mmkv 的所有key
     *
     * @return
     */
    fun getMMKVAllKey(): Array<out String>? {
        return mmkv.allKeys()
    }


}