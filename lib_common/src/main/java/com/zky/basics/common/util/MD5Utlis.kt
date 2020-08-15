package com.zky.basics.common.util

import java.security.MessageDigest

/**
 * Created by lk
 * Date 2019-11-05
 * Time 17:55
 * Detail:
 */
fun MD5(inStr: String): String {
    var md5: MessageDigest?
    try {
        md5 = MessageDigest.getInstance("MD5")
    } catch (e: Exception) {
        return ""
    }
    if (InfoVerify.isEmpty(inStr)) {
        return ""
    }
    val charArray = inStr.toCharArray()
    val byteArray = ByteArray(charArray.size)

    for (i in charArray.indices)
        byteArray[i] = charArray[i].toByte()

    val md5Bytes = md5!!.digest(byteArray)

    val hexValue = StringBuffer()

    for (i in md5Bytes.indices) {
        val byte = md5Bytes[i].toInt() and 0xff
        if (byte < 16)
            hexValue.append("0")
        hexValue.append(Integer.toHexString(byte))
    }

    return hexValue.toString()
}