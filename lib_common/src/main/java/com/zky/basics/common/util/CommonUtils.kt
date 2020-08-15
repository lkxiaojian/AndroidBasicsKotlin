package com.zky.basics.common.util

import android.os.Build
import android.support.v4.app.FragmentActivity
import android.view.View



/**
 * Created by lk
 * Date 2019-11-11
 * Time 17:33
 * Detail:
 */

fun ViewToActivity(view: View): FragmentActivity {
    var activity:FragmentActivity
    if (Build.VERSION.SDK_INT > 21) {
        activity = view.context as FragmentActivity
    } else {
        activity = view.rootView.context as FragmentActivity
    }
    return activity
}