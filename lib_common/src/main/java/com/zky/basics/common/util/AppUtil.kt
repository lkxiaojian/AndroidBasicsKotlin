package com.zky.basics.common.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.P)
fun getVersionCode(context: Context): Long {
    try {
        //获取packagemanager的实例
        val packageManager = context.packageManager
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            packInfo.longVersionCode
//        } else {
//            packInfo.versionCode.toLong()
//        }
       return packInfo.longVersionCode
    } catch (e: Exception) {
    }
    return 0
}

/**
 * 获取当前程序的版本名
 * @return
 */
fun getVersionName(context: Context): String {
    try {
        //获取packageManager的实例
        val packageManager = context.packageManager
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packInfo.versionName
    } catch (e: Exception) {
    }
    return ""
}
