package com.zky.basics.common.util

import android.widget.Toast
import com.zky.basics.common.BaseApplication

object ToastUtil {
    @JvmStatic
    fun showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(BaseApplication.instance, message, duration).show()
    }

    @JvmStatic
    fun showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(
            BaseApplication.instance,
            BaseApplication.instance.getString(resId),
            duration
        )
            .show()
    }
}