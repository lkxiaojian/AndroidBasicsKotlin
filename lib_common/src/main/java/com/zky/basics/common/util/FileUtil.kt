package com.zky.basics.common.util

import android.text.TextUtils
import com.zky.basics.common.util.InfoVerify.isEmpty
import java.io.*
import java.util.regex.Pattern

object FileUtil {
    fun isImageFile(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = ".+(\\.jpeg|\\.jpg|\\.gif|\\.bmp|\\.png).*"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(url.toLowerCase())
        return matcher.find()
    }

    fun isVideoFile(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg =
            ".+(\\.avi|\\.wmv|\\.mpeg|\\.mp4|\\.mov|\\.mkv|\\.flv|\\.f4v|\\.m4v|\\.rmvb|\\.rm|\\.rmvb|\\.3gp|\\.dat|\\.ts|\\.mts|\\.vob).*"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(url.toLowerCase())
        return matcher.find()
    }

    fun isUrl(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val reg = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
        return url.matches(reg)
    }

    fun getFileByte(filename: String?): ByteArray? {
        val f = File(filename)
        if (!f.exists()) {
            return null
        }
        val bos = ByteArrayOutputStream(
            f.length().toInt()
        )
        var `in`: BufferedInputStream? = null
        try {
            `in` = BufferedInputStream(FileInputStream(f))
            val buf_size = 1024
            val buffer = ByteArray(buf_size)
            var len = 0
            while (-1 != `in`.read(buffer, 0, buf_size).also { len = it }) {
                bos.write(buffer, 0, len)
            }
            `in`.close()
            return bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 根据文件路径获取文件的名称
     *
     * @param path
     * @return
     */
    fun getNameByPath(path: String): String {
        if (isEmpty(path)) {
            return ""
        }
        val list = path.split("/").toTypedArray()
        return list[list.size - 1]
    }
}