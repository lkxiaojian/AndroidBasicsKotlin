package com.zky.basics.common.util.security

import org.bouncycastle.crypto.digests.SM3Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import java.io.UnsupportedEncodingException
import java.security.Security
import java.util.*

/**
 * SM3 加密
 */
object SM3 {
    private const val ENCODING = "UTF-8"

    /**
     * SM3加密
     * @param paramStr    待加密字符串
     * @return    返回加密后，固定长度=32的16进制字符串
     */
    fun encrypt(paramStr: String): String {
        //将返回的hash值转换为16进制字符串
        var resultHexString = ""
        try {
            //将字符串转换成byte数组
            val srcData = paramStr.toByteArray(charset(ENCODING))
            val resultHash = hash(srcData)
            //将返回的hash值转换成16进制字符串
            resultHexString = ByteUtils.toHexString(resultHash)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return resultHexString
    }

    /**
     * 返回长度为32的byte数组
     * 生成对应的hash值
     * @param srcData
     * @return
     */
    fun hash(srcData: ByteArray): ByteArray {
        val digest = SM3Digest()
        digest.update(srcData, 0, srcData.size)
        val hash = ByteArray(digest.digestSize)
        digest.doFinal(hash, 0)
        return hash
    }

    /**
     * 通过指定密钥进行加密
     * @param key 密钥
     * @param srcData 被加密的byte数组
     * @return
     */
    fun hmac(key: ByteArray?, srcData: ByteArray): ByteArray {
        val keyParameter = KeyParameter(key)
        val digest = SM3Digest()
        val mac = HMac(digest)
        mac.init(keyParameter)
        mac.update(srcData, 0, srcData.size)
        val result = ByteArray(mac.macSize)
        mac.doFinal(result, 0)
        return result
    }

    /**
     * 判断数据源与加密数据是否一致，通过验证原数组和生成是hash数组是否为同一数组，验证二者是否为同一数据
     * @param srcStr
     * @param sm3HexString
     * @return
     */
    fun vertify(srcStr: String, sm3HexString: String?): Boolean {
        var flag = false
        try {
            val srcData = srcStr.toByteArray(charset(ENCODING))
            val sm3Hash = ByteUtils.fromHexString(sm3HexString)
            val newHash = hash(srcData)
            if (Arrays.equals(newHash, sm3Hash));
            flag = true
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return flag
    }

    init {
        Security.addProvider(BouncyCastleProvider())
    }
}