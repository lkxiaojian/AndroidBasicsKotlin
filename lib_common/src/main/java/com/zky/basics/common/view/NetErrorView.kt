package com.zky.basics.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.zky.basics.common.R

class NetErrorView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private var mOnClickListener: OnClickListener? = null
    private val mRlNetWorkError: RelativeLayout
    fun setRefreshBtnClickListener(listener: OnClickListener?) {
        mOnClickListener = listener
    }

    fun setNetErrorBackground(@ColorRes colorResId: Int) {

//        mRlNetWorkError.setBackgroundColor(resources.getColor(colorResId))
        mRlNetWorkError.setBackgroundColor(ContextCompat.getColor(context,colorResId))
    }

    init {
        inflate(context, R.layout.view_net_error, this)
        findViewById<View>(R.id.btn_net_refresh).setOnClickListener { v ->
            if (mOnClickListener != null) {
                mOnClickListener!!.onClick(v)
            }
        }
        mRlNetWorkError = findViewById(R.id.rl_net_error_root)
    }
}