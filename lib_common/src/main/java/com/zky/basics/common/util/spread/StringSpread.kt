package com.zky.basics.common.util.spread

/**
 * Created by lk
 * Date 2020/8/16
 * Time 09:48
 * Detail:String 函数的扩展
 */

/**
 * 字符串最后一个字符
 */
fun String.lastString(): String = if (this.isNullOrEmpty()) "" else this[this.length - 1].toString()

/**
 * 字符串第一个字符
 */
fun String.firstString(): String = if (this.isNullOrEmpty()) "" else  this[0].toString()

/**
 * 去掉最后一个字符
 */
fun String.removeLastString(): String =  if (this.isNullOrEmpty()) "" else this.substring(0, this.length - 1)





