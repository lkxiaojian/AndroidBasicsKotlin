package com.zky.basics.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

/**
 * Description: <文字图标居中TextView><br></br>
</文字图标居中TextView> */
@SuppressLint("AppCompatCustomView")
class DrawableCenterTextView : TextView {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?) : super(context) {}

    override fun onDraw(canvas: Canvas) {
        val drawables = compoundDrawables
        val drawableLeft = drawables[0]
        if (drawableLeft != null) {
            val textWidth = paint.measureText(text.toString())
            val drawablePadding = compoundDrawablePadding

            var  drawableWidth = drawableLeft.intrinsicWidth
            val bodyWidth = textWidth + drawableWidth + drawablePadding
            val v = width - bodyWidth
            if (v > 0) canvas.translate(v / 2, 0f) else canvas.translate(0f, 0f)
        }
        super.onDraw(canvas)
    }
}