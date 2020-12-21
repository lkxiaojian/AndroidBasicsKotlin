package com.zky.basics.common.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import com.zky.basics.common.R

class LoadingInitView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private val mAnimationDrawable: AnimationDrawable
    fun startLoading() {
        mAnimationDrawable.start()
    }

    fun stopLoading() {
        mAnimationDrawable.stop()
    }

    fun loading(b: Boolean) {
        if (b) {
            startLoading()
        } else {
            stopLoading()
        }
    }

    init {
        inflate(context, R.layout.view_init_loading, this)
        val imgLoading = findViewById<ImageView>(R.id.img_init_loading)
        mAnimationDrawable = imgLoading.drawable as AnimationDrawable
    }
}