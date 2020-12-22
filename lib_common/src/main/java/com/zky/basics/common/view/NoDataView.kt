package com.zky.basics.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.zky.basics.common.R

class NoDataView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private val mRlNoDataRoot: RelativeLayout
    private val mImgNoDataView: ImageView
    fun setNoDataBackground(@ColorRes colorResId: Int) {

        mRlNoDataRoot.setBackgroundColor(ContextCompat.getColor(context,colorResId))
    }

    fun setNoDataView(@DrawableRes imgResId: Int) {
        mImgNoDataView.setImageResource(imgResId)
    }

    init {
        inflate(context, R.layout.view_no_data, this)
        mRlNoDataRoot = findViewById(R.id.rl_no_data_root)
        mImgNoDataView = findViewById(R.id.img_no_data)
    }
}