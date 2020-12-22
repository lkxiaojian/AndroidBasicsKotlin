package com.zky.basics.common.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.zky.basics.common.R
import com.zky.basics.common.util.DisplayUtil.dip2px

/**
 * Description: <旋转进度的dialog><br></br>
</旋转进度的dialog> */
class RotateProgressDialog : DialogFragment() {
    private var mTxtProgress: TextView? = null
    private var mImgProgress: ImageView? = null
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setLayout(dip2px(100f), dip2px(100f))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.rotate_progress_dialog_layout, null, false)
        mImgProgress = view.findViewById(R.id.img_progress)
        mTxtProgress = view.findViewById(R.id.txt_progress)

        //mImgProgress.animate().rotation(360).setDuration(5).r.start();
        val rotation =
            ObjectAnimator.ofFloat(mImgProgress, "rotation", 0f, 359f) //最好是0f到359f，0f和360f的位置是重复的
        rotation.repeatCount = ObjectAnimator.INFINITE
        rotation.interpolator = LinearInterpolator()
        rotation.duration = 2000
        rotation.start()
        return view
    }

    fun setProgress(value: Int) {
        mTxtProgress!!.text = "$value%"
    }
}