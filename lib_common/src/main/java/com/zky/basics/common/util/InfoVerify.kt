package com.zky.basics.common.util

import android.text.TextUtils
import java.util.regex.Pattern

object InfoVerify {
    /**
     * 校验邮箱
     *
     * @param paramString
     * @return
     */
    fun isValidEmail(paramString: String): Boolean {
        val regex = "[a-zA-Z0-9_\\.]{1,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}"
        return paramString.matches(regex.toRegex())
    }

    /**
     * 校验ＱＱ
     *
     * @param paramString
     * @return
     */
    fun isValidQQ(paramString: String): Boolean {
        val regex = "^[1-9](\\d){4,9}$"
        return paramString.matches(regex.toRegex())
    }

    /**
     * 校验车牌号
     *
     * @param paramString
     * @return
     */
    fun isValidPlatnum(paramString: String): Boolean {
        if (TextUtils.isEmpty(paramString)) return false
        val regex = "^[\u4e00-\u9fa5]{1}[A-Z_a-z]{1}[A-Z_0-9_a-z]{5}$"
        return paramString.matches(regex.toRegex())
    }

    /**
     * 校验手机号
     *
     * @param paramString
     * @return
     */
    fun isValidMobiNumber(paramString: String?): Boolean {
        // String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        if (paramString == null) return false
        val regex = "^1\\d{10}$"
        return paramString.matches(regex.toRegex())
    }

    /**
     * 检验是否都是数字
     * @param str
     * @return
     */
    fun isNumeric(str: String?): Boolean {
        val pattern = Pattern.compile("^[0-9]+\\.?[0-9]*[0-9]$")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 是否都是中文
     * @param strName
     * @return
     */
    fun isChinese(strName: String?): Boolean {
        if (strName.isNullOrEmpty()) {
            return false
        }
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (isChinese(c)) {
                return true
            }
        }
        return false
    }

    /**
     * 验证 11为数字
     * @param phone
     * @return
     */
    fun isPhone(phone: String?): Boolean {
        if (phone == null || phone.length != 11) {
            return false
        }
        val type = "^[1]\\d{10}$"
        val p = Pattern.compile(type)
        val m = p.matcher(phone)
        return m.matches()
    }

    /**
     * 验证字符串是否为空
     *
     * @param message
     * @return
     */
    @JvmStatic
    fun isEmpty(message: String?): Boolean {
        return message == null || message.isEmpty()
    }

    /**
     * 验证密码符合规范  请输入6-20位字母和数字组合，必须同时含有字母和数字
     *
     * @param pwd
     * @return
     */
    fun isPwd(pwd: String?): Boolean {
        if (pwd == null) {
            return false
        }
        val type = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$"
        val p = Pattern.compile(type)
        val m = p.matcher(pwd)
        return m.matches()
    }

    fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    }
}