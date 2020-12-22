package com.zky.basics.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 版本相关
 */
@SuppressLint("ObsoleteSdkInt")
object SdkVersionUtil {
    /**
     * hasForyo
     *
     * @return true false
     */

    fun hasFroyo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }

    /**
     * hasGingerbread
     *
     * @return true false
     */
    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    /**
     * hasHoneycomb
     *
     * @return true false
     */
    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    /**
     * hasHoneycombMR1
     *
     * @return true false
     */
    fun hasHoneycombMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    /**
     * hasHoneycombMR2
     *
     * @return true false
     */
    fun hasHoneycombMR2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2
    }

    /**
     * hasIceCreamSandwich
     *
     * @return true false
     */
    fun hasIceCreamSandwich(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    /**
     * hasJellyBean
     *
     * @return true false
     */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * 4.2以上
     *
     * @return true false
     */
    fun aboveJellyBean(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getAppVersion(context: Context): Int {
        var version = 0
        try {
            version = context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode.toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }
}