package com.zky.basics.common.util

import android.widget.Toast
import com.zky.basics.common.BaseApplication

object ToastUtil {
    @JvmStatic
    fun showToast(message: String?) {
        Toast.makeText(BaseApplication.instance, message, Toast.LENGTH_SHORT).show()
    }
    @JvmStatic
    fun showToast(resid: Int) {
        Toast.makeText(BaseApplication.instance,
            BaseApplication.instance.getString(resid),
            Toast.LENGTH_SHORT
        )
            .show()
    }
}