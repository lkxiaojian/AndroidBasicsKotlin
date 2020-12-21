package com.zky.basics.common.util.spread

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 *create_time : 20-12-21 上午10:40
 *author: root
 *description： ViewSpread  view 的扩展函数
 */


/**
 * TODO
 *Snackbar 的显示
 * 使用
 *   view.snackBar("tip","Undo"){
 *    想要执行的方法
 *   // "toast".showToast()
 *  }
 * @param tip 提示文本
 * @param actionText  点击文本
 * @param duration 时长
 * @param block  方法
 *
 */
fun View.snackBar(
    tip: String,
    actionText: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    block: (() -> Unit)? = null
) {
    val sandbar = Snackbar.make(this, tip, duration)
    if (actionText != null && block != null) {
        sandbar.setAction(actionText) {
            block()
        }
    }
    sandbar.show()
}
/**
 * TODO
 *Snackbar 的显示
 * @param tip 提示文本
 * @param actionText  点击文本
 * @param duration 时长
 * @param block  方法
 * 使用
 */
fun View.snackBar(
    resID: Int,
    actionText: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    block: (() -> Unit)? = null
) {
    val sandbar = Snackbar.make(this, resID, duration)
    if (actionText != null && block != null) {
        sandbar.setAction(actionText) {
            block()
        }
    }
    sandbar.show()
}