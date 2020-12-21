package com.zky.basics.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Description: <RecyclerView空白区域点击监听><br></br>
</RecyclerView空白区域点击监听> */
class TouchyRecyclerView(context: Context?, attrs: AttributeSet?) : RecyclerView(
    context!!, attrs
) {
    private var listener: OnNoChildClickListener? = null

    interface OnNoChildClickListener {
        fun onNoChildClick()
    }

    fun setOnNoChildClickListener(listener: OnNoChildClickListener?) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN
            && findChildViewUnder(event.x, event.y) == null
        ) {
            if (listener != null) {
                listener!!.onNoChildClick()
            }
        }
        return super.dispatchTouchEvent(event)
    }
}