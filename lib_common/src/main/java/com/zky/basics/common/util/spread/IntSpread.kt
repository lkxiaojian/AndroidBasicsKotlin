package com.zky.basics.common.util.spread

import android.widget.Toast
import com.zky.basics.common.BaseApplication

/**
 *create_time : 20-12-21 上午10:37
 *author: root
 *description： IntSpread  Int 的扩展函数
 */

/**
 * TODO
 * 资源resID  toast
 * @param duration toast 时间 ，默认 Toast.LENGTH_SHORT  可以传参数 Toast.LENGTH_LONG
 */
fun Int.showToast(duration:Int=Toast.LENGTH_SHORT)= Toast.makeText(BaseApplication.instance, this, duration).show()

