package com.zky.basics.common.util.spread

import android.widget.Toast
import com.zky.basics.common.BaseApplication
import com.zky.basics.common.util.InfoVerify
import java.util.regex.Pattern

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
fun String.firstString(): String = if (this.isNullOrEmpty()) "" else this[0].toString()

/**
 * 去掉最后一个字符
 */
fun String.removeLastString(): String =
    if (this.isNullOrEmpty()) "" else this.substring(0, this.length - 1)

/**
 * TODO
 *字符串toast
 * @param duration toast 时间 ，默认 Toast.LENGTH_SHORT  可以传参数 Toast.LENGTH_LONG
 */
fun String.showToast(duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(BaseApplication.instance, this, duration).show()

/**
 * TODO 判断是否包含为中文
 *
 * @return
 */
fun String.isChinese(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    val ch = this.toCharArray()
    for (i in ch.indices) {
        val c = ch[i]
        if (InfoVerify.isChinese(c)) {
            return true
        }
    }
    return false
}

/**
 * TODO 判断是否为11为数字
 *
 * @return
 */
fun String.isPhone11(): Boolean {
    if (this.length != 11) {
        return false
    }
    val type = "^[1]\\d{10}$"
    val p = Pattern.compile(type)
    val m = p.matcher(this)
    return m.matches()
}

/**
 * TODO 验证密码符合规范  请输入6-20位字母和数字组合，必须同时含有字母和数字
 *
 * @return
 */
fun String.isPwd(): Boolean {
    val type = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$"
    val p = Pattern.compile(type)
    val m = p.matcher(this)
    return m.matches()
}




