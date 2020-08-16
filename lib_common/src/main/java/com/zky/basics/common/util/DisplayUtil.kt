package com.zky.basics.common.util

import android.content.Context
import com.zky.basics.common.BaseApplication

/**
 * Description: <单位转换工具类><br></br>
 * <h1>功能列表：</h1>
 *  * 1. dp转px
 *  * 2. px转dp
 *  * 3. sp转px
 *  * 4. px转sp
 *
</单位转换工具类> */
object DisplayUtil {
    private val context: Context =
        BaseApplication.instance.applicationContext

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale
     * （DisplayMetrics类中属性density）
     * @return
     */
    fun px2dip(pxValue: Float, scale: Float): Int {
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * （DisplayMetrics类中属性density）
     * @return
     */
    fun px2dip(pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale
     * （DisplayMetrics类中属性density）
     * @return
     */
    fun dip2px(dipValue: Float, scale: Float): Int {
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    @JvmStatic
    fun dip2px(dipValue: Float): Int {
        return (dipValue * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(pxValue: Float, fontScale: Float): Int {
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2sp(pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(spValue: Float, fontScale: Float): Int {
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    @JvmStatic
    fun sp2px(spValue: Float): Int {
        return (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }
}