package com.zky.basics.common.util.spread

import android.os.Parcelable
import com.zky.basics.common.util.security.MMKVUtil

/**
 *create_time : 20-12-22 上午10:30
 *author: lk
 *description： MMKVSpread  mmkvUtlis 的扩展函数
 */

/**
 * TODO Int 型存储
 *
 * @param key 存储key
 * @param security 是否加密
 */
fun Int.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//Double 型存储
fun Double.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//Float 型存储
fun Float.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//String 型存储
fun String.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//Long 型存储
fun Long.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//ByteArray 型存储
fun ByteArray.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//Boolean 型存储
fun Boolean.encode(key: String, security: Boolean = false) = MMKVUtil.put(key, this, security)

//Set<String> 型存储
fun Set<String>.encode(key: String) = MMKVUtil.put(key, this)

// extends Parcelable 型存储
fun Parcelable.encode(key: String) = MMKVUtil.put(key, this)

/**
 * TODO mmkv Int 取值
 *
 * @param key   取值对应的key
 * @param security 是否是加密的 默认为false
 */
fun Int.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun Double.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun Float.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun String.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun Long.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun ByteArray.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun Boolean.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)
fun Set<String>.decode(key: String, security: Boolean = false) = MMKVUtil.get(key, this, security)

/**
 * TODO mmkv 存储的对象
 *
 * @param T 对应的class
 * @param key
 * @return
 */
inline fun <reified T : Parcelable> decodeParcelable(key: String): T? = MMKVUtil.get(key)






