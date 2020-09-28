package com.zky.basics.common.util

import android.os.Build
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.XXPermissions
import com.zky.basics.common.util.ToastUtil.showToast


/**
 * Created by lk
 * Date 2019-11-11
 * Time 17:33
 * Detail:
 */

fun ViewToActivity(view: View): FragmentActivity {
    var activity: FragmentActivity
    if (Build.VERSION.SDK_INT > 21) {
        activity = view.context as FragmentActivity
    } else {
        activity = view.rootView.context as FragmentActivity
    }
    return activity
}


fun PermissionToSetting(
    activity: FragmentActivity,
    denied: List<String>,
    never: Boolean,
    message: String
) {
    showToast(message)
    if (never) {
        // 如果是被永久拒绝就跳转到应用权限系统设置页面
        XXPermissions.startPermissionActivity(activity, denied)
    }


}