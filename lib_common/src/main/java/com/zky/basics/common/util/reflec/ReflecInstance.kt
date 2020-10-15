package com.zky.basics.common.util.reflec

/**
 * Created by lk
 * Date 2020/8/16
 * Time 09:13
 * Detail:
 */

/**
 * 获得T.class
 */
inline fun <reified T> classOf() = T::class.java

/**
 * 获得 T object 对象
 */
inline fun <reified T> instanceOf(): T = T::class.java.newInstance()